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
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PREZZO_CAMERA", schema = "HOTEL_SERVICE")
public class FasciaEtaEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -2756792403083252622L;

    @Id
    @Column(name = "ID", nullable = false, precision = 2)
    private Integer id;

    @Column(name = "DESCRIZIONE", nullable = false, length = 50)
    private String descrizione;

    @Column(name = "ETA_MIN", nullable = false, precision = 2)
    private Integer etaMin;

    @Column(name = "ETA_MAX", nullable = false, precision = 2)
    private Integer etaMax;

    @Column(name = "PERCENTUALE_SCONTO", nullable = false, precision = 5, scale = 2)
    private BigDecimal percentualeSconto;

}
