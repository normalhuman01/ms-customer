FROM adoptopenjdk/openjdk11:alpine-jre
ARG JAR_FILE=target/ms-customer-*-SNAPSHOT.jar
COPY ${JAR_FILE} ms-customer.jar
RUN addgroup -S bootcampgroup && adduser -S bootcampuser -G bootcampgroup
RUN mkdir -p /opt/logs/ms-customers
RUN chown -R bootcampuser:bootcampgroup /opt/logs/ms-customers
USER bootcampuser:bootcampgroup
ENTRYPOINT ["java", "-jar", "/ms-customer.jar"]