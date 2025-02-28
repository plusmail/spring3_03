package kroryi.spring.dto;

import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link kroryi.spring.entity.Address}
 */
@Value
public class AddressDTO implements Serializable {
    Long id;
    @Size(max = 255)
    String city;
    @Size(max = 255)
    String street;
}