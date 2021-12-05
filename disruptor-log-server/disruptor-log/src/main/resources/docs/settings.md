# Docker-compose로 컨테이너 실행 방법 
### 1. java application(disruptor-log.jar) 커스텀 이미지 생성
1. 아래 Dockerfile 을 프로젝트 루트(disruptor-to-kafka/disruptor-log-server/disruptor-log/) 에 'Dockerfile' 이름으로 생성한다.
   ```dockerfile
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
2. 이미지 생성 명령어 입력: `docker build -t disruptor-log .`
3. 생성된 이미지 확인: `docker-images`
<br>
   
### 2. postgresql 이미지 생성
1. postgresql 이미지를 docker-hub로부터 pull 한다. => `docker pull postgres`
<br>
   
### 3. docker-compose 로 각 컨테이너 생성 및 기동 후 연동 확인
1. 프로젝트 루트(disruptor-to-kafka/disruptor-log-server/disruptor-log/) 에 'docker-compose.yml' 파일명으로 아래 스크립트를 생성한다.
   ```yaml
    version: '2'
    
    services:
      disruptor-log:
        image: 'disruptor-log'
        build:
          context: .
        container_name: disruptor-log
        depends_on:
          - postgres-for-log
        environment:
          - SPRING_DATASOURCE_URL=jdbc:postgresql://postgres-for-log:5432/log?stringtype=unspecified
          - SPRING_DATASOURCE_USERNAME={Postgresql 계정명}
          - SPRING_DATASOURCE_PASSWORD={Postgresql 위 계정에 대한 비밀번호}
    
      postgres-for-log:
        image: 'postgres'
        container_name: postgres-for-log
        environment:
          - POSTGRES_USER={Postgresql 계정명}
          - POSTGRES_PASSWORD={Postgresql 위 계정에 대한 비밀번호}
    ```
2. `docker-compose up` 으로 기동
3. 'postgres-for-log' 컨테이너와 'disruptor-log' 컨테이너가 정상적으로 기동되는지 확인(disruptor-log 로그에서 log 라는 DB가 없다고 뜨는게 정상) 
<br>
   
### 4. postgresql 컨테이너에서 계정 및 테이블 생성
1. `docker exec -it postgres-for-log /bin/bash` 명령어로 postgres 컨테이너 접속
2. `psql -U {Postgresql 계정명}`
3. 계정 정보 설정 및 기본 Database 생성
   ```text
   CREATE DATABSE log OWNER {Postgresql 계정명};
   \c log {Postgresql 계정명};
   ```
4. 기본 테이블 생성 => CREATE_TABLE.sql 쿼리 실행 
<br>
   
### 5. docker-compose 재기동
1. `docker-compose down`
2. `docker-compose up`

<br><br>

---
**Reference**
- docker 네트워크 생성: https://galid1.tistory.com/723 => 시도해봤으나 안됨 ㅠ
- spring-boot + postgersql docker-compose 구성법: https://www.baeldung.com/spring-boot-postgresql-docker
