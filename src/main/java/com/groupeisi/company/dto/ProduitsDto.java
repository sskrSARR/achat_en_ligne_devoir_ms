package com.groupeisi.company.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProduitsDto {

    // ref = clé primaire UUID (null à la création = auto-généré, fourni en update/delete)
    private String ref;

    @NotBlank(message = "Le nom est obligatoire")
    private String name;

    @Min(value = 0, message = "Le stock ne peut pas être négatif")
    private double stock;

    @NotNull(message = "L'ID utilisateur est obligatoire")
    private Long userId;
}
