[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=Hummel009_UN-ION-Discord-Bot&metric=code_smells)](https://sonarcloud.io/summary/overall?id=Hummel009_UN-ION-Discord-Bot)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=Hummel009_UN-ION-Discord-Bot&metric=sqale_rating)](https://sonarcloud.io/summary/overall?id=Hummel009_UN-ION-Discord-Bot)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=Hummel009_UN-ION-Discord-Bot&metric=security_rating)](https://sonarcloud.io/summary/overall?id=Hummel009_UN-ION-Discord-Bot)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=Hummel009_UN-ION-Discord-Bot&metric=bugs)](https://sonarcloud.io/summary/overall?id=Hummel009_UN-ION-Discord-Bot)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=Hummel009_UN-ION-Discord-Bot&metric=vulnerabilities)](https://sonarcloud.io/summary/overall?id=Hummel009_UN-ION-Discord-Bot)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=Hummel009_UN-ION-Discord-Bot&metric=duplicated_lines_density)](https://sonarcloud.io/summary/overall?id=Hummel009_UN-ION-Discord-Bot)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=Hummel009_UN-ION-Discord-Bot&metric=reliability_rating)](https://sonarcloud.io/summary/overall?id=Hummel009_UN-ION-Discord-Bot)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Hummel009_UN-ION-Discord-Bot&metric=alert_status)](https://sonarcloud.io/summary/overall?id=Hummel009_UN-ION-Discord-Bot)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=Hummel009_UN-ION-Discord-Bot&metric=sqale_index)](https://sonarcloud.io/summary/overall?id=Hummel009_UN-ION-Discord-Bot)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=Hummel009_UN-ION-Discord-Bot&metric=ncloc)](https://sonarcloud.io/summary/overall?id=Hummel009_UN-ION-Discord-Bot)

Мультиплатформенный бот для моего сервера в Discord. Может быть запущен с Windows либо Android. Поскольку телефон у меня включен и находится в зоне досягаемости интернета практически всегда, то это, фактически, бесплатный хост.

## Общая информация

Этот репозиторий - проект Gradle, который должен быть открыт через IntelliJ IDEA.

| Технология                             | Версия |
|----------------------------------------|--------|
| Система автоматической сборки Gradle   | 8.5    |
| Java, используемая для запуска Gradle  | 8+     |
| Java, используемая для сборки проекта  | 8+     |
| Java, используемая для запуска проекта | 8+     |
| Kotlin                                 | 1.9.22 |

## Установка

Установка и системные требования всех моих проектов Gradle примерно одинаковы, так что смотрите [общую инструкцию](https://github.com/Hummel009/The-Rings-of-Power#readme). Проект из этого репозитория устанавливается так же, как проект Minecraft 1.18.2 из общей инструкции.

## Использование

* Скомпилировать приложение в файл с расширением .jar;
* Создать бота на портале Discord Developers и скопировать его токен;
* Положить в папке рядом с ним файл `token.txt` (строка с токеном бота);
* Открыть консоль Windows в папке с вышеупомянутым файлом с расширением .jar и выполнить команду `java -jar JarFileName.jar`.
* Пригласить бота на сервер по сгенерированной ссылке.

## Функционал

* Сохранение сообщений из чата и рандомное их цитирование с шансом.
* Рандомные реакции на сообщения с шансом.
* Изменение шанса реакции и цитирования.
* Шар судьбы (ответ на вопрос - да/нет)
* Нейросеть-продление текста.
* Выбор значения из списка.
* Рандомайзер.
* Удалённое выключение бота и хоста.
* Импорт и экспорт данных серверов.
* Чистка чата - удаление N сообщений.
* Смена языка бота - английский и русский.
* Добавление и удаление дней рождений участников.
* Добавление и удаление менеджеров бота.
* Добавление и удаление скрытых каналов, откуда нельзя сохранять сообщения.
* Список команд и их идентификаторы - для разработчика.
