package it.nesea.albergo.hotel_service.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "CAMERA", schema = "HOTEL_SERVICE")
@Data
@DynamicInsert
public class Camera implements Serializable {

    @Serial
    private static final long serialVersionUID = 8007514805587848964L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cameraGenerator")
    @SequenceGenerator(name = "cameraGenerator", schema = "hotel_service", sequenceName = "seq_camera", allocationSize = 1)
    @Column(name = "ID", nullable = false, precision = 4)
    private Integer id;

    @Column(name = "NUMERO_CAMERA", nullable = false, length = 20)
    private String numeroCamera;

    @Column(name = "TIPO", nullable = false, length = 50)
    private String tipo;

    @Column(name = "ID_STATO", nullable = false)
    private Integer idStato;

    @Column(name = "CAPACITA", nullable = false, precision = 2)
    private Integer capacita;

    @Column(name = "NUMERO_ALLOGGIATI", nullable = false)
    private Integer numeroAlloggiati;

    @Column(name = "PREZZO_PER_NOTTE", nullable = false, precision = 10, scale = 2)
    private BigDecimal prezzoPerNotte;

    @Column(name = "DATA_INIZIO_DISPONIBILITA")
    @Temporal(TemporalType.DATE)
    private LocalDate dataInizioDisponibilita;

    @Column(name = "DATA_FINE_DISPONIBILITA")
    @Temporal(TemporalType.DATE)
    private LocalDate dataFineDisponibilita;

    @Column(name = "DATA_RIMOZIONE")
    @Temporal(TemporalType.DATE)
    private LocalDate dataRimozione;

    @Column(name = "MOTIVAZIONE_RIMOZIONE")
    private String motivazioneRimozione;


}
