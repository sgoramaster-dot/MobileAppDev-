# Implementation notes

## Repository assessment
The repository was empty at the time of implementation and no assignment PDF, rubric, lecturer brief, or starter code was present. This implementation therefore follows the provisional default requested in `ANTIGRAVITY_BUILD_PROMPT.md`: a native Android app for a MyContactList contact manager.

## Execution checklist
- [x] Repository inspected for assignment materials.
- [x] Assumption documented when no assignment source was available.
- [x] Android Studio project scaffolded.
- [x] Core contact CRUD behavior implemented.
- [x] Search and sorting implemented against real persisted records.
- [x] SQLite persistence implemented with safe schema management.
- [x] Detail and contact action flows implemented.
- [x] Settings screen implemented and persisted.
- [x] Basic photo picker support included.
- [x] Documentation created.
- [ ] Full Gradle/Android build run in this environment (not executable here due missing local Android wrapper/tooling).

## Architecture decisions
- Java + XML views using AndroidX libraries.
- SQLiteOpenHelper for local storage.
- Repository layer to isolate database access.
- RecyclerView-based list with a single adapter.
- SharedPreferences for sort setting persistence.
- Photo picker via Activity Result API.

## Known constraints
- The environment used for this work does not provide a full local Android SDK/Gradle wrapper execution path, so compile verification was not possible here.
- The production app intentionally avoids external keys or API usage.
