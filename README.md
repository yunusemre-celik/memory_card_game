# 🧠 Memory Match & Collect Game

A robust, feature-rich Memory Card Game developed in Java using the Swing framework. This project demonstrates advanced Object-Oriented Programming (OOP) principles, Model-View-Controller (MVC) architecture, and custom UI component design.

![Game Banner](screenshots/menu_preview.png)
*(Replace this line with an actual screenshot of your main menu)*

## 📋 Table of Contents
- [About the Project](#-about-the-project)
- [Key Features](#-key-features)
- [Game Modes](#-game-modes)
- [Technical Architecture](#-technical-architecture)
- [Installation & How to Run](#-installation--how-to-run)
- [How to Play](#-how-to-play)
- [Future Improvements](#-future-improvements)

## 📖 About the Project
**Memory Match** is more than just a simple puzzle game. It is a comprehensive software engineering project designed to simulate a card matching experience with a smart AI opponent, dynamic theming, and persistent high scores.

The objective is simple: Find pairs of matching cards on a grid. The player with the most pairs at the end wins.

## ✨ Key Features

* **🤖 Intelligent AI Opponent:**
    * Unlike random bots, this AI has a "Memory" system.
    * It remembers cards revealed on the board based on probability.
    * **Three Difficulty Levels:** Easy, Normal, and Hard (Perfect Memory).
* **🎨 Dynamic Theming Engine:**
    * Switch between **Cozy Pastel**, **Cyberpunk**, and **Sapphire Glass** themes at runtime.
    * Recursive UI updating without restarting the application.
* **💾 Persistence:**
    * High scores are saved locally to `highscores.txt`.
    * Automatic sorting and "Top 10" management.
* **⚡ Smooth Animations:**
    * Custom `CardButton` component uses `javax.swing.Timer` and `AlphaComposite` for smooth cross-fade flip animations.
    * Non-blocking threading ensures the UI never freezes during AI turns.
* **📱 Responsive Grid:**
    * Supports grid sizes from 4x4 up to 10x10.
    * Card images automatically scale to fit the window size.

## 🎮 Game Modes

### 1. Single Player (vs Computer)
Challenge the AI engine. You can configure the AI's intelligence in the setup menu:
* **Easy:** The AI has a 10% memory retention rate. It plays mostly randomly.
* **Normal:** The AI has a 50% memory retention rate. It plays like an average human.
* **Hard:** The AI has 100% memory retention. Once a card is revealed, the AI *never* forgets it.

### 2. Two Player (PvP)
Hot-seat multiplayer mode. Two human players take turns on the same computer.
* If a player finds a match, they get **+10 points** and go again.
* If they miss, the turn passes to the next player.

## 🏗 Technical Architecture
The project strictly follows the **MVC (Model-View-Controller)** pattern:

* **Model:** (`Card`, `Deck`, `Player`) Handles state, data logic, and encapsulation.
* **View:** (`MainFrame`, `GamePanel`, `Theme`) Handles rendering and user interaction.
* **Controller:** (`GameEngine`) Manages game rules, turn switching, and score validation.

**Key OOP Concepts Used:**
* **Polymorphism:** `ComputerPlayer` overrides `playTurn()` to implement AI logic.
* **Inheritance:** `CardButton` extends Swing's `JButton`.
* **Encapsulation:** Strict access control for game state variables.

## 🚀 Installation & How to Run

### Prerequisites
* Java Development Kit (JDK) 8 or higher.
* An IDE (IntelliJ IDEA, Eclipse) or Terminal.

### Steps
1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/yourusername/memory-match-game.git](https://github.com/yourusername/memory-match-game.git)
    ```
2.  **Navigate to the project folder:**
    ```bash
    cd memory-match-game
    ```
3.  **Compile the code:**
    ```bash
    javac -d bin src/**/*.java
    ```
4.  **Run the application:**
    ```bash
    java -cp bin main.Main
    ```

## 🕹 How to Play

1.  **Start:** Click "New Game" on the main menu.
2.  **Setup:** Enter player names, select the grid size (e.g., 4x4), and choose the mode (PvP or Single Player).
3.  **Play:**
    * Click a card to reveal it.
    * Click a second card to try and find its pair.
    * **Match:** Cards stay face up. You get 10 points. Take another turn.
    * **Mismatch:** Cards flip back down. Turn ends.
4.  **Win:** The game ends when all pairs are found. The player with the highest score wins.

## 🔮 Future Improvements
* [ ] Network Multiplayer (LAN/Online).
* [ ] Sound Effects for card flips and matches.
* [ ] Save/Load functionality for games in progress.

---
**Course:** CS 202 - Object Oriented Programming
**Student:** [Your Name]
**Department:** Computer Engineering
