package be.pxl.services.domain;

import java.time.LocalDate;

public record ProductRequest(
    Long id,
    String name,
    String label,
    String description,
    Category category,
    Double price,
    LocalDate added,
    String userName
){}

