package hust.iot.kien.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import hust.iot.kien.restapi.model.IotUser;

public interface IotUserRepository extends JpaRepository<IotUser, Long> {
    
}
