package RH;

import java.sql.*;
import java.time.LocalTime;

public class RegistrarPontoDAO {

    public boolean salvar(RegistrarPonto ponto) {
        Connection connection = Bd.conectar();

        if (connection == null) {
            System.out.println("Erro na conexão com o banco.");
            return false;
        }

        try {
            String coluna = null;
            LocalTime hora = null;

            if (ponto.getHora_entrada() != null) {
                coluna = "hora_entrada";
                hora = ponto.getHora_entrada();
            } else if (ponto.getHora_saida_almoco() != null) {
                coluna = "hora_saida_almoco";
                hora = ponto.getHora_saida_almoco();
            } else if (ponto.getHora_volta_almoco() != null) {
                coluna = "hora_volta_almoco";
                hora = ponto.getHora_volta_almoco();
            } else if (ponto.getHora_saida_final() != null) {
                coluna = "hora_saida_final";
                hora = ponto.getHora_saida_final();
            }

            if (coluna == null || hora == null) {
                System.out.println("Nenhum ponto a registrar.");
                return false;
            }

            String updateSql = "UPDATE ponto SET " + coluna + " = ? WHERE matricula = ? AND data_registro = ?";
            PreparedStatement updateStmt = connection.prepareStatement(updateSql);
            updateStmt.setTime(1, Time.valueOf(hora));
            updateStmt.setInt(2, ponto.getMatricula());
            updateStmt.setDate(3, Date.valueOf(ponto.getData_registro()));
            int rowsUpdated = updateStmt.executeUpdate();
            updateStmt.close();

            if (rowsUpdated == 0) {
                String insertSql = "INSERT INTO ponto (matricula, data_registro, " + coluna + ") VALUES (?, ?, ?)";
                PreparedStatement insertStmt = connection.prepareStatement(insertSql);
                insertStmt.setInt(1, ponto.getMatricula());
                insertStmt.setDate(2, Date.valueOf(ponto.getData_registro()));
                insertStmt.setTime(3, Time.valueOf(hora));
                insertStmt.executeUpdate();
                insertStmt.close();
                System.out.println("Registro de ponto criado e salvo com sucesso!");
            } else {
                System.out.println("Ponto atualizado com sucesso!");
            }

            connection.close();
            return true;

        } catch (SQLException e) {
            System.out.println("Erro ao salvar ponto: " + e.getMessage());
            return false;
        }
    }
}
