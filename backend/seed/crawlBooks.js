import mongoose from "mongoose";
import dotenv from "dotenv";
import axios from "axios";
import Book from "../src/models/bookModel.js";

dotenv.config();

const CATEGORIES = [
  "programming",
  "classic",
  "self-help",
  "science",
  "history",
  "business",
  "fantasy",
  "mystery"
];

function getCoverUrl(coverId) {
  if (!coverId) return "";
  return `https://covers.openlibrary.org/b/id/${coverId}-L.jpg`;
}

async function fetchBooksByCategory(category) {
  const url = `https://openlibrary.org/subjects/${category}.json?limit=10`;
  const response = await axios.get(url);

  const works = response.data.works || [];

  return works.map((item) => {
    const author =
      item.authors && item.authors.length > 0
        ? item.authors.map((a) => a.name).join(", ")
        : "Unknown Author";

    const coverUrl = getCoverUrl(item.cover_id);
    const stock = Math.floor(Math.random() * 5) + 1;

    return {
      title: item.title || "Untitled",
      author,
      category,
      coverImage: coverUrl,
      img: coverUrl,
      image: coverUrl,
      description: item.subject
        ? item.subject.slice(0, 5).join(", ")
        : "No description available.",
      intro: item.title
        ? `A book about ${category}: ${item.title}.`
        : `A book about ${category}.`,
      fileUrl: "",
      pages: 0,
      availableCopies: stock,
      stock,
      available: stock,
    };
  });
}

async function crawlAndSeedBooks() {
  try {
    await mongoose.connect(process.env.MONGO_URI);
    console.log("Connected to MongoDB");

    const allBooks = [];

    for (const category of CATEGORIES) {
      console.log(`Fetching category: ${category}`);
      const books = await fetchBooksByCategory(category);
      allBooks.push(...books);
    }

    await Book.deleteMany({});
    await Book.insertMany(allBooks);

    console.log(`Inserted ${allBooks.length} books successfully`);
    process.exit(0);
  } catch (error) {
    console.error("Crawl books failed:", error);
    process.exit(1);
  }
}

crawlAndSeedBooks();
