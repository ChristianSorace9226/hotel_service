package it.nesea.albergo.hotel_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EliminaCameraRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1359725594001871594L;

    @NotBlank(message = "numero camera non può essere vuoto")
    String numeroCamera;

    @NotNull(message = "il tipo di rimozione non può essere null")
    Boolean rimozioneLogica;

    String motivazione;
}
