# bankapp

Приложение реализовано согласно ТЗ. Его особенности:
- Программа уже содержит 3 добавленных пользователей. Данные их аккаунтов: у 1-го - логин "1" / пароль "1", у 2-го - "2"/"2", у 3-го - "3"/"3" (все вводится без кавычек). 
У этих пользователей уже есть счета в разных валютах, и т.о. они могут совершать переводы друг другу.
- В модуле blocker случайным образом с заданными вероятностями 0.20 и 0.25 блокируются операции перевода со счета на счет и снятия сумм, соответственно.
- При недоступности сервиса уведомления другие модули производят ретраи. В случае неудачи осуществляется фоллбэк с переходом на страницу с информацией о недоступности одного из сервисов и предложением вернуться в аккаунт.
- Все операции (с данными пользователя, денежные) сопровождаются переходом на страницу с информацией об итоге операции.
- Посредством html и JS установлены ограничения на формат и величину для вводимых сумм, проверка совпадения вводимых данных (при подтверждении пароля), проверка возраста пользователя и др.
- Курсы валют являются случайными величинами (рассчитываются как заданная базовая величина с применением к ней рандомного толеранса) и обновляются каждый раз при обновлении страницы.

Инструкция по работе с приложением.
1. Запуск программы.
   Запуск можно осуществить как локально (в IDE), так и в Docker-контейнере:

        1.1. Локальный:
            1.1.1. Запустить Docker. 
            1.1.2. В терминале IDE:
                1.1.2.1. Запустить Keycloak командой:
                docker run -d -p 8080:8080 --name keycloak -e KC_BOOTSTRAP_ADMIN_USERNAME=admin -e KC_BOOTSTRAP_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:26.1.3 start-dev
                После того, как контейнер Keycloak запущен, перейти по адресу localhost:8080 -> ввести логин и пароль 
                (и то, и другое - "admin") -> Clients -> Create client. Создать клиента можно 2 способами: импортировав 
                готового клиента или создав его вручную. Нужно создать 5 клиентов: accounts, cash, exchange, front-ui, transfer
                    1.1.2.2.1. Готовые клиенты находится в {имя клиента из п. 1.1.2.1}/src/main/resources/{имя клиента из п. 1.1.2.1}.json. Проще всего просто 
                    импортировать этих клиента (в файле application.yml сервисов уже прописан соответствующий 
                    "client-secret").
                    1.1.2.2.2. В качестве альтернативы п. 1.1.2.2.1 - создание вручную. Нужно будет задать параметры 
                    создаваемого клиента:
                      - Client ID: {имя клиента из п. 1.1.2.1}
                      - Client authentication: On
                      - Authentication flow: отметить Standard flow, Direct access grants, Service account roles
                      - Valid Redirect URIs: http://localhost:{порты}/*
                          Порты: accounts - 8070, cash - 8092, exchange - 8095, front-ui - 8081, transfer - 8096.
                    Зайти в раздел "Credentials" созданного клиента и скопировать оттуда ключ из "Client secret". Вставить 
                    этот ключ в файл application.yml соответствующего приложения (в параметр "client-secret").
                1.1.2.2. Запустить Consul командой:
                docker run -d -p 8500:8500 hashicorp/consul
                Перейти в UI Consul по адресу localhost:8500 и создать KV-хранилище: 
                    1.1.2.2.1. Во вкладке "Key/Value" создать папку "config/application/data".
                    1.1.2.2.2. В поле "Value" добавить:
                    module-accounts: http://accounts/
                    module-cash: http://cash/
                    module-exchange-generator: http://exchange-generator/
                    module-transfer: http://transfer/
                    module-notifications: http://notifications/
                    module-exchange: http://exchange/
                    module-blocker: http://blocker/
                    Прим.: при каждом перезапуске Consul зачищает хранилище "Key/Value", поэтому его нужно заполнять заново.
            1.1.3. Запустить сервисы:
                1.1.3.1. FrontUIApplication#main (это gateway);
                1.1.3.2. AccountsApplication#main; 
                1.1.3.3. ExchangeGeneratorApplication#main;
                1.1.3.4. NotificationsApplication#main;
                1.1.3.5. BlockerApplication#main;
                1.1.3.6. CashApplication#main;
                1.1.3.7. ExchangeApplication#main;
                1.1.3.8. TransferApplication#main.
        1.2. В Docker-контейнере:
            1.2.1. Для локального запуска и для запуска в Docker-контейнере применяются разные настройки, поэтому 
            необходимо провести перенастройку во всех модулях:
                1.2.1.1. Модуль front-ui:
                    1.2.1.1. Файл application.yml, параметр spring.security.oauth2.client.provider.keycloak.issuer-uri: закомментировать значение 
                        "http://localhost:8080/realms/master", раскомментировать значение 
                        "http://keycloak:8080/realms/master".
                    1.2.1.2. Модуль accounts:
                        1.2.1.2.1. Файл application.yml, параметр spring.security.oauth2.
                            1.2.1.2.1.1. resourceserver.jwt.issuer-uri: закомментировать значение 
                            "http://localhost:8080/realms/master", раскомментировать значение "http://keycloak:8080/realms/master".
                            1.2.1.2.1.2. client.provider.keycloak.issuer-uri: закомментировать значение 
                            "http://localhost:8080/realms/master", раскомментировать значение "http://keycloak:8080/realms/master". 
                        1.2.1.2.2. Класс SecurityConfiguration: закомментировать 
                        "return JwtDecoders.fromIssuerLocation("http://localhost:8080/realms/master");", 
                        раскомментировать "return JwtDecoders.fromIssuerLocation("http://keycloak:8080/realms/master");".
                    1.2.1.3. Модуль exchange-generator, файл application.yml, параметр spring.security.oauth2.resourceserver.jwt.issuer-uri: 
                    закомментировать значение "http://localhost:8080/realms/master", раскомментировать значение "http://keycloak:8080/realms/master".
                    1.2.1.4. Модуль notifications, файл application.yml, параметр spring.security.oauth2.resourceserver.jwt.issuer-uri: 
                    закомментировать значение "http://localhost:8080/realms/master", раскомментировать значение "http://keycloak:8080/realms/master".
                    1.2.1.5. Модуль blocker, файл application.yml, параметр spring.security.oauth2.resourceserver.jwt.issuer-uri: 
                    закомментировать значение "http://localhost:8080/realms/master", раскомментировать значение "http://keycloak:8080/realms/master".
                    1.2.1.6. Модуль cash:
                        1.2.1.6.1. Файл application.yml, параметр spring.security.oauth2.
                            1.2.1.6.1.1. resourceserver.jwt.issuer-uri: закомментировать значение 
                            "http://localhost:8080/realms/master", раскомментировать значение "http://keycloak:8080/realms/master".
                            1.2.1.6.1.2. client.provider.keycloak.issuer-uri: закомментировать значение 
                            "http://localhost:8080/realms/master", раскомментировать значение "http://keycloak:8080/realms/master". 
                        1.2.1.6.2. Класс SecurityConfiguration: закомментировать 
                        "return JwtDecoders.fromIssuerLocation("http://localhost:8080/realms/master");", 
                        раскомментировать "return JwtDecoders.fromIssuerLocation("http://keycloak:8080/realms/master");".
                    1.2.1.7. Модуль exchange:
                        1.2.1.7.1. Файл application.yml, параметр spring.security.oauth2.
                            1.2.1.7.1.1. resourceserver.jwt.issuer-uri: закомментировать значение 
                            "http://localhost:8080/realms/master", раскомментировать значение "http://keycloak:8080/realms/master".
                            1.2.1.7.1.2. client.provider.keycloak.issuer-uri: закомментировать значение 
                            "http://localhost:8080/realms/master", раскомментировать значение "http://keycloak:8080/realms/master". 
                        1.2.1.7.2. Класс SecurityConfiguration: закомментировать 
                        "return JwtDecoders.fromIssuerLocation("http://localhost:8080/realms/master");", 
                        раскомментировать "return JwtDecoders.fromIssuerLocation("http://keycloak:8080/realms/master");".
                    1.2.1.8. Модуль transfer:
                        1.2.1.8.1. Файл application.yml, параметр spring.security.oauth2.
                            1.2.1.8.1.1. resourceserver.jwt.issuer-uri: закомментировать значение 
                            "http://localhost:8080/realms/master", раскомментировать значение "http://keycloak:8080/realms/master".
                            1.2.1.8.1.2. client.provider.keycloak.issuer-uri: закомментировать значение 
                            "http://localhost:8080/realms/master", раскомментировать значение "http://keycloak:8080/realms/master". 
                        1.2.1.8.2. Класс SecurityConfiguration: закомментировать 
                        "return JwtDecoders.fromIssuerLocation("http://localhost:8080/realms/master");", 
                        раскомментировать "return JwtDecoders.fromIssuerLocation("http://keycloak:8080/realms/master");".
                    1.2.1.9. Тестовые классы всех сервисов: тесты выполняются на настройках локального запуска программы, поэтому 
                    часть тестов нужно закомментировать. Проще всего просто закомментировать все тестовые классы 
                    (работоспособность всех тестов проверяется при локальном запуске).

            1.2.2. Собрать проект командой "gradle clean build".
            1.2.3. Важный нюанс: client-secret прописывается в application.yml и docker-compose.yml до сборки образов. 
            Но после сборки образа, запуска контейнера keycloak и ручного создания в нем Client ID сервисов 
            client-secret будет сгенерирован в keycloak автоматически и не совпадет с client-secret, указанным файлах 
            .yml. А функционала ручного изменения client-secret в keycloak не предусмотрено! Поэтому, чтобы решить эту 
            проблему, нужно:
                1.2.3.1. Либо импортировать уже готовый Client ID (повторив действия из п. 1.1.2.2.1);
                1.2.3.2. Либо создать отдельный контейнер только для Keycloak и добавив в него Client ID (повторив
                действия из п. 1.1.2.2.2). После этого эскпортировать созданный Client ID (на компьютер). Открыть 
                экспортированный файл и скопировать оттуда client-secret файл application.yml указанных в п. 1.1.2.1 приложений 
                (в параметр "client-secret") и в файл docker-compose.yml в параметр окружения 
                "SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_MAIN_APP_CLIENT_SECRET".
            1.2.4. Запустить Docker. В терминале IDE выполнить команду "docker compose up".
            В docker-compose.yml прописана зависимость сервисов от keycloak, но при сборке 
            образов и автоматическом 1-м запуске keycloak все равно может запуститься уже после запуска других 
            приложений. В этом случае сервисы упадут с ошибкой. Если такое произойдет, нужно 
            в Docker просто перезапустить контейнер "bankapp" - теперь все должно запуститься в правильном порядке.

2. Перейти в браузере по адресу http://localhost:8081/ (это gateway).

Приложение готово к работе. Можно добавлять пользователей, заходить под разными пользователями, работатать со счетами (пополнять и
снимать со своих, делать переводы другим клиентам).


Consul Config: создать папку config/application/data, добавить в нее
module-accounts: http://accounts/
module-cash: http://cash/
module-exchange-generator: http://exchange-generator/
module-transfer: http://transfer/
module-notifications: http://notifications/
module-exchange: http://exchange/
module-blocker: http://blocker/