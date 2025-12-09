package org.example.repository.relatorio;

import org.example.database.Conexao;
import org.example.dto.FalhaDetalhadaDTO;
import org.example.dto.RelatorioParadaDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RelatorioRepositoryImpl implements RelatorioRepository {

    public List<RelatorioParadaDTO> gerarRelatorioTempoParada() throws SQLException{

        List<RelatorioParadaDTO> relatorioParadas = new ArrayList<>();

        String query = """
                SELECT e.id,e.nome,f.tempoParadaHoras
                FROM Falha f
                LEFT JOIN Equipamento e
                ON f.equipamentoId = e.id
                WHERE status = 'ABERTA'
                """;

        try(Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(query)){

            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                Long id = rs.getLong("id");
                String nomeEquipamento = rs.getString("nome");
                double tempoParada = rs.getDouble("tempoParadaHoras");

                var equip = new RelatorioParadaDTO(id,nomeEquipamento,tempoParada);
                relatorioParadas.add(equip);
            }

        }

        return relatorioParadas;
    }

    @Override
    public List<FalhaDetalhadaDTO> buscarDetalhesFalha(long falhaId) throws SQLException{
        return null;
    }
}
