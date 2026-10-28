package sportspot.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String name;

    private boolean indoorAllowed;
    private double minTemperature;
    private double maxTemperature;
    private double windSpeedKph;
    private boolean rainAllowed;
}
