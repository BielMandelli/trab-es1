package br.unioeste.padaria.unidade.service;

import br.unioeste.padaria.ingrediente.model.dto.CriarUnidadeMedidaDTO;
import br.unioeste.padaria.unidade.model.UnidadeMedida;
import br.unioeste.padaria.unidade.repository.UnidadeRepository;
import br.unioeste.padaria.utils.SpecificationUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@AllArgsConstructor
public class UnidadeService {

    UnidadeRepository unidadeRepository;

    public UnidadeMedida salvarUnidade(@Valid CriarUnidadeMedidaDTO dto) {
        UnidadeMedida unidadeMedida = new UnidadeMedida();
        unidadeMedida.setNomeUnidadeMedida(dto.nomeUnidadeMedida());
        unidadeMedida.setAbreviacao(dto.abreviacao());
        return unidadeRepository.save(unidadeMedida);
    }

    public Page<UnidadeMedida> listarTodasUnidadesMedida(String nomeUnidadeIngrediente, Pageable pageable) {
        Specification<UnidadeMedida> specification = Specification.allOf();

        if (nomeUnidadeIngrediente != null && !nomeUnidadeIngrediente.isBlank()) {
            specification = specification.and(SpecificationUtils.containsIgnoreCase("nomeUnidadeMedida", nomeUnidadeIngrediente));
        }

        return unidadeRepository.findAll(specification, pageable);
    }

    public UnidadeMedida buscarUnidadeMedidaPorId(Long idUnidadeMedida) {
        return unidadeRepository.findById(idUnidadeMedida).orElseThrow(() -> naoEncontradoErro("UnidadeMedida", idUnidadeMedida));
    }

    private ResponseStatusException naoEncontradoErro(String entidade, Long id) {
        return new ResponseStatusException(NOT_FOUND, entidade + " com " + id + " não encontrada");
    }
}
