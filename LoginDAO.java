package RH;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginDAO {

    public Funcionario verificarLogin(String matricula, String senha) {
        Funcionario funcionario = null;
        String sql = "SELECT * FROM funcionario WHERE matricula = ? AND senha = ?";

        try (Connection conexao = Bd.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, matricula);
            stmt.setString(2, senha);  //

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                
                funcionario = new Funcionario(
                    rs.getString("nome"),
                    rs.getString("cpf"),
                    Sexo.valueOf(rs.getString("sexo")),
                    EstadoCivil.valueOf(rs.getString("estado_civil")),
                    rs.getDate("data_nascimento").toLocalDate(),
                    rs.getDate("data_admissao").toLocalDate(),
                    rs.getDate("data_demissao") != null ? rs.getDate("data_demissao").toLocalDate() : null,
                    rs.getString("cargo"),
                    Perfil.valueOf(rs.getString("perfil")),
                    rs.getDouble("salario_base"),
                    rs.getBoolean("status")
                );
                funcionario.setMatricula(rs.getInt("matricula"));
                funcionario.setSenha(rs.getString("senha"));
            }
            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return funcionario;
    }
}

