import asyncHandler from "express-async-handler";
import Book from "../models/bookModel.js";

const normalizeBookPayload = (body = {}) => {
  const data = { ...body };

  if (data.category) data.category = data.category.trim().toLowerCase();

  const cover = data.coverImage || data.img || data.image || "";
  data.coverImage = data.coverImage || cover;
  data.img = data.img || cover;
  data.image = data.image || cover;

  const stock = Number(data.stock ?? data.availableCopies ?? 1);
  const available = Number(data.available ?? data.availableCopies ?? stock);

  data.stock = Number.isFinite(stock) && stock >= 0 ? stock : 1;
  data.available = Number.isFinite(available) && available >= 0 ? Math.min(available, data.stock) : data.stock;
  data.availableCopies = data.available;

  return data;
};

// GET /api/books?keyword=&category=
export const getBooks = asyncHandler(async (req, res) => {
  const keyword = req.query.keyword?.trim() || req.query.q?.trim();
  const category = req.query.category?.trim();

  const filter = {};

  if (keyword) {
    filter.$or = [
      { title: { $regex: keyword, $options: "i" } },
      { author: { $regex: keyword, $options: "i" } },
    ];
  }

  if (category && category.toLowerCase() !== "all") {
    filter.category = category.toLowerCase();
  }

  const books = await Book.find(filter).sort({ createdAt: -1 });
  res.json(books.map((b) => b.toJSON()));
});

// GET /api/books/:id
export const getBookById = asyncHandler(async (req, res) => {
  const book = await Book.findById(req.params.id);
  if (!book) return res.status(404).json({ message: "Book not found" });
  res.json(book.toJSON());
});

// POST /api/books (Admin)
export const createBook = asyncHandler(async (req, res) => {
  const data = normalizeBookPayload(req.body);

  if (!data.title || !data.author) {
    return res.status(400).json({ message: "title and author are required" });
  }

  data.category = data.category || "others";

  const book = await Book.create(data);
  res.status(201).json(book.toJSON());
});

// PUT /api/books/:id (Admin)
export const updateBook = asyncHandler(async (req, res) => {
  const book = await Book.findById(req.params.id);
  if (!book) return res.status(404).json({ message: "Book not found" });

  const data = normalizeBookPayload({ ...book.toObject(), ...req.body });

  const fields = [
    "title",
    "author",
    "category",
    "coverImage",
    "img",
    "image",
    "intro",
    "description",
    "fileUrl",
    "pages",
    "availableCopies",
    "stock",
    "available",
  ];

  fields.forEach((field) => {
    if (data[field] !== undefined) book[field] = data[field];
  });

  const updated = await book.save();
  res.json(updated.toJSON());
});

// DELETE /api/books/:id (Admin)
export const deleteBook = asyncHandler(async (req, res) => {
  const book = await Book.findById(req.params.id);
  if (!book) return res.status(404).json({ message: "Book not found" });

  await book.deleteOne();
  res.json({ message: "Deleted" });
});
