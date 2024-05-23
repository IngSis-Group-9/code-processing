FROM gradle:7.6.1-jdk17 as builder
COPY . /home/gradle/src
WORKDIR /home/gradle/src
# Da permisos de ejecución al script gradlew
RUN chmod +x ./gradlew
RUN sed -i -e 's/\r$//' gradlew  # remove windows line endings
RUN ./gradlew bootJar

#RUN gradle build
#EXPOSE 8081

# Cambia a la imagen de OpenJDK para la ejecución
FROM openjdk:17-jdk-alpine
# Establece el directorio de trabajo
WORKDIR /app
# Copia el archivo JAR desde la etapa de construcción anterior
COPY --from=builder /home/gradle/src/build/libs/code-processing-0.0.1-SNAPSHOT.jar code-processing.jar
# Establece el comando de inicio de la aplicación
ENTRYPOINT ["java", "-jar", "code-processing.jar"]
