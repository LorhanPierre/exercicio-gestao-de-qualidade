package org.example.repository.falha;

import org.example.database.Conexao;
import org.example.model.Falha;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class FalhaRepositoryImpl implements FalhaRepository {

    @Override
    public Falha registrarFalha(Falha falha) throws SQLException {

        String query = "INSERT INTO Falha (id,equipamentoId,dataHoraOcorrencia,descricao,criticidade,status,tempoParadaHoras) VALUES (default,?,?,?,?,?,?) ";

        try(Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            stmt.setLong(1, falha.getEquipamentoId());
            stmt.setTimestamp(2,java.sql.Timestamp.valueOf(falha.getDataHoraOcorrencia()));
            stmt.setString(3,falha.getDescricao());
            stmt.setString(4,falha.getCriticidade());
            stmt.setString(5,falha.getStatus());
            stmt.setBigDecimal(6,falha.getTempoParadaHoras());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();

            if(rs.next()){
                falha.setId(rs.getLong(1));
            }

        }
        return falha;
    }

    @Override
    public List<Falha> buscarFalhasCriticasAbertas() throws SQLException {

        List<Falha> falhas = new ArrayList<>();

        String query = """
                SELECT
                id,
                equipamentoId,
                dataHoraOcorrencia,
                descricao,
                criticidade,
                status,
                tempoParadaHoras
                FROM Falha
                WHERE status = ? and criticidade = ?
                """;

        try(Connection conn = Conexao.conectar();
        PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            stmt.setString(1,"ABERTA");
            stmt.setString(2, "CRITICA");
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                Long id = rs.getLong("id");
                Long equipamentoId = rs.getLong("equipamentoId");
                LocalDateTime dataHoraOcorrencia = rs.getTimestamp("dataHoraOcorrencia").toLocalDateTime();
                String descricao = rs.getString("descricao");
                String criticidade = rs.getString("criticidade");
                String status = rs.getString("status");
                BigDecimal tempoParadaHoras = rs.getBigDecimal("tempoParadaHoras");

                Falha falha = new Falha(id,equipamentoId,dataHoraOcorrencia,descricao,criticidade,status,tempoParadaHoras);
                falhas.add(falha);
            }
        }
        return falhas;
    }

    public void AtualizarFalha(String newStatus, Long id) throws SQLException{

        String query = "UPDATE Falha SET status = ? WHERE id = ?";

        try(Connection conn = Conexao.conectar();
        PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){

            stmt.setString(1, newStatus);
            stmt.setLong(2, id);
            stmt.executeUpdate();
        }
    }

    public Falha buscarFalhaPorId(Long id) throws SQLException {

        Falha falha = new Falha();

        String query = """
                SELECT
                id,
                equipamentoId,
                dataHoraOcorrencia,
                descricao,
                criticidade,
                status,
                tempoParadaHoras
                FROM Falha
                WHERE id = ?
                """;

        try(Connection conn = Conexao.conectar();
        PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){

            stmt.setLong(1, id);

            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                falha.setId(rs.getLong("id"));
                falha.setEquipamentoId(rs.getLong("equipamentoId"));
                falha.setDataHoraOcorrencia(rs.getTimestamp("dataHoraOcorrencia").toLocalDateTime());
                falha.setDescricao(rs.getString("descricao"));
                falha.setCriticidade(rs.getString("criticidade"));
                falha.setStatus(rs.getString("status"));
                falha.setTempoParadaHoras(rs.getBigDecimal("tempoParadaHoras"));
            }
        }
        return falha;
    }
}

/*            """
            CREATE TABLE IF NOT EXISTS Falha (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                equipamentoId BIGINT NOT NULL,
                dataHoraOcorrencia DATETIME NOT NULL,
                descricao TEXT NOT NULL,
                criticidade VARCHAR(50) NOT NULL,
                status VARCHAR(50) NOT NULL,
                tempoParadaHoras DECIMAL(10,2) DEFAULT 0.00,

                CONSTRAINT chk_criticidade_falha CHECK (criticidade IN ('BAIXA','MEDIA','ALTA','CRITICA')),
                CONSTRAINT chk_status_falha CHECK (status IN ('ABERTA','EM_ANDAMENTO','RESOLVIDA'))
            );
            """;
 */
