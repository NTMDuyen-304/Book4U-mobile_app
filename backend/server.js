import express from "express";
import dotenv from "dotenv";
import cors from "cors";
import morgan from "morgan";
import connectMongoDB from "./src/config/db.js";

import authRoutes from "./src/routes/authRoutes.js";
import bookRoutes from "./src/routes/bookRoutes.js";
import borrowRoutes from "./src/routes/borrowRoutes.js";

import swaggerUi from "swagger-ui-express";
import swaggerJsdoc from "swagger-jsdoc";

dotenv.config();

const app = express();

// Core middlewares
app.use(cors({ origin: true, credentials: true }));
app.use(express.json());
app.use(morgan("dev"));

// Health check
app.get("/", (req, res) => {
  res.send("Book4U API is running");
});

app.get("/api/health", (req, res) => {
  res.json({
    message: "Book4U API is running",
    status: "ok",
  });
});

// API routes
app.use("/api/auth", authRoutes);
app.use("/api/books", bookRoutes);
app.use("/api/borrow", borrowRoutes);
app.use("/api/borrows", borrowRoutes);

// Swagger
const options = {
  definition: {
    openapi: "3.0.0",
    info: {
      title: "Book4U API Documentation",
      version: "1.0.0",
      description: "API Documents for Book4U Library Project",
    },
    servers: [
      {
        url: "http://localhost:5001",
        description: "Local",
      },
      {
        url: "https://book4u-be.onrender.com",
        description: "Production",
      },
    ],
    tags: [
      { name: "Auth", description: "Quản lý xác thực Login/Profile" },
      { name: "Books", description: "Quản lý sách" },
      { name: "Borrow", description: "Quản lý mượn trả" },
    ],
    components: {
      securitySchemes: {
        bearerAuth: {
          type: "http",
          scheme: "bearer",
          bearerFormat: "JWT",
        },
      },
    },
    security: [{ bearerAuth: [] }],
  },
  apis: [
    "./src/routes/*.js",
    "./src/swagger.schemas.js",
  ],
};

const specs = swaggerJsdoc(options);
app.use("/api-docs", swaggerUi.serve, swaggerUi.setup(specs));

// Start server
const PORT = process.env.PORT || 5001;

connectMongoDB().then(() => {
  app.listen(PORT, () => {
    console.log(
      `Server running in ${process.env.NODE_ENV || "development"} mode on port ${PORT}`
    );
  });
});

export default app;