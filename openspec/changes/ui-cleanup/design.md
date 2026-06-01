# Design: UI Cleanup

## 1. Удаление стрелок сортировки

### Почему
Стрелки `KeyboardArrowUp`/`KeyboardArrowDown` на каждой карточке — устаревший паттерн. Они занимают место, усложняют UI и дублируют функциональность, которой достаточно редко пользуются.

### Как
- **WishCard.kt**: удалить параметры `onMoveUp`, `onMoveDown` и весь Column с IconButton.
- **WishlistScreen.kt**: удалить параметры `onMoveUp`, `onMoveDown`, убрать передачу в `WishCard`.
- **MainActivity.kt**: убрать передачу `onMoveUp`/`onMoveDown` в `WishlistScreen`.
- **WishlotViewModel.kt**: удалить методы `moveWishUp()` и `moveWishDown()`.
- **WishRepository.kt**: удалить методы `moveWishUp()` и `moveWishDown()` (если есть).
- **strings.xml**: удалить `wish_move_up`, `wish_move_down`.

## 2. Удаление настройки анимации колеса

### Почему
Анимация — неотъемлемая часть UX колеса фортуны. Переключатель избыточен.

### Как
- **SettingsScreen.kt**: удалить блок "Анимация колеса" (Text + Switch + HorizontalDivider).
- **MainActivity.kt**: убрать передачу `wheelAnimationEnabled` и `onWheelAnimationChange` в `SettingsScreen`.
- **WishlotViewModel.kt**: 
  - Удалить `val wheelAnimationEnabled` и `setWheelAnimationEnabled()`.
  - В `startPick()` убрать `if (wheelAnimationEnabled.value)` — всегда показывать `Spinning`.
- **SettingsRepository.kt**: удалить `wheelAnimationKey`, `wheelAnimationEnabled`, `setWheelAnimationEnabled()`.
- **strings.xml**: удалить `settings_animation_title`, `settings_animation_subtitle`.

## 3. Форматирование цены в ₽

### Почему
`NumberFormat.getCurrencyInstance(locale)` с `Currency.getInstance("RUB")` даёт непредсказуемый вывод в зависимости от системного locale ("руб.", "RUB", "₽"). Нужен deterministic символ `₽`.

### Как
- **MoneyUtils.kt**: заменить `formatMinor()` на:
  ```kotlin
  fun formatMinor(minor: Long): String {
      val rubles = minor / 100.0
      return String.format(java.util.Locale.getDefault(), "%,.0f ₽", rubles).replace(" ", "\u00A0")
  }
  ```
  Или с `java.text.DecimalFormat`:
  ```kotlin
  fun formatMinor(minor: Long): String {
      val formatter = java.text.DecimalFormat("#,##0.00 ₽")
      return formatter.format(minor / 100.0)
  }
  ```
  Проще всего — `String.format(Locale.getDefault(), "%,.0f ₽", minor / 100.0)`.

### Проверка
- `0` → `0 ₽`
- `10000` → `100 ₽`
- `125000` → `1 250 ₽`
- `999999` → `9 999,99 ₽` (если с копейками) или `10 000 ₽` (если без)

Учитывая, что `minorToDisplayInput` возвращает рубли без копеек, логично показывать целые рубли: `%,.0f ₽`.

## 4. Launcher Icon (Out of Scope)

Обновление PNG-ресурсов `ic_launcher*.png` требует графического ассета. Это делается отдельно через дизайнер или генератор иконок.

## Файлы для изменения

| Файл | Изменение |
|------|-----------|
| `ui/components/WishCard.kt` | Удалить стрелки |
| `ui/screens/WishlistScreen.kt` | Удалить onMoveUp/onMoveDown |
| `MainActivity.kt` | Убрать onMoveUp/onMoveDown, wheelAnimation |
| `viewmodel/WishlotViewModel.kt` | Удалить moveWishUp/Down, wheelAnimation |
| `data/SettingsRepository.kt` | Удалить wheelAnimation |
| `ui/screens/SettingsScreen.kt` | Удалить блок анимации |
| `data/MoneyUtils.kt` | Заменить формат на ₽ |
| `res/values/strings.xml` | Удалить неиспользуемые строки |
