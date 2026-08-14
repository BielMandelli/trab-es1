package br.unioeste.padaria.ingrediente.service;

import br.unioeste.padaria.ingrediente.model.dto.CriarCategoriaIngredienteDTO;
import br.unioeste.padaria.ingrediente.model.dto.CriarIngredienteDTO;
import br.unioeste.padaria.ingrediente.model.dto.CriarUnidadeIngredienteDTO;
import br.unioeste.padaria.ingrediente.model.entity.CategoriaIngrediente;
import br.unioeste.padaria.ingrediente.model.entity.Ingrediente;
import br.unioeste.padaria.ingrediente.model.entity.UnidadeIngrediente;
import br.unioeste.padaria.ingrediente.repository.CategoriaIngredienteRepository;
import br.unioeste.padaria.ingrediente.repository.IngredienteRepository;
import br.unioeste.padaria.ingrediente.repository.UnidadeIngredienteRepository;
import br.unioeste.padaria.utils.SpecificationUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class IngredienteService {

    private final IngredienteRepository ingredienteRepository;
    private final CategoriaIngredienteRepository categoriaIngredienteRepository;
    private final UnidadeIngredienteRepository unidadeIngredienteRepository;

    public UnidadeIngrediente salvarUnidade(@Valid CriarUnidadeIngredienteDTO dto) {
        UnidadeIngrediente unidadeIngrediente = new UnidadeIngrediente();
        unidadeIngrediente.setNomeUnidade(dto.nomeUnidade());
        unidadeIngrediente.setAbreviacaoUnidade(dto.abreviacaoUnidade());
        return unidadeIngredienteRepository.save(unidadeIngrediente);
    }

    public CategoriaIngrediente salvarCategoria(@Valid CriarCategoriaIngredienteDTO dto) {
        CategoriaIngrediente categoriaIngrediente = new CategoriaIngrediente();
        categoriaIngrediente.setNomeCategoria(dto.nomeCategoria());
        return categoriaIngredienteRepository.save(categoriaIngrediente);
    }

    public Ingrediente salvarIngrediente(@Valid CriarIngredienteDTO dto) {
        Ingrediente ingrediente = new Ingrediente();

        UnidadeIngrediente unidadeIngrediente = this.buscarUnidadeIngredientePorId(dto.idUnidadeIngrediente());
        CategoriaIngrediente categoriaIngrediente = this.buscarCategoriaIngredientePorId(dto.idCategoriaIngrediente());

        ingrediente.setNomeIngrediente(dto.nomeIngrediente());
        ingrediente.setCategoriaIngrediente(categoriaIngrediente);
        ingrediente.setUnidadeIngrediente(unidadeIngrediente);
        ingrediente.setPrecoPorUnidade(dto.precoPorUnidade());
        ingrediente.setEstoqueAtual(dto.estoqueAtual());
        ingrediente.setEstoqueMinimo(dto.estoqueMinimo());

        return ingredienteRepository.save(ingrediente);
    }

    public Page<Ingrediente> listarTodosIngredientes(String nomeIngrediente, String nomeCategoriaIngrediente, Pageable pageable) {
        Specification<Ingrediente> specification = Specification.allOf();

        if (nomeIngrediente != null && !nomeIngrediente.isBlank()) {
            specification = specification.and(SpecificationUtils.containsIgnoreCase("nomeIngrediente", nomeIngrediente));
        }

        if (nomeCategoriaIngrediente != null && !nomeCategoriaIngrediente.isBlank()) {
            specification = specification.and(SpecificationUtils.containsIgnoreCase("ingredientCategory.nomeCategoria", nomeCategoriaIngrediente));
        }

        return ingredienteRepository.findAll(specification, pageable);
    }

    public Page<CategoriaIngrediente> listarTodasCategoriasIngrediente(String nomeCategoriaIngrediente, Pageable pageable) {
        Specification<CategoriaIngrediente> specification = Specification.allOf();

        if (nomeCategoriaIngrediente != null && !nomeCategoriaIngrediente.isBlank()) {
            specification = specification.and(SpecificationUtils.containsIgnoreCase("nomeCategoria", nomeCategoriaIngrediente));
        }

        return categoriaIngredienteRepository.findAll(specification, pageable);
    }

    public Page<UnidadeIngrediente> listarTodasUnidadesIngrediente(String nomeUnidadeIngrediente, Pageable pageable) {
        Specification<UnidadeIngrediente> specification = Specification.allOf();

        if (nomeUnidadeIngrediente != null && !nomeUnidadeIngrediente.isBlank()) {
            specification = specification.and(SpecificationUtils.containsIgnoreCase("nomeUnidade", nomeUnidadeIngrediente));
        }

        return unidadeIngredienteRepository.findAll(specification, pageable);
    }

    public Ingrediente buscarIngredientePorId(Long idIngrediente) {
        return ingredienteRepository.findById(idIngrediente).orElseThrow(() -> naoEncontradoErro("Ingrediente", idIngrediente));
    }

    public CategoriaIngrediente buscarCategoriaIngredientePorId(Long idCategoriaIngrediente) {
        return categoriaIngredienteRepository.findById(idCategoriaIngrediente).orElseThrow(() -> naoEncontradoErro("CategoriaIngrediente", idCategoriaIngrediente));
    }

    public UnidadeIngrediente buscarUnidadeIngredientePorId(Long idUnidadeIngrediente) {
        return unidadeIngredienteRepository.findById(idUnidadeIngrediente).orElseThrow(() -> naoEncontradoErro("UnidadeIngrediente", idUnidadeIngrediente));
    }

    private ResponseStatusException naoEncontradoErro(String entidade, Long id) {
        return new ResponseStatusException(NOT_FOUND, entidade + " com " + id + " não encontrada");
    }
}

