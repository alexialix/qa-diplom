# План автоматизации

## 1. Перечень автоматизируемых сценариев
### Позитивные сценарии
- Покупка тура через дебетовую карту.
- Покупка тура через кредитную карту.

### Негативные сценарии
- Ввод невалидных данных карты:
  - Невалидный номер карты.
  - Истёкший срок действия карты.
  - Неверный CVV.
- Ошибки транзакций:
  - Недостаточно средств.
  - Превышение кредитного лимита.
  - Отказ банка. 
- Оставление полей пустыми

## 2. Перечень используемых инструментов
- Язык программирования: Java.
  - Применим на основе опыта, полученного в рамках курса. Хорошо подходит для автоматизации тестирования веб-приложений.
- Selenide
  - Для UI-тестирования, так как это удобный инструмент с минимальными настройками.
- REST Assured
  - Для тестирования API эмулятора банковских сервисов.
- JUnit 5
  - Фреймворк для написания и структурирования тестов.
- Allure
  - Для генерации наглядных отчётов о тестировании.
- Docker
  - Для запуска MySQL, PostgreSQL и эмулятора банковских сервисов в контейнерах.
- Gradle
  - Для управления зависимостями и сборки проекта.

## 3. Возможные риски.
- Трудности с настройкой инструментов.
-  Ошибки в логике тестов.

## 4. Интервальная оценка времени
- Настройка окружения: 2–3 ч
- Разработка автотестов: 6-8 часов.
- Отладка: 2–3 ч
- Отчёты: 3–5 ч
- **Итого:** 13–19 ч

## 5. План сдачи работ
- Автотесты: через 6 дней.
- Результаты тестов: через 8 дней.
- Итоговый отчёт: через 10 дней.
