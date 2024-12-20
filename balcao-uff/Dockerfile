FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY target/*.jar app.jar

ENV SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/balcao_anuncios_uff
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=1996
ENV API_SECURITY_TOKEN_SECRET=my-secret-key
ENV GOOGLE_CLIENT_ID=1080697336488-lqffnqk5lppnqkionbrnit6ubr0hpam6.apps.googleusercontent.com
ENV GOOGLE_CLIENT_SECRET=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

# Criação do diretório de uploads
RUN mkdir -p /app/src/main/resources/uploads/images/ && chmod -R 777 /app/src/main/resources/uploads/images/

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
