import mongoose from "mongoose";
import dotenv from "dotenv";
import User from "./models/userModel.js";

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

const seedUsers = async () => {
  try {
    await mongoose.connect(process.env.MONGO_URI);
    console.log("✅ Connected to MongoDB");

    await User.insertMany(
      students.map(([mssv, name]) => ({
        name,
        email: `${mssv}@gm.uit.edu.vn`,
        password: "1234",
        role: "student",
      }))
    );

    console.log("🎓 Inserted all students successfully!");
    process.exit();
  } catch (err) {
    console.error("❌ Error:", err);
    process.exit(1);
  }
};

seedUsers();
