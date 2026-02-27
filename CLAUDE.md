# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Run JUnit 5 unit tests (no device needed)
./gradlew test

# Run a single unit test class
./gradlew test --tests "com.example.searchviewbottomnav.FruitsTest"

# Run instrumented tests (requires connected device/emulator)
./gradlew connectedAndroidTest

# Lint
./gradlew lint
```

## Architecture

Single-module Android app (`:app`) in Kotlin with a single `MainActivity` hosting a `BottomNavigationView` wired to three top-level destinations via the Navigation Component:

- **Home** (`navigation_home`) — fast-scroll demo
- **Search** (`navigation_search`) — fruit search
- **Settings** (`navigation_settings`) — theme preference

**Search flow** (`ui/search/`): `SearchFragment` hosts two child fragments — `RecentSearchesFragment` and `SearchMatchesFragment` — and toggles their visibility based on whether the search `EditText` is empty. Selecting a match opens `FruitActivity` and saves the term to SharedPreferences via `PrefsUtil` (`PREVIOUS_SEARCHES_KEY`). `SearchViewModel` holds `previousSearchStringList` as LiveData but the persistence is driven directly through `PrefsUtil` in the fragments.

**Fast scroll** (`ui/fastscroll/`): `FastscrollFragment` uses a `RecyclerView` with a custom `FastscrollBubble` class that listens to scroll events and touch events on a draggable thumb image, animating it in/out and updating a current-month label. `OneShotTimer` (coroutine-based countdown, IO dispatcher) drives the hide-after-idle behavior.

**Fruit data** (`Fruits.kt`): A hardcoded object with 21 fruits (name + imgur URL). `searchFruit()` does case-insensitive substring matching. `FruitActivity` receives a fruit name via `Intent.EXTRA_NAME` and loads the image with Glide.

**Settings** (`ui/settings/`): `SettingsFragment` manages a `ListPreference` for dark/light/system theme. `bashTheTheme()` (companion object, also called from `MainActivity.onCreate`) applies `AppCompatDelegate` night mode. On API < 29 the "Follow System" option is replaced with "Battery Saver".

**Utilities**:
- `PrefsUtil` — singleton wrapping `PreferenceManager.getDefaultSharedPreferences`; `prefsContext` must be set at app start (done in `MainApplication`).
- `Months.kt` — generates an array of 253 month strings from Dec 2020 down to Jan 2000 (`generateMonthList()`, `monthStringByKey()`).
- `Defs.kt` — `TEN_Q_GOOD_BUDDY` flag for API ≥ 29 checks.

## Key Versions

- Kotlin 2.3.10, AGP 9.0.1, compileSdk/targetSdk 36, minSdk 24
- Navigation 2.9.7, Lifecycle 2.10.0, Material 1.13.0
- Unit tests use JUnit Jupiter (JUnit 5), not JUnit 4
- Version catalog: `gradle/libs.versions.toml`
