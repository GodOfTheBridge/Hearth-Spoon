# AGENTS.md

## Scope
This repository is an Android project built with Kotlin and Gradle. Follow existing patterns. Prefer the smallest correct change.

## Core rules
- Read nearby code before editing and match the existing architecture, naming, and style.
- Change only files required for the task.
- Never revert or rewrite unrelated user or agent changes.
- Do not introduce new libraries unless they are clearly required by the task.

## Android architecture
- Keep module boundaries intact.
- Reuse the existing DI, navigation, UI-state, and error-handling patterns already used in the affected module.
- Keep business logic out of Activities, Fragments, and Composables.
- Prefer localized fixes over broad refactors.

## Build and dependency safety
- Do not change Gradle wrapper, AGP, Kotlin version, version catalog, convention plugins, CI, signing, or release config unless the task explicitly requires it.
- If the project uses product flavors or custom build types, use existing variants already defined in the repo. Do not invent new task names.

## Validation
- Start with the smallest relevant Gradle task.
- If only one module changed, prefer module-scoped checks before running project-wide checks.
- Run relevant build, test, and lint checks for the touched code.
- Typical commands in Android repos:
  - `./gradlew :app:assembleDebug`
  - `./gradlew testDebugUnitTest`
  - `./gradlew lintDebug`

## Binary files policy
- Do not add or modify binary files through Codex PR flow.
- Do not commit generated artifacts or build outputs such as `*.apk`, `*.aab`, `*.aar`, `*.jar`, `*/build/*`, caches, or IDE artifacts.
- If a task requires a binary asset, change only source/text files and clearly note the manual step.

## Finish criteria
- Confirm what changed.
- List the validation commands actually run.
- Mention any remaining risk or follow-up needed.
# User-provided custom instructions

PR всегда пиши на русском языке
