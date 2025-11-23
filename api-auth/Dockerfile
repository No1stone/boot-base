# ==== Build stage ====
FROM amazoncorretto:17-alpine AS build
WORKDIR /app

# 루트 전체를 컨텍스트로 복사 (루트 gradlew/설정/모듈 전부 포함)
COPY . .

# 모듈 지정 빌드: 모노레포 기준으로 gateway만 빌드
RUN chmod +x ./gradlew && ./gradlew :api-auth:clean :api-auth:bootJar -x test

# ==== Runtime stage ====
FROM amazoncorretto:17-alpine
WORKDIR /app

# 모듈 산출물 경로에서 JAR만 복사
COPY --from=build /app/api-auth/build/libs/*SNAPSHOT.jar app.jar

# (선택) JVM 옵션 주입을 위해 ENV 준비
ENV SPRING_PROFILES_ACTIVE=""
ENV SPRING_PROFILES_ACTIVE=""
ENV JAVA_OPTS="-Xms256m -Xmx512m"

#EXPOSE 8080
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar --spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]

#docker build -f api-auth/Dockerfile -t api-auth:local .