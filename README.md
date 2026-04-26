# Book4U - Library Management Mobile App

Book4U is a mobile library management application built with Android Java and a Node.js/Express backend.  
The app supports two roles: **Student** and **Admin**.

Students can browse books, request to borrow books, track borrowed books, view history, and receive notifications.  
Admins can manage books, approve/reject borrow requests, confirm returns, view dashboard statistics, and receive notifications.

---

## 1. Project Structure

```text
Book4U-mobile_app/
│
├── app/                         # Android mobile app
│   └── src/main/java/com/example/book4u/
│       ├── activities/           # Android activities
│       ├── adapters/             # RecyclerView adapters
│       ├── fragments/            # Student/Admin/Shared fragments
│       ├── models/               # Data models
│       ├── network/              # Retrofit API service
│       ├── repository/           # Repository layer
│       ├── storage/              # SessionManager
│       └── utils/                # Constants and helpers
│
├── backend/                      # Node.js + Express backend
│   ├── src/
│   │   ├── config/               # MongoDB connection
│   │   ├── controllers/          # API controllers
│   │   ├── middleware/           # Auth and role middleware
│   │   ├── models/               # Mongoose models
│   │   └── routes/               # API routes
│   ├── seed/                     # Seed and crawl scripts
│   ├── server.js                 # Backend entry point
│   ├── package.json
│   └── .env
│
└── README.md
```

---

## 2. Technologies Used

### Mobile App

- Java
- Android Studio
- Retrofit
- Gson
- OkHttp Logging Interceptor
- RecyclerView
- Material Components

### Backend

- Node.js
- Express.js
- MongoDB
- Mongoose
- JWT Authentication
- bcryptjs
- dotenv
- cors
- Swagger

---

## 3. Main Features

### Student

- Login
- View book list from backend
- Search books
- Send borrow request
- View current borrowed books
- View borrow history
- View notifications
- Profile and logout

### Admin

- Login
- Dashboard with real-time statistics
- Manage books
    - Add book
    - Edit book
    - Delete book
    - Update book availability
- Manage borrow requests
    - Approve request
    - Reject request
    - Confirm book return
- View notifications
- Profile and logout

---

## 4. Backend Setup

### Step 1: Open backend folder

```bash
cd backend
```

### Step 2: Install dependencies

```bash
npm install
```

### Step 3: Create `.env` file

Create a `.env` file inside the `backend` folder:

```env
PORT=5001
MONGO_URI=mongodb://127.0.0.1:27017/book4u
JWT_SECRET=book4u_secret_key
NODE_ENV=development
```

### Step 4: Start MongoDB

If MongoDB was installed using Homebrew:

```bash
brew services start mongodb/brew/mongodb-community
```

Check MongoDB service:

```bash
brew services list
```

### Step 5: Run backend server

```bash
npm run dev
```

If the backend runs successfully, the terminal should show:

```text
Connected to MongoDB...
Server running in development mode on port 5001
```

### Step 6: Test backend

Open this URL in browser:

```text
http://localhost:5001/api/health
```

Expected response:

```json
{
  "message": "Book4U API is running",
  "status": "ok"
}
```

---

## 5. Seed Database

### Seed default users and sample books

From the `backend` folder, run:

```bash
node seed/seed.js
```

### Crawl book database

To crawl and insert more books into MongoDB:

```bash
node seed/crawlBooks.js
```

This script inserts around 80 books into the database.

> Note: The crawl script may clear the old books collection before inserting new books, depending on the script configuration.

---

## 6. Test Accounts

### Admin Account

```text
Email: admin@book4u.com
Password: 123456
```

### Student Account

```text
Email: student@book4u.com
Password: 123456
```

---

## 7. Android App Setup

### Step 1: Open project in Android Studio

Open the root folder:

```text
Book4U-mobile_app
```

### Step 2: Check backend base URL

In Android app, open:

```text
app/src/main/java/com/example/book4u/utils/Constants.java
```

Make sure the base URL is:

```java
public static final String BASE_URL = "http://10.0.2.2:5001/";
```

Explanation:

```text
localhost:5001     → used by browser on computer
10.0.2.2:5001      → used by Android Emulator to call backend on computer
```

### Step 3: Sync Gradle

In Android Studio:

```text
File → Sync Project with Gradle Files
```

### Step 4: Run app

Start the backend first:

```bash
cd backend
npm run dev
```

Then run the Android app using Android Studio.

---

## 8. API Endpoints

### Auth

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/login` | Login |
| POST | `/api/auth/register` | Register |
| GET | `/api/auth/me` | Get current user |

### Books

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/books` | Get all books |
| GET | `/api/books/:id` | Get book detail |
| POST | `/api/books` | Create book |
| PUT | `/api/books/:id` | Update book |
| DELETE | `/api/books/:id` | Delete book |

### Borrow

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/borrow` | Create borrow request |
| GET | `/api/borrow/my` | Get current student's borrow list |
| GET | `/api/borrow` | Get all borrow requests |
| PUT | `/api/borrow/:id/approve` | Approve borrow request |
| PUT | `/api/borrow/:id/reject` | Reject borrow request |
| PUT | `/api/borrow/:id/return` | Confirm book return |

### Health Check

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/health` | Check backend status |

---

## 9. Demo Flow

### Student Borrow Flow

```text
1. Login as student
2. Open Books screen
3. Choose a book
4. Click Borrow
5. Borrow request is created with Pending Approval status
6. Open My Borrow to track request status
```

### Admin Approval Flow

```text
1. Login as admin
2. Open Borrow Requests screen
3. View pending borrow requests
4. Approve or reject request
5. If approved, the book status becomes Borrowing
6. If returned, admin confirms Return
```

### Student History Flow

```text
1. Login as student
2. Open My Borrow
3. After admin confirms return
4. Returned book appears in History
```

### Admin Dashboard Flow

```text
1. Login as admin
2. Open Dashboard
3. View:
   - Total Books
   - Available Books
   - Pending Requests
   - Borrowing
   - Returned
   - Rejected
```

---

## 10. Notification Logic

The notification screen is generated based on borrow status.

### Student Notifications

- Borrow request submitted
- Borrow request approved
- Borrow request rejected
- Book returned successfully
- Book overdue

### Admin Notifications

- New borrow request
- Request approved
- Request rejected
- Book returned
- Book overdue

Notifications are stored locally in the Android app using SharedPreferences and are synchronized from borrow data.

---

## 11. Common Issues

### Backend cannot connect to MongoDB

Error:

```text
connect ECONNREFUSED 127.0.0.1:27017
```

Solution:

```bash
brew services start mongodb/brew/mongodb-community
```

---

### Port 5000 already in use

The project uses port `5001` to avoid port conflicts.

Check `.env`:

```env
PORT=5001
```

---

### Android cannot connect to backend

Make sure the backend is running:

```bash
cd backend
npm run dev
```

Make sure Android uses:

```java
http://10.0.2.2:5001/
```

Do not use this in Android Emulator:

```java
http://localhost:5001/
```

---

### Gradle or Material resource error

Try:

```bash
rm -rf .gradle
rm -rf app/build
rm -rf build
./gradlew clean
```

Then in Android Studio:

```text
File → Sync Project with Gradle Files
Build → Rebuild Project
```

---

## 12. Notes

- Backend and Android app are stored in the same project repository.
- Backend is located in the `backend/` folder.
- Android app is located in the `app/` folder.
- The backend must be running before testing features that use API data.
- MongoDB must be running before starting the backend server.

---

## 13. Project Status

Current status:

```text
Completed:
- Authentication
- Book management
- Borrow/return workflow
- Student history
- Admin dashboard
- Notifications
- MongoDB book database
- Android FE connected to Node.js BE
```

The application is ready for demo and final report.
