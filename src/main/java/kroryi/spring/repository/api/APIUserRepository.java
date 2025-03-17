package kroryi.spring.repository.api;

import kroryi.spring.entity.APIUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface APIUserRepository extends JpaRepository<APIUser,String> {

}
