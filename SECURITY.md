# Security Policy

## Reporting

Report vulnerabilities through GitHub private vulnerability reporting when available, or contact the repository owner privately through GitHub to establish a secure channel. Do not publish real student records, FCM tokens, signing details, Play credentials, or exploit instructions in a public issue.

## Mobile security model

The APK is distributed to users and can be inspected. Any bundled URL, API header, Firebase client configuration, or User-Agent is public and cannot protect privileged backend operations.

- Backend admin keys, cloud credentials, Firebase Admin service accounts, and database secrets must never enter this repository/app.
- Release signing and Play upload credentials exist only in protected CI secrets.
- Backend privileged routes must enforce server-side authorization.

## Student and device data

The app handles hall-ticket numbers, names, branches, academic summaries, result payloads, a random notification device ID, subscribed roll numbers, and FCM tokens.

- Minimize persistence and logs.
- Use Room only for intended student summaries and DataStore only for documented preferences/subscriptions/shortcuts.
- Do not log complete FCM tokens or academic responses.
- Use synthetic data in tests/screenshots.
- Keep clear-data controls aligned with backend subscription removal.

## Network security

- Release traffic must use HTTPS.
- Cleartext is enabled only by the debug manifest for emulator development.
- Validate external URLs before opening them.
- Treat certificate/TLS failures as failures; do not disable verification.
- Maintain bounded timeouts and actionable offline/rate-limit states.

## Firebase and notifications

`google-services.json` is client configuration and may be present for the Firebase app, but Firebase Admin service-account JSON can send messages and must stay private. Restrict backend FCM privileges, validate notification navigation values, and remove invalid/stale tokens server-side.

## Build and supply chain

- Review Gradle wrapper, plugin, catalog, and lock/config changes.
- Investigate Dependabot/security advisories.
- Protect `main`; it deploys to Play internal automatically.
- Limit workflow write permission and third-party Actions to reviewed versions.
- Never print decoded keystores, passwords, or service-account JSON.

## Secret exposure

If a keystore or Play/Firebase Admin credential is exposed, revoke/rotate provider access immediately, replace CI secrets, audit releases/provider logs, and follow Google/Play key recovery guidance. Git history removal does not replace rotation.
