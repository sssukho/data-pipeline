# Start with a base image containing Java runtime
FROM java:8

# Add Author info
LABEL maintainer="sssukho@gmail.com"

# Add a volume to /tmp
VOLUME /tmp

# Make port 9099 available to the world outside this container
EXPOSE 9099

# The application's jar file
ARG JAR_FILE=target/disruptor-log.jar

# Add the application jar's to the container
ADD ${JAR_FILE} disruptor-log.jar

# Run the jar file
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/disruptor-log.jar"]
#ENTRYPOINT ["java", "-jar", ">", "logs/flas_$(date '%Y_%m_%d').log", "2>&1", "&"]
