# bankapp

Программа уже содержит 3 добавленных пользователей. Данные их аккаунтов: у 1-го - логин "1" / пароль "1", у 2-го - "2"/"2",
у 3-го - "3"/"3" (все вводится без кавычек). У этих пользователей уже есть счета в разных валютах, и т.о. они могут совершать переводы друг другу.

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
                    1.1.2.2.1. Готовые клиенты находится в {имя клиента из п. 1.1.2.1}/src/main/resources/{имя клиента из п. 1.1.2.1}.json. 
                    Проще всего просто импортировать этих клиента (в файле application.yml сервисов уже прописан соответствующий 
                    "client-secret"): Clients -> Import client -> файл соответствующего клиента в Resource file -> Save.
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
                    1.1.2.2.2. В поле "Value" добавить (убрав пробелы перед началом каждой строки):
                    module-accounts: http://accounts/
                    module-cash: http://cash/
                    module-exchange-generator: http://exchange-generator/
                    module-transfer: http://transfer/
                    module-notifications: http://notifications/
                    module-exchange: http://exchange/
                    module-blocker: http://blocker/
                    issuer-uri: http://localhost:8080/realms/master
                    keycloak-location: http://localhost:8080/realms/master
                    Прим.: при каждом перезапуске Consul зачищает хранилище "Key/Value", поэтому его нужно заполнять заново.
            1.1.3. Запустить сервисы:
                1.1.3.1. AccountsApplication#main; 
                1.1.3.2. BlockerApplication#main;
                1.1.3.3. CashApplication#main;
                1.1.3.4. ExchangeApplication#main;
                1.1.3.5. ExchangeGeneratorApplication#main;
                1.1.3.6. FrontUIApplication#main (это gateway);
                1.1.3.7. NotificationsApplication#main;
                1.1.3.8. TransferApplication#main.
        1.2. В Docker-контейнере:
            1.2.1. Собрать проект командой "gradle clean build". Если при этом сборке будут мешать тестовые классы, их нужно будет закомментировать.
            1.2.2. Запустить Docker. В терминале IDE выполнить команду "docker compose up".
            Запустятся написанные сервисы приложения, а также Keycloak и Consul, от настроек в которых эти сервисы
            зависят. Т.к. изначально в Keycloak и Consul настройки не заданы, то все написанные сервисы упадут. Поэтому
            нужно после запуска:
                1.2.3.1. В Keycloak: перейти в UI по адресу localhost:8080 и импортировать сервисы accounts, cash, exchange, front-ui, transfer. 
                Файлы для импортов находятся в {имя клиента}/src/main/resources/{имя клиента}.json.
                1.2.3.2. В Consul: перейти в UI по адресу localhost:8500 и создать KV-хранилище. Далее повторить действия из:
                    1.2.3.2.1. П. 1.1.2.2.1;
                    1.2.3.2.2. П. 1.1.2.2.2, заменив во последнюю строку на "keycloak-location: http://keycloak:8080/realms/master" "
                    (вместо "keycloak-location: http://localhost:8080/realms/master").
                При этом нужно учитывать примечание из п. 1.1.2.2.
                1.2.3.3. Заново запустить все сервисы в следующем порядке:
                    1.2.3.3.1. blocker, exchange-generator, notifications (эти сервисы не зависят от других);
                    1.2.3.3.2. accounts, cash, exchange (эти сервисы зависят от сервисов из п. 1.2.3.3.1);
                    1.2.3.3.3. front-ui, transfer (эти сервисы зависят от сервисов из п.п. 1.2.3.3.1, 1.2.3.3.2).

2. Перейти в браузере по адресу http://localhost:8081/ (это gateway).

Приложение готово к работе. Можно добавлять пользователей, заходить под разными пользователями, работать со счетами (пополнять и
снимать со своих, делать переводы другим клиентам).
