# AGENTS.md

## Scope
This repository is an Android project built with Kotlin and Gradle.
Follow the existing architecture, naming, and module boundaries.
Prefer the smallest correct change that fully solves the task.

## Core rules
- Read nearby code before editing and match the existing architecture, naming, and style.
- Change only files required for the task.
- Never revert, rewrite, or reformat unrelated user or agent changes.
- Do not introduce new libraries, plugins, or frameworks unless they are clearly required by the task.
- Prefer localized fixes over broad refactors.
- Preserve existing public APIs unless the task explicitly requires changing them.

## Android architecture
- Keep module boundaries intact.
- Reuse the existing DI, navigation, UI-state, and error-handling patterns already used in the affected module.
- Keep business logic out of Activities, Fragments, and Composables.
- Prefer ViewModel/use-case/repository level fixes over UI-layer workarounds.
- Do not make a feature module depend directly on a data implementation module unless the architecture already does so.
- Keep interfaces in domain-facing modules and implementations in data-facing modules when that pattern already exists.

## Compose rules
- Apply Compose-specific configuration only to modules that actually contain Compose UI code.
- Do not enable Compose globally for all Android modules.
- If a module applies the Compose compiler plugin, ensure required Compose runtime dependencies are present on that module classpath.
- Do not add Compose dependencies to non-UI modules unless the task explicitly requires it.
- Prefer minimal Compose dependency sets and reuse existing BOM/version patterns in the repository.

## DI / Hilt rules
- Reuse the existing Hilt setup and module placement patterns.
- Keep interfaces in domain/api modules when that is the current architecture.
- Keep bindings/providers in the appropriate data/core module.
- Do not move DI wiring across modules unless the task explicitly requires it.
- If editing Hilt modules, verify that the consuming module still receives the expected bindings.

## Build logic and convention plugin safety
- Be very careful when changing `build-logic`, convention plugins, version catalogs, plugin management, or settings scripts.
- If changing a convention plugin, keep plugin ids, implementationClass names, package names, and source file names consistent.
- Use fully qualified implementation class names where required.
- Prefer minimal fixes in convention plugins over broad restructuring.
- When changing shared build logic, validate at least one module that applies the changed plugin.
- Do not rename plugin ids, modules, or package paths unless explicitly required by the task.

## Build and dependency safety
- Do not change Gradle Wrapper, AGP, Kotlin version, Compose compiler versioning strategy, version catalog, convention plugins, CI, signing, publishing, or release config unless the task explicitly requires it.
- Do not upgrade or downgrade dependencies "while here".
- If the project uses product flavors or custom build types, use existing variants already defined in the repo.
- Do not invent new Gradle task names, build variants, or source sets.
- Assume JDK 17 unless the task explicitly requires another version.
- If build validation is blocked by an environment mismatch, state the exact mismatch clearly.

## Validation
- Start with the smallest relevant Gradle task.
- If only one module changed, prefer module-scoped checks before running project-wide checks.
- Run only the smallest relevant build, test, or lint check for the touched code.
- Avoid project-wide validation unless the change affects shared build logic, dependency management, multiple modules, or application wiring.
- If you cannot run validation, say exactly why.

## Typical validation commands
Use the smallest relevant command first.

- `./gradlew :app:assembleDebug`
- `./gradlew testDebugUnitTest`
- `./gradlew lintDebug`

Module-scoped examples:
- `./gradlew :feature:home:compileDebugKotlin`
- `./gradlew :core:ui:compileDebugKotlin`
- `./gradlew :data:home:testDebugUnitTest`

## Binary files policy
- Do not add or modify binary files through Codex PR flow.
- Do not commit generated artifacts or build outputs such as `*.apk`, `*.aab`, `*.aar`, `*/build/*`, caches, or IDE artifacts.
- Do not modify `gradle/wrapper/gradle-wrapper.jar` through Codex PR flow.
- If Gradle Wrapper must be updated, change only text files such as `gradle/wrapper/gradle-wrapper.properties`, `gradlew`, and `gradlew.bat`, and clearly note that `gradle-wrapper.jar` must be restored or updated manually outside Codex PR flow.
- If a task requires a binary asset, make only the necessary source/text changes and clearly document the manual step.

## Files that must not be committed
- `local.properties`
- `.gradle/`
- `.idea/`
- `**/build/`
- `*.iml`
- signing keys, keystores, credentials, secrets, tokens
- generated reports unless the task explicitly asks for them

## Change discipline
- Keep diffs focused and easy to review.
- Do not mix architectural cleanup with functional fixes unless explicitly requested.
- Do not silently remove dependencies, modules, or features unless that is the task.
- When fixing build issues, identify the smallest real cause and fix that cause directly.

## Finish criteria
At the end of the task:
- Confirm what changed.
- List the validation commands actually run.
- State what was not validated.
- Mention any remaining risk, environment limitation, or manual follow-up needed.

## Pull request language
- PR title, PR description, and final summary to the user must always be written in Russian.
# User-provided custom instructions

PR всегда пиши на русском языке
