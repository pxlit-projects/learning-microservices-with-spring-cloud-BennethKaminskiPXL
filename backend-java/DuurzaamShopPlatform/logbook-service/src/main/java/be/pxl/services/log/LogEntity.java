package be.pxl.services.log;

import be.pxl.services.domain.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class LogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String label;
    private String description;
    @Enumerated(EnumType.STRING)
    private Category category;
    private Double price;
    private LocalDateTime changed;
    private String userName;
    private String action;
}
