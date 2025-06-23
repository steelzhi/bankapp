Работа с программой:
1. Собрать минимальный набор образов:
   docker build -t accounts-service:latest ./accounts-service
   docker build -t exchange-generator-service:latest ./exchange-generator-service
   docker build -t front-ui-service:latest ./front-ui-service
   docker build -t notifications-service:latest ./notifications-service

2. Запустить Minikube - для Windows команда будет такая:
   minikube start --vm-driver=hyperv

3. Загрузить локальные образы в Minikube:
   minikube image load accounts-service:latest
   minikube image load exchange-generator-service:latest
   minikube image load front-ui-service:latest
   minikube image load notifications-service:latest

4. Обновить зависимости для helm-чартов:
   helm dependency update .

5. Установить helm-релиз:
   helm install myapp ./

6. Проверить поды - убедиться, что все они запустились без ошибок (находятся в состоянии Running)
   kubectl get pods


Примечания:
1. В корневом values.yaml сервисам назначены хосты ({имя сервиса}.ingress.hosts.host). Имена этих хостов использованы
в application.yaml указанных в п. 1 сервисов (для возможности межсервисного взаимодействия).