package RH;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Bd {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/sistema_rh";
    private static final String USER = "root"; 
    private static final String PASSWORD = ""; 

    
    public static Connection conectar() {
        try {
            Class.forName(DRIVER); 
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("Driver não encontrado!\n" + e.toString());
            return null;
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados:\n" + e.toString());
            return null;
        }
    }

  
    public static void desconectar(Connection conexao) {
        if (conexao != null) {
            try {
                conexao.close();
            } catch (SQLException e) {
                System.out.println("Erro ao desconectar do banco de dados:\n" + e.toString());
            }
        }
    }


    public static void main(String[] args) {
        Connection conexao = conectar();
        if (conexao != null) {
            System.out.println("Conexão realizada com sucesso!");
            desconectar(conexao);
        }
    }
}
