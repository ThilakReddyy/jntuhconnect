# 📚 JNTUH Connect

<p align="center">
  <img src="app/src/main/res/ic_launcher-web.png" alt="JNTUH Connect Logo" width="100"/>
</p>

<p align="center">
  <b>Your one-stop Android app for JNTUH students — check results, stay updated, and explore opportunities.</b>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android" alt="Platform"/>
  <img src="https://img.shields.io/badge/Language-Kotlin-7F52FF?style=for-the-badge&logo=kotlin" alt="Kotlin"/>
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpack-compose" alt="Jetpack Compose"/>
  <img src="https://img.shields.io/badge/Min%20SDK-24-green?style=for-the-badge" alt="Min SDK"/>
  <img src="https://img.shields.io/badge/Version-1.0.43-orange?style=for-the-badge" alt="Version"/>
</p>

<p align="center">
  <a href="https://play.google.com/store/apps/details?id=com.dhethi.jntuhconnect">
    <img src="https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png" alt="Get it on Google Play" height="70"/>
  </a>
</p>

---

## ✨ Features

| Feature | Description |
|---|---|
| 🔍 **Result Search** | Search JNTUH exam results by roll number, with recent-search history |
| 📊 **Student Results** | Gradient result hero, animated **CGPA ring**, and semester cards with color-coded grade pills |
| 🎓 **Credits Tracker** | Year-wise credit progress bars to track your academic completion |
| 🏆 **Class Results** | Full class rankings with 🥇🥈🥉 medals |
| ⚖️ **Result Contrast** | Compare two students' results side by side |
| 📝 **Grace Marks** | Instant eligibility check + upload supporting proof |
| 📢 **Latest Updates** | Real-time JNTUH notifications with filters (Results, Exams, Timetables) |
| 🗓️ **Academic Calendars** | Live, drill-down academic calendars fetched from the backend |
| 📖 **Syllabus** | Browse full syllabus trees by branch, year & subject |
| 💼 **Careers** | Discover job and internship opportunities |
| 📡 **Channels & Help** | Useful JNTUH channels plus in-app help |
| 🎨 **Premium UI** | "Academic Emerald" design system with gradients, animations & dark mode |
| 🔔 **Push Notifications** | Get notified instantly via Firebase Cloud Messaging |

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
| **CI/CD** | GitHub Actions → Google Play |

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
│   ├── remote/                # Retrofit API interface, DTOs & mappers
│   └── repository/            # Repository implementations
│
├── domain/                    # Domain Layer (Business Logic)
│   ├── model/                 # Domain models (results, content trees, etc.)
│   ├── repository/            # Repository interfaces
│   └── use_case/              # Use cases (GetAllResults, GetContent, etc.)
│
├── di/                        # Hilt Dependency Injection modules
│
├── presentation/              # Presentation Layer (UI)
│   ├── components/            # Reusable composables (hero, cards, pills, rings…)
│   ├── theme/                 # "Academic Emerald" colors, dimens & theming
│   ├── home/                  # Home (search) screen
│   ├── explore/               # Explore tools grid
│   ├── profile/               # Profile screen
│   ├── studentResult/         # Detailed student result screen
│   ├── classResult/           # Class ranking screen
│   ├── resultContrast/        # Side-by-side result comparison
│   ├── graceMarks/            # Grace marks eligibility + proof upload
│   ├── updates/               # JNTUH notifications/updates screen
│   └── content/               # Calendars, Syllabus, Channels, Careers, Help
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
| `GET /calendars` | Fetch the academic calendar tree |
| `GET /syllabus` | Fetch the syllabus tree |

---

## 🚀 Getting Started

### Prerequisites

- Android Studio **Ladybug** (2024.2) or later
- JDK 17+ (required by Android Gradle Plugin 8.x)
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

## 📱 Navigation

The app is organized into **3 tabs**, with everything else reachable from them:

| Tab | Description |
|---|---|
| 🏠 **Home** | Search results by roll number; jump into student/class results |
| 🧭 **Explore** | Tools grid — Class Result, Result Contrast, Grace Marks, Calendars, Syllabus, Careers, Channels, Help |
| 👤 **Profile** | Student profile, saved roll number, updates & settings |

---

## 🔔 Notifications

The app uses **Firebase Cloud Messaging (FCM)** to deliver real-time push notifications for:
- New exam results published
- Examination timetable releases
- Important JNTUH announcements

Notification permission is requested at runtime on Android 13+ (`TIRAMISU` and above).

### Send a test notification from Python

The test sender defaults to the `result-updates` topic and can also target one
Firebase Installation ID:

```bash
python3 -m pip install -r scripts/requirements-fcm.txt
export GOOGLE_APPLICATION_CREDENTIALS="/absolute/path/to/firebase-admin-key.json"

# Send to one test phone
python3 scripts/send_fcm_notification.py \
  --title "Test notification" \
  --body "Firebase messaging is working" \
  --fid "YOUR_FIREBASE_INSTALLATION_ID"

# Send to every app installation subscribed to result-updates
python3 scripts/send_fcm_notification.py \
  --title "Results released" \
  --body "Tap to view the latest JNTUH results" \
  --link "https://jntuhresults.dhethi.com/"
```

Create the private key from Firebase Console → Project settings → Service
accounts → Generate new private key. Keep that file outside this repository;
`google-services.json` configures the Android client and cannot authorize sends.

---

## 📦 Build Configuration

| Property | Value |
|---|---|
| Application ID | `com.dhethi.jntuhconnect` |
| Min SDK | 24 (Android 7.0 Nougat) |
| Target SDK | 36 |
| Compile SDK | 36 |
| Android Gradle Plugin | 8.13.0 |
| Version | Auto-managed (see below) |

### Versioning
`versionCode` is the single source of truth in [`app/version.properties`](app/version.properties);
`versionName` is derived as `1.0.<versionCode>`. It bumps **automatically**:
- **Local release builds** (`assembleRelease` / `bundleRelease`) increment `version.properties`.
- **CI builds** derive `versionCode = 18 + GITHUB_RUN_NUMBER`, guaranteeing an ever-increasing code.

### Release Build
The release build has **ProGuard/R8 minification enabled** with full NDK debug symbols for better crash reports.

---

## 🤖 CI/CD — Auto-deploy to Play Store

Every push to `main` triggers [`.github/workflows/deploy-play-store.yml`](.github/workflows/deploy-play-store.yml), which:

1. Builds a **signed release AAB** (`bundleRelease`).
2. Uploads it to the Play Store **internal** track via the Google Play Developer API.
3. Updates the version badge in this README to the deployed version.

Requires these GitHub repository secrets: `KEYSTORE_BASE64`, `KEYSTORE_PASSWORD`,
`KEY_ALIAS`, `KEY_PASSWORD`, and `PLAY_SERVICE_ACCOUNT_JSON`.

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
