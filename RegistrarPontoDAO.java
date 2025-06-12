package RH;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegistrarPontoDAO {

    public List<RegistrarPonto> listarPorMesEAno(int matricula, int mes, int ano) {
    List<RegistrarPonto> lista = new ArrayList<>();
    Connection con = Bd.conectar();

    if (con == null) return lista;

    try {
        String sql = "SELECT * FROM ponto WHERE matricula = ? AND MONTH(data_registro) = ? AND YEAR(data_registro) = ? ORDER BY data_registro";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setInt(1, matricula);
        stmt.setInt(2, mes);
        stmt.setInt(3, ano);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            RegistrarPonto rp = new RegistrarPonto();
            rp.setData_registro(rs.getDate("data_registro").toLocalDate());
            rp.setHora_entrada(rs.getTime("hora_entrada") != null ? rs.getTime("hora_entrada").toLocalTime() : null);
            rp.setHora_saida_almoco(rs.getTime("hora_saida_almoco") != null ? rs.getTime("hora_saida_almoco").toLocalTime() : null);
            rp.setHora_volta_almoco(rs.getTime("hora_volta_almoco") != null ? rs.getTime("hora_volta_almoco").toLocalTime() : null);
            rp.setHora_saida_final(rs.getTime("hora_saida_final") != null ? rs.getTime("hora_saida_final").toLocalTime() : null);
            lista.add(rp);
        }

        rs.close();
        stmt.close();
        con.close();
    } catch (Exception e) {
        System.out.println("Erro ao listar pontos: " + e.getMessage());
    }

    return lista;
}

	public void registrar(RegistrarPonto ponto) {
		// TODO Auto-generated method stub
		
	}

	public void salvar(RegistrarPonto ponto) {
		// TODO Auto-generated method stub
		
	}

	public int contarFaltas(int matricula, int mes, int ano) {
	    int faltas = 0;
	    Connection conn = Bd.conectar();

	    String sql = "SELECT COUNT(*) AS faltas " +
	                 "FROM ponto " +
	                 "WHERE MONTH(data) = ? AND YEAR(data) = ? AND matricula = ? " +
	                 "AND (entrada IS NULL OR entrada = '') " +
	                 "AND (saida_almoco IS NULL OR saida_almoco = '') " +
	                 "AND (retorno_almoco IS NULL OR retorno_almoco = '') " +
	                 "AND (saida_final IS NULL OR saida_final = '')";

	    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setInt(1, mes);
	        stmt.setInt(2, ano);
	        stmt.setInt(3, matricula);

	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            faltas = rs.getInt("faltas");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return faltas;
	}
}
