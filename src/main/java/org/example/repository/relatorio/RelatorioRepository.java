package org.example.repository.relatorio;

import org.example.dto.FalhaDetalhadaDTO;
import org.example.dto.RelatorioParadaDTO;

import java.sql.SQLException;
import java.util.List;

public interface RelatorioRepository {

    List<RelatorioParadaDTO> gerarRelatorioTempoParada() throws SQLException;

    List<FalhaDetalhadaDTO> buscarDetalhesFalha(long falhaId) throws SQLException;
}
