package br.unioeste.padaria.receita.service;

import br.unioeste.padaria.ingrediente.model.entity.Ingrediente;
import br.unioeste.padaria.ingrediente.repository.IngredienteRepository;
import br.unioeste.padaria.receita.model.entity.Receita;
import br.unioeste.padaria.receita.model.entity.ReceitaIngrediente;
import br.unioeste.padaria.receita.model.dto.CriarReceitaDTO;
import br.unioeste.padaria.receita.model.dto.ReceitaIngredienteDTO;
import br.unioeste.padaria.receita.model.dto.ReceitaIngredienteQuantidadeDTO;
import br.unioeste.padaria.receita.model.dto.AtualizarReceitaDTO;
import br.unioeste.padaria.receita.repository.ReceitaRepository;
import br.unioeste.padaria.utils.SpecificationUtils;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ReceitaService {

    private final ReceitaRepository receitaRepository;
    private final IngredienteRepository ingredienteRepository;

    public Receita salvarReceita(@Valid CriarReceitaDTO dto) {
        Receita receita = new Receita();

        receita.setNomeReceita(dto.nomeReceita());
        receita.setPrecoVenda(dto.precoVenda());
        receita.setRendimento(dto.rendimento());
        receita.setTempoPreparacao(dto.tempoPreparacao());

        return receitaRepository.save(receita);
    }

    public Page<Receita> listarTodasReceitas(String nomeReceita, Pageable pageable) {
        Specification<Receita> specification = Specification.allOf();

        if (nomeReceita != null && !nomeReceita.isBlank()) {
            specification = specification.and(SpecificationUtils.containsIgnoreCase("nomeReceita", nomeReceita));
        }

        return receitaRepository.findAll(specification, pageable);
    }

    public Receita buscarReceitaPorId(Long idReceita) {
        return receitaRepository.findById(idReceita).orElseThrow(() -> naoEncontradoErro("Receita", idReceita));
    }

    @Transactional
    public Receita atualizarReceita(Long idReceita, @Valid AtualizarReceitaDTO dto) {
        Receita receita = this.buscarReceitaPorId(idReceita);

        if (dto.nomeReceita() != null) {
            receita.setNomeReceita(dto.nomeReceita());
        }
        if (dto.precoVenda() != null) {
            receita.setPrecoVenda(dto.precoVenda());
        }
        if (dto.rendimento() != null) {
            receita.setRendimento(dto.rendimento());
        }
        if (dto.tempoPreparacao() != null) {
            receita.setTempoPreparacao(dto.tempoPreparacao());
        }

        return receitaRepository.save(receita);
    }

    @Transactional
    public ReceitaIngrediente adicionarReceitaIngrediente(Long idReceita, @Valid ReceitaIngredienteDTO dto) {
        Receita receita = buscarReceitaPorId(idReceita);

        boolean jaAdicionado = receita.getIngredientList().stream().anyMatch(item -> item.getIngrediente().getIdIngrediente().equals(dto.idIngrediente()));

        if (jaAdicionado) {
            throw new ResponseStatusException(BAD_REQUEST, "Ingrediente já está nessa receita");
        }

        Ingrediente ingrediente = ingredienteRepository.findById(dto.idIngrediente()).orElseThrow(() -> naoEncontradoErro("Ingrediente", dto.idIngrediente()));

        ReceitaIngrediente receitaIngrediente = new ReceitaIngrediente();

        receitaIngrediente.setReceita(receita);
        receitaIngrediente.setIngrediente(ingrediente);
        receitaIngrediente.setQuantidade(dto.quantidade());
        receita.getIngredientList().add(receitaIngrediente);

        receitaRepository.save(receita);

        return receitaIngrediente;
    }

    @Transactional
    public ReceitaIngrediente atualizarQuantidadeIngrediente(Long idReceita, Long idIngrediente, @Valid ReceitaIngredienteQuantidadeDTO dto) {
        ReceitaIngrediente receitaIngrediente = encontrarReceitaIngrediente(idReceita, idIngrediente);

        receitaIngrediente.setQuantidade(dto.quantidade());

        return receitaIngrediente;
    }

    @Transactional
    public void removerReceitaIngrediente(Long idReceita, Long idIngrediente) {
        Receita receita = buscarReceitaPorId(idReceita);
        ReceitaIngrediente receitaIngrediente = encontrarReceitaIngrediente(receita, idIngrediente);

        receita.getIngredientList().remove(receitaIngrediente);

        receitaRepository.save(receita);
    }

    @Transactional
    public void deletar(Long idReceita) {
        receitaRepository.delete(buscarReceitaPorId(idReceita));
    }

    private ReceitaIngrediente encontrarReceitaIngrediente(Long idReceita, Long idIngrediente) {
        return encontrarReceitaIngrediente(buscarReceitaPorId(idReceita), idIngrediente);
    }

    private ReceitaIngrediente encontrarReceitaIngrediente(Receita receita, Long ingredientId) {
        return receita.getIngredientList().stream()
                .filter(item -> item.getIngrediente().getIdIngrediente().equals(ingredientId))
                .findFirst()
                .orElseThrow(() -> naoEncontradoErro("Ingrediente na Receita", ingredientId));
    }

    private ResponseStatusException naoEncontradoErro(String entidade, Long id) {
        return new ResponseStatusException(NOT_FOUND, entidade + " com id " + id + " não encontrado");
    }
}
