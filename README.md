<div align="center">

# ⚽ Soccer Manager: Sports Club Management System

**A desktop app for running a football club: sign players, record matches and plan training sessions.**

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX_21-007396?style=for-the-badge&logo=java&logoColor=white)
![SQLite](https://img.shields.io/badge/SQLite-003B57?style=for-the-badge&logo=sqlite&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)

</div>

## ✨ Features
- 🔒 **Secure login** screen for club staff
- 📊 **Dashboard** that links to every module
- ✍️ **Player signing (enrollment):** add players with their position, market value and status
- 👥 **Squad list:** view and remove players
- 🏟️ **Match results:** record scores and delete old matches
- 🏋️ **Training sessions:** schedule training for players
- 💾 Everything is stored locally in **SQLite**, with no server needed
- 🎨 Custom JavaFX styling in `style.css`

## 🧱 Tech Stack
| Layer | Tech |
|---|---|
| UI | JavaFX 21 (one view class per screen) |
| Data | SQLite via `sqlite-jdbc` (`DatabaseHandler`) |
| Build | Maven + `javafx-maven-plugin` |

## 🚀 Run it
```bash
git clone https://github.com/Muktaditbf/Soccer-Manager.git
cd Soccer-Manager
mvn clean javafx:run
```
Requires JDK 17+ and Maven. The database file (`sports_club_v3.db`) is created automatically on first run.

## 👤 Author
**Muktadi** · CSE @ Southeast University · [GitHub](https://github.com/Muktaditbf) · [LinkedIn](https://www.linkedin.com/in/muktadi-mohammad)
