package it.nesea.albergo.hotel_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DisponibilitaDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -7545937526351697769L;

    private Integer disponibilitaTotale;
    private Map<String, Map<Boolean, Integer>> cameraPostiDisponibili;
    private Integer disponibilitaReale;

}
