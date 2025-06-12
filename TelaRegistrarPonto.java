package RH;

import javax.swing.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.awt.Font;

public class TelaRegistrarPonto extends JFrame {
    private static final long serialVersionUID = 1L;

    private JLabel lbData, lbHora, lbMatricula, lbNome;
    private JTextField tfData, tfHora, tfMatricula;
    private JButton btBuscar, btEntrada, btSaidaAlmoco, btVoltaAlmoco, btSaidaFinal, btSair, btEditar;
    private String nomeFuncionario = "";

    public TelaRegistrarPonto() {
    	getContentPane().setFont(new Font("Verdana", Font.PLAIN, 14));
        setTitle("Registro de Ponto");
        setBounds(200, 150, 824, 450);
        getContentPane().setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        lbMatricula = new JLabel("Matrícula:");
        lbMatricula.setBounds(30, 20, 100, 25);
        getContentPane().add(lbMatricula);

        tfMatricula = new JTextField();
        tfMatricula.setBounds(88, 20, 132, 25);
        getContentPane().add(tfMatricula);

        lbData = new JLabel("Data:");
        lbData.setBounds(262, 20, 100, 25);
        getContentPane().add(lbData);

        tfData = new JTextField(LocalDate.now().toString());
        tfData.setBounds(300, 20, 100, 25);
        tfData.setEditable(false);
        getContentPane().add(tfData);

        lbHora = new JLabel("Horário:");
        lbHora.setBounds(420, 20, 100, 25);
        getContentPane().add(lbHora);

        tfHora = new JTextField(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        tfHora.setBounds(480, 20, 100, 25);
        tfHora.setEditable(false);
        getContentPane().add(tfHora);

        btBuscar = new JButton("Buscar Nome");
        btBuscar.setBounds(600, 20, 150, 48);
        getContentPane().add(btBuscar);

        lbNome = new JLabel("Funcionário:");
        lbNome.setFont(new Font("Verdana", Font.PLAIN, 14));
        lbNome.setBounds(30, 56, 370, 25);
        getContentPane().add(lbNome);

        btEntrada = new JButton("Entrada");
        btEntrada.setBounds(70, 99, 150, 54);
        getContentPane().add(btEntrada);

        btSaidaAlmoco = new JButton("Saída Almoço");
        btSaidaAlmoco.setBounds(70, 182, 150, 54);
        getContentPane().add(btSaidaAlmoco);

        btVoltaAlmoco = new JButton("Volta Almoço");
        btVoltaAlmoco.setBounds(300, 99, 150, 54);
        getContentPane().add(btVoltaAlmoco);

        btSaidaFinal = new JButton("Saída Final");
        btSaidaFinal.setBounds(300, 182, 150, 54);
        getContentPane().add(btSaidaFinal);

        btEditar = new JButton("Editar Ponto");
        btEditar.setBounds(600, 79, 150, 48);
        btEditar.setVisible(false);
        getContentPane().add(btEditar);

        btSair = new JButton("Sair");
        btSair.setBounds(180, 272, 150, 54);
        getContentPane().add(btSair);

        btBuscar.addActionListener(e -> buscarFuncionario());
        btEntrada.addActionListener(e -> registrar("entrada"));
        btSaidaAlmoco.addActionListener(e -> registrar("saida_almoco"));
        btVoltaAlmoco.addActionListener(e -> registrar("volta_almoco"));
        btSaidaFinal.addActionListener(e -> registrar("saida_final"));
        btEditar.addActionListener(e -> new TelaEditarPonto().setVisible(true));
        btSair.addActionListener(e -> dispose());
    }

    private void buscarFuncionario() {
        try {
            int matricula = Integer.parseInt(tfMatricula.getText());
            Connection con = Bd.getConexao();
            String sql = "SELECT nome, perfil FROM funcionario WHERE matricula = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, matricula);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                nomeFuncionario = rs.getString("nome");
                String perfil = rs.getString("perfil");
                lbNome.setText("Funcionário: " + nomeFuncionario);
                btEditar.setVisible(perfil.equalsIgnoreCase("ADMIN") || perfil.equalsIgnoreCase("GESTOR"));
            } else {
                nomeFuncionario = "";
                lbNome.setText("Funcionário não encontrado.");
                btEditar.setVisible(false);
            }

            rs.close();
            stmt.close();
            con.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar funcionário.");
            ex.printStackTrace();
        }
    }

    private void registrar(String tipo) {
        if (nomeFuncionario.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Busque primeiro o funcionário.");
            return;
        }

        try {
            int matricula = Integer.parseInt(tfMatricula.getText());
            LocalTime horaAtual = LocalTime.now();
            RegistrarPonto ponto = new RegistrarPonto(matricula);

            switch (tipo) {
                case "entrada": ponto.registrarEntrada(horaAtual); break;
                case "saida_almoco": ponto.registrarSaidaAlmoco(horaAtual); break;
                case "volta_almoco": ponto.registrarVoltaAlmoco(horaAtual); break;
                case "saida_final": ponto.registrarSaidaFinal(horaAtual); break;
            }

            RegistrarPontoDAO dao = new RegistrarPontoDAO();
            dao.registrar(ponto);

            JOptionPane.showMessageDialog(this, tipo.replace("_", " ") + " registrada com sucesso!");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao registrar ponto.");
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new TelaRegistrarPonto().setVisible(true);
    }
}
