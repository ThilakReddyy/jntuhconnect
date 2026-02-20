# 📚 JNTUH Connect

<p align="center">
  <img src="app/src/main/res/mipmap-hdpi/ic_launcher.webp" alt="JNTUH Connect Logo" width="100"/>
</p>

<p align="center">
  <b>Your one-stop Android app for JNTUH students — check results, stay updated, and explore opportunities.</b>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android" alt="Platform"/>
  <img src="https://img.shields.io/badge/Language-Kotlin-7F52FF?style=for-the-badge&logo=kotlin" alt="Kotlin"/>
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpack-compose" alt="Jetpack Compose"/>
  <img src="https://img.shields.io/badge/Min%20SDK-24-green?style=for-the-badge" alt="Min SDK"/>
  <img src="https://img.shields.io/badge/Version-1.0.15-orange?style=for-the-badge" alt="Version"/>
</p>

---

## ✨ Features

| Feature | Description |
|---|---|
| 🔍 **Result Search** | Search JNTUH exam results by roll number |
| 📊 **All Results** | View complete semester-wise academic performance |
| 🎓 **Academic Results** | Track subject-wise marks and grades |
| ⚠️ **Backlog Results** | Quickly see pending backlogs |
| 📢 **Latest Updates** | Stay informed with real-time JNTUH notifications (Results, Exams, Timetables) |
| 💼 **Jobs Board** | Discover job and internship opportunities |
| 👤 **Profile** | Manage your student profile |
| 📄 **PDF Viewer** | View and access documents directly in-app |
| 🔔 **Push Notifications** | Get notified instantly via Firebase Cloud Messaging |
| 💾 **Recent Searches** | Local history of recently searched roll numbers |

---

## 🛠️ Tech Stack

| Category | Technology |
|---|---|
| **Language** | Kotlin |
| **UI Framework** | Jetpack Compose + Material 3 |
| **Architecture** | Clean Architecture (Data → Domain → Presentation) |
| **Dependency Injection** | Hilt (Dagger) |
| **Networking** | Retrofit 2 + Gson |
| **Local Database** | Room |
| **Navigation** | Navigation Compose |
| **State Management** | ViewModel + Compose State |
| **Notifications** | Firebase Cloud Messaging (FCM) |
| **Preferences** | DataStore Preferences |
| **Browser** | AndroidX Custom Tabs |

---

## 🏗️ Architecture

The project follows **Clean Architecture** principles, organized into three clear layers:

```
com.dhethi.jntuhconnect/
│
├── common/                    # Shared constants and utility classes
│
├── data/                      # Data Layer
│   ├── local/                 # Room database, DAOs, and entities
│   ├── remote/                # Retrofit API interface and DTOs
│   └── repository/            # Repository implementations
│
├── domain/                    # Domain Layer (Business Logic)
│   ├── model/                 # Domain models
│   ├── repository/            # Repository interfaces
│   └── use_case/              # Use cases (GetAllResults, GetAcademicResult, etc.)
│
├── di/                        # Hilt Dependency Injection modules
│
├── presentation/              # Presentation Layer (UI)
│   ├── components/            # Reusable composable components
│   ├── results/               # Roll Number search screen
│   ├── studentResult/         # Detailed student result screen
│   ├── updates/               # JNTUH notifications/updates screen
│   ├── jobs/                  # Jobs board screen
│   ├── profile/               # Profile screen
│   ├── pdf/                   # PDF viewer screen
│   └── theme/                 # App colors, typography, and themes
│
└── service/                   # Firebase Messaging Service
```

---

## 🌐 API

The app communicates with the backend at:

```
https://jntuhresults.dhethi.com/api/
```

### Key Endpoints

| Endpoint | Description |
|---|---|
| `GET /getAllResult?rollNumber=` | Fetch all semester results for a student |
| `GET /getAcademicResult?rollNumber=` | Fetch academic/regular results |
| `GET /getBacklogs?rollNumber=` | Fetch backlog subjects |
| `GET /notifications?page=&category=` | Fetch paginated JNTUH notifications |

---

## 🚀 Getting Started

### Prerequisites

- Android Studio **Hedgehog** (2023.1.1) or later
- JDK 11+
- Android SDK with API Level **24** or higher
- A `google-services.json` file (Firebase configuration)

### Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/ThilakReddyy/jntuhconnect.git
   cd JntuhConnect
   ```

2. **Open in Android Studio**
   - Open Android Studio → `File` → `Open` → Select the cloned folder

3. **Add Firebase Config**
   - Place your `google-services.json` file in the `app/` directory  
   - (Required for Firebase Cloud Messaging / push notifications)

4. **Build & Run**
   - Connect an Android device or start an emulator
   - Click ▶️ **Run** or use `Shift + F10`

---

## 📱 Screens

| Screen | Route | Description |
|---|---|---|
| Results Search | `results` | Home screen — search by roll number |
| Student Results | `studentResults/{rollNumber}` | Detailed result view with tabs |
| Updates | `updates` | Latest JNTUH notifications |
| Jobs | `jobs` | Job listings and opportunities |
| Profile | `profile` | Student profile management |

---

## 🔔 Notifications

The app uses **Firebase Cloud Messaging (FCM)** to deliver real-time push notifications for:
- New exam results published
- Examination timetable releases
- Important JNTUH announcements

Notification permission is requested at runtime on Android 13+ (`TIRAMISU` and above).

---

## 📦 Build Configuration

| Property | Value |
|---|---|
| Application ID | `com.dhethi.jntuhconnect` |
| Min SDK | 24 (Android 7.0 Nougat) |
| Target SDK | 36 |
| Compile SDK | 36 |
| Version Name | 1.0.15 |
| Version Code | 15 |

### Release Build
The release build has **ProGuard/R8 minification enabled** with full NDK debug symbols for better crash reports.

---

## 🤝 Contributing

Contributions are welcome! If you'd like to improve the app:

1. Fork the repository
2. Create a new branch: `git checkout -b feature/your-feature-name`
3. Commit your changes: `git commit -m 'Add some feature'`
4. Push to the branch: `git push origin feature/your-feature-name`
5. Open a Pull Request

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).

---

## 👨‍💻 Author

**Thilak Reddy**  
GitHub: [@ThilakReddyy](https://github.com/ThilakReddyy)

---

<p align="center">Made with ❤️ for JNTUH Students</p>
