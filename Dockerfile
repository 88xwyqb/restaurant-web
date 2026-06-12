FROM maven:3.9-eclipse-temurin-17

WORKDIR /app
COPY . .

EXPOSE 8080

# 使用嵌入式 Tomcat 启动
CMD ["mvn", "tomcat7:run"]
