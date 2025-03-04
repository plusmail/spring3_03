package kroryi.spring.controller;


import com.fasterxml.jackson.annotation.JsonCreator;
import kroryi.spring.entity.Address;
import kroryi.spring.entity.Employee;
import kroryi.spring.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee savedEmployee = employeeService.createEmployee(employee);
        return ResponseEntity.ok(savedEmployee);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    @GetMapping("/{id}/addresses")
    public ResponseEntity<List<Address>> getEmployeeAddresses(@PathVariable Long id) {
        List<Address> addresses = employeeService.getEmployeeAddresses(id);
        return ResponseEntity.ok(addresses);
    }

    @PostMapping("/{id}/addresses")
    public ResponseEntity<Address> addEmployeeAddress(
            @PathVariable Long id,
            @RequestBody Address address) {
        Address savedAddress = employeeService.createAddress(id, address);
        return ResponseEntity.ok(savedAddress);
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<Address> updateAddress(
            @PathVariable Long addressId,
            @RequestBody Address address) {
        Address updatedAddress = employeeService.updateAddress(addressId, address);
        return ResponseEntity.ok(updatedAddress);
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long addressId) {
        employeeService.deleteAddress(addressId);
        return ResponseEntity.noContent().build();
    }

}
