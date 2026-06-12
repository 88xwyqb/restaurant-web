# ── 构建阶段 ──
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests -B

# ── 运行阶段 ──
FROM tomcat:9.0-jdk17-temurin
RUN mkdir -p /usr/local/tomcat/data
COPY --from=build /app/target/restaurant-web.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]
