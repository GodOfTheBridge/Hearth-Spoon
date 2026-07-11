# Hearth & Spoon

Hearth & Spoon — развиваемое многомодульное Android-приложение на Kotlin и Jetpack Compose. Сейчас в проекте реализованы базовая навигация, загрузка содержимого главного экрана, экран деталей и пользовательские настройки темы и языка.

Репозиторий разделён на слои `app`, `core`, `domain`, `data` и `feature`. Устойчивые архитектурные решения и правила зависимостей описаны в [ARCHITECTURE.md](./ARCHITECTURE.md).

## Технологии

- Kotlin
- Jetpack Compose
- MVI
- Hilt
- Room
- Ktor
- Navigation 3
- многомодульная сборка Gradle

## Требования

- JDK 17
- Android SDK Platform 36
- Android Studio для запуска из IDE
- Android-устройство или эмулятор с API 24 и выше
- путь к Android SDK, заданный через `ANDROID_HOME` или свойство `sdk.dir` в `local.properties`

Перед сборкой убедитесь, что команда `java -version` указывает на JDK 17.

## Локальный запуск

1. Откройте корень репозитория в Android Studio.
2. Дождитесь завершения Gradle Sync.
3. Выберите конфигурацию `app` и устройство с API 24 или выше.
4. Запустите приложение кнопкой **Run**.

Для установки debug-сборки из командной строки используйте:

```powershell
# Windows
.\gradlew.bat :app:installDebug
```

```bash
# macOS / Linux
./gradlew :app:installDebug
```

## Сборка и проверки

Сборка debug-варианта на Windows:

```powershell
.\gradlew.bat :app:assembleDebug
```

На macOS или Linux:

```bash
./gradlew :app:assembleDebug
```

Основные локальные проверки:

```powershell
.\gradlew.bat testDebugUnitTest
.\gradlew.bat lintDebug
```

Для изменения в одном модуле сначала запускайте соответствующую модульную задачу.

## Структура проекта

- `app` — точка входа, сборка DI-графа и верхнеуровневая навигация.
- `core` — общие модели, UI-компоненты и инфраструктура.
- `domain` — публичные доменные контракты.
- `data` — реализации репозиториев и источники данных.
- `feature` — пользовательские сценарии и экраны.

Подробные границы модулей зафиксированы в [ARCHITECTURE.md](./ARCHITECTURE.md).

## Если отсутствует `gradle-wrapper.jar`

Без файла `gradle/wrapper/gradle-wrapper.jar` Gradle Wrapper не запустится.

1. Восстановите `gradle/wrapper/gradle-wrapper.jar` из Git или доверенной копии этого репозитория.
2. Если восстановить файл нельзя, установите совместимую версию Gradle, указанную в `gradle/wrapper/gradle-wrapper.properties`, и выполните `gradle wrapper`.
3. После восстановления снова используйте `./gradlew` или `gradlew.bat`.
