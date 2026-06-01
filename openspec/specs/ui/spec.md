# UI Specification

## Purpose
Общие правила UI/UX проекта Wishlot.

## Requirements

### Requirement: Язык интерфейса
Весь пользовательский интерфейс SHALL быть на русском языке.

#### Scenario: Открытие любого экрана
- GIVEN пользователь открывает приложение
- THEN все метки, кнопки и подсказки на русском

### Requirement: Тема оформления
Приложение SHALL использовать дизайн-токены из `WishlotTheme` (палитра GIFT SPIN).

#### Scenario: Рендеринг компонентов
- GIVEN экран отрисовывается
- THEN цвета, типографика и формы соответствуют `WishlotTheme`
- AND не используются токены из других тем (IRON CORE, CLEAR BELL)

### Requirement: Отображение цен
Цены SHALL отображаться в рублях с учётом того, что в БД хранятся в minor units.

#### Scenario: Отображение бюджета
- GIVEN в БД budget = 500000 (minor units)
- WHEN отображается на экране
- THEN пользователь видит "5 000 ₽"
