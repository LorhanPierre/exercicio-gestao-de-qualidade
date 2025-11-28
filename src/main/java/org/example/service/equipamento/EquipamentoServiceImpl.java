package org.example.service.equipamento;

import org.example.model.Equipamento;
import org.example.repository.equipamento.EquipamentoRepositoryImpl;

import java.sql.SQLException;

public class EquipamentoServiceImpl implements EquipamentoService{
    @Override
    public Equipamento criarEquipamento(Equipamento equipamento) throws SQLException {
        try{
            equipamento.setStatusOperacional("OPERACIONAL");

            var equipamentoRepository = new EquipamentoRepositoryImpl();
            return equipamentoRepository.criarEquipamento(equipamento);

        }catch(Exception e){
            throw new RuntimeException("Erro ao criar equipamento");
        }

    }

    @Override
    public Equipamento buscarEquipamentoPorId(Long id) throws SQLException {
        try{
            var repositorio = new EquipamentoRepositoryImpl();
            Equipamento equipamento =  repositorio.buscarEquipamentoPorId(id);
            if(equipamento != null){
                return equipamento;
            }else{
                throw new SQLException("Equipamento não encontrado!");
            }
        }catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}

/*
            CREATE TABLE IF NOT EXISTS Equipamento (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                nome VARCHAR(255) NOT NULL,
                numeroDeSerie VARCHAR(100) NOT NULL UNIQUE,
                areaSetor VARCHAR(100) NOT NULL,
                statusOperacional VARCHAR(50) NOT NULL,

                -- Garante que o status só possa ter valores pré-definidos
                CONSTRAINT chk_status_equipamento CHECK (statusOperacional IN ('OPERACIONAL', 'EM_MANUTENCAO', 'INATIVO'))
                    );
 */