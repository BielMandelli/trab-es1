package br.unioeste.padaria.ingredient.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ingredient")
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    @ManyToOne
    IngredientCategory ingredientCategory;
    @ManyToOne
    IngredientUnit ingredientUnit;
    BigDecimal pricePerUnit;
    Integer stock;
    Integer minStock;
}
