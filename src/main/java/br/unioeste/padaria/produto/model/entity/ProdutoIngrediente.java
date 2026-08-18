package br.unioeste.padaria.produto.model.entity;

import br.unioeste.padaria.ingrediente.model.entity.Ingrediente;
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
@Table(name = "produto_ingrediente")
public class ProdutoIngrediente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idProdutoIngrediente;

    @JsonIgnore
    @ManyToOne
    Produto produto;

    @ManyToOne
    Ingrediente ingrediente;

    BigDecimal quantidade;
}
