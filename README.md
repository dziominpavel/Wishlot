# Wishlot

Android-приложение **личного вишлиста**: список желаемых покупок с ценами. Когда вы готовы **побаловать себя** на выбранную сумму, приложение отфильтрует подходящие желания, **случайно** выберет одно (анимация «колеса фортуны») и попросит подтвердить — после «Беру!» желание попадёт в историю как исполненное.

> Статус: **MVP+** — полный цикл + категории, архив, приоритет, бэкап JSON.

## Как это работает (кратко)

1. Добавляете желания: название и цена.
2. На экране «Побаловать себя» вводите **бюджет** («могу потратить до …»).
3. Приложение оставляет только активные желания **не дороже бюджета**.
4. Крутится **колесо** → показывается одно случайное желание.
5. **«Беру!»** — отмечается исполненным; **«Не сейчас»** — остаётся в вишлисте, можно крутить снова.

## Возможности (целевые)

| Область | Описание |
|---------|----------|
| Вишлист | Добавление, редактирование, удаление желаний с ценой |
| Бюджет | Фильтр перед розыгрышем |
| Розыгрыш | Случайный выбор + анимация колеса |
| Решение | Явное согласие / отказ |
| История | Исполненные желания |
| Offline | Room, данные на устройстве |

## Стек (план)

- **Kotlin**, модуль `:app`
- **Jetpack Compose** + Material 3
- **Room**, **DataStore**
- **minSdk 26** (Android 8.0+), **targetSdk 36**
- Архитектура кода — как **GymProgress** / **VoiceMind** (один ViewModel, `safeDb`, overlay-навигация)

## Документация

| Файл | Содержание |
|------|------------|
| [docs/README.md](docs/README.md) | Оглавление документации |
| [docs/PROJECT_OVERVIEW.md](docs/PROJECT_OVERVIEW.md) | Продукт, сценарии, модель данных |
| [docs/BUSINESS_PROCESS.md](docs/BUSINESS_PROCESS.md) | Бизнес-процесс end-to-end |
| [docs/WISH_PICK_LOGIC.md](docs/WISH_PICK_LOGIC.md) | Фильтр и random |
| [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md) | Пакеты, ViewModel, Room |
| [docs/FEATURE_PLAN.md](docs/FEATURE_PLAN.md) | План реализации по фазам |
| [docs/DESIGN_SYSTEM.md](docs/DESIGN_SYSTEM.md) | UI «GIFT SPIN» |
| [docs/FOLDER_STRUCTURE.md](docs/FOLDER_STRUCTURE.md) | Дерево каталогов |

## Референсы

- **Структура проекта и стиль кода:** GymProgress, VoiceMind (`docs/`, Gradle, Compose-паттерны).
- **minSdk:** 26 как VoiceMind (не 29 как GymProgress).

## Быстрый старт

1. Открыть в Android Studio; `local.properties` с `sdk.dir` (или скопировать из VoiceMind/GymProgress).
2. Сборка: `gradlew.bat assembleDebug` (Windows) / `./gradlew assembleDebug`.
3. Установка: `gradlew.bat installDebug` или Run в IDE (API 26+).
4. APK: `app/build/outputs/apk/debug/app-debug.apk`.

## Лицензия

Уточняется владельцем репозитория.
