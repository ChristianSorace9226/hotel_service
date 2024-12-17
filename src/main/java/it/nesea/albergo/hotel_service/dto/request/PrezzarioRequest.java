package it.nesea.albergo.hotel_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrezzarioRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 3508182050514536405L;

    @NotBlank(message = "il nome della camera deve essere fornito")
    @Size(min = 1)
    private String numeroCamera;

    @NotNull(message = "il numero delle persone nella prenotazione deve essere fornito")
    private Integer numeroOccupanti;

    @NotNull(message = "le età delle persone devono essere fornite")
    private List<Integer> eta;

}
