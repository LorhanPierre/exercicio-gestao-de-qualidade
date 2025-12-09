package org.example.repository.equipamento;

import org.example.database.Conexao;
import org.example.dto.EquipamentoContagemFalhasDTO;
import org.example.model.Equipamento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EquipamentoRepositoryImpl implements EquipamentoRepository {

    @Override
    public Equipamento criarEquipamento(Equipamento equipamento) throws SQLException{

        String query = "INSERT INTO Equipamento (id,nome,numeroDeSerie,areaSetor,statusOperacional) values (default,?,?,?,?) ";

        try(Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)){

            stmt.setString(1, equipamento.getNome());
            stmt.setString(2, equipamento.getNumeroDeSerie());
            stmt.setString(3, equipamento.getAreaSetor());
            stmt.setString(4, equipamento.getStatusOperacional());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()){
                equipamento.setId(rs.getLong(1));
            }
        }

        return equipamento;
    }

    @Override
    public Equipamento buscarEquipamentoPorId(Long id) throws SQLException{

        String query = """
                SELECT
                id,
                nome,
                numeroDeSerie,
                areaSetor,
                statusOperacional
                FROM Equipamento
                WHERE id=?
                """;

        try(Connection conn = Conexao.conectar();
        PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)){
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                Long idEquipamento = rs.getLong("id");
                String nomeEquipamento = rs.getString("nome");
                String numeroDeSerie = rs.getString("numeroDeSerie");
                String areaSetor = rs.getString("areaSetor");
                String statusOperacional = rs.getString("statusOperacional");

                return new Equipamento(idEquipamento,nomeEquipamento,numeroDeSerie,areaSetor,statusOperacional);
            }
            return null;
        }
    }

    @Override
    public void atualizarStatusEquipamento(String newStatus, Long id) throws SQLException{

        String query = """
                UPDATE Equipamento
                SET statusOperacional= ?
                WHERE id=?
                """;

        try(Connection conn = Conexao.conectar();
        PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)){
            stmt.setString(1,newStatus);
            stmt.setLong(2, id);
            stmt.executeUpdate();
        }
    }

    public List<EquipamentoContagemFalhasDTO> gerarRelatorioManutencaoPreventiva(int contagemMinimaFalhas) throws SQLException {

        List<EquipamentoContagemFalhasDTO> list = new ArrayList<>();

        String query = """
                SELECT
                e.id,
                e.nome,
                count(f.equipamentoId) AS "TotalDeFalhas"
                FROM Falha f
                JOIN Equipamento e
                ON f.equipamentoId = e.id
                GROUP BY equipamentoId
                """;

        try(Connection conn = Conexao.conectar();
        PreparedStatement stmt = conn.prepareStatement(query)){

            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                if(rs.getInt("TotalDeFalhas") >= contagemMinimaFalhas){
                    Long idEquipamento = rs.getLong("id");
                    String nomeEquipamento = rs.getString("nome");
                    int totalDeFalhas = rs.getInt("TotalDeFalhas");

                    var contagemDeFalhaDeEquipamento = new EquipamentoContagemFalhasDTO(idEquipamento,nomeEquipamento,totalDeFalhas);
                    list.add(contagemDeFalhaDeEquipamento);
                }
            }
        }
        return list;
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