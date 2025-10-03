FROM registry.cn-hangzhou.aliyuncs.com/xx_blog/openjdk:17-jdk
ADD target/Trial-Assignment-Backend-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=test"]
EXPOSE 8080