# 📚 JNTUH Connect

<p align="center">
  <img src="app/src/main/res/ic_launcher-web.png" alt="JNTUH Connect Logo" width="100"/>
</p>

<p align="center">
  <b>Your one-stop Android app for JNTUH students — check results, stay updated, and explore opportunities.</b>
</p>

<p align="center">
  <a href="https://github.com/ThilakReddyy/jntuhconnect/actions/workflows/deploy-play-store.yml"><img src="https://github.com/ThilakReddyy/jntuhconnect/actions/workflows/deploy-play-store.yml/badge.svg" alt="Play Store deployment status"/></a>
  <a href="https://github.com/ThilakReddyy/jntuhconnect"><img src="https://img.shields.io/github/languages/code-size/ThilakReddyy/jntuhconnect?style=flat-square" alt="Code size"/></a>
  <a href="https://github.com/ThilakReddyy/jntuhconnect/commits/main"><img src="https://img.shields.io/github/last-commit/ThilakReddyy/jntuhconnect?style=flat-square" alt="Last commit"/></a>
  <a href="https://github.com/ThilakReddyy/jntuhconnect/blob/main/LICENSE"><img src="https://img.shields.io/github/license/ThilakReddyy/jntuhconnect?style=flat-square" alt="License"/></a>
</p>

<p align="center">
  <a href="https://play.google.com/store/apps/details?id=com.dhethi.jntuhconnect"><img src="https://img.shields.io/endpoint?url=https%3A%2F%2Fplay.cuzi.workers.dev%2Fplay%3Fi%3Dcom.dhethi.jntuhconnect%26hl%3Den%26gl%3DIN%26l%3DGoogle%2520Play%2520version%26m%3D%2524version&style=flat-square&logo=googleplay&color=4285F4" alt="Google Play production version"/></a>
  <a href="https://play.google.com/store/apps/details?id=com.dhethi.jntuhconnect"><img src="https://img.shields.io/endpoint?url=https%3A%2F%2Fplay.cuzi.workers.dev%2Fplay%3Fi%3Dcom.dhethi.jntuhconnect%26hl%3Den%26gl%3DIN%26l%3DGoogle%2520Play%2520downloads%26m%3D%2524shortinstalls&style=flat-square&logo=googleplay&color=34A853" alt="Google Play downloads"/></a>
  <a href="https://play.google.com/store/apps/details?id=com.dhethi.jntuhconnect"><img src="https://img.shields.io/endpoint?url=https%3A%2F%2Fplay.cuzi.workers.dev%2Fplay%3Fi%3Dcom.dhethi.jntuhconnect%26hl%3Den%26gl%3DIN%26l%3DGoogle%2520Play%2520updated%26m%3D%2524updated&style=flat-square&logo=googleplay&color=FBBC04" alt="Google Play last update"/></a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android" alt="Platform"/>
  <img src="https://img.shields.io/badge/Language-Kotlin-7F52FF?style=for-the-badge&logo=kotlin" alt="Kotlin"/>
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpack-compose" alt="Jetpack Compose"/>
  <img src="https://img.shields.io/badge/Min%20SDK-24-green?style=for-the-badge" alt="Min SDK"/>
  <img src="https://img.shields.io/badge/Hilt-Dependency_Injection-2196F3?style=for-the-badge&logo=google" alt="Hilt"/>
  <img src="https://img.shields.io/badge/Retrofit-2-48B983?style=for-the-badge" alt="Retrofit 2"/>
  <img src="https://img.shields.io/badge/Room-SQLite-003B57?style=for-the-badge&logo=sqlite&logoColor=white" alt="Room"/>
  <img src="https://img.shields.io/badge/Firebase-Cloud_Messaging-FFCA28?style=for-the-badge&logo=firebase&logoColor=black" alt="Firebase Cloud Messaging"/>
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

## 📖 Documentation

| Guide | Purpose |
|---|---|
| [Architecture](architecture.md) | Layer boundaries, result flow, local persistence, notifications, and external services |
| [Contributing](CONTRIBUTING.md) | Setup, tests, conventions, and pull request expectations |
| [Deployment](DEPLOYMENT.md) | Signing, versioning, Play internal-track release, verification, and rollback |
| [Security](SECURITY.md) | Vulnerability reporting, mobile security model, student data, and signing secrets |
| [Operations runbook](RUNBOOK.md) | Triage for builds, API results, local storage, FCM, and Play releases |

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
- JDK 17+ (used by local builds and CI)
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
| Android Gradle Plugin | 9.3.0 |
| Version | Auto-managed (see below) |

### Versioning
`versionCode` is the single source of truth in [`app/version.properties`](app/version.properties);
`versionName` is derived as `1.0.<versionCode>`. It bumps **automatically**:
- **Local release builds** (`assembleRelease` / `bundleRelease`) increment `version.properties`.
- **CI builds** derive `versionCode = version.properties base + GITHUB_RUN_NUMBER`, guaranteeing an ever-increasing code while the base remains controlled.

The current badge-update step still calculates `18 + GITHUB_RUN_NUMBER`, while `version.properties` has a different base. Treat the Play artifact/build output as authoritative until those calculations are aligned.

### Release Build
The release build has **ProGuard/R8 minification enabled** with full NDK debug symbols for better crash reports.

### Feedback reminders

After five days of using the feedback-enabled app, Home offers an optional 1–5
star experience rating. Reminders appear at most once every five days; choosing
**Not now** starts another five-day wait. They appear only after Home has been
resumed for 20 seconds, after onboarding and while no update snackbar is visible.

For 4–5 stars, users can open the Google Play listing to leave a rating or review.
For 1–3 stars, **Write feedback** opens an email composer addressed to
`thilakreddy@dhethi.com`. Emails are sent only by the user. Google Play and email
apps do not report whether feedback was submitted, so returning from either
shows a confirmation. **I've already shared feedback** or **Yes, I've shared it**
permanently stops reminders for this app's stored data. Dismissing or cancelling
the external flow does not count as completed feedback. Schedule, unfinished
rating and completion state persist across app restarts and updates.

### Results native ads

Student results show one native AdMob card after the selected tab's results. It
scrolls with the page and is separated from result controls. Loading, failed and
empty result tabs do not show ads. Result fetching never waits for ads, ad failures
are silent, and scrolling or tab switches reuse the screen's ad. Cached ads expire
after an hour and are replaced when the slot is next visited or the screen resumes.

Release builds use app ID `ca-app-pub-5512897194230969~5533754978` and native ad
unit `ca-app-pub-5512897194230969/1414828404` (named **RESULTS BANNER** in AdMob).
The unit must use the **Native advanced** format. Debug builds always use Google's
test app and native ad IDs. Optional `ADMOB_APP_ID` and `ADMOB_RESULTS_NATIVE_ID`
Gradle properties or environment variables override release defaults; CI reads
repository variables with those names. See [Deployment](DEPLOYMENT.md#configure-admob).

The native layout registers the headline, icon, body, advertiser, media and call to
action with `NativeAdView`, which handles clicks and impressions. It includes an
**Ad** badge, space for the SDK's AdChoices overlay, and a 180 dp media view. Text
wraps without truncation and the card follows the app's light/dark theme. See
[Google's native implementation guide](https://developers.google.com/admob/android/native/advanced)
and [native ad policies](https://support.google.com/admob/answer/6329638).

UMP checks consent at launch. Configure messages in AdMob's **Privacy & messaging**;
**Profile → About → Ad privacy choices** appears when UMP requires that entry point.
SDK initialization runs in the background after results are ready and the ad slot
enters composition. Consent changes clear the ad even when it is off screen, and
late callbacks release their ads. Ad requests do not include student details or marks.

[`app-ads.txt`](app-ads.txt) contains the authorized seller entry. The website copy
is `JNTUHRESULTS-WEB/public/app-ads.txt` and must be deployed to
`https://jntuhconnect.dhethi.com/app-ads.txt`, matching the developer website in
the Google Play listing. Adding it to the Android repository alone does not host it.

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

Contributions are welcome. See [CONTRIBUTING.md](CONTRIBUTING.md) for local setup, required validation, architectural rules, and the pull request checklist.

---

## 📄 License

This project is licensed under the [GNU General Public License v3.0](LICENSE).

---

## 👨‍💻 Author

**Thilak Reddy**  
GitHub: [@ThilakReddyy](https://github.com/ThilakReddyy)

---

<p align="center">Made with ❤️ for JNTUH Students</p>
