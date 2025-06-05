package RH;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FuncionarioDAO {

    public boolean salvar(Funcionario funcionario) {
        Connection conexao = Bd.conectar();

        if (conexao == null) {
            System.out.println("Erro na conexão com o banco.");
            return false;
        }

        String sql = "INSERT INTO funcionario (nome, cpf, sexo, estado_civil, data_nascimento, data_admissao, data_demissao, cargo, perfil, salario_base, status, matricula) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        
        

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


            int resultado = stmt.executeUpdate();

            if (resultado > 0) {
                System.out.println("Funcionário cadastrado com sucesso!");
                System.out.println("Matrícula: " + funcionario.getMatricula());

                return true;
            } else {
                System.out.println("Erro ao cadastrar funcionário.");
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Erro ao salvar no banco: " + e.getMessage());
            return false;
        } finally {
            Bd.desconectar(conexao);
        }
        
       
    }
    
}
