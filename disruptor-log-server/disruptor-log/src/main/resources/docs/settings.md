# Postgresql 컨테이너와 disruptor-log 컨테이너 연동법
### 1. postgresql 컨테이너 생성
```shell
docker run -p 5432:5432 --name postgres -e POSTGRES_PASSWORD=tjzhtjzh01 -d postgres

docker run -p 5432:5432 --name postgres-for-log -e POSTGRES_PASSWORD=tjzhtjzh01 -d --network log-network postgres
docker exec -it postgres-for-log /bin/bash
psql -U postgres
CREATE USER sssukho PASSWORD 'sssukho' SUPERUSER;
CREATE DATABASE log OWNER sssukho;
\c log sssukho
```

- postgresql 접속한 상태에서 USER와 기본 DB를 만든 후, 테이블 생성 쿼리는 CREATE_TABLE.sql 참고
<br>
  
### 2. disruptor-log 이미지 생성
- 아래 Dockerfile 을 프로젝트 루트에 생성하여 위치시킨다.
  ```
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
  ``` 
- 이미지 생성 명령어 입력
  ```shell
  docker build -t disruptor-log .
  ```
- 컨테이너 생성
  ```shell
  docker run --name disruptor-log -d -p 9099:9099 --network log-network disruptor-log
  ```

### 3. DB(postgersql) 컨테이너와 disruptor-log 컨테이너 연동
