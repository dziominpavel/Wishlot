# Wishlot — UI-направление

> Дата: 2026-05-18

**Стиль кода** — как GymProgress / VoiceMind (Compose, токены `Spacing`/`Dimens`, Material 3). **Визуальный стиль — свой**, не IRON CORE и не CLEAR BELL.

## Направление: «GIFT SPIN»

Ассоциации: **радость**, **сюрприз**, **осознанная покупка**. Не казино-агрессия (без неона и «джекпота»), не строгий финтех.

### Палитра (выбрать при фазе 0)

| Роль | Цвет | HEX (ориентир) |
|------|------|----------------|
| Primary | Тёплый коралл / подарочная лента | `#E85D4C` |
| Secondary | Мягкий золотой | `#F4B942` |
| Surface (light) | Кремовый | `#FFFBF7` |
| Surface (dark) | Тёплый уголь | `#1C1917` |
| Success (「Беру!」) | Зелёный мятный | `#2D9B83` |
| On surface muted | Серый для заметок | M3 `onSurfaceVariant` |

Тёмная тема — **равноправная** (вечерний сценарий «побаловать себя»).

### Типографика

- **Цена** на карточке — `titleLarge`, tabular nums.
- **Название желания** — `titleMedium`, max 2 строки + ellipsis.
- **Бюджет на экране розыгрыша** — `displaySmall` при вводе/подтверждении.

---

## Экраны

### WishlistScreen

- Список карточек: слева название, справа цена акцентом.
- **Swipe-to-delete**: свайп влево или вправо удаляет желание (красный фон + иконка `Delete`).
- FAB «+» — primary container.
- Empty: иллюстрация подарка (опционально) + «Добавьте первое желание».

### TreatYourselfScreen

- Крупное поле **бюджета** по центру.
- Подсказка: «Покажем желания не дороже этой суммы».
- CTA «Крутить» — full width, иконка `Casino` перед текстом, disabled если бюджет ≤ 0 или нет ACTIVE желаний.
- Chips быстрого бюджета (опционально фаза 3): последний, 1000, 3000, 5000.

### SpinResultScreen (колесо)

- Верх: `FortuneWheel` ~ 60% ширины экрана.
- Во время вращения — только рулетка, карточка скрыта.
- По окончании анимации — карточка победителя (elevation 2).
- Одна кнопка: **Принято** (filled, primary tint) — переводит желание в `FULFILLED`.

### HistoryScreen

- Карточка с зачёркнутым стилем не нужен — позитивный тон «уже порадовал себя».
- Дата исполнения мелким `labelMedium`.

### SettingsScreen

- Статистика, архив, экспорт/импорт, версия приложения.

---

## FortuneWheel (компонент)

| Параметр | Значение |
|----------|----------|
| Изображения | `wheel_disc`, `wheel_pointer` (PNG) |
| Длительность | ~3 s, `FastOutSlowInEasing` |
| Вращение | `wheel_disc` — `Modifier.graphicsLayer { rotationZ }` через `Animatable`; остальные слои статичны |
| Обороты | 4–6 полных + случайный финальный угол |
| Haptic | Light impact при остановке |
| a11y | `contentDescription`: «Колесо фортуны, крутится» |

### Слои

- **wheel_disc** — вращающийся круг с сегментами и центральным подарком (единый PNG), `clip(CircleShape)`.
- **wheel_pointer** — статичный указатель сверху (`Alignment.TopCenter`), смещён вверх для эффекта «над диском».

Не использовать rapid flashing (более 3 Hz) — доступность.

---

## Правила (как в GymProgress)

- Отступы: `Spacing`, размеры: `Dimens` в `ui/theme/`.
- Цвета через `MaterialTheme` + `WishlotTheme.colors`, без inline hex в screens.
- Иконки: `CardGiftcard` (вишлист), `wheel_disc` PNG (крутить), `CheckCircle`, `History`, `Settings`.
- Lint: `ContentDescription` на интерактивных иконках (ошибка сборки — как GymProgress).

---

## Связанные документы

- [FEATURE_PLAN.md](FEATURE_PLAN.md) — фаза 0 (тема)
- [BUSINESS_PROCESS.md](BUSINESS_PROCESS.md) — когда показывать экраны
- GymProgress `docs/DESIGN_SYSTEM.md` — **только** структура документа
