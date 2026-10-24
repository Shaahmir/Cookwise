# Cookwise

Cookwise is an Android recipe-discovery app built with **Kotlin** and **Jetpack Compose**. It fetches meal data from [TheMealDB](https://www.themealdb.com/api.php) and displays recipes dynamically, helping you find a dish, learn a little about its cuisine, and open a video tutorial when you're ready to cook.

The project is also a hands-on learning exercise in building a modern Android UI, consuming a REST API, mapping JSON responses, and managing UI state.

## Features

- **Discover meals** - load a collection of random recipes from TheMealDB.
- **Search by name** - look up a recipe using the search field.
- **Recipe details** - tap a recipe to read its cooking instructions.
- **Cuisine and category** - see a meal's cuisine and category alongside its name.
- **Food photography** - display recipe thumbnails loaded from their image URLs.
- **Video tutorials** - open the recipe's YouTube tutorial; when a direct tutorial URL isn't available, Cookwise searches YouTube for the meal.
- **Minimal interface** - a focused, multi-screen browsing experience with clear typography and rounded recipe imagery.
- **Light and dark appearance** - adapts to the device theme, with dynamic color support on compatible Android versions.
- **Loading and empty states** - show feedback while meals load and when a search has no results.

## Tech Stack

| Technology | Purpose |
| --- | --- |
| **Kotlin** | Main programming language |
| **Jetpack Compose** | Declarative native UI |
| **Material 3** | UI components and theming |
| **Retrofit 2** | HTTP requests to TheMealDB |
| **Gson** | Convert API JSON responses into Kotlin data classes |
| **Coil** | Load meal thumbnails from URLs |
| **ViewModel + Kotlin coroutines** | Manage recipe state and run network requests asynchronously |
| **Gradle Kotlin DSL** | Build configuration and dependency management |

## API

Cookwise uses the public [TheMealDB API](https://www.themealdb.com/api.php) to retrieve its recipe data. The current implementation uses these endpoints:

| Purpose | Endpoint |
| --- | --- |
| Get a random meal | `https://www.themealdb.com/api/json/v1/1/random.php` |
| Search meals by name | `https://www.themealdb.com/api/json/v1/1/search.php?s={query}` |

The API response is mapped into Kotlin data classes. The `Recipe` model reads fields such as the meal name, cuisine, category, instructions, thumbnail URL, and YouTube tutorial URL from the corresponding JSON properties.

Recipe information and images are provided by [TheMealDB](https://www.themealdb.com/).

## Project Structure

```text
Cookwise/
├── app/
│   ├── build.gradle.kts
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/app/cookwise/
│       │   ├── MainActivity.kt       # Activity and Compose entry point
│       │   ├── App.kt                # Main application composable
│       │   ├── HomeScreen.kt         # Recipe list, search UI, and recipe cards
│       │   ├── DetailScreen.kt       # Detailed recipe view, ingredients, and instructions
│       │   ├── Screen.kt             # Navigation routes and screen definitions
│       │   ├── AppViewModel.kt       # Recipe fetching, search, and UI state
│       │   ├── ApiService.kt         # Retrofit setup and API endpoints
│       │   ├── RecipeResponse.kt     # Recipe and API response data classes
│       │   └── ui/theme/
│       │       ├── Color.kt
│       │       ├── Theme.kt
│       │       └── Type.kt
│       └── res/                      # App resources, fonts, icons, and XML resources
├── gradle/
│   └── libs.versions.toml            # Centralized dependency versions
├── build.gradle.kts
├── settings.gradle.kts
├── gradlew
└── gradlew.bat
```

## Getting Started

### Prerequisites

- [Android Studio](https://developer.android.com/studio)
- Android SDK for the project (minimum supported Android version: **Android 7.0 / API 24**)
- An Android emulator or a physical Android device
- Internet access to retrieve recipes from TheMealDB

### Clone the repository

```bash
git clone https://github.com/Shaahmir/Cookwise.git
cd Cookwise
```

### Open and run

1. Open the cloned project in Android Studio.
2. Allow Gradle to sync and download the required dependencies.
3. Start an emulator or connect an Android device.
4. Select the `app` run configuration and press **Run**.

### Build from the command line

**Windows (PowerShell):**

```powershell
.\gradlew.bat assembleDebug
```

**macOS / Linux:**

```bash
./gradlew assembleDebug
```

## What This Project Practices

Cookwise brings together several foundational Android development concepts:

- Building layouts and interactive components with Jetpack Compose
- Fetching REST API data with Retrofit
- Parsing JSON with Gson annotations
- Performing asynchronous work with Kotlin coroutines
- Exposing UI state through a ViewModel
- Displaying remote images with Coil
- Supporting system themes and Material 3 styling
- Handling search results, loading, and basic request errors

## License

This project is licensed under the **MIT License**.
