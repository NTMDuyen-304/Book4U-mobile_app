import asyncHandler from "express-async-handler";
import Borrow from "../models/borrowModel.js";
import Book from "../models/bookModel.js";
import User from "../models/userModel.js";

// Helper tính ngày còn lại
const calcRemaining = (dueDate) => {
  if (!dueDate) return null;
  return Math.ceil((new Date(dueDate) - new Date()) / (1000 * 60 * 60 * 24));
};

// Chuẩn hóa dữ liệu trả về cho Admin
const shapeForAdmin = (rec) => {
  const bookTitle = rec.book?.title || rec.title || rec.bookTitle || "Unknown";
  return {
    id: rec.id,
    bookId: rec.book?._id?.toString?.() || rec.book?.id || "",
    userId: rec.user?._id?.toString?.() || rec.user?.id || "",
    title: bookTitle,
    bookTitle,
    borrowerName: rec.user?.name || "Unknown",
    borrowerEmail: rec.user?.email || rec.borrower || "Unknown",
    borrowDate: rec.borrowDate ? rec.borrowDate.toISOString().slice(0, 10) : "",
    dueDate: rec.dueDate ? rec.dueDate.toISOString().slice(0, 10) : "",
    status: rec.status,
    extendedDays: rec.extendedDays || 0,
  };
};

// Chuẩn hóa dữ liệu trả về cho Student
const shapeForStudent = (rec) => {
  const remaining = calcRemaining(rec.dueDate);
  const isOverdue = remaining !== null && remaining < 0;
  const bookTitle = rec.book?.title || rec.title || rec.bookTitle || "Unknown";

  return {
    id: rec.id,
    bookId: rec.book?._id?.toString?.() || rec.book?.id || "",
    title: bookTitle,
    bookTitle,
    borrowDate: rec.borrowDate ? rec.borrowDate.toISOString().slice(0, 10) : "",
    dueDate: rec.dueDate ? rec.dueDate.toISOString().slice(0, 10) : "",
    status: isOverdue && rec.status === "Borrowing" ? "Overdue" : rec.status,
    daysRemaining: remaining,
    extendedDays: rec.extendedDays || 0,
  };
};

// GET /api/borrow hoặc /api/borrows (Admin)
export const getAll = asyncHandler(async (req, res) => {
  const list = await Borrow.find().populate("book user").sort({ createdAt: -1 });
  res.json(list.map(shapeForAdmin));
});

// GET /api/borrow/me hoặc /api/borrows/me (Student)
export const getMine = asyncHandler(async (req, res) => {
  const list = await Borrow.find({ user: req.user.id }).populate("book").sort({ createdAt: -1 });
  res.json(list.map(shapeForStudent));
});

// GET /api/borrow/user/:userId (Admin)
export const getByUser = asyncHandler(async (req, res) => {
  const list = await Borrow.find({ user: req.params.userId }).populate("book").sort({ createdAt: -1 });
  res.json(list.map(shapeForStudent));
});

// POST /api/borrow hoặc /api/borrows (Tạo yêu cầu mượn)
export const createBorrow = asyncHandler(async (req, res) => {
  const bookId = req.body.bookId || req.body.book;
  const userId = req.user.role === "admin" && req.body.userId ? req.body.userId : req.user.id;

  if (!bookId) return res.status(400).json({ message: "bookId is required" });

  const user = await User.findById(userId);
  const book = await Book.findById(bookId);

  if (!user || !book) return res.status(400).json({ message: "Invalid user or book" });

  if (book.available < 1) {
    return res.status(400).json({ message: "This book is out of stock, please choose another book." });
  }

  const activeCount = await Borrow.countDocuments({
    user: userId,
    status: { $in: ["Pending Approval", "Borrowing", "Overdue"] },
  });

  if (activeCount >= 3) {
    return res.status(400).json({ message: "You have reached the limit of 3 active borrowings." });
  }

  const existedPending = await Borrow.findOne({
    user: userId,
    book: bookId,
    status: { $in: ["Pending Approval", "Borrowing", "Overdue"] },
  });

  if (existedPending) {
    return res.status(400).json({ message: "You already have an active request for this book." });
  }

  const rec = await Borrow.create({
    user: user._id,
    book: book._id,
    borrower: user.email,
    status: "Pending Approval",
  });

  await rec.populate("book user");
  res.status(201).json(shapeForAdmin(rec));
});

// PUT /api/borrow/:id/approve (Admin duyệt)
export const approve = asyncHandler(async (req, res) => {
  const rec = await Borrow.findById(req.params.id).populate("book user");
  if (!rec) return res.status(404).json({ message: "Not found" });

  if (rec.status !== "Pending Approval") {
    return res.status(400).json({ message: "Can only approve pending requests." });
  }

  if (rec.book.available < 1) {
    return res.status(400).json({ message: "This book is out of stock!" });
  }

  rec.book.available -= 1;
  rec.book.availableCopies = rec.book.available;
  await rec.book.save();

  const now = new Date();
  const due = new Date(now);
  due.setDate(now.getDate() + 14);

  rec.borrowDate = now;
  rec.dueDate = due;
  rec.status = "Borrowing";
  await rec.save();

  res.json(shapeForAdmin(rec));
});

// PUT /api/borrow/:id/reject (Admin từ chối)
export const rejectBorrow = asyncHandler(async (req, res) => {
  const rec = await Borrow.findById(req.params.id).populate("book user");
  if (!rec) return res.status(404).json({ message: "Not found" });

  if (rec.status !== "Pending Approval") {
    return res.status(400).json({ message: "Only pending requests can be declined." });
  }

  rec.status = "Rejected";
  await rec.save();
  res.json(shapeForAdmin(rec));
});

// PUT /api/borrow/:id/return (Trả sách)
export const markReturned = asyncHandler(async (req, res) => {
  const rec = await Borrow.findById(req.params.id).populate("book user");
  if (!rec) return res.status(404).json({ message: "Not found" });

  if (rec.status === "Returned") {
    return res.status(400).json({ message: "Sách này đã trả rồi." });
  }

  if (!["Borrowing", "Overdue"].includes(rec.status)) {
    return res.status(400).json({ message: "Only borrowing/overdue records can be returned." });
  }

  rec.book.available += 1;
  if (rec.book.available > rec.book.stock) rec.book.available = rec.book.stock;
  rec.book.availableCopies = rec.book.available;
  await rec.book.save();

  rec.status = "Returned";
  await rec.save();

  res.json(shapeForAdmin(rec));
});

// PUT /api/borrow/:id/extend (Gia hạn)
export const extend = asyncHandler(async (req, res) => {
  const rec = await Borrow.findById(req.params.id).populate("book user");
  if (!rec) return res.status(404).json({ message: "Not found" });

  if (rec.status !== "Borrowing" && rec.status !== "Overdue") {
    return res.status(400).json({ message: "Only borrowing/overdue records can be extended" });
  }

  const due = new Date(rec.dueDate || new Date());
  due.setDate(due.getDate() + 7);
  rec.dueDate = due;
  rec.extendedDays = (rec.extendedDays || 0) + 7;
  await rec.save();

  res.json(shapeForAdmin(rec));
});

export const removeBorrow = asyncHandler(async (req, res) => {
  const rec = await Borrow.findById(req.params.id);
  if (!rec) return res.status(404).json({ message: "Not found" });
  await rec.deleteOne();
  res.json({ message: "Deleted" });
});
