package br.unioeste.padaria.ingrediente.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "unidade_ingrediente")
public class UnidadeIngrediente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idUnidadeIngrediente;
    String nomeUnidade;
    String abreviacaoUnidade;
}
