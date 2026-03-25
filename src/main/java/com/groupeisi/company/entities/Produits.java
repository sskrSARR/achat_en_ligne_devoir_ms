package com.groupeisi.company.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "produits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"achats", "ventes"})
public class Produits {

    /**
     * La clé primaire EST la référence : UUID généré automatiquement.
     * Ne jamais fournir cette valeur à la création.
     */
    @Id
    @UuidGenerator
    @Column(name = "ref", updatable = false, nullable = false)
    private String ref;

    @NotBlank(message = "Le nom est obligatoire")
    @Column(nullable = false)
    private String name;

    @Min(value = 0, message = "Le stock ne peut pas être négatif")
    @Column(nullable = false)
    private double stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "L'utilisateur est obligatoire")
    private UserAccount user;

    /**
     * Relations inverses — mappedBy pour cascader la suppression.
     * Si on supprime un produit, ses achats et ventes liés sont aussi supprimés.
     */
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Achats> achats = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Ventes> ventes = new ArrayList<>();
}
