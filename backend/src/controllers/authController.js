import asyncHandler from "express-async-handler";
import jwt from "jsonwebtoken";
import User from "../models/userModel.js";

// Tạo JWT token
const generateToken = (user) => {
  return jwt.sign(
    { id: user._id, role: user.role },
    process.env.JWT_SECRET || "secretkey",
    { expiresIn: "30d" }
  );
};

// [POST] /api/auth/register
export const registerUser = asyncHandler(async (req, res) => {
  const { name, email, password, role } = req.body;

  if (!name || !email || !password) {
    return res.status(400).json({ message: "name, email and password are required" });
  }

  const isMSSV = /^\d{8}$/.test(email);
  const finalEmail = isMSSV ? `${email}@gm.uit.edu.vn` : email.toLowerCase();

  const existed = await User.findOne({ email: finalEmail });
  if (existed) {
    return res.status(400).json({ message: "Email already exists" });
  }

  const user = await User.create({
    name,
    email: finalEmail,
    password,
    role: role === "admin" ? "admin" : "student",
  });

  res.status(201).json({
    token: generateToken(user),
    user: {
      id: user._id,
      name: user.name,
      email: user.email,
      role: user.role,
    },
  });
});

// [POST] /api/auth/login
export const loginUser = asyncHandler(async (req, res) => {
  const { email, password } = req.body;

  if (!email || !password) {
    return res.status(400).json({ message: "email and password are required" });
  }

  // Nếu người dùng chỉ nhập MSSV, tự động nối domain
  const isMSSV = /^\d{8}$/.test(email);
  const finalEmail = isMSSV ? `${email}@gm.uit.edu.vn` : email.toLowerCase();

  const user = await User.findOne({ email: finalEmail });

  if (user && (await user.matchPassword(password))) {
    res.json({
      token: generateToken(user),
      user: {
        id: user._id,
        name: user.name,
        email: user.email,
        role: user.role,
      },
    });
  } else {
    res.status(401).json({ message: "Invalid email or password" });
  }
});

// [GET] /api/auth/profile hoặc /api/auth/me
export const getProfile = asyncHandler(async (req, res) => {
  const user = await User.findById(req.user._id).select("-password");
  if (!user) return res.status(404).json({ message: "User not found" });
  res.json({
    id: user._id,
    name: user.name,
    email: user.email,
    role: user.role,
    createdAt: user.createdAt,
    updatedAt: user.updatedAt,
  });
});
