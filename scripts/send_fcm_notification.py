#!/usr/bin/env python3
"""Send a test notification to JNTUH Connect through Firebase Cloud Messaging."""

from __future__ import annotations

import argparse
import os
import sys
from urllib.parse import urlparse


DEFAULT_PROJECT_ID = "jntuhconnect-test"
DEFAULT_TOPIC = "result-updates"


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(
        description=(
            "Send an FCM notification to one Firebase Installation ID or to the "
            f"default '{DEFAULT_TOPIC}' topic."
        )
    )
    parser.add_argument("--title", required=True, help="Notification title.")
    parser.add_argument("--body", required=True, help="Notification message.")

    target = parser.add_mutually_exclusive_group()
    target.add_argument(
        "--fid",
        help="Send to one Firebase Installation ID (useful for testing).",
    )
    target.add_argument(
        "--topic",
        help=f"Send to an FCM topic. Defaults to '{DEFAULT_TOPIC}'.",
    )

    parser.add_argument(
        "--link",
        help="Optional http(s) URL opened when the notification is tapped.",
    )
    parser.add_argument(
        "--project-id",
        default=os.environ.get("GOOGLE_CLOUD_PROJECT", DEFAULT_PROJECT_ID),
        help=(
            "Firebase project ID. Defaults to GOOGLE_CLOUD_PROJECT or "
            f"'{DEFAULT_PROJECT_ID}'."
        ),
    )
    parser.add_argument(
        "--dry-run",
        action="store_true",
        help="Validate the message without delivering it.",
    )
    return parser.parse_args()


def validate_args(args: argparse.Namespace) -> None:
    if not args.title.strip() or not args.body.strip():
        raise ValueError("--title and --body cannot be blank")
    if args.fid is not None and not args.fid.strip():
        raise ValueError("--fid cannot be blank")
    if args.topic is not None and not args.topic.strip():
        raise ValueError("--topic cannot be blank")

    if args.link:
        parsed_link = urlparse(args.link)
        if parsed_link.scheme not in {"http", "https"} or not parsed_link.netloc:
            raise ValueError("--link must be a valid http:// or https:// URL")


def main() -> int:
    args = parse_args()

    try:
        validate_args(args)
    except ValueError as error:
        print(f"Error: {error}", file=sys.stderr)
        return 2

    if not os.environ.get("GOOGLE_APPLICATION_CREDENTIALS"):
        print(
            "Error: set GOOGLE_APPLICATION_CREDENTIALS to your Firebase Admin "
            "service-account JSON file.",
            file=sys.stderr,
        )
        return 2

    try:
        import firebase_admin
        from firebase_admin import messaging
    except ModuleNotFoundError:
        print(
            "Error: firebase-admin is not installed. Run:\n"
            "  python3 -m pip install -r scripts/requirements-fcm.txt",
            file=sys.stderr,
        )
        return 2

    data = {"destination": "updates"}
    if args.link:
        data["link"] = args.link

    target = (
        {"fid": args.fid.strip()}
        if args.fid
        else {"topic": (args.topic or DEFAULT_TOPIC).strip()}
    )
    message = messaging.Message(
        notification=messaging.Notification(
            title=args.title.strip(),
            body=args.body.strip(),
        ),
        data=data,
        **target,
    )

    target_name, target_value = next(iter(target.items()))
    try:
        firebase_admin.initialize_app(options={"projectId": args.project_id})
        message_id = messaging.send(message, dry_run=args.dry_run)
    except Exception as error:
        print(f"FCM send failed: {error}", file=sys.stderr)
        return 1

    action = "Validated" if args.dry_run else "Sent"
    print(f"{action} notification for {target_name} '{target_value}'.")
    print(f"FCM message ID: {message_id}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
