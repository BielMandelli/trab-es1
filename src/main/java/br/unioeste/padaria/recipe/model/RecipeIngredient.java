package br.unioeste.padaria.recipe.model;

import br.unioeste.padaria.ingredient.model.entity.Ingredient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Table(name = "recipe_ingredient")
public class RecipeIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @JsonIgnore
    @ManyToOne
    Recipe recipe;

    @ManyToOne
    Ingredient ingredient;

    BigDecimal quantity;
}
