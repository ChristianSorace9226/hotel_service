package it.nesea.albergo.hotel_service.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class OccupazioneDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 2007514205587848964L;

    @NotNull(message = "Il numero totale di camere non può essere null")
    @Min(value = 0, message = "Il totale delle camere deve essere maggiore o uguale a 0")
    int totaleCamere;

    @NotNull(message = "Il numero di camere occupate non può essere null")
    @Min(value = 0, message = "Il numero di camere occupate deve essere maggiore o uguale a 0")
    int camereOccupate;

    @DecimalMin(value = "0.0", inclusive = false, message = "La percentuale di occupazione deve essere maggiore di 0")
    @DecimalMax(value = "100.0", inclusive = true, message = "La percentuale di occupazione non può essere maggiore di 100")
    double percentualeOccupazione;

    @NotNull(message = "Il numero di posti liberi totali non può essere null")
    @Positive(message = "Il numero di posti liberi deve essere positivo")
    int postiLiberoTotali;

    @NotNull(message = "Il numero di posti occupati totali non può essere null")
    @Positive(message = "Il numero di posti occupati deve essere positivo")
    int postiOccupatiTotali;
}
