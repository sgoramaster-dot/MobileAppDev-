# Android Studio Mobile App - MyContactList

This repository contains a native Android Studio app for managing a personal contact list. The assignment brief was not present in the repository, so this implementation follows the provisional default described in `ANTIGRAVITY_BUILD_PROMPT.md`: contacts with create/read/update/delete, search, sorting, local SQLite persistence, navigation, settings, and optional photo support.

## Features
- Contact list with searchable RecyclerView
- Add, edit, and delete contact flows
- Contact details screen with dial/email/map actions
- Local SQLite persistence across app restarts
- Sort options persisted in settings
- Optional contact photo using Android Photo Picker
- Validation for required fields and common email/phone formats

## Prerequisites
- Android Studio Hedgehog or newer
- JDK 17
- Android SDK 34

## Import and run
1. Open Android Studio.
2. Choose Open and select this repository folder.
3. Let Gradle sync complete.
4. Select an emulator or connected device.
5. Run the app configuration.

## Build commands
- Project sync: `./gradlew help`
- Debug build: `./gradlew assembleDebug`
- Unit tests: `./gradlew testDebugUnitTest`

## Notes
- The app is designed to work without API keys or external services.
- The photo selection uses Android Photo Picker when available.
- This is a default contact-list implementation and may need adjustment if the actual assignment brief is later supplied.
