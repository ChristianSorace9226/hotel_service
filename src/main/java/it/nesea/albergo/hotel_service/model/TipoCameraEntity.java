package it.nesea.albergo.hotel_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "HOTEL_SERVICE", name = "TIPO")
public class TipoCameraEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -2114117393400681263L;

    @Id
    @Column(name = "ID", nullable = false, precision = 2)
    private Integer id;

    @Column(name = "TIPO", nullable = false, length = 30)
    private String tipo;
}
