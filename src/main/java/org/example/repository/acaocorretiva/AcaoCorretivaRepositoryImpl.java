package org.example.repository.acaocorretiva;

import org.example.database.Conexao;
import org.example.model.AcaoCorretiva;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AcaoCorretivaRepositoryImpl implements AcaoCorretivaRepository {

    public AcaoCorretiva registrarAcaoCorretiva(AcaoCorretiva acao) throws SQLException{

        String query = "INSERT INTO AcaoCorretiva (id,falhaId,dataHoraInicio,dataHoraFim,responsavel,descricaoAcao) VALUES (default,?,?,?,?,?)";

        try(Connection conn = Conexao.conectar();
        PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){

            stmt.setLong(1, acao.getFalhaId());
            stmt.setObject(2, acao.getDataHoraInicio());
            stmt.setObject(3, acao.getDataHoraFim());
            stmt.setString(4, acao.getResponsavel());
            stmt.setString(5, acao.getDescricaoArea());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next()){
                acao.setId(rs.getLong(1));
            }
        }
        return acao;
    }

    public List<String> buscarDescricaoAcoesCorretivasPorIdFalha(Long idFalha) throws SQLException{

        List<String> lista = new ArrayList<>();

        String query = """
                SELECT
                descricaoAcao
                FROM AcaoCorretiva
                WHERE falhaId = ?
                """;

        try(Connection conn = Conexao.conectar();
        PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setLong(1, idFalha);

            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                String descricaoArea = rs.getString("descricaoAcao");
                lista.add(descricaoArea);
            }
        }

        return lista;
    }

}

/*
            CREATE TABLE IF NOT EXISTS AcaoCorretiva (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                falhaId BIGINT NOT NULL,
                dataHoraInicio DATETIME NOT NULL,
                dataHoraFim DATETIME NOT NULL,
                responsavel VARCHAR(255) NOT NULL,
                descricaoAcao TEXT NOT NULL,

                CONSTRAINT fk_acao_falha FOREIGN KEY (falhaId)
                REFERENCES Falha(id)
                ON DELETE RESTRICT
            );
*/