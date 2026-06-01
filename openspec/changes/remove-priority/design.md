# Design: Удаление приоритета желаний

## Technical Approach

### 1. Модель данных

- Удалить `priority: Int` из `Wish` entity
- Удалить `WishPriority` enum
- Обновить `AppDatabase` версия +1, добавить миграцию

### 2. Миграция БД (SQLite)

SQLite до 3.35 не поддерживает `ALTER TABLE DROP COLUMN`. Стратегия:

```sql
-- 1. Создать новую таблицу без priority
CREATE TABLE wishes_new (...)
-- 2. Скопировать данные
INSERT INTO wishes_new SELECT id, clientId, title, priceMinor, note, status, category, sortOrder, createdAt, fulfilledAt FROM wishes
-- 3. Удалить старую
DROP TABLE wishes
-- 4. Переименовать
ALTER TABLE wishes_new RENAME TO wishes
-- 5. Пересоздать индексы
```

### 3. WishPicker (Domain)

- Удалить `pickWeighted()` — оставить только равновероятный `pickRandom()`
- `pickRandom()` уже вызывает `pickWeighted()` — заменить на прямой `random.nextInt(candidates.size)`
- Удалить `priority: Int` из `WishSnapshot` и `toSnapshot()`

### 4. UI

- Удалить `PriorityPicker` composable из `CategoryPicker.kt` (или файл целиком, если нет других компонентов)
- Удалить блок `PriorityPicker` из `AddEditWishScreen.kt`
- Удалить `priority` из `WishEditDraft`
- Удалить обработку `priority` в `WishlotViewModel.saveWish()` и `toDraft()`

### 5. Бэкап

- Удалить `priority` из экспорта/импорта в `BackupCodec`
- Обновить `SCHEMA_VERSION` → 2 (или какая следующая)
- При импорте старых бэкапов — игнорировать поле `priority`, использовать default

### 6. Тесты

- `WishPickerTest`: удалить `pickWeighted_highPriorityWinsMoreOften`, обновить snapshots (убрать priority)
- `BackupCodecTest`: обновить JSON-фикстуры

## Architecture Decisions

### Decision: Пересоздание таблицы вместо DROP COLUMN
Room не генерит миграцию с DROP COLUMN. Ручной SQL с пересозданием таблицы — единственный путь для SQLite на Android.

### Decision: Увеличение schemaVersion бэкапа
Старые бэкапы содержат `priority`. Новая версия 2 позволяет импортировать их без ошибки (optInt с default). Если встретить старый бэкап — priority игнорируется.

## File Changes

| Файл | Действие |
|------|----------|
| `data/Wish.kt` | Удалить `priority` |
| `data/WishPriority.kt` | Удалить файл |
| `data/AppDatabaseMigrations.kt` | Добавить `MIGRATION_2_3` |
| `data/AppDatabase.kt` | Увеличить version, подключить миграцию |
| `data/pick/WishSnapshot.kt` | Удалить `priority` |
| `data/pick/WishPicker.kt` | Упростить до равновероятного random |
| `data/backup/BackupCodec.kt` | Удалить priority из JSON, schemaVersion=2 |
| `viewmodel/WishEditDraft.kt` | Удалить `priority` |
| `viewmodel/WishlotViewModel.kt` | Удалить priority из save/load draft |
| `ui/screens/AddEditWishScreen.kt` | Удалить PriorityPicker |
| `ui/components/CategoryPicker.kt` | Удалить PriorityPicker composable |
| `data/pick/WishPickerTest.kt` | Удалить weighted-тест, обновить фикстуры |
| `data/backup/BackupCodecTest.kt` | Обновить JSON-фикстуры |
| `res/values/strings.xml` | Удалить строки priority_* |
