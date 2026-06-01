# Tasks: Удаление приоритета желаний

## 1. Модель и база данных
- [x] 1.1 Удалить `priority: Int` из `Wish` entity
- [x] 1.2 Удалить `WishPriority.kt` (enum)
- [x] 1.3 Добавить `MIGRATION_2_3` с пересозданием таблицы `wishes`
- [x] 1.4 Увеличить `version` в `AppDatabase`, подключить миграцию
- [x] 1.5 Собрать и проверить, что Room компилируется

## 2. Domain layer (WishPicker)
- [x] 2.1 Удалить `priority` из `WishSnapshot` и `toSnapshot()`
- [x] 2.2 Удалить `pickWeighted()`, оставить только `pickRandom()` с равной вероятностью
- [x] 2.3 Обновить `WishPickerTest`: удалить weighted-тест, поправить фикстуры
- [x] 2.4 Запустить тесты, убедиться что проходят

## 3. ViewModel
- [x] 3.1 Удалить `priority: WishPriority` из `WishEditDraft`
- [x] 3.2 Удалить priority из `WishlotViewModel.saveWish()` (insert/update)
- [x] 3.3 Удалить priority из `toDraft()`

## 4. UI
- [x] 4.1 Удалить `PriorityPicker` composable из `CategoryPicker.kt` (или файл)
- [x] 4.2 Удалить блок priority из `AddEditWishScreen.kt`
- [x] 4.3 Удалить строки `priority_*` из `strings.xml`

## 5. Бэкап
- [x] 5.1 Удалить `priority` из экспорта/импорта в `BackupCodec`
- [x] 5.2 Увеличить `SCHEMA_VERSION` → 2
- [x] 5.3 Обновить `BackupCodecTest` JSON-фикстуры
- [x] 5.4 Запустить тесты

## 6. Финальная проверка
- [x] 6.1 Clean build — **BUILD SUCCESSFUL**
- [x] 6.2 Запуск на эмуляторе: работает без priority
- [ ] 6.3 Проверить импорт старого бэкапа (с priority)
- [x] 6.4 Обновить дельта-спеки → архивировать change
