package it.nesea.albergo.hotel_service.model;

import jakarta.persistence.Column;
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
@Table(schema = "HOTEL_SERVICE", name = "STATO")
public class StatoCameraEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 831617395545567249L;

    @Id
    @Column(name = "ID", nullable = false, precision = 2)
    private Integer id;

    @Column(name = "STATO", nullable = false, length = 20)
    private String stato;

}
