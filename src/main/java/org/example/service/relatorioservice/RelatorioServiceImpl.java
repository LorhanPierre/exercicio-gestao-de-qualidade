package org.example.service.relatorioservice;

import org.example.dto.EquipamentoContagemFalhasDTO;
import org.example.dto.FalhaDetalhadaDTO;
import org.example.dto.RelatorioParadaDTO;
import org.example.model.AcaoCorretiva;
import org.example.model.Equipamento;
import org.example.model.Falha;
import org.example.repository.acaocorretiva.AcaoCorretivaRepositoryImpl;
import org.example.repository.equipamento.EquipamentoRepositoryImpl;
import org.example.repository.falha.FalhaRepositoryImpl;
import org.example.repository.relatorio.RelatorioRepositoryImpl;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RelatorioServiceImpl implements RelatorioService{
    @Override
    public List<RelatorioParadaDTO> gerarRelatorioTempoParada() throws SQLException {
        try{
            var relatorioParadas = new RelatorioRepositoryImpl();

            return relatorioParadas.gerarRelatorioTempoParada();
        }catch (Exception e){
            throw new SQLException("Erro ao gerar relatorio parada."+e.getMessage());
        }
    }

    @Override
    public List<Equipamento> buscarEquipamentosSemFalhasPorPeriodo(LocalDate dataInicio, LocalDate datafim) throws SQLException {
        return List.of();
    }

    @Override
    public Optional<FalhaDetalhadaDTO> buscarDetalhesCompletosFalha(long falhaId) throws SQLException {
        try{
            var falhas =  new FalhaRepositoryImpl();
            Falha falha = falhas.buscarFalhaPorId(falhaId);

            if(falha == null){
                throw new RuntimeException();
            }

            if(falha.getEquipamentoId() == null){
                throw new IllegalArgumentException();
            }

            var equipamentos = new EquipamentoRepositoryImpl();
            Equipamento equipamento = equipamentos.buscarEquipamentoPorId(falha.getEquipamentoId());

            var acoes = new AcaoCorretivaRepositoryImpl();
            List<String> acoesCorretivas = acoes.buscarDescricaoAcoesCorretivasPorIdFalha(falha.getId());

            var detalheCompleto = new FalhaDetalhadaDTO(falha,equipamento,acoesCorretivas);
            return Optional.of(detalheCompleto);
        }catch(Exception e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public List<EquipamentoContagemFalhasDTO> gerarRelatorioManutencaoPreventiva(int contagemMinimaFalhas) throws SQLException {
        try{

            var gerarRelatorioPreventiva = new EquipamentoRepositoryImpl();

            if(contagemMinimaFalhas <= 0){
                throw new IllegalArgumentException();
            }

            return gerarRelatorioPreventiva.gerarRelatorioManutencaoPreventiva(contagemMinimaFalhas);

        }catch (Exception e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
