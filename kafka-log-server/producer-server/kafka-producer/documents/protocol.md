## 로그인 데이터 JSON 프로토콜

- Producer 측으로 들어오는 데이터
- HTTP Method: POST
- URI: /v1/trace/audit

``` json
{
    "version": "1.0",
    "type": "audit",
    "count": 1,
    "data": [
        {
            "log_date": 1640390400000,
            "request_id": "38492f11-1565-40db-8e50-8a9e0f6c9d00",
            "user_id": "sssukho",
            "user_name": "임석호",
            "user_type": "Member",
            "event_type": "Login",
            "application_id": "03e78a26-69d4-4011-928d-7fca55779b07",
            "application_name": "iCloud",
            "ip": "xxx.xxx.xx.xx",
            "location": "Mapo-Gu, Seoul",
            "user_agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Safari/537.36 Edg/96.0.1054.62",
            "event_result": "Success"
        }
    ]
}
```

- version: 프로토콜 버전
- type: 로그 유형 (producer 측에서 분기)
- count: 로그 count (`data` 사이즈)
- data: 로그 데이터 (JSON List)
  - log_date: 로그 발생 시간
    - Long
  - request_id: 요청 ID
  - user_id: 사용자 ID
  - user_name: 사용자 이름
  - event_type: 이벤트 유형
  - application_id: 이벤트 발생한 application ID
  - application_name: 이벤트 발생한 application 이름
  - ip: 이벤트 발생한 ip
  - location: 이벤트 발생한 위치(지역)
  - user_agent: User Agent
  - event_result: 이벤트 성공 여부





## 통계용 TOPIC 에 들어가야 할 데이터

``` json
{
    "version": "1.0",
    "type": "audit",
    "count": 1,
    "data": [
        {
            "log_date": 1640390400000,
            "user_id": "sssukho",
            "user_name": "임석호",
            "event_type": "Login",
            "application": "iCloud",
            "ip": "xxx.xxx.xx.xx",
            "location": "Mapo-Gu, Seoul",
            "event_result": "Success"
        }
    ]
}
```





## 일반 조회용 TOPIC 에 들어가야 할 데이터

``` json
{
    "version": "1.0",
    "type": "audit",
    "count": 1,
    "data": [
        {
            "log_date": 1640390400000,
            "request_id": "38492f11-1565-40db-8e50-8a9e0f6c9d00",
            "user_id": "sssukho",
            "user_name": "임석호",
            "user_type": "Member",
            "event_type": "Login",
            "application_id": "03e78a26-69d4-4011-928d-7fca55779b07",
            "application_name": "iCloud",
            "ip": "xxx.xxx.xx.xx",
            "location": "Mapo-Gu, Seoul",
            "user_agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Safari/537.36 Edg/96.0.1054.62",
            "event_result": "Success"
        }
    ]
}
```



















