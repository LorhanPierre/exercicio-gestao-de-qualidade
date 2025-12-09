package org.example.repository.equipamento;

import org.example.model.Equipamento;

import java.sql.SQLException;

public interface EquipamentoRepository {

    Equipamento criarEquipamento(Equipamento equipamento) throws SQLException;

    Equipamento buscarEquipamentoPorId(Long idEquipamento) throws SQLException;

    void atualizarStatusEquipamento(String newStatus, Long id) throws SQLException;
}
