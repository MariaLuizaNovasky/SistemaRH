package RH;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class FuncionarioDAO {
	
	
	 public boolean autenticar(String usuario, String senha) {
	        Connection conexao = Bd.conectar();

	        if (conexao == null) {
	            System.out.println("Erro na conexão com o banco.");
	            return false;
	        }

	        String sql = "SELECT senha FROM funcionario WHERE cpf = ?";

	        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
	            stmt.setString(1, usuario);
	            ResultSet rs = stmt.executeQuery();

	            if (rs.next()) {
	                String senhaNoBanco = rs.getString("senha");
	                return senhaNoBanco.equals(senha);
	                // Se usar hash: return BCrypt.checkpw(senha, senhaNoBanco);
	            } else {
	                return false; // Usuário não encontrado
	            }

	        } catch (SQLException e) {
	            System.out.println("Erro na autenticação: " + e.getMessage());
	            return false;
	        }
	    }

 
    public boolean salvar(Funcionario funcionario) {
        Connection conexao = Bd.conectar();

        if (conexao == null) {
            System.out.println("Erro na conexão com o banco.");
            return false;
        }

        String sql = "INSERT INTO funcionario (nome, cpf, sexo, estado_civil, data_nascimento, data_admissao, data_demissao, cargo, perfil, salario_base, status, matricula, senha)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, funcionario.getNome());
            stmt.setString(2, funcionario.getCpf());
            stmt.setString(3, funcionario.getSexo().name());
            stmt.setString(4, funcionario.getEstadoCivil().name());
            stmt.setDate(5, java.sql.Date.valueOf(funcionario.getDataNascimento()));

            if (funcionario.getDataAdmissao() != null) {
                stmt.setDate(6, java.sql.Date.valueOf(funcionario.getDataAdmissao()));
            } else {
                stmt.setNull(6, java.sql.Types.DATE);
            }

            if (funcionario.getDataDemissao() != null) {
                stmt.setDate(7, java.sql.Date.valueOf(funcionario.getDataDemissao()));
            } else {
                stmt.setNull(7, java.sql.Types.DATE);
            }

            stmt.setString(8, funcionario.getCargo());
            stmt.setString(9, funcionario.getPerfil().name());
            stmt.setDouble(10, funcionario.getSalarioBase());
            stmt.setBoolean(11, funcionario.isStatus());
            stmt.setInt(12, funcionario.getMatricula());
            stmt.setString(13, funcionario.getSenha());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao salvar funcionário: " + e.getMessage());
            return false;
        }
    }
}