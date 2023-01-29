package hust.iot.kien.restapi.model;

import java.util.Set;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class IotUser {
    @GeneratedValue
    @Id
    private Long id;

    private String username;

    @OneToMany(mappedBy = "user")
    private Set<Device> devices;
}
