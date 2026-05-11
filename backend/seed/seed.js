import dotenv from "dotenv";
import mongoose from "mongoose";
import bcrypt from "bcryptjs";
import connectDB from "../src/config/db.js";
import User from "../src/models/userModel.js";
import Book from "../src/models/bookModel.js";

dotenv.config();

const students = [
  ["22520038", "Bùi Huỳnh Quốc Anh"],
  ["22520150", "Nguyễn Thị Thanh Châu"],
  ["22520165", "Nguyễn Chu Nguyên Chương"],
  ["22520177", "Nguyễn Minh Cường"],
  ["22520188", "Đỗ Ngọc Hải Đăng"],
  ["22520196", "Trần Duy Hải Đăng"],
  ["22520201", "Võ Thành Danh"],
  ["22520228", "Nguyễn Vĩnh Đạt"],
  ["22520245", "Huỳnh Ngọc Diễm"],
  ["22520251", "Nguyễn Hữu Đình"],
  ["22520268", "Nguyễn An Đức"],
  ["22520281", "Lê Trọng Hoàng Dũng"],
  ["22520321", "Nguyễn Anh Duy"],
  ["22520350", "Nguyễn Thị Mỹ Duyên"],
  ["22520421", "Nguyễn Thúy Hiền"],
  ["22520422", "Phạm Thị Minh Hiền"],
  ["22520468", "Nguyễn Huy Hoàng"],
  ["22520544", "Huỳnh Trần Quốc Huy"],
  ["22520566", "Nguyễn Quốc Huy"],
  ["22520585", "Võ Hà Minh Huy"],
  ["22520615", "Lê Nguyễn Tân Khang"],
  ["22520639", "Ngô Hoàng Phương Khanh"],
  ["22520709", "Phan Huy Kiên"],
  ["22520753", "Nguyễn Thị Kim Liên"],
  ["22520772", "Nguyễn Thị Huyền Linh"],
  ["22520786", "Hồ Tấn Lộc"],
  ["22520836", "Ngô Thị Hồng Ly"],
  ["22520883", "Phạm Gia Minh"],
  ["22520935", "Trần Thị Kim Ngân"],
  ["22520944", "Đào Trọng Nghĩa"],
  ["22520953", "Hồng Bảo Ngọc"],
  ["22520986", "Tống Thuần Nguyên"],
  ["22520995", "Hồ Phúc Nhân"],
  ["22521033", "Huỳnh Ngọc Nhi"],
  ["22521099", "Lê Hoàng Thiên Phú"],
  ["22521137", "Phạm Quang Đại Phúc"],
  ["22521179", "Lê Hoàng Quân"],
  ["22521234", "Nguyễn Thị Xuân Quỳnh"],
  ["22521240", "Lê Minh Sang"],
  ["22521258", "Tăng Kim Sơn"],
  ["22521279", "Nguyễn Phát Tài"],
  ["22521316", "Hà Nhật Thái"],
  ["22521335", "Nguyễn Phan Huỳnh Thắng"],
  ["22521368", "Trần Tịnh Đan Thanh"],
  ["22521398", "Trần Nguyễn Duy Thiện"],
  ["22521407", "Huỳnh Quang Thịnh"],
  ["22521432", "Huỳnh Quốc Minh Thông"],
  ["22521458", "Phạm Lê Anh Thúy"],
  ["22521514", "Nguyễn Thùy Trang"],
  ["22521532", "Huỳnh Quốc Triệu"],
  ["22521547", "Trà Minh Trọng"],
  ["22521561", "Ngô Thành Trung"],
  ["22521607", "Nguyễn Khánh Tuấn"],
  ["22521609", "Phạm Duy Tuấn"],
  ["22521629", "Lâm Mai Tuyền"],
  ["22521643", "Nguyễn Thị Thu Uyên"],
  ["22521662", "Đoàn Hoài Việt"],
];

const run = async () => {
  await connectDB();

  await User.deleteMany({});
  await Book.deleteMany({});

  const adminPassword = await bcrypt.hash("123456", 10);
  const studentPassword = await bcrypt.hash("123456", 10);
  const classStudentPassword = await bcrypt.hash("1234", 10);

  const users = await User.insertMany([
    {
      name: "Admin",
      email: "admin@book4u.com",
      password: adminPassword,
      role: "admin",
    },
    {
      name: "Student",
      email: "student@book4u.com",
      password: studentPassword,
      role: "student",
    },
    ...students.map(([mssv, name]) => ({
      name,
      email: `${mssv}@gm.uit.edu.vn`,
      password: classStudentPassword,
      role: "student",
      mssv,
    })),
  ]);

  const books = await Book.insertMany([
    {
      title: "The Great Gatsby",
      author: "F. Scott Fitzgerald",
      category: "Classic",
      img: "https://images.unsplash.com/photo-1524995997946-a1c2e315a42f",
      intro: "A timeless story of love and illusion set in the roaring 1920s.",
      description: "Nick Carraway meets the enigmatic Jay Gatsby...",
      stock: 5,
      available: 5,
    },
    {
      title: "Atomic Habits",
      author: "James Clear",
      category: "Self-Help",
      img: "https://images.unsplash.com/photo-1589820296156-2454bb8fd0ec",
      intro: "Tiny changes, remarkable results.",
      description: "An operating system for building better habits.",
      stock: 8,
      available: 8,
    },
    {
      title: "Clean Code",
      author: "Robert C. Martin",
      category: "Programming",
      img: "https://images.unsplash.com/photo-1515879218367-8466d910aaa4",
      intro: "A handbook of agile software craftsmanship.",
      description: "Principles, patterns, and practices of writing clean code.",
      stock: 3,
      available: 3,
    },
  ]);

  console.log("✅ Seeded:", {
    users: users.length,
    books: books.length,
  });

  await mongoose.connection.close();
};

run().catch((e) => {
  console.error(e);
  process.exit(1);
});