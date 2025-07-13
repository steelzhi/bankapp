FROM amazoncorretto:21
VOLUME /tmp
#RUN yum install -y bind-utils
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar"]