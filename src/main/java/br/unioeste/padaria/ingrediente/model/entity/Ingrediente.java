package br.unioeste.padaria.ingrediente.model.entity;

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
@Table(name = "ingrediente")
public class Ingrediente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idIngrediente;
    String nomeIngrediente;
    @ManyToOne
    CategoriaIngrediente categoriaIngrediente;
    @ManyToOne
    UnidadeIngrediente unidadeIngrediente;
    BigDecimal precoPorUnidade;
    Integer estoqueAtual;
    Integer estoqueMinimo;
}
