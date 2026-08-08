# CLAUDE.md

Guidance for coding assistants working in the Android client.

## Priority and architecture

Student results are the primary feature. The app follows data → domain → presentation boundaries with Hilt dependency injection.

- Compose screens/ViewModels: `app/src/main/java/com/dhethi/jntuhconnect/presentation/`
- Domain models/repositories/use cases: `domain/`
- Retrofit, Room, DataStore, implementations: `data/`
- Hilt modules: `di/`
- FCM service: `service/MyFirebaseMessagingService.kt`

Read `architecture.md` before changing cross-layer flow.

## Commands

```bash
./gradlew testDebugUnitTest lintDebug assembleDebug --no-daemon
./gradlew connectedDebugAndroidTest --no-daemon
```

Do not run release tasks casually: local release task names increment `app/version.properties`. Every push to `main` publishes an AAB to the Play internal track.

The build version uses the `version.properties` base plus `GITHUB_RUN_NUMBER`; the README badge step currently hard-codes a different base (`18`). Do not use the badge as the source of truth until the workflow is aligned.

## Networking

- Debug base: `http://10.0.2.2:8000/api/`, with debug-only cleartext.
- Release base: `https://jntuhresults.dhethi.com/api/`.
- Retrofit contract: `data/remote/JntuhConnectApi.kt`.
- Backend access header/User-Agent values in an APK are public compatibility mechanisms, not secrets/authentication.

Handle backend 202/pending, 429/rate limit, offline, timeout, decoding, and error states explicitly.

## Persistence and privacy

- Room stores student summaries only.
- DataStore stores theme/notification preferences, random device ID, subscribed rolls, and recent document shortcuts.
- Do not persist full results without a reviewed privacy/migration change.
- Avoid logging FCM tokens, academic payloads, signing values, or service credentials.
- Keep Firebase Admin/Play/signing credentials outside the repository.

## Change rules

- Update DTOs, mappers, domain models, repositories, ViewModels, and tests together for API changes.
- Keep composables free of direct Retrofit/Room construction.
- Add Room migrations for production schema changes.
- Preserve debug/release security separation.
- Test notification cold/warm/foreground navigation and token rotation.
- Update README/architecture/deployment/security/runbook docs with behavior changes.
- Preserve unrelated user changes in dirty worktrees.
