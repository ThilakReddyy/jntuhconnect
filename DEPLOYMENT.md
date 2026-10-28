# Android Deployment Guide

## Release trigger

Every push to `main` triggers `.github/workflows/deploy-play-store.yml`. The workflow builds a signed AAB, publishes it to the Google Play internal track with `status: completed`, and commits an updated version badge to `README.md` using `[skip ci]`.

Treat any `main` push—including documentation changes—as a release action.

## Required GitHub secrets

| Secret | Purpose |
|---|---|
| `KEYSTORE_BASE64` | Base64-encoded Android upload keystore |
| `KEYSTORE_PASSWORD` | Keystore password |
| `KEY_ALIAS` | Upload key alias |
| `KEY_PASSWORD` | Upload key password |
| `PLAY_SERVICE_ACCOUNT_JSON` | Google Play Developer API service-account JSON |

Restrict the Play service account to required app/track permissions. Rotate signing/deployment credentials according to the Play account recovery plan; never commit them.

## Configure AdMob

Release builds default to these public production IDs:

| Setting | Configured value |
|---|---|
| App ID | `ca-app-pub-5512897194230969~5533754978` |
| Results native ad unit | `ca-app-pub-5512897194230969/1414828404` |

The **RESULTS BANNER** unit must be **Native advanced**, matching the integration.
Optional `ADMOB_APP_ID` and `ADMOB_RESULTS_NATIVE_ID` GitHub repository variables,
Gradle properties, or environment variables override these defaults. Empty CI
variables fall back to the configured IDs. The previous `ADMOB_RESULTS_BANNER_ID`
setting is no longer used because banner and native units are different formats.
Debug builds always use Google's test app and native ad IDs.

Configure UMP messages in **Privacy & messaging**. Account for the Mobile Ads SDK in
the Play ads declaration, Data safety form and published privacy policy. Validate
with Google's demo native ads before release, including AdChoices visibility,
light/dark themes and larger text. See the
[native ad policies](https://support.google.com/admob/answer/6329638).

The developer website in the Play listing is `https://jntuhconnect.dhethi.com`.
Deploy `JNTUHRESULTS-WEB/public/app-ads.txt` with the existing static website so
`https://jntuhconnect.dhethi.com/app-ads.txt` returns HTTP 200 and plain text:

```text
google.com, pub-5512897194230969, DIRECT, f08c47fec0942fa0
```

The Android repository's root `app-ads.txt` is a reference copy, not a hosted file.
After deployment, open the URL and check the seller record, then use
**AdMob → Apps → View all apps → app-ads.txt → Check for updates** to request
verification. See [Google's app-ads.txt setup guide](https://support.google.com/admob/answer/9363762).

## Versioning

`app/version.properties` is the local base. `versionName` is `1.0.<versionCode>`.

- CI uses `baseCode + GITHUB_RUN_NUMBER`, providing a monotonic Play version code.
- A local task whose name contains `Release` increments `version.properties` before building.
- Debug tasks use the file value without mutation.

Review version behavior before resetting workflow run history, changing the base, renaming release tasks, or moving repositories.

### Current badge mismatch

The Gradle build derives its code from the current `version.properties` base plus `GITHUB_RUN_NUMBER`. The README badge step separately hard-codes `18 + GITHUB_RUN_NUMBER`. Because the current base is not 18, the badge can differ from the uploaded AAB. Use the Gradle/Play artifact as authoritative and align the workflow to one derived value before relying on the badge.

## Release build

CI:

1. Uses JDK 17.
2. Decodes the keystore into the runner temp directory.
3. Writes the Play service-account JSON to runner temp.
4. Exposes signing values as environment variables.
5. Runs `./gradlew bundleRelease --no-daemon`.
6. Uploads `app-release.aab` to `internal`.
7. Updates and pushes the README version badge.

Release configuration points at `https://jntuhresults.dhethi.com/api/`, enables R8/resource shrinking, and generates full NDK debug symbols.

## Pre-release validation

Pull requests run:

```bash
./gradlew testDebugUnitTest lintDebug assembleDebug --no-daemon
./gradlew connectedDebugAndroidTest --no-daemon
```

Before promoting beyond internal testing, verify on a release-like build/device:

- Academic/all/backlog/credits/class/contrast result flows.
- Backend 202 pending and rate-limit states.
- Calendars, syllabus, jobs, grace proof, and external links.
- FCM topic and per-roll subscriptions, token rotation, permission denial, and notification navigation.
- In-app update behavior.
- R8 has not removed DTOs/reflection-required code.
- Light/dark, phone/tablet, and accessibility behavior.
- Privacy/Data safety declarations match current data handling.

## Promotion

The workflow stops at the Play internal track. Promote to closed/open/production through a reviewed Play Console process or a separately approved workflow. Review release notes, country/device availability, Data safety, content rating, privacy URL, and staged rollout before production.

## Rollback

Google Play version codes cannot be reused. Roll forward with a corrected higher-version build. For an urgent internal release, revert the offending code, push the revert to `main`, and let CI generate a new code. For production, halt/stage rollout in Play Console as appropriate and publish the corrected higher version.

## Release incident

Use `RUNBOOK.md` for failed signing/build/upload, broken API behavior, FCM issues, and version-badge automation. Never expose workflow secrets or upload artifacts in public logs/issues.
