# Структура папок Wishlot

> 2026-05-18 · Целевое дерево (код ещё не создан)

Референс bootstrap: **VoiceMind** + **GymProgress** (`gradle/`, `app/`, `docs/`, `.cursor/rules/`).

```
Wishlot/
├── .cursor/
│   ├── plans/
│   └── rules/
│       └── project-context.mdc      # при bootstrap
├── app/src/
│   ├── main/java/com/example/wishlot/
│   │   ├── data/
│   │   │   ├── pick/                # WishPicker, PickResult
│   │   │   └── backup/              # фаза 5
│   │   ├── viewmodel/
│   │   └── ui/
│   │       ├── components/          # WishCard, FortuneWheel, BudgetField
│   │       ├── navigation/
│   │       ├── screens/
│   │       └── theme/
│   ├── main/res/
│   │   ├── values/                  # strings, themes
│   │   └── xml/                     # backup_rules (фаза 5)
│   ├── test/java/.../pick/          # WishPickerTest
│   └── androidTest/
├── gradle/
│   └── libs.versions.toml
├── docs/                            # эта папка
├── version.properties
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradlew / gradlew.bat
├── AGENTS.md
└── README.md
```

## Отличия от VoiceMind

| VoiceMind | Wishlot |
|-----------|---------|
| `data/speech/`, `data/scheduling/` | не нужны в MVP |
| `data/parse/` (время) | `data/pick/` (бюджет + random) |
| `ConfirmReminderScreen` | `SpinResultScreen` |

## Отличия от GymProgress

| GymProgress | Wishlot |
|-------------|---------|
| `minSdk 29` | **`minSdk 26`** |
| scoring, exercises, workout | wishes, pick, wheel |

Пустые каталоги при bootstrap — `.gitkeep`.

См. [ARCHITECTURE.md](ARCHITECTURE.md).
