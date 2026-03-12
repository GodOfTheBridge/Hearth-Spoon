# Hearth & Spoon

`Hearth & Spoon` - Android-приложение на Kotlin с Compose UI и многомодульной структурой. Репозиторий разделен на слои `app`, `core`, `domain`, `data`, `feature`; устойчивые архитектурные правила и соглашения описаны в [ARCHITECTURE.md](./ARCHITECTURE.md).

## Стек

- Kotlin
- Jetpack Compose
- MVI
- Hilt
- Room
- Ktor
- multi-module Gradle
- Navigation 3

## Требования

- JDK 17
- Android SDK, настроенный через `ANDROID_HOME` или `local.properties`

## Локальная сборка

На Windows:

```powershell
.\gradlew.bat :app:assembleDebug
```

На macOS / Linux:

```bash
./gradlew :app:assembleDebug
```

Для минимальной локальной проверки после изменений можно использовать:

```powershell
.\gradlew.bat testDebugUnitTest
```

## Если отсутствует `gradle-wrapper.jar`

Без файла `gradle/wrapper/gradle-wrapper.jar` Gradle Wrapper не запустится.

1. Сначала восстановите `gradle/wrapper/gradle-wrapper.jar` из Git или из доверенной копии того же репозитория.
2. Если восстановить файл нельзя, используйте локально установленный Gradle той же линии, что указана в `gradle/wrapper/gradle-wrapper.properties`, и выполните `gradle wrapper`.
3. После восстановления wrapper снова используйте обычные команды `./gradlew` или `gradlew.bat`.

## Где смотреть дальше

- Локальный запуск, сборка и базовые требования: этот `README.md`
- Устойчивые архитектурные решения и правила зависимостей: [ARCHITECTURE.md](./ARCHITECTURE.md)
