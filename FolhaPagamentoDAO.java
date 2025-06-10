package RH;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FolhaPagamentoDAO {

    public boolean inserir(FolhaPagamento folha) {
        Connection conn = Bd.conectar();
        if (conn == null) return false;

        String sql = "INSERT INTO folhapagamento (data, matricula, salario_base, descontos, beneficios, salario_liquido) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDate(1, new java.sql.Date(folha.getData().getTime()));
            stmt.setInt(2, folha.getFuncionario().getMatricula()); 
            stmt.setDouble(3, folha.getSalarioBruto());
            stmt.setDouble(4, folha.calcularTotalDescontos());
            stmt.setDouble(5, folha.calcularTotalBeneficios());
            stmt.setDouble(6, folha.getSalarioLiquido());
            stmt.executeUpdate();
            stmt.close();
            conn.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<FolhaPagamento> listarTodos() {
        List<FolhaPagamento> lista = new ArrayList<>();
        Connection conn = Bd.conectar();
        if (conn == null) return lista;

        String sql = "SELECT * FROM folhapagamento";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                FolhaPagamento folha = new FolhaPagamento();
                folha.setIdFolha(rs.getInt("matricula"));
                folha.setData(rs.getDate("data"));
                folha.setSalarioBruto(rs.getDouble("salario_base"));
                folha.setSalarioLiquido(rs.getDouble("salario_liquido"));

                lista.add(folha);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}
