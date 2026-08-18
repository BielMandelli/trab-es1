package br.unioeste.padaria.unidade.model.entity;

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
@Table(name = "unidade_medida")
public class UnidadeMedida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idUnidadeMedida;
    String nomeUnidadeMedida;
    String abreviacao;
}
