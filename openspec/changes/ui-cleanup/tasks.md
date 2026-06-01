# Tasks: UI Cleanup

## 1. Стрелки сортировки
- [x] 1.1 Удалить `onMoveUp`, `onMoveDown` из `WishCard.kt`
- [x] 1.2 Удалить `onMoveUp`, `onMoveDown` из `WishlistScreen.kt`
- [x] 1.3 Убрать `viewModel::moveWishUp`/`Down` из `MainActivity.kt`
- [x] 1.4 Удалить `moveWishUp()`, `moveWishDown()` из `WishlotViewModel.kt`
- [x] 1.5 Удалить `moveWishUp()`, `moveWishDown()` из `WishRepository.kt`
- [x] 1.6 Удалить строки `wish_move_up`, `wish_move_down` из `strings.xml`

## 2. Настройка анимации колеса
- [x] 2.1 Удалить блок анимации из `SettingsScreen.kt`
- [x] 2.2 Убрать `wheelAnimationEnabled`, `onWheelAnimationChange` из `MainActivity.kt`
- [x] 2.3 Удалить `wheelAnimationEnabled`, `setWheelAnimationEnabled()` из `WishlotViewModel.kt`
- [x] 2.4 В `startPick()` убрать `if (wheelAnimationEnabled.value)`, всегда `Spinning`
- [x] 2.5 Удалить `wheelAnimationKey`, `wheelAnimationEnabled`, `setWheelAnimationEnabled()` из `SettingsRepository.kt`
- [x] 2.6 Удалить строки `settings_animation_title`, `settings_animation_subtitle` из `strings.xml`
- [x] 2.7 Удалить `animationEnabled` из `FortuneWheel.kt`

## 3. Формат цены
- [x] 3.1 Заменить `MoneyUtils.formatMinor()` на формат с символом `₽`

## 4. Финальная проверка
- [x] 4.1 Clean build — **BUILD SUCCESSFUL**
- [ ] 4.2 Запуск на эмуляторе
- [ ] 4.3 Обновить дельта-спеки
