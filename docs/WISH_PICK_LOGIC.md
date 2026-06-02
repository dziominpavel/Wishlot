# Логика фильтра и случайного выбора

> Дата: 2026-05-18

Чистая бизнес-логика без Android SDK — тестируется в `app/src/test/`.

---

## Входные данные

```kotlin
data class PickInput(
    val budgetMinor: Long,           // бюджет сессии, > 0
    val wishes: List<WishSnapshot>,  // обычно все ACTIVE из репозитория
)

data class WishSnapshot(
    val id: Long,
    val title: String,
    val priceMinor: Long,
    val status: WishStatus,
)
```

---

## Фильтр кандидатов

```kotlin
fun filterCandidates(wishes: List<WishSnapshot>, budgetMinor: Long): List<WishSnapshot> =
    wishes.filter { it.status == WishStatus.ACTIVE && it.priceMinor <= budgetMinor }
```

| Условие | Включить в candidates? |
|---------|------------------------|
| `status == FULFILLED` | ❌ |
| `status == ARCHIVED` | ❌ |
| `priceMinor > budgetMinor` | ❌ |
| `priceMinor == budgetMinor` | ✅ (ровно в бюджет) |
| `priceMinor <= 0` | ❌ (не должно попасть в БД; валидация при сохранении) |

---

## Случайный выбор

```kotlin
fun pickRandom(candidates: List<WishSnapshot>, random: Random): WishSnapshot? {
    if (candidates.isEmpty()) return null
    return candidates[random.nextInt(candidates.size)]
}
```

### Требования к random

| Требование | Реализация |
|------------|------------|
| Равномерность | `Random.nextInt(size)` или `kotlin.random` |
| Тестируемость | Инжектить `Random` / фиксированный seed в unit-тестах |
| Не зависеть от анимации | `pickRandom` вызывается **до** старта анимации колеса |

### Повторный розыгрыш

- Пользователь закрывает экран результата и нажимает «Крутить» заново.
- Тот же `budgetMinor`, тот же пул `ACTIVE` с `price <= budget`.
- Повторное выпадение того же `id` — **допустимо** (классический random).

---

## Результат операции

```kotlin
sealed class PickResult {
    data object NoCandidates : PickResult()
    data class Winner(val wish: WishSnapshot) : PickResult()
}

fun runPick(input: PickInput, random: Random): PickResult {
    val candidates = filterCandidates(input.wishes, input.budgetMinor)
    val winner = pickRandom(candidates, random) ?: return PickResult.NoCandidates
    return PickResult.Winner(winner)
}
```

---

## Колесо фортуны и список кандидатов

Анимация — **декоративный слой**, не влияет на результат. Используется вектор/PNG рулетки (`ic_launcher_foreground`).

| candidates.size | Поведение UI |
|-----------------|--------------|
| 0 | Не запускать колесо; empty state |
| 1+ | Рулетка крутится ~3 с, затем появляется карточка победителя |

Рулетка — единое изображение, вращается через `Modifier.graphicsLayer { rotationZ }` (`Animatable` + `tween`).
Карточка результата (`title`, `priceMinor`, `note`) показывается **только после окончания анимации**.

Инвариант: `winner.id` из `runPick` == желание на финальной карточке.

---

## Форматирование цены

| Слой | Ответственность |
|------|-----------------|
| БД / domain | `priceMinor: Long` |
| UI | `String.format(locale, "%,.0f", amount)` — число с разделителем тысяч, без символа валюты |
| Ввод | Парсить строку → minor (копейки), округление до целых копеек |

Пример: 1 499,99 ₽ → хранить `149999` или округлить до `150000` — **в MVP округлять до целых рублей** (копейки = 0) для простоты ввода.

---

## Unit-тесты (минимальный набор)

| Тест | Ожидание |
|------|----------|
| Пустой список | `NoCandidates` |
| Все дороже бюджета | `NoCandidates` |
| Один подходящий | всегда он |
| Два подходящих, fixed Random(0) | первый по порядку фильтра (стабильный порядок: `createdAt ASC, id ASC`) |
| FULFILLED не попадает | исключён |
| price == budget | попадает |

---

## Связанные документы

- [BUSINESS_PROCESS.md](BUSINESS_PROCESS.md)
- [ARCHITECTURE.md](ARCHITECTURE.md) — `WishPicker` в `data/pick/`
