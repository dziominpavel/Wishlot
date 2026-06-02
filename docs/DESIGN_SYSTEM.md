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
- FAB «+» — primary container.
- Empty: иллюстрация подарка (опционально) + «Добавьте первое желание».

### TreatYourselfScreen

- Крупное поле **бюджета** по центру.
- Подсказка: «Покажем желания не дороже этой суммы».
- CTA «Крутить» — full width, disabled если бюджет ≤ 0 или нет ACTIVE желаний.
- Chips быстрого бюджета (опционально фаза 3): последний, 1000, 3000, 5000.

### SpinResultScreen (колесо)

- Верх: `FortuneWheel` ~ 60% ширины экрана.
- Низ: карточка победителя (elevation 2).
- Кнопки в ряд: **Не сейчас** (outlined) | **Беру!** (filled, success tint).
- Во время вращения — кнопки disabled.

### HistoryScreen

- Карточка с зачёркнутым стилем не нужен — позитивный тон «уже порадовал себя».
- Дата исполнения мелким `labelMedium`.

### SettingsScreen

- Статистика, архив, экспорт/импорт, версия приложения.

---

## FortuneWheel (компонент)

| Параметр | Значение |
|----------|----------|
| Длительность | 2.5 s, easing out |
| Сегменты | 2–8 цветов чередуются primary/secondary |
| Указатель | Треугольник сверху по центру |
| Haptic | Light impact при остановке (фаза 3) |
| a11y | `contentDescription`: «Колесо выбора, крутится»; по окончании — название победителя |

Не использовать rapid flashing (более 3 Hz) — доступность.

---

## Правила (как в GymProgress)

- Отступы: `Spacing`, размеры: `Dimens` в `ui/theme/`.
- Цвета через `MaterialTheme` + `WishlotTheme.colors`, без inline hex в screens.
- Иконки: `CardGiftcard`, `Casino` (колесо), `CheckCircle`, `History`, `Settings`.
- Lint: `ContentDescription` на интерактивных иконках (ошибка сборки — как GymProgress).

---

## Связанные документы

- [FEATURE_PLAN.md](FEATURE_PLAN.md) — фаза 0 (тема)
- [BUSINESS_PROCESS.md](BUSINESS_PROCESS.md) — когда показывать экраны
- GymProgress `docs/DESIGN_SYSTEM.md` — **только** структура документа
