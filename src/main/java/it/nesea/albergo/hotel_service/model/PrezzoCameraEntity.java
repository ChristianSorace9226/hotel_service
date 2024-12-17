package it.nesea.albergo.hotel_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PREZZO_CAMERA", schema = "HOTEL_SERVICE")
public class PrezzoCameraEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -6982382671386812516L;

    @Id
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_TIPO", nullable = false)
    private TipoCameraEntity tipo;

    @Column(name = "PREZZO_TOTALE", nullable = false, precision = 10, scale = 2)
    private BigDecimal prezzoTotale;

    @Column(name = "NUMERO_OCCUPANTI", nullable = false)
    private Integer numeroOccupanti;
}
