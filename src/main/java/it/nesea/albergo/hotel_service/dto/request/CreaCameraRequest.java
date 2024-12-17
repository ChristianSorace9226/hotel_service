package it.nesea.albergo.hotel_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class CreaCameraRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 7359725594001871594L;

    @NotBlank(message = "numero camera non può essere vuoto")
    @Size(max = 20)
    String numeroCamera;

    @NotNull(message = "il tipo della camera deve essere definito")
    Integer idTipo;

    @NotNull(message = "lo stato della camera deve essere definito")
    Integer idStato;

    @NotNull(message = "capacita non può essere null")
    Integer capacita;

    @NotNull(message = "prezzo per notte non può essere null")
    BigDecimal prezzoPerNotte;

    @NotNull(message = "data inizio disponibilita non può essere null")
    LocalDate dataInizioDisponibilita;
}