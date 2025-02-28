package kroryi.spring.dto;

import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link kroryi.spring.entity.Employee}
 */
@Value
public class EmployeeDTO implements Serializable {
    Long id;

    @Size(max = 30)
    String department;

    @Size(max = 50)
    String name;

    List<AddressDTO> addresses;
}