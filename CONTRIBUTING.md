# Contributing to JNTUH Connect Android

## Prerequisites

- Android Studio with the SDK versions declared by Gradle.
- JDK 17 for Gradle/CI compatibility.
- Android SDK/API 36 for compilation and an API 24+ device/emulator.
- Firebase Android client configuration at `app/google-services.json` for notification-enabled builds.

## Setup

```bash
git clone https://github.com/ThilakReddyy/jntuhconnect.git
cd jntuhconnect
./gradlew assembleDebug
```

The debug build calls `http://10.0.2.2:8000/api/`. Start the backend locally before testing uncached results. A physical device requires an intentionally reachable HTTPS or LAN development endpoint; do not weaken the release network policy.

## Architecture rules

- Compose UI belongs in `presentation/` and observes ViewModel state.
- Business operations belong in domain use cases/repository interfaces.
- Retrofit/Room/DataStore implementation details stay in `data/`.
- Add dependencies through Hilt modules rather than service locators.
- Preserve DTO/domain separation when backend shapes change.
- Do not treat local Room summaries as authoritative result data.
- Keep debug/release backend URLs and cleartext rules separate.

Read `architecture.md` before changing data flow, persistence, notifications, or build behavior.

## Validation

Match the pull-request workflow:

```bash
./gradlew testDebugUnitTest lintDebug assembleDebug --no-daemon
```

When navigation, permissions, adaptive layout, notifications, or end-to-end behavior changes, also run:

```bash
./gradlew connectedDebugAndroidTest --no-daemon
```

Use an emulator/device with notification permission where tests require it. Do not run a local release task merely to validate ordinary code: release tasks intentionally increment `app/version.properties`.

## Testing expectations

- Add unit tests for validation, DTO mapping, ranking/filtering, pagination, and ViewModel state.
- Add instrumentation tests for navigation, permissions, deep-link/notification routing, and adaptive UI behavior.
- Use synthetic student data.
- Test pending `202`, offline, timeout, rate-limit, decoding, empty, and successful result states.

## Security and privacy

- Never add upload keystores, Play service-account JSON, Firebase Admin credentials, backend admin keys, or cloud secrets.
- `google-services.json` is Firebase client configuration, not authorization to send messages; server service-account credentials remain outside the app.
- Avoid logging full FCM tokens, subscription payloads, or academic records.
- Treat any bundled API header/User-Agent access as public compatibility, not authentication.
- Review `SECURITY.md` for disclosure and hardening guidance.

## Pull requests

1. Branch from current `main`.
2. Keep the change focused and update tests/docs.
3. Run unit tests, lint, debug build, and relevant instrumentation tests.
4. Explain API/schema, permission, local-data, notification, and release impact.
5. Do not push directly to `main` for documentation-only work without intending a Play internal-track release; every main push triggers deployment.

Contributions use the repository's GPL-3.0 license.
