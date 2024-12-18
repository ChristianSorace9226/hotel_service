package it.nesea.albergo.hotel_service.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OccupazioneDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 2007514205587848964L;

    int numeroCamere;

    BigDecimal percentualeOccupazioneTotale;

    List<Map<String, BigDecimal>> percentualeOccupazioneCamera;
}