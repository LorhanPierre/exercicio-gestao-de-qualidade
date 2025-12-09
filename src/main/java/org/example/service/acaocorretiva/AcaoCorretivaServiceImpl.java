package org.example.service.acaocorretiva;

import org.example.model.AcaoCorretiva;
import org.example.model.Falha;
import org.example.repository.acaocorretiva.AcaoCorretivaRepositoryImpl;
import org.example.repository.equipamento.EquipamentoRepositoryImpl;
import org.example.repository.falha.FalhaRepositoryImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AcaoCorretivaServiceImpl implements AcaoCorretivaService{

    @Override
    public AcaoCorretiva registrarConclusaoDeAcao(AcaoCorretiva acao) throws SQLException {
        try{

            var acaoCorretiva = new AcaoCorretivaRepositoryImpl();
            var falha = new FalhaRepositoryImpl();
            var equip = new EquipamentoRepositoryImpl();

            List<Falha> falhas = falha.buscarFalhasCriticasAbertas();

            for(Falha falhasDisponiveis : falhas){
                if(falhasDisponiveis.getId().equals(acao.getFalhaId())){
                    equip.atualizarStatusEquipamento("OPERACIONAL",falhasDisponiveis.getEquipamentoId());
                }
            }

            falha.AtualizarFalha("RESOLVIDA",acao.getFalhaId());
            return acaoCorretiva.registrarAcaoCorretiva(acao);

        }catch (Exception e){
            throw new RuntimeException("Falha não encontrada!");
        }
    }
}
