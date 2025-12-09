package org.example.service.falha;

import org.example.model.Equipamento;
import org.example.model.Falha;
import org.example.repository.equipamento.EquipamentoRepositoryImpl;
import org.example.repository.falha.FalhaRepository;
import org.example.repository.falha.FalhaRepositoryImpl;

import java.sql.SQLException;
import java.util.List;

public class FalhaServiceImpl implements FalhaService{
    @Override
    public Falha registrarNovaFalha(Falha falha) throws SQLException {
        try{
            var buscarEquipamento = new EquipamentoRepositoryImpl();
            Equipamento equipamento = buscarEquipamento.buscarEquipamentoPorId(falha.getEquipamentoId());

            if(equipamento != null){
                var falhaRepository = new FalhaRepositoryImpl();
                    if(falha.getCriticidade().equalsIgnoreCase("CRITICA")){
                        buscarEquipamento.atualizarStatusEquipamento("EM_MANUTENCAO",falha.getEquipamentoId());
                    }
                falha.setStatus("ABERTA");
                return falhaRepository.registrarFalha(falha);
            }else{
                throw new IllegalArgumentException("Equipamento não encontrado!");
            }
        }catch (Exception e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public List<Falha> buscarFalhasCriticasAbertas() throws SQLException {

        try{
            var buscarFalhas = new FalhaRepositoryImpl();
            return buscarFalhas.buscarFalhasCriticasAbertas();
        }catch (Exception e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
