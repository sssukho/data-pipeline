## Kafka Conatiner 설치 및 실행 방법 (docker-compose)

### Docker Image 설치

1. Kafka 설치

   ``` shell
   docker pull wurstmeister/kafka
   ```

2. Zookeeper 설치

   ``` shell
   docker pull wurstmeister/zookeeper
   ```



### docker-compose 파일 생성

- `docker-compose.yml` 파일 생성 => local PC에 경로는 자유롭게 파일 생성)

  - docker-compose.yml

    ``` yaml
    version: '2'
    services:
    	zookeeper:
    		image: wurstemeister/zookeeper
    		container_name: zookeeper
    		ports:
    			- "2181:2181"
    	kafka:
    		image: wurstmeister/kafka
    		container_name: kafka
    		ports:
    			- "9092:9092"
    		environment:
    			KAFKA_ADVERTISED_HOST_NAME: 127.0.0.1
    			KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
    		volumes:
    			- /var/run/docker.sock:/var/run/docker.sock
    ```



### docker-compose 파일 실행

1. 로컬 PC에 위에서 생성한 `docker-compose.yml` 파일의 위치로 이동

2. docker-compose 실행

   ``` shell
   docker-compose up -d
   ```

3. docker container 접속

   ``` shell
   docker container exec -it kafka bash
   docker container exec -it zookeeper bash
   ```

4. docker-compose 중지

   ``` shell
   # container 정지
   docker-compose stop
   # conatiner 와 network 정지 및 삭제
   docker-compose down
   ```





