# ⚡ GuessTheCry - Pokémon Sound Quiz

**GuessTheCry** is an interactive web application where you listen to a Pokémon cry and guess which Pokémon it is. Choose between **Normal Mode** (multiple choice) or **Expert Mode** (just sound and a hint). Can you score a perfect 10/10?

---

## 🚀 Features

* 🎧 **Authentic Pokémon cries** streamed from MinIO (S3-compatible)
* 🧠 **Normal Mode**: Multiple-choice with images
* 🔥 **Expert Mode**: Only the cry, optional hints, and visual feedback after answering
* ⚙️ **Backend**: Java 21 with Spring Boot and Jersey
* 🎨 **Frontend**: React + TypeScript + Vite + Tailwind CSS
* 🗃️ **Database**: PostgreSQL for metadata
* 🌐 **S3 Storage**: MinIO for hosting sound files

---

## 📁 Project Structure

```bash
GuessTheCry/
├── backend/                 # Java Spring Boot + Jersey API
│   ├── src/main/java/com/guessthecry
│   ├── application.properials
│   └── build.gradle
├── frontend/                # React + Vite + TypeScript + Tailwind
│   ├── src/App.tsx
│   ├── tailwind.config.js
│   └── postcss.config.js
├── README.md
```

---

## ⚙️ Getting Started

### 1. Backend Setup

Make sure you have Java 21 and PostgreSQL installed. You also need a MinIO server running and configured.

```bash
cd backend
./gradlew bootRun
```

> Edit `application.properties` to set your database and S3/MinIO credentials.

### 2. Frontend Setup

You need Node.js (v18+ recommended):

```bash
cd frontend
npm install
npm run dev
```

> Frontend runs at `http://localhost:5173` and expects the backend at `http://localhost:8080`.

---

## 🔌 API Overview

### `GET /api/quiz/question?mode=normal|expert`

Returns a new quiz question depending on the selected mode.

#### 📦 Normal Mode response

```json
{
  "pokemonName": "squirtle",
  "audioUrl": "http://...",
  "choices": [
    { "name": "pikachu", "imageUrl": "..." },
    { "name": "bulbasaur", "imageUrl": "..." },
    { "name": "squirtle", "imageUrl": "..." },
    { "name": "charmander", "imageUrl": "..." }
  ]
}
```

#### 🧠 Expert Mode response

```json
{
  "pokemonName": "charmander",
  "audioUrl": "...",
  "hint": "Fire",
  "imageUrl": "https://..." // only shown after correct answer
}
```

---

## 🧪 Technologies Used

| Category     | Tech Stack                            |
| ------------ | ------------------------------------- |
| Frontend     | React, TypeScript, Vite               |
| Styling      | Tailwind CSS                          |
| Backend      | Java 21, Spring Boot, Jersey (JAX-RS) |
| Persistence  | Spring Data JPA, PostgreSQL           |
| File Storage | MinIO (S3-compatible)                 |
| Deployment   | Dev-mode local with Gradle and Vite   |

---

## 📸 Screenshots

-

---

## 🛑 Disclaimer

All Pokémon names, sounds, and images belong to Nintendo, Game Freak, and The Pokémon Company. This project is for educational purposes only and is not intended for commercial use.

---

## 🙌 Credits

Made with ❤️ by **Thijs Herman**

* Sounds by [Pokémon Showdown](https://play.pokemonshowdown.com/audio/cries/)
* Images via [PokéAPI Artwork CDN](https://pokeapi.co/)

