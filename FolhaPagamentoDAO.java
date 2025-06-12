package RH;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FolhaPagamentoDAO {

    public boolean inserir(FolhaPagamento folha) {
        Connection conn = Bd.conectar();
        if (conn == null) return false;

        String sqlFolha = "INSERT INTO FolhaPagamento (data, matricula, salario_bruto, salario_liquido) VALUES (?, ?, ?, ?)";

        try {
            conn.setAutoCommit(false);

            PreparedStatement stmtFolha = conn.prepareStatement(sqlFolha, Statement.RETURN_GENERATED_KEYS);
            stmtFolha.setDate(1, new java.sql.Date(folha.getData().getTime()));
            stmtFolha.setInt(2, folha.getFuncionario().getMatricula());
            stmtFolha.setDouble(3, folha.getSalarioBruto());
            stmtFolha.setDouble(4, folha.getSalarioLiquido());
            stmtFolha.executeUpdate();

            ResultSet rs = stmtFolha.getGeneratedKeys();
            int idFolha = -1;
            if (rs.next()) {
                idFolha = rs.getInt(1);
            }
            stmtFolha.close();

            String sqlBenef = "INSERT INTO FolhaBeneficio (id_folha, nome_beneficio, valor) VALUES (?, ?, ?)";
            PreparedStatement stmtBenef = conn.prepareStatement(sqlBenef);
            for (Beneficios b : folha.getBeneficios()) {
                double valor = b.calcularValor(folha.getSalarioBruto());
                stmtBenef.setInt(1, idFolha);
                stmtBenef.setString(2, b.name());
                stmtBenef.setDouble(3, valor);
                stmtBenef.addBatch();
            }
            stmtBenef.executeBatch();
            stmtBenef.close();

            String sqlDesc = "INSERT INTO FolhaDesconto (id_folha, nome_desconto, valor) VALUES (?, ?, ?)";
            PreparedStatement stmtDesc = conn.prepareStatement(sqlDesc);
            for (Descontos d : folha.getDescontos()) {
                double valor = d.calcularValor(folha.getSalarioBruto());
                stmtDesc.setInt(1, idFolha);
                stmtDesc.setString(2, d.name());
                stmtDesc.setDouble(3, valor);
                stmtDesc.addBatch();
            }
            stmtDesc.executeBatch();
            stmtDesc.close();

            conn.commit();
            conn.close();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        }
    }

    public FolhaPagamento buscarPorFuncionarioMesAno(int matricula, int mes, int ano) {
        Connection conn = Bd.conectar();
        FolhaPagamento folha = null;

        String sql = "SELECT * FROM FolhaPagamento WHERE MONTH(data) = ? AND YEAR(data) = ? AND matricula = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, mes);
            stmt.setInt(2, ano);
            stmt.setInt(3, matricula);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                folha = new FolhaPagamento();
                folha.setIdFolha(rs.getInt("id_folha"));
                folha.setData(rs.getDate("data"));
                folha.setSalarioBruto(rs.getDouble("salario_bruto"));
                folha.setSalarioLiquido(rs.getDouble("salario_liquido"));

                folha.setBeneficios(buscarBeneficiosDaFolha(folha.getIdFolha()));
                folha.setDescontos(buscarDescontosDaFolha(folha.getIdFolha()));

                folha.calcularSalarioLiquido();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return folha;
    }

    public List<Beneficios> buscarBeneficiosDaFolha(int idFolha) {
        List<Beneficios> lista = new ArrayList<>();
        Connection conn = Bd.conectar();

        String sql = "SELECT nome_beneficio FROM FolhaBeneficio WHERE id_folha = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idFolha);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(Beneficios.valueOf(rs.getString("nome_beneficio")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<Descontos> buscarDescontosDaFolha(int idFolha) {
        List<Descontos> lista = new ArrayList<>();
        Connection conn = Bd.conectar();

        String sql = "SELECT nome_desconto FROM FolhaDesconto WHERE id_folha = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idFolha);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(Descontos.valueOf(rs.getString("nome_desconto")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}
