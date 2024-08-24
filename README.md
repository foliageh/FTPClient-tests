# FTP Client tests
Тесты для приложения [FTP Client](../../../FTPClient).

## Сборка и запуск
Чтобы запустить тесты, достаточно установить [FTP Client](../../../FTPClient) 
в локальный Maven репозиторий через `mvn install`, и затем в приложении с тестами выполнить `mvn test`.

Альтернативный вариант – собрать jar через `mvn clean package` и запустить его как `java -jar target/FTPClient-tests`.  
_Размер jar подозрительно большой, но это всё из-за maven-assembly, до лучшего решения я пока не догадался._

Чтобы тесты работали, необходимо также указать данные подключения к тестовому FTP-серверу в файле [testng.xml](src/test/resources/testng.xml).

## Описание тестов
### FTPClientTests
- `Connect_to_existing_server_successful` - проверяет успешное подключение к существующему серверу
- `Disconnect_after_connection_successful` - проверяет успешное отключение после подключения
- `Login_with_correct_credentials_successful` - проверяет успешную аутентификацию с правильными учетными данными
- `Login_with_incorrect_credentials_fails_with_exception` - проверяет, что попытка аутентификации с неправильными учетными данными вызовет исключение
- `Upload_textual_data_successful` - проверяет загрузку текстовых данных на сервер
- `Download_textual_data_successful` - проверяет скачивание текстовых данных с сервера
- `Download_nonexistent_textual_data_fails_with_exception` - проверяет, что попытка скачивания несуществующих текстовых данных вызовет исключение

Продолжение следует...