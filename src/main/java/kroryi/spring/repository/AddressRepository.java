package kroryi.spring.repository;

import kroryi.spring.entity.Address;
import kroryi.spring.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByEmployee(Employee employee);
}
