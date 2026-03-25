package com.groupeisi.company.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "ventes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"product", "user"})
public class Ventes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La date de vente est obligatoire")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date dateP;

    @Min(value = 1, message = "La quantité doit être au moins 1")
    @Column(nullable = false)
    private double quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_ref", referencedColumnName = "ref", nullable = false)
    @NotNull(message = "Le produit est obligatoire")
    private Produits product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "L'utilisateur est obligatoire")
    private UserAccount user;
}
