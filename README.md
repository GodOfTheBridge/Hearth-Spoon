# Hearth & Spoon

Android Kotlin Compose multi-module template:
- `app`
- `core:common`, `core:model`, `core:ui`, `core:designsystem`, `core:network`, `core:database`
- `domain:api`
- `data:home`
- `feature:home`

Includes base MVI (`StateFlow` + `Channel` effects), Hilt DI, Ktor client, Room database, and build-logic convention plugins with version catalog.


## Requirements

- JDK 17 (project is configured for Java 17; JDK 21/25 is not required).
- Android SDK configured via `ANDROID_HOME` or `local.properties` (`sdk.dir=...`).
