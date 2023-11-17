Бот для моего сервера в Discord. Собирает все сообщения в чате и сохраняет в файл, после чего с шансом 10% (или другим) цитирует одно сообщение из сохранённых. Сообщения хранятся в зашифрованном виде (самое примитивное шифрование номерами юникода, чтобы файл был нечитаем посторонними лицами). Имеет дополнительный функционал, по типу поздравления с днём рождения, выбора случайного слова из списка, ответа на вопрос (шар судьбы) и так далее.

## Общая информация

Этот репозиторий - проект Gradle, который должен быть открыт через IntelliJ IDEA.

| Технология | Версия  | Пояснение                                    | Примечание                                              |
|------------|---------|----------------------------------------------|---------------------------------------------------------|
| Gradle     | 8.4-bin | Версия системы автоматической сборки         | -                                                       |
| Gradle JVM | 17.0.9  | Версия Java, используемая для запуска Gradle | Настраивается в переменных средах ОС (JAVA_HOME и Path) |
| Kotlin     | 1.9.20  | Версия Kotlin, используемая в проекте        | -                                                       |
| JDK        | 17.0.9  | Версия SDK, используемая в проекте           | Настраивается в IntelliJ IDEA (Project Structure)       |

## Использование

Человек, который хостит бота, должен запустить скомпилированный jar-файл при помощи консоли Windows, либо вызовом из другой программы: `java -jar UN-ION-Discord-Bot.jar`. Рядом при этом должен находиться файл token.txt с токеном бота.

Сообщество, желающее использовать запущенного на хосте бота, должно пригласить бота на сервер по сгенерированной ссылке.
