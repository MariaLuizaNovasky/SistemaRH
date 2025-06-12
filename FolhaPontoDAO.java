package RH;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FolhaPontoDAO {

    public boolean salvar(FolhaPonto folha) {
        Connection connection = Bd.conectar();
        if (connection == null) return false;

        try {
            String select = "SELECT id FROM folhaponto WHERE matricula = ? AND mesReferencia = ? AND anoReferencia = ?";
            PreparedStatement check = connection.prepareStatement(select);
            check.setLong(1, folha.getFuncionario().getMatricula());
            check.setInt(2, folha.getMesReferencia());
            check.setInt(3, folha.getAnoReferencia());
            ResultSet rs = check.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String update = "UPDATE folhaponto SET horasTrabalhadas = ?, horasExtras = ?, horasNegativas = ?, saldoHoras = ? WHERE id = ?";
                PreparedStatement stmt = connection.prepareStatement(update);
                stmt.setDouble(1, folha.getHorasTrabalhadas());
                stmt.setDouble(2, folha.getHorasExtras());
                stmt.setDouble(3, folha.getHorasNegativas());
                stmt.setDouble(4, folha.getSaldoHoras());
                stmt.setInt(5, id);
                stmt.executeUpdate();
                stmt.close();
                System.out.println("Folha atualizada com sucesso.");
            } else {
                String insert = "INSERT INTO folhaponto (matricula, mesReferencia, anoReferencia, horasTrabalhadas, horasExtras, horasNegativas, saldoHoras) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = connection.prepareStatement(insert);
                stmt.setInt(1, folha.getFuncionario().getMatricula());
                stmt.setInt(2, folha.getMesReferencia());
                stmt.setInt(3, folha.getAnoReferencia());
                stmt.setDouble(4, folha.getHorasTrabalhadas());
                stmt.setDouble(5, folha.getHorasExtras());
                stmt.setDouble(6, folha.getHorasNegativas());
                stmt.setDouble(7, folha.getSaldoHoras());
                stmt.executeUpdate();
                stmt.close();
                System.out.println("Folha inserida com sucesso.");
            }

            rs.close();
            check.close();
            connection.close();
            return true;

        } catch (SQLException e) {
            System.out.println("Erro ao salvar folha ponto: " + e.getMessage());
            return false;
        }
    }

    public FolhaPonto buscarPorMesAno(int matricula, int mes, int ano) {
        Connection connection = Bd.conectar();
        if (connection == null) return null;

        try {
            String sql = "SELECT * FROM folhaponto WHERE matricula = ? AND mesReferencia = ? AND anoReferencia = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, matricula);
            stmt.setInt(2, mes);
            stmt.setInt(3, ano);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                FolhaPonto folha = new FolhaPonto();
                folha.setId(rs.getInt("id"));
                folha.setMesReferencia(rs.getInt("mesReferencia"));
                folha.setAnoReferencia(rs.getInt("anoReferencia"));
                folha.setHorasTrabalhadas(rs.getDouble("horasTrabalhadas"));
                folha.setHorasExtras(rs.getDouble("horasExtras"));
                folha.setHorasNegativas(rs.getDouble("horasNegativas"));
                folha.setSaldoHoras(rs.getDouble("saldoHoras"));

                Funcionario funcionario = Funcionario();
                funcionario.setMatricula(rs.getInt("matricula"));
                folha.setFuncionario(funcionario);

                return folha;
            }

            rs.close();
            stmt.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("Erro ao buscar folha ponto: " + e.getMessage());
        }

        return null;
    }

    public Funcionario Funcionario() {
        return new Funcionario();
    }


	public List<FolhaPonto> listarPorFuncionario(int matricula) {
        List<FolhaPonto> lista = new ArrayList<>();
        Connection connection = Bd.conectar();
        if (connection == null) return lista;

        try {
            String sql = "SELECT * FROM folhaponto WHERE matricula = ? ORDER BY anoReferencia DESC, mesReferencia DESC";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, matricula);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                FolhaPonto folha = new FolhaPonto();
                folha.setId(rs.getInt("id"));
                folha.setMesReferencia(rs.getInt("mesReferencia"));
                folha.setAnoReferencia(rs.getInt("anoReferencia"));
                folha.setHorasTrabalhadas(rs.getDouble("horasTrabalhadas"));
                folha.setHorasExtras(rs.getDouble("horasExtras"));
                folha.setHorasNegativas(rs.getDouble("horasNegativas"));
                folha.setSaldoHoras(rs.getDouble("saldoHoras"));

                Funcionario funcionario = Funcionario();
                funcionario.setMatricula(rs.getInt("matricula"));
                folha.setFuncionario(funcionario);

                lista.add(folha);
            }

            rs.close();
            stmt.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("Erro ao listar histórico da folha ponto: " + e.getMessage());
        }
        
        return lista;
    }
}
