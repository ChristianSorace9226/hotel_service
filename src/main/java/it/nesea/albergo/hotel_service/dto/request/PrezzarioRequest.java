package it.nesea.albergo.hotel_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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

    @Size(min = 1)
    private String numeroCamera;

    @NotNull(message = "l'età delle persone non devono essere null")
    @NotEmpty(message = "l'età delle persone devono essere fornite")
    private List<Integer> eta;

}
