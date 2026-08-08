# Android Operations Runbook

## Quick triage

Determine whether the issue affects:

1. App startup/navigation.
2. All backend calls or one endpoint.
3. Debug only or Play release builds.
4. Local persistence.
5. FCM delivery/navigation.
6. Build/signing/Play deployment.

Collect Android version, app version, build type, device model, network state, and sanitized logs. Never request a user's full academic payload or FCM token.

## Debug build cannot reach local backend

- Emulator backend URL is `http://10.0.2.2:8000/api/`.
- Confirm the backend listens on host port 8000 and `/api/health` responds.
- Confirm debug manifest cleartext override merged correctly.
- A physical device cannot use `10.0.2.2`; provide an intentional reachable development endpoint.
- Check backend CORS/header/User-Agent behavior only after basic connectivity.

## Results stay loading or pending

- Inspect Logcat for Retrofit/HTTP errors without exposing payloads.
- A backend 202 means scraping is queued; show pending/retry behavior.
- Confirm release/debug base URL and network connectivity.
- If repeated retries never resolve, follow the backend runbook for worker, RabbitMQ, PostgreSQL, Redis, and JNTUH upstream health.
- A decoding failure usually means DTO/backend contract drift; compare the sanitized JSON shape with DTOs and mappings.

## App shows stale recent student data

Room stores summaries, not authoritative marks. Refresh results from the backend. If the local schema/query is corrupted during development, clear app data; do not use destructive migration behavior as a production fix. Verify DataStore recent-document expiration and Room ordering independently.

## Notifications missing

Check:

- Android 13+ notification permission.
- Local notifications-enabled preference.
- FCM token retrieval and topic subscription.
- Per-roll backend registration for the stable random device ID.
- Token refresh handling in `MyFirebaseMessagingService`.
- Backend FCM provider/configuration and subscription rows.
- Notification channel settings and payload navigation destination.

Use `scripts/send_fcm_notification.py` only with a private Firebase Admin credential stored outside the repository and a controlled test device/topic.

## Notification opens wrong screen

- Inspect the sanitized data payload keys.
- Check `MyFirebaseMessagingService` destination mapping and `MainActivity` intent handling.
- Reproduce with `AppNavigationTest`/a controlled message.
- Preserve behavior across cold start, warm start, and foreground presentation.

## Build or tests fail

```bash
./gradlew testDebugUnitTest lintDebug assembleDebug --no-daemon
```

For instrumentation:

```bash
./gradlew connectedDebugAndroidTest --no-daemon
```

Check JDK 17, SDK/API availability, emulator boot/permissions, Firebase client config, Gradle cache corruption, and the first real compiler/test error.

## Play workflow fails

- Signing decode: validate protected `KEYSTORE_BASE64` and passwords without printing them.
- Bundle build: inspect Gradle/R8 failure and version code.
- Upload: check Play service-account permissions, package name, internal track, and duplicate/lower version errors.
- Badge commit: check workflow `contents: write`, branch protection, and bot push result.

Every `main` push triggers a release. Do not retry by pushing meaningless commits until the cause is understood.

## Bad internal release

Revert the code and push a corrected commit; CI must publish a higher version code. Play cannot replace an existing artifact with the same version code. For a wider rollout, stop/adjust it in Play Console while preparing the corrected build.

## Recovery verification

- App starts on supported Android/API versions.
- Known results and 202 pending behavior work.
- Local recent summaries/preferences survive expected lifecycle.
- FCM opt-in, token refresh, receipt, and navigation work.
- Unit/lint/debug build and relevant instrumentation tests pass.
- Internal Play artifact installs and calls the production HTTPS backend.
