package it.nesea.albergo.hotel_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FasciaEtaDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -6982382671386812516L;

    private Integer id;
    private String descrizione;
    private Integer etaMin;
    private Integer etaMax;
    private BigDecimal percentualeSconto;

}
