# ⚡ GuessTheCry - Pokémon Sound Quiz

**GuessTheCry** is an interactive web application where you listen to a Pokémon cry and guess which Pokémon it is. Create an account, choose your favorite generation, and test your knowledge in **Normal Mode** (multiple choice) or **Expert Mode** (sound only with optional hints). Can you get a perfect score and top your personal leaderboard?

---

## 🚀 Features

*   🔐 **User Authentication**: Secure registration and login system using JWT.
*   🎧 **Self-Hosted Assets**: Pokémon cries and high-quality sprites are self-hosted on S3-compatible storage container for reliability and performance.
*   🕹️ **Secure Game Loop**: All game logic and scoring are handled server-side to prevent cheating.
*   🧠 **Two Game Modes**:
    *   **Normal Mode**: Multiple-choice with images.
    *   **Expert Mode**: Guess by sound only, with optional, weighted hints.
*   📈 **Personal Profile**: Track your game statistics, including games played and average accuracy per mode.
*    поколения **Generation Filter**: Play with Pokémon from a specific generation or all generations combined.
*   ⚡ **High-Performance Backend**: Built with Java 21, Spring Boot, and Jersey, featuring an intelligent caching layer for external APIs and database queries.
*   🎨 **Modern Frontend**: A responsive and fast UI built with React, TypeScript, Vite, and Tailwind CSS.

---

## 📁 Project Structure

```bash
GuessTheCry/
├── backend/                 # Java Spring Boot + Jersey API
│   ├── src/main/java/com/guessthecry
│   └── build.gradle
├── frontend/                # React + Vite + TypeScript + Tailwind
│   ├── src/
│   └── package.json
├── scripts/                 # Scripts for fetching assets and generating data
│   └── create-update-pokemon/
│       └── generate-pokemon.mjs
├── README.md
```

---

## ⚙️ Getting Started

### Prerequisites
- Java 21
- Node.js (v18+)
- PostgreSQL
- S3-compatible Server

### 1. Backend Setup

1.  **Configure Environment**: Copy `.env.example` to `.env` in the `backend/` directory and fill in your PostgreSQL and S3 credentials.
2.  **Run the application**:
    ```bash
    cd backend
    ./gradlew bootRun
    ```
The backend will start on `http://localhost:8080`.

### 2. Frontend Setup

1.  **Install dependencies**:
    ```bash
    cd frontend
    npm install
    ```
2.  **Run the development server**:
    ```bash
    npm run dev
    ```
The frontend is now available at `http://localhost:5173`.

### 3. Generate and Upload Pokémon Assets

The script in the `scripts/` directory is essential for populating your S3 server and creating the necessary data for the backend.

1.  **Configure Script**: Ensure the `.env` file in the `backend/` directory is correctly set up, as the script reads from it.
2.  **Run the script**:
    ```bash
    cd scripts/create-update-pokemon
    npm install # Install dependencies like 'got' and 'aws-sdk'
    node generate-pokemon.mjs
    ```

This script will:
-   Download all `.mp3` cries from Pokémon Showdown.
-   Download all official artwork sprites from PokéAPI's CDN.
-   Upload cries and sprites to ಅವರವರ S3-buckets (`pokemon-cries` en `pokemon-sprites`).
-   Generate a `pokemon-data.json` file.

**Important**: After running the script, move the generated `pokemon-data.json` to `backend/src/main/resources/data/`. The backend will use this file to seed de database bij de eerste opstart.

---

## 🔌 API Overview (New Secure Flow)

### Authentication
*   `POST /api/auth/register` - Create a new user account.
*   `POST /api/auth/login` - Log in to receive a JWT.

### Game Flow
*   `POST /api/game/start?mode=...&generation=...`
    - Starts a new game session.
    - **Requires JWT.**
    - Returns a `gameId` and the first question.

*   `POST /api/game/{gameId}/answer`
    - Submits a user's answer for the current question.
    - **Requires JWT.**
    - Returns whether the answer was correct and provides the next question or the final game results.

### Profile
*   `GET /api/auth/{userId}/stats`
    - Retrieves the game statistics for a given user.
    - **Requires JWT.**

---

## 🧪 Technologies Used

| Category      | Tech Stack                                  |
| ------------- | ------------------------------------------- |
| Frontend      | React, TypeScript, Vite                     |
| Styling       | Tailwind CSS                                |
| State Mgt.    | React Context                               |
| Backend       | Java 21, Spring Boot, Jersey (JAX-RS)       |
| Persistence   | Spring Data JPA, PostgreSQL                 |
| Security      | Spring Security, JWT                        |
| Caching       | Spring Cache                                |
| File Storage  | S3                                          |
| Scripting     | Node.js                                     |

---

## 📸 Screenshots

- Coming soon...

---

## 🛑 Disclaimer

This project is a non-commercial, educational endeavor. All Pokémon-related names, sounds, and images are the property of Nintendo, Game Freak, and The Pokémon Company.

---

## 🙌 Credits

Made with ❤️ by **Thijs Herman**

*   Sounds sourced from [Pokémon Showdown](https://play.pokemonshowdown.com/audio/cries/).
*   Original sprites and Pokémon data sourced from [PokéAPI](https://pokeapi.co/).