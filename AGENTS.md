# Руководство для агента (Cursor)

**Wishlot** — вишлист с розыгрышем желания по бюджету (колесо фортуны + подтверждение).

**GymProgress** и **VoiceMind** — референс **структуры** и **стиля кода** (Kotlin, Compose, Room, ViewModel). Продуктовые требования — только `docs/` Wishlot.

## Перед изменениями

1. `docs/PROJECT_OVERVIEW.md` — продукт.
2. `docs/BUSINESS_PROCESS.md`, `docs/WISH_PICK_LOGIC.md`.
3. `docs/FEATURE_PLAN.md` — фазы.
4. `docs/ARCHITECTURE.md`, `docs/DESIGN_SYSTEM.md`.

## Приоритеты

- **`FULFILLED` только после «Беру!»** — random не меняет статус.
- **Pick до анимации** — winner известен до колеса.
- **Цены в minor units** (`Long`).
- **`minSdk 26`** — не поднимать без причины.
- **Один ViewModel** — `WishlotViewModel`.
- **Ошибки** — Snackbar через `errorMessage`.

## Сортировка

| Список | ORDER BY |
|--------|----------|
| Активный вишлист | `createdAt ASC, id ASC` |
| История | `fulfilledAt DESC, id DESC` |

## Стиль кода

- Как GymProgress / VoiceMind: корутины, StateFlow, Compose.
- UI-тексты — русский; код — английский.
- UI-токены — `WishlotTheme` (GIFT SPIN), не IRON CORE / CLEAR BELL.
