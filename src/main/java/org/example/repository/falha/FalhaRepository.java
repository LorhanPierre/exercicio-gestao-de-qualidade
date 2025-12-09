package org.example.repository.falha;

import org.example.model.Falha;

import java.sql.SQLException;
import java.util.List;

public interface FalhaRepository {

    Falha registrarFalha(Falha falha) throws SQLException;

    List<Falha> buscarFalhasCriticasAbertas() throws SQLException;

    void AtualizarFalha(String newStatus, Long id) throws SQLException;
}
