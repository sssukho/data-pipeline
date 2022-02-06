package com.sssukho.kafkaconsumerlog.repository;

import com.sssukho.kafkaconsumerlog.dao.AuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLogEntity, String> {

    // findBy 뒤에 컬럼명을 붙여주면 이를 이용한 검색이 가능하다.

}
