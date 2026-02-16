# KarteikartenSystem

A desktop flashcard application built with Java and JavaFX, including SQLite persistence and a custom learning workflow.

<img width="1091" height="726" alt="Bildschirmfoto 2026-02-16 um 00 58 23" src="https://github.com/user-attachments/assets/b2de4889-0a8f-49ad-ae3f-ce4923959fba" />

## Features

- Create and manage decks
- Create, edit and delete flashcards
- Quiz mode with recognition level tracking
- SQLite-based local persistence
- Progress tracking with visual feedback
- Cross-platform packaging (Windows .exe & macOS .dmg)

## Tech Stack

- Java 21
- JavaFX (FXML)
- CSS
- SQLite
- Maven
- jpackage
- DAO Pattern

## Architecture

The application follows a layered architecture:

- **Core Layer** – Business logic (Deck, Karte, RecognitionLevel)
- **DAO Layer** – Database access using SQLite
- **GUI Layer** – JavaFX + FXML controllers
- **Helper Layer** – Database management and utilities

The project uses the DAO pattern to separate persistence logic from business logic.

## Run the Application

### macOS
Download the `.dmg` file from the Releases section and install the app.

### Windows
Download the `.exe` installer from the Releases section.

### From Source
```bash
mvn clean package
java -jar target/KarteikartenSystem.jar
```

---

## Future Improvements

- Spaced repetition algorithm
- Import/Export functionality
- Statistics dashboard
- REST backend integration

## Why I Built This


```markdown
## Motivation

This project was built to deepen my understanding of:

- Desktop application architecture
- modeling and practical implementation
- Database design and SQL
- Maven build management
- Cross-platform packaging
- Separation of concerns in software design
