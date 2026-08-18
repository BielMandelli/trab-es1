package br.unioeste.padaria.receita.model.entity;

import br.unioeste.padaria.unidade.model.entity.UnidadeMedida;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "receita")
public class Receita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idReceita;

    String nomeReceita;

    BigDecimal rendimento;

    Integer tempoPreparo;

    Integer validade;

    @ManyToOne
    UnidadeMedida unidadeMedida;

    @OneToMany(mappedBy = "receita", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ReceitaIngrediente> ingredientList = new ArrayList<>();
}
