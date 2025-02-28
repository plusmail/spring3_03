package kroryi.spring.service;

import jakarta.transaction.Transactional;
import kroryi.spring.entity.Address;
import kroryi.spring.entity.Employee;
import kroryi.spring.repository.AddressRepository;
import kroryi.spring.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Transactional
    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElseThrow(
                ()-> new RuntimeException("Employee not found")
        );
    }

    public List<Address> getEmployeeAddresses(Long id) {
        Employee employee = getEmployeeById(id);
        return addressRepository.findByEmployee(employee);
    }

    // 직원주소 추가
    @Transactional
    public Address createAddress(Long employeeId, Address address) {
        Employee employee = getEmployeeById(employeeId);
        address.setEmployee(employee);
        return addressRepository.save(address);
    }

    //직원 주소 수정
    @Transactional
    public Address updateAddress(Long addressId, Address newAddress) {
        Address address = addressRepository.findById(addressId).orElseThrow(
                ()-> new RuntimeException("Address not found")
        );

        address.setCity(newAddress.getCity());
        address.setStreet(newAddress.getStreet());
        return addressRepository.save(address);
    }

    // 직원 주소 삭제
    @Transactional
    public void deleteAddress(Long addressId) {
        Address address = addressRepository.findById(addressId).orElseThrow(
                ()-> new RuntimeException("Address not found")
        );
        addressRepository.delete(address);
    }

}

