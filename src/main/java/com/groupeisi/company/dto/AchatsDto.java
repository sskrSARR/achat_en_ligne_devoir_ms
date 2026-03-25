package com.groupeisi.company.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AchatsDto {

    private Long id;

    @NotNull(message = "La date d'achat est obligatoire")
    private Date dateP;

    @Min(value = 1, message = "La quantité doit être au moins 1")
    private double quantity;

    @NotBlank(message = "La référence du produit est obligatoire")
    private String productRef;  // UUID du produit

    @NotNull(message = "L'ID utilisateur est obligatoire")
    private Long userId;
}
