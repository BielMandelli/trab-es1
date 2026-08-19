package br.unioeste.padaria.receita.service;

import br.unioeste.padaria.ingrediente.model.entity.Ingrediente;
import br.unioeste.padaria.ingrediente.repository.IngredienteRepository;
import br.unioeste.padaria.receita.model.dto.*;
import br.unioeste.padaria.receita.model.entity.Receita;
import br.unioeste.padaria.receita.model.entity.ReceitaIngrediente;
import br.unioeste.padaria.receita.repository.ReceitaRepository;
import br.unioeste.padaria.unidade.model.entity.UnidadeMedida;
import br.unioeste.padaria.unidade.repository.UnidadeRepository;
import br.unioeste.padaria.utils.SpecificationUtils;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@AllArgsConstructor
public class ReceitaService {

    private final ReceitaRepository receitaRepository;
    private final IngredienteRepository ingredienteRepository;
    private final UnidadeRepository unidadeRepository;

    @Transactional
    public Receita salvarReceita(@Valid CriarReceitaDTO dto) {
        Receita receita = new Receita();

        receita.setNomeReceita(dto.nomeReceita());
        receita.setValidade(dto.validade());
        receita.setRendimento(dto.rendimento());
        receita.setTempoPreparo(dto.tempoPreparo());
        receita.setUnidadeMedida(this.buscarUnidadeMediaPorId(dto.idUnidadeMedida()));

        for (CriarReceitaIngredienteDTO criarReceitaIngredienteDTO : dto.ingredientes()) {

            Ingrediente ingrediente = this.buscarIngredientePorId(criarReceitaIngredienteDTO.idIngrediente());

            boolean jaAdicionado = receita.getIngredientList().stream().anyMatch(item -> item.getIngrediente().getIdIngrediente().equals(criarReceitaIngredienteDTO.idIngrediente()));

            if (jaAdicionado) continue;

            ReceitaIngrediente receitaIngrediente = new ReceitaIngrediente();

            receitaIngrediente.setReceita(receita);
            receitaIngrediente.setIngrediente(ingrediente);
            receitaIngrediente.setQuantidade(criarReceitaIngredienteDTO.quantidade());

            receita.getIngredientList().add(receitaIngrediente);
        }

        return receitaRepository.save(receita);
    }

    public Page<ReceitaDTO> listarTodasReceitas(String nomeReceita, Pageable pageable) {
        Specification<Receita> specification = Specification.allOf();

        if (nomeReceita != null && !nomeReceita.isBlank()) {
            specification = specification.and(SpecificationUtils.containsIgnoreCase("nomeReceita", nomeReceita));
        }

        Page<Receita> receitaPage = receitaRepository.findAll(specification, pageable);

        return receitaPage.map(this::formatarReceita);
    }

    public Receita buscarReceitaPorId(Long idReceita) {
        return receitaRepository.findById(idReceita).orElseThrow(() -> naoEncontradoErro("Receita", idReceita));
    }

    public Ingrediente buscarIngredientePorId(Long idIngrediente) {
        return ingredienteRepository.findById(idIngrediente).orElseThrow(() -> naoEncontradoErro("Ingrediente", idIngrediente));
    }

    public UnidadeMedida buscarUnidadeMediaPorId(Long idUnidadeMedida) {
        return unidadeRepository.findById(idUnidadeMedida).orElseThrow(() -> naoEncontradoErro("Unidade de Medida", idUnidadeMedida));
    }

    @Transactional
    public Receita atualizarReceita(Long idReceita, @Valid AtualizarReceitaDTO dto) {
        Receita receita = this.buscarReceitaPorId(idReceita);

        if (dto.nomeReceita() != null) {
            receita.setNomeReceita(dto.nomeReceita());
        }
        if (dto.rendimento() != null) {
            receita.setRendimento(dto.rendimento());
        }
        if (dto.tempoPreparo() != null) {
            receita.setTempoPreparo(dto.tempoPreparo());
        }
        if(dto.validade() != null){
            receita.setValidade(dto.validade());
        }
        if(dto.idUnidadeMedida() != null){
            receita.setUnidadeMedida(this.buscarUnidadeMediaPorId(dto.idUnidadeMedida()));
        }

        if(!dto.ingredientes().isEmpty()){
            this.removerTodosReceitaIngrediente(receita.getIdReceita());

            for (AtualizarReceitaIngredienteDTO criarReceitaIngredienteDTO : dto.ingredientes()) {

                Ingrediente ingrediente = this.buscarIngredientePorId(criarReceitaIngredienteDTO.idIngrediente());

                boolean jaAdicionado = receita.getIngredientList().stream().anyMatch(item -> item.getIngrediente().getIdIngrediente().equals(criarReceitaIngredienteDTO.idIngrediente()));

                if (jaAdicionado) continue;

                ReceitaIngrediente receitaIngrediente = new ReceitaIngrediente();

                receitaIngrediente.setReceita(receita);
                receitaIngrediente.setIngrediente(ingrediente);
                receitaIngrediente.setQuantidade(criarReceitaIngredienteDTO.quantidade());

                receita.getIngredientList().add(receitaIngrediente);
            }
        }



        return receitaRepository.save(receita);
    }

    @Transactional
    public ReceitaIngrediente adicionarReceitaIngrediente(Long idReceita, @Valid CriarReceitaIngredienteDTO dto) {
        Receita receita = buscarReceitaPorId(idReceita);

        boolean jaAdicionado = receita.getIngredientList().stream().anyMatch(item -> item.getIngrediente().getIdIngrediente().equals(dto.idIngrediente()));

        if (jaAdicionado) throw new ResponseStatusException(BAD_REQUEST, "Ingrediente já está nessa receita");

        Ingrediente ingrediente = this.buscarIngredientePorId(dto.idIngrediente());

        ReceitaIngrediente receitaIngrediente = new ReceitaIngrediente();

        receitaIngrediente.setIdReceitaIngrediente(receita.getIdReceita());
        receitaIngrediente.setReceita(receita);
        receitaIngrediente.setIngrediente(ingrediente);
        receitaIngrediente.setQuantidade(dto.quantidade());
        receita.getIngredientList().add(receitaIngrediente);

        receitaRepository.save(receita);

        return receitaIngrediente;
    }

    @Transactional
    public ReceitaIngrediente atualizarQuantidadeIngrediente(Long idReceita, Long idIngrediente, @Valid AtualizarQuantidadeIngredienteDTO dto) {
        ReceitaIngrediente receitaIngrediente = encontrarReceitaIngrediente(idReceita, idIngrediente);

        receitaIngrediente.setQuantidade(dto.quantidade());

        return receitaIngrediente;
    }

    @Transactional
    public void removerTodosReceitaIngrediente(Long idReceita) {
        Receita receita = buscarReceitaPorId(idReceita);

        receita.getIngredientList().clear();

        receitaRepository.save(receita);
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

    public ReceitaDTO formatarReceita(Receita receita) {

        List<ReceitaIngredienteDTO> ingredienteList = receita.getIngredientList()
                .stream()
                .map(receitaIngrediente -> {
                    Ingrediente ingrediente = receitaIngrediente.getIngrediente();

                    return new ReceitaIngredienteDTO(
                            ingrediente.getIdIngrediente(),
                            ingrediente.getNomeIngrediente(),
                            ingrediente.getCategoriaIngrediente(),
                            ingrediente.getUnidadeMedida(),
                            ingrediente.getCustoPorUnidade(),
                            ingrediente.getEstoqueAtual(),
                            receitaIngrediente.getQuantidade()
                    );
                })
                .toList();

        BigDecimal custoPorReceita = calcularCustoPorReceita(receita.getIngredientList());

        return new ReceitaDTO(
                receita.getIdReceita(),
                receita.getNomeReceita(),
                receita.getRendimento(),
                receita.getValidade(),
                receita.getUnidadeMedida(),
                receita.getTempoPreparo(),
                ingredienteList,
                custoPorReceita
        );
    }

    private BigDecimal calcularCustoPorReceita(List<ReceitaIngrediente> ingredienteList){
        BigDecimal custoTotal = BigDecimal.ZERO;

        for (ReceitaIngrediente receitaIngrediente : ingredienteList){
            BigDecimal valorUnidade = receitaIngrediente.getIngrediente().getCustoPorUnidade();
            BigDecimal quantidade = receitaIngrediente.getQuantidade();

            BigDecimal custoIngrediente = valorUnidade.multiply(quantidade);
            custoTotal = custoTotal.add(custoIngrediente);
        }

        return custoTotal;
    }

    public SimulacaoReceitaDTO simularReceita(@Valid CriarSimulacaoReceitaDTO dto) {
        Receita receita = this.buscarReceitaPorId(dto.idReceita());

        if (dto.lotes() <= 0) {throw new ResponseStatusException(BAD_REQUEST, "A quantidade de lotes deve ser maior que zero");}

        BigDecimal lotesDecimal = BigDecimal.valueOf(dto.lotes());

        List<SimulacaoIngredienteDTO> ingredientes = new ArrayList<>();

        int maximoLotes = Integer.MAX_VALUE;
        boolean ingredientesSuficientes = true;

        BigDecimal custoTotal = BigDecimal.ZERO;

        for (ReceitaIngrediente receitaIngrediente : receita.getIngredientList()) {

            Ingrediente ingrediente = receitaIngrediente.getIngrediente();

            BigDecimal quantidadePorLote = receitaIngrediente.getQuantidade();
            BigDecimal quantidadeNecessaria = quantidadePorLote.multiply(lotesDecimal);

            BigDecimal estoqueAtual = ingrediente.getEstoqueAtual();
            BigDecimal saldoAposProducao = estoqueAtual.subtract(quantidadeNecessaria);

            boolean suficiente = estoqueAtual.compareTo(quantidadeNecessaria) >= 0;

            if (!suficiente) ingredientesSuficientes = false;

            int lotesPossiveis = estoqueAtual.divideToIntegralValue(quantidadePorLote).intValue();

            maximoLotes = Math.min(maximoLotes, lotesPossiveis);

            BigDecimal custoIngrediente = ingrediente.getCustoPorUnidade().multiply(quantidadeNecessaria);
            custoTotal = custoTotal.add(custoIngrediente);

            ingredientes.add(
                    new SimulacaoIngredienteDTO(
                            ingrediente.getIdIngrediente(),
                            ingrediente.getNomeIngrediente(),
                            ingrediente.getCategoriaIngrediente(),
                            ingrediente.getUnidadeMedida(),
                            quantidadeNecessaria,
                            estoqueAtual,
                            saldoAposProducao,
                            suficiente
                    )
            );
        }

        return new SimulacaoReceitaDTO(
                receita.getIdReceita(),
                receita.getNomeReceita(),
                receita.getUnidadeMedida(),
                dto.lotes(),
                custoTotal,
                maximoLotes,
                ingredientesSuficientes,
                ingredientes
        );
    }
}
