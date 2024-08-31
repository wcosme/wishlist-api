# Use a imagem do OpenJDK 17
FROM openjdk:17-jdk-slim

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o arquivo JAR gerado pelo Maven para dentro do container
COPY target/wishlist-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta 8080 para o tráfego HTTP
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]