# Changelog

## 0.1.12-SNAPSHOT (unreleased)

## [0.1.11] — 2024-01-28

### 🆕 Added
* adding more `DomainObjectName` overloads by @RBusarow in https://github.com/rickbusarow/kotlin-gradle-extensions/pull/153
### 🧰 Maintenance
* fix(deps): update kotlin and compiler plugins to v1.9.22 by @renovate in https://github.com/rickbusarow/kotlin-gradle-extensions/pull/156
* Update dependency com.rickbusarow.kgx:kotlin-gradle-extensions to v0.1.10 by @renovate in https://github.com/rickbusarow/kotlin-gradle-extensions/pull/140
* Update dependency com.github.gmazzo.buildconfig to v5.3.5 by @renovate in https://github.com/rickbusarow/kotlin-gradle-extensions/pull/141
* Update dependency com.diffplug.spotless:spotless-plugin-gradle to v6.24.0 by @renovate in https://github.com/rickbusarow/kotlin-gradle-extensions/pull/143
* Update rickBusarow.kase to v0.7.0 by @renovate in https://github.com/rickbusarow/kotlin-gradle-extensions/pull/142
* Update dependency com.squareup:kotlinpoet to v1.16.0 by @renovate in https://github.com/rickbusarow/kotlin-gradle-extensions/pull/145
* Update dependencyAnalysis to v1.29.0 by @renovate in https://github.com/rickbusarow/kotlin-gradle-extensions/pull/144
* Update rickBusarow.kase to v0.8.0 by @renovate in https://github.com/rickbusarow/kotlin-gradle-extensions/pull/146
* Update styfle/cancel-workflow-action action to v0.12.1 by @renovate in https://github.com/rickbusarow/kotlin-gradle-extensions/pull/150
* Update dependency com.github.ben-manes:gradle-versions-plugin to v0.51.0 by @renovate in https://github.com/rickbusarow/kotlin-gradle-extensions/pull/148
* Update dependency com.diffplug.spotless:spotless-plugin-gradle to v6.25.0 by @renovate in https://github.com/rickbusarow/kotlin-gradle-extensions/pull/149
* Update rickBusarow.kase to v0.9.1 by @renovate in https://github.com/rickbusarow/kotlin-gradle-extensions/pull/147
* chore(deps): update plugin com.gradle.enterprise to v3.16.2 by @renovate in https://github.com/rickbusarow/kotlin-gradle-extensions/pull/152
* Renovate config updates by @RBusarow in https://github.com/rickbusarow/kotlin-gradle-extensions/pull/154

**Full Changelog**: https://github.com/rickbusarow/kotlin-gradle-extensions/compare/0.1.10...0.1.11
## [0.1.10] — 2024-01-08

### 🆕 Added

- add common `with____{...}` callback extensions to `PluginManager` and `PluginContainer` by @RBusarow in https://github.com/rickbusarow/kotlin-gradle-extensions/pull/131
- add `orElse` and `convention` extensions for providers and properties by @RBusarow in https://github.com/rickbusarow/kotlin-gradle-extensions/pull/138

**Full Changelog**: https://github.com/rickbusarow/kotlin-gradle-extensions/compare/0.1.9...0.1.10

## [0.1.9] — 2023-12-03

### 🆕 Added

- `org.jetbrains.kotlin.gradle.dsl.JvmTarget` extensions to/from `Int` in https://github.com/rickbusarow/kotlin-gradle-extensions/pull/98
- `VersionCatalog` extension delegates in https://github.com/rickbusarow/kotlin-gradle-extensions/pull/99
- move `SourceSetName` -> `ConfigurationName` extensions from `SourceSetName.kt` to `ConfigurationName.kt` in https://github.com/rickbusarow/kotlin-gradle-extensions/pull/101
- add `GradleLazy<T>` in https://github.com/rickbusarow/kotlin-gradle-extensions/pull/106

### 🧰 Maintenance

- Update github.release to v2.5.1 in https://github.com/rickbusarow/kotlin-gradle-extensions/pull/68
- Update kotest to v5.8.0 in https://github.com/rickbusarow/kotlin-gradle-extensions/pull/79
- Update junit5 monorepo to v5.10.1 in https://github.com/rickbusarow/kotlin-gradle-extensions/pull/80
- Update KtLint things in https://github.com/rickbusarow/kotlin-gradle-extensions/pull/81
- Update rickBusarow.ktlint to v0.2.1 in https://github.com/rickbusarow/kotlin-gradle-extensions/pull/82
- Update dependency com.github.gmazzo.buildconfig to v4.2.0 in https://github.com/rickbusarow/kotlin-gradle-extensions/pull/86
- Update detekt to v1.23.4 in https://github.com/rickbusarow/kotlin-gradle-extensions/pull/88
- Update dependency gradle to v8.5 in https://github.com/rickbusarow/kotlin-gradle-extensions/pull/92
- Update dependency com.squareup:kotlinpoet to v1.15.2 in https://github.com/rickbusarow/kotlin-gradle-extensions/pull/93
- Update actions/setup-java action to v4 in https://github.com/rickbusarow/kotlin-gradle-extensions/pull/94
- explicitly set the internal KtLint version in https://github.com/rickbusarow/kotlin-gradle-extensions/pull/96
- Update dependency com.diffplug.spotless:spotless-plugin-gradle to v6.23.2 in https://github.com/rickbusarow/kotlin-gradle-extensions/pull/97
- misc build-logic cleanup in https://github.com/rickbusarow/kotlin-gradle-extensions/pull/100
- Update dependency com.github.ben-manes:gradle-versions-plugin to v0.50.0 in https://github.com/rickbusarow/kotlin-gradle-extensions/pull/103
- Update dependencyAnalysis to v1.27.0 in https://github.com/rickbusarow/kotlin-gradle-extensions/pull/105

**Full Changelog**: https://github.com/rickbusarow/kotlin-gradle-extensions/compare/0.1.8...0.1.9

## [0.1.8] - 2023-10-31

### 🆕 Added

- add `ObjectFactory` extension functions for property delegates

### Fixed

- add missing `@InternalGradleApiAccess` opt-ins to some internal functions
- move some non-internal extensions out of the `internal` package

## [0.1.7] - 2023-10-25

### Fixed

- `SourceSetName.___Config()` functions now return correct values for `main` source sets

## [0.1.6] - 2023-10-12

### 🆕 Added

- added the `DomainObjectName` type https://github.com/rickbusarow/kotlin-gradle-extensions/pull/69

**Full Changelog**: https://github.com/rickbusarow/kotlin-gradle-extensions/compare/0.1.5...0.1.6

## [0.1.5] - 2023-10-10

## What's Changed

### 🆕 Added

- more syntactic sugar around `whenElementRegistered {}`
- `Project.projectDependency(...)` and `DependencyHandler.project(...)` https://github.com/RBusarow/kotlin-gradle-extensions/pull/58

### 🧰 Maintenance

- symlink gradle files into build-logic
- remove `kotlin-gradle-plugin-api-gradle76` dependency declaration https://github.com/rickbusarow/kotlin-gradle-extensions/pull/55
- consume KGX internally https://github.com/rickbusarow/kotlin-gradle-extensions/pull/57

**Full Changelog**: https://github.com/rickbusarow/kotlin-gradle-extensions/compare/0.1.4...0.1.5

## [0.1.4] - 2023-09-09

### Changed

- revert Kotlin version to 1.8.10

**Full Changelog**: https://github.com/RBusarow/kotlin-gradle-extensions/compare/0.1.2...0.1.3

## [0.1.3] - 2023-09-07

### Fixed

- replace leftover "doks"
  copy-pasta ([#46](https://github.com/RBusarow/kotlin-gradle-extensions/pull/46))

### Other Changes

- lower the published JVM target to 1.8 (down from 11) ([#47](https://github.com/RBusarow/kotlin-gradle-extensions/pull/47))

**Full Changelog**: https://github.com/RBusarow/kotlin-gradle-extensions/compare/0.1.2...0.1.3

## [0.1.2] - 2023-09-06

## What's Changed

### 🧰 Maintenance

- archive Dokka versioning docs as .zip
  files https://github.com/RBusarow/kotlin-gradle-extensions/pull/40
- don't use the shadow plugin https://github.com/RBusarow/kotlin-gradle-extensions/pull/43

**Full Changelog**: https://github.com/RBusarow/kotlin-gradle-extensions/compare/0.1.1...0.1.2

## [0.1.1] - 2023-09-05

## What's Changed

### 🧰 Maintenance

- delete unused dependency definitions https://github.com/RBusarow/kotlin-gradle-extensions/pull/31
- fix the repo check in the snapshot publishing
  workflow https://github.com/RBusarow/kotlin-gradle-extensions/pull/32
- update ktrules to 1.1.4 https://github.com/RBusarow/kotlin-gradle-extensions/pull/35
- consume the released version
  internally https://github.com/RBusarow/kotlin-gradle-extensions/pull/39

### Other Changes

- fix rendering in the README https://github.com/RBusarow/kotlin-gradle-extensions/pull/33
- make internal things public https://github.com/RBusarow/kotlin-gradle-extensions/pull/38

**Full Changelog**: https://github.com/RBusarow/kotlin-gradle-extensions/commits/0.1.1

## [0.1.0] - 2023-09-05

Hello World

[0.1.0]: https://github.com/rbusarow/kotlin-gradle-extensions/releases/tag/0.1.0
[0.1.1]: https://github.com/rbusarow/kotlin-gradle-extensions/releases/tag/0.1.1
[0.1.2]: https://github.com/rbusarow/kotlin-gradle-extensions/releases/tag/0.1.2
[0.1.3]: https://github.com/rbusarow/kotlin-gradle-extensions/releases/tag/0.1.3
[0.1.4]: https://github.com/rbusarow/kotlin-gradle-extensions/releases/tag/0.1.4
[0.1.5]: https://github.com/rbusarow/kotlin-gradle-extensions/releases/tag/0.1.5
[0.1.6]: https://github.com/rbusarow/kotlin-gradle-extensions/releases/tag/0.1.6
[0.1.7]: https://github.com/rbusarow/kotlin-gradle-extensions/releases/tag/0.1.7
[0.1.8]: https://github.com/rbusarow/kotlin-gradle-extensions/releases/tag/0.1.8
[0.1.9]: https://github.com/rbusarow/kotlin-gradle-extensions/releases/tag/0.1.9
[0.1.10]: https://github.com/rbusarow/kotlin-gradle-extensions/releases/tag/0.1.10
[0.1.11]: https://github.com/rbusarow/kotlin-gradle-extensions/releases/tag/0.1.11
