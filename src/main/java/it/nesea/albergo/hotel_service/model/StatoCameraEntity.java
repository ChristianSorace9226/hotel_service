package it.nesea.albergo.hotel_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Entity
@Table(schema = "hotel_service", name = "stato")
public class StatoCameraEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 831617395545567249L;

    @Id
    private Long id;
    private String stato;

}
