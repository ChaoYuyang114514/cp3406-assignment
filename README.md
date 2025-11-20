# Book Reading Tracker 📚
A modern Android app to search books, build a personal shelf, track reading sessions, and view analytics.

## Overview
- Goal: Help users discover books, curate a shelf, record reading time and notes, and view clear analytics.
- Platform: Android (minSdk 24).
- Status: Core features are complete with offline cache and background refresh; tests cover main paths.

## Core Features
- Home
  - Two-line header: "Welcome!" and "Use Discover to search books", centered with larger typography.
  - Adaptive grid showing book covers only: 3 columns when the item count is divisible by 3, otherwise 2 columns.
  - Tap a cover to navigate to Book Detail.
- Discover
  - Search books via Google Books API and browse results.
  - Add a book to your shelf from detail.
- Shelf
  - View saved books; add/remove items.
  - Works offline using Room cache.
- Book Detail
  - Scrollable page for details, reading, and notes.
  - Reading section with +10m and +30m buttons and an aggregated display like "+40min".
  - Notes list tied to the book, with a 10% bottom spacer so input and content are always reachable.
  - Consistent 5% top spacer for aesthetics.
- Analytics
  - Top summary: Total Reading Time in minutes.
  - Per-book analytics list with icon (cover), name, and time columns, sorted by time descending.
  - Data is independent of shelf size: aggregation is based on reading sessions; book metadata is pulled from cache.
- Settings
  - Avatar icon at top, "UserName" below.
  - Dark Mode toggle with preference persisted via DataStore.
- Bottom Navigation
  - Icons for Home, Discover, Shelf, Analytics, Settings with lowercase English labels under each icon.
- Design Consistency
  - Every screen keeps a 5% top spacer to maintain a clean aesthetic.

## Key Implementation Details
- UI
  - Jetpack Compose (Material 3): LazyVerticalGrid for Home, LazyColumn for Analytics three-column list.
  - Coil AsyncImage for covers; Navigation Compose for screen transitions.
  - Layout spacers calculated with LocalConfiguration to achieve 5%/10% spacing.
- Data & Domain
  - Repository pattern over Room (local) + Retrofit (remote) with coroutines and Flow.
  - Offline caching: search results and details persisted for fast, offline reads.
  - Background refresh: a WorkManager job refreshes shelf data periodically.
  - Analytics use case aggregates sessions: groups by bookId, sums minutes, enriches with cached book metadata, sorts descending.
- App State & Preferences
  - Theme preference (Dark Mode) persisted via DataStore and applied at app startup.
- Error Handling
  - Snackbar-based messaging for non-blocking error feedback.

## Technology Stack
- Kotlin • Jetpack Compose (Material 3) • Hilt • Room • Retrofit • Coroutines/Flow • Navigation • DataStore • WorkManager • Coil

## Architecture
- Layers: Data (Room/Retrofit/Repository) / Domain (UseCase) / UI (Compose + ViewModel).
- Dependency Injection: Hilt modules provide Retrofit, Room, DAO, and repositories.

## Code Map
- UI & navigation: `app/src/main/java/.../ui/screens/*`, `app/src/main/java/.../ui/nav/*`
- Dependency injection: `app/src/main/java/.../di/*`
- Local data: `app/src/main/java/.../data/local/*` (entities, DAO, AppDatabase)
- Remote data: `app/src/main/java/.../data/remote/*` (BookApi, response models, mappers)
- Repository & interfaces: `app/src/main/java/.../data/repository/*`
- Domain use cases: `app/src/main/java/.../domain/usecase/*`

## Build & Run
1. Open the project in Android Studio and sync Gradle.
2. Run on an emulator or device (Android 7.0+).
3. Typical flow: Discover (search) → Detail (add to shelf / log reading / notes) → Shelf → Analytics.

## Testing
- Unit tests: `app/src/test` (repository behavior, analytics aggregation).
- Instrumented tests: `app/src/androidTest` (DAO and repository integration with MockWebServer, Compose UI tests).
- Run:
  - `./gradlew test` for unit tests.
  - `./gradlew connectedAndroidTest` on a device/emulator.

Report Paths

- Debug unit test report: e:\cp3406 assignment\app\build\reports\tests\testDebugUnitTest\index.html
- Release unit test report: e:\cp3406 assignment\app\build\reports\tests\testReleaseUnitTest\index.html

Unit test reports remain passing:
Debug: e:\cp3406 assignment\app\build\reports\tests\testDebugUnitTest\index.html
Release: e:\cp3406 assignment\app\build\reports\tests\testReleaseUnitTest\index.html