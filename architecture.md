# JNTUH Connect Android Architecture

## Purpose

This repository is the native Android client for the JNTUH Connect platform. Student results are the primary flow; notifications, content, jobs, grace marks, and profile tools build on the same backend and local app foundation.

## System context

```mermaid
flowchart LR
    user[Student] --> ui[Jetpack Compose UI]
    ui --> vm[Hilt ViewModels]
    vm --> usecases[Domain use cases]
    usecases --> repositories[Repository interfaces]
    repositories --> remote[Retrofit data source]
    remote --> backend[JNTUH FastAPI backend]
    repositories --> room[(Room summaries)]
    repositories --> prefs[(DataStore preferences)]
    fcm[Firebase Cloud Messaging] --> service[Messaging service]
    service --> ui
    service --> repositories
```

## Layers

### Presentation

`presentation/` contains the Compose navigation shell, screens, reusable components, theme, and `@HiltViewModel` state holders. ViewModels launch domain use cases and expose loading/success/error state to composables. `MainActivity` owns the root navigation and notification destination handling.

Feature packages include student results, class rankings, contrast, grace marks, updates, calendars/syllabus, careers, explore, profile, and home.

### Domain

`domain/model/` defines app-facing result/content types. `domain/repository/` defines boundaries independent of Retrofit and Room. `domain/use_case/` provides focused operations for result views, content, recent students, and mutations.

Keep business decisions such as roll validation, ranking, filtering, and response-to-domain mapping outside composables.

### Data

- `data/remote/JntuhConnectApi.kt`: Retrofit endpoint contract and DTOs.
- `data/repository/JntuhResultsRepositoryImpl.kt`: remote result/content adapter.
- `data/repository/NotificationRepository.kt`: FCM topic and per-roll subscription coordination.
- `data/local/database/`: Room database for student summary/history convenience data.
- `data/local/preferences/AppPreferences.kt`: theme, notification opt-in, random device ID, subscribed rolls, and recent document shortcuts.

Hilt modules in `di/` construct the database, Retrofit client, API, and repositories.

## Result flow

```mermaid
sequenceDiagram
    actor Student
    participant Screen as Compose screen
    participant VM as ViewModel
    participant UC as Result use case
    participant Repo as Repository
    participant API as FastAPI backend
    participant DB as Room summary DB

    Student->>Screen: Submit hall-ticket number
    Screen->>VM: Load selected result view
    VM->>UC: Execute request
    UC->>Repo: Fetch result
    Repo->>API: Retrofit GET
    alt Result available
        API-->>Repo: 200 result payload
        Repo-->>UC: DTO/domain result
        UC-->>VM: Success
        VM-->>Screen: Render result
        VM->>DB: Save recent student summary where applicable
    else Backend scrape pending
        API-->>Repo: 202 queued response
        Repo-->>VM: Pending/error state
        VM-->>Screen: Explain retry behavior
    else Network/backend failure
        API-->>Repo: Error/timeout
        Repo-->>VM: Failure
        VM-->>Screen: Actionable error state
    end
```

The mobile client never scrapes JNTUH directly. It relies on the backend to cache, persist, queue, and refresh results.

## Networking

`BuildConfig.API_BASE_URL` is:

- Debug: `http://10.0.2.2:8000/api/` for an Android emulator reaching the development host.
- Release: `https://jntuhresults.dhethi.com/api/`.

Debug permits cleartext traffic through `app/src/debug/AndroidManifest.xml`; release does not opt into it. The shared OkHttp client uses 15-second connect, 30-second read, and 40-second total call timeouts.

The backend recognizes Android's OkHttp/Dalvik User-Agent compatibility bypass. One jobs call also sends a public `X-Api-Key` default. Neither mechanism is user authentication; privileged backend operations must use server-held authorization.

## Local persistence

Room stores `StudentDetailsEntity` summaries for recent student convenience. It does not replace the backend's authoritative mark store. DataStore persists theme/notification preferences, a random result-notification device ID, subscribed roll numbers, and up to two recent documents that expire after 24 hours.

Schema changes require an explicit Room migration or a deliberate destructive-development strategy; do not silently change the database version in production.

## Notifications

Firebase provides:

- The `result-updates` topic for release broadcasts.
- Device FCM tokens registered with backend per-roll subscriptions.
- `MyFirebaseMessagingService` for token refresh, payload handling, notification channels, and navigation destinations.

The repository restores topic state and re-registers subscribed rolls after token rotation. Notification permission is requested at runtime on Android 13+.

## Results advertising

`presentation/ads/` owns a native advanced card after successful student result
content. Ads are independent of result use cases and never block result requests.
The screen retains one `NativeAd` and `NativeAdView` across scrolling/tab changes.
The SDK registers assets, clicks, impressions, and AdChoices; the layout supplies
an Ad badge and a 180 dp `MediaView`. Video pauses when detached or backgrounded.
Screen disposal or consent changes destroy the ad and invalidate late callbacks.
Loading, error and empty tabs have no ad, and failures do not trigger retry loops.

The Hilt singleton `AdsConsentManager` updates UMP consent on each activity launch.
The SDK initializes on a background dispatcher only when consent permits requests
and the results ad slot is composed. A composition local passes this manager to UI;
Profile exposes UMP privacy options when required. No result data is added to ad
requests. Debug uses demo IDs; release defaults to the app's production native IDs,
with optional Gradle/environment overrides. Developer-site seller authorization
is served separately through the web application's `public/app-ads.txt`.

## Feedback reminders

`presentation/feedback/` owns an optional Home-only rating prompt. DataStore
persists its first-use timestamp, last shown/deferred timestamp, unfinished
rating, and user-confirmed completion. A prompt is reserved atomically before
display; first use and each deferral require five full days before another ask.
Home must be resumed for 20 seconds and have no update snackbar visible.

Ratings of 4–5 offer an external Google Play listing; 1–3 offer an `ACTION_SENDTO`
email to `thilakreddy@dhethi.com`. No in-app review API is used. External activity
results are not submission receipts: completion is recorded only after an
explicit user confirmation, including an **I've already shared feedback**
action. Pending ratings survive process death and resume at the next eligible
reminder, preserving the five-day cooldown. Confirmed completion prevents
future reminders while local app data remains available.

## Build and release

Gradle version catalogs pin plugins/libraries. Release builds enable R8/resource shrinking and full native debug symbols. `app/version.properties` holds the base version code:

- Debug/local non-release builds do not mutate it.
- Local release tasks increment the file.
- GitHub Actions derives a monotonic code from the base plus `GITHUB_RUN_NUMBER` without changing the file during the build.

Every push to `main` triggers the signed Play internal-track release workflow, followed by a bot README version-badge commit.

## Architectural invariants

- Backend result DTO changes must update mappings, domain models, ViewModels, and tests together.
- Room/DataStore are convenience stores, not authoritative academic data.
- Preserve release HTTPS and debug-only cleartext.
- FCM/admin/server credentials never belong in the APK.
- Handle 202/pending, rate-limit, offline, timeout, decoding, and empty states explicitly.
- Compose screens should consume state and events rather than call Retrofit directly.
- Keep signing/version automation aligned with the Play workflow.
