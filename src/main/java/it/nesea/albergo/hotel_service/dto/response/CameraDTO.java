package it.nesea.albergo.hotel_service.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CameraDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 8007514805587848964L;

    Integer id;

    String numeroCamera;

    Integer idTipo;

    Integer idStato;

    Integer capacita;

    BigDecimal prezzoPerNotte;

    LocalDate dataInizioDisponibilita;

    LocalDate dataFineDisponibilita;

    LocalDate dataRimozione;
}