package org.palemire.autobook.appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Integer> {

    Optional<AppointmentEntity> findByXid(String xid);
}
