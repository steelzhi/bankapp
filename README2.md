Примечание: в корневом values.yaml сервисам назначены хосты ({имя сервиса}.ingress.hosts.host). Имена этих хостов использованы
в application.yaml указанных в п. 1 сервисов (для возможности межсервисного взаимодействия).

Работа с программой:
1. Упаковка в K8s

1.1. Собрать образы микросервисов:
docker build -t accounts-service:latest ./accounts
docker build -t blocker-service:latest ./blocker 
docker build -t cash-service:latest ./cash 
docker build -t exchange-service:latest ./exchange
docker build -t exchange-generator-service:latest ./exchange-generator
docker build -t front-ui-service:latest ./front-ui
docker build -t notifications-service:latest ./notifications
docker build -t transfer-service:latest ./transfer

1.2. Запустить Minikube - для Windows команда будет такая:
minikube start --vm-driver=hyperv
(при этом, возможно, сперва придется остановить работающий Minikube командой minikube stop)

1.3. Загрузить локальные образы в Minikube (для Windows - через PowerShell с правами администратора):
minikube image load accounts-service:latest
minikube image load blocker-service:latest
minikube image load cash-service:latest
minikube image load exchange-service:latest
minikube image load exchange-generator-service:latest
minikube image load front-ui-service:latest
minikube image load notifications-service:latest
minikube image load transfer-service:latest

1.4. Проверить Ingress: 
kubectl get svc -n ingress-nginx 

Если возвращается пустое значение, нужно установить Ingress: 
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.10.1/deploy/static/provider/cloud/deploy.yaml

1.5. Включить Ingress в Minikube:
Перед включением нужно сделать даунгрейд версии Minikube до 1.23.2 и удалить мешающие аддону патчи командами:
kubectl delete job ingress-nginx-admission-create -n ingress-nginx
kubectl delete job ingress-nginx-admission-patch -n ingress-nginx

Включить аддон:
minikube addons enable ingress

1.6. Перейти в папку "my-microservices-app" и обновить зависимости для helm-чартов (для обновления чарта Keycloak с bitnami нужно включать VPN):
helm dependency update .

1.7. Установить helm-релиз:
helm install myapp ./

1.8. Проверить поды - убедиться, что все они запустились без ошибок (находятся в состоянии Running)
kubectl get pods

kubectl get svc
kubectl get ingress

Также можно посмотреть логи внутри пода:
kubectl logs -f имя_пода

1.9. Узнать IP кластера
kubectl get nodes -o wide

1.10. Для Windows - папка C:\Windows\System32\drivers\etc,
предварительно нужно будет включить отображение скрытых файлов,
далее находим файл _hosts_, щелкаем правой кнопкой мыши и выбираем "Запуск от имени администратора"
В файл _hosts_ надо добавить ip, который получили из поля _INTERNAL-IP_ на шаге 12
далее пробел и имя хоста, которое можно найти в файле _values.yaml_ ->
ищем сервисы customer-service и order-service и в них ищем _hosts_ и далее поле _host_

(В Linux: надо прописать в /etc/hosts
sudo nano /etc/hosts )

Далее сохраняем файл _hosts_

1.11. Проверка доменов
http://accounts.myapp.local/actuator/health
http://blocker.myapp.local/actuator/health
http://cash.myapp.local/actuator/health
http://exchange.myapp.local/actuator/health
http://exchange-generator.myapp.local/actuator/health
http://front-ui.myapp.local/actuator/health
http://notifications.myapp.local/actuator/health
http://transfer.myapp.local/actuator/health


2. Настройка CI/CD через Jenkins

2.1. Установить Ingress Controller в кластер
(Ingress Controller — это компонент, который позволяет обращаться к сервисам Kubernetes через удобные HTTP-домены (например, `http://accounts.test.local`))

Будет использован `ingress-nginx`. Его установка в кластер:


helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
helm repo update

helm upgrade --install ingress-nginx ingress-nginx/ingress-nginx   --namespace ingress-nginx --create-namespace


2.2. Создать файл `.env` в корне проекта:

# Путь до локального kubeconfig-файла
KUBECONFIG_PATH=/Users/username/.kube/jenkins_kubeconfig.yaml

# Параметры для GHCR (вставить)
GITHUB_USERNAME=your-username
GITHUB_TOKEN=ghp_...
GHCR_TOKEN=ghp_...

# Docker registry (в данном случае GHCR; вставить)
DOCKER_REGISTRY=ghcr.io/your-username
GITHUB_REPOSITORY=your-username/YandexHelmApp

# Пароль к базе данных PostgreSQL (добавить)
DB_PASSWORD=your-db-password

Убедиться, что мой GitHub Token имеет права `write:packages`, `read:packages` и `repo`.

---

2.3. Запустить Jenkins

cd jenkins
docker compose up -d --build

- Откройте Jenkins: [http://localhost:8080](http://localhost:8080)
- Перейти в задачу `YandexHelmApp` → `Build Now`
- Jenkins выполнит:
   - сборку и тесты
   - сборку Docker-образов
   - публикацию образов в GHCR
   - деплой в Kubernetes в два namespace: `test` и `prod`

2.4. Проверка успешного деплоя.
2.4.1. Добавить записи в `/etc/hosts` (ниже - команда для Linux)

sudo nano /etc/hosts

2.4.2. Добавить:
127.0.0.1 accounts.test.local
127.0.0.1 blocker.test.local
127.0.0.1 cash.test.local
127.0.0.1 exchange.test.local
127.0.0.1 exchange-generator.test.local
127.0.0.1 front-ui.test.local
127.0.0.1 notifications.test.local
127.0.0.1 transfer.test.local
127.0.0.1 accounts.prod.local
127.0.0.1 blocker.prod.local
127.0.0.1 cash.prod.local
127.0.0.1 exchange.prod.local
127.0.0.1 exchange-generator.prod.local
127.0.0.1 front-ui.prod.local
127.0.0.1 notifications.prod.local
127.0.0.1 transfer.prod.local

2.4.3. Отправить запросы на `/actuator/health`

http://accounts.myapp.local/actuator/health
http://blocker.myapp.local/actuator/health
http://cash.myapp.local/actuator/health
http://exchange.myapp.local/actuator/health
http://exchange-generator.myapp.local/actuator/health
http://front-ui.myapp.local/actuator/health
http://notifications.myapp.local/actuator/health
http://transfer.myapp.local/actuator/health

**Ожидаемый ответ:**

```json
{"status":"UP","groups":["liveness","readiness"]}
```

2.5. Завершение работы и очистка
2.5.1. Для полной остановки Jenkins, удаления namespace'ы `test` и `prod`, а также всех установленных ресурсов, нужно использовать скрипт `nuke-all.sh` (для Linux).
Он находится в папке `jenkins`:

```bash
cd jenkins
./nuke-all.sh
```

2.5.2. Убедиться, что в .env указаны мои значения из github
Для полей GHCR_TOKEN и GITHUB_TOKEN нужно сгенерить токен в github: зайти в свой github,
далее Settings -> Developer Settings -> Personal access tokens -> Tokens (classic)
Далее либо сгенерировать новый токен, либо использовать существующий.

3) Проверить релизы
   kubectl get pods -n test
   kubectl get pods -n prod

helm list -n test
helm list -n prod