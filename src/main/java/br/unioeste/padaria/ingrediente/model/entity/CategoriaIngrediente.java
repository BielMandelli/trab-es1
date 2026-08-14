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
@Table(name = "categoria_ingrediente")
public class CategoriaIngrediente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idCategoriaIngrediente;
    String nomeCategoria;
}
