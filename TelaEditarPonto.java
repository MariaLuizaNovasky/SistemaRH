package RH;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.time.LocalDate;

public class TelaEditarPonto extends JFrame {
    private static final long serialVersionUID = 1L;

    private JTextField tfMatricula, tfData, tfNome;
    private JTable table;
    private DefaultTableModel model;

    public TelaEditarPonto() {
        setTitle("Editar Ponto do Dia");
        setBounds(200, 150, 1100, 450);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel lbMatricula = new JLabel("Matrícula:");
        lbMatricula.setBounds(30, 20, 80, 25);
        add(lbMatricula);

        tfMatricula = new JTextField();
        tfMatricula.setBounds(100, 20, 100, 25);
        add(tfMatricula);

        JLabel lbData = new JLabel("Data:");
        lbData.setBounds(220, 20, 50, 25);
        add(lbData);

        tfData = new JTextField();
        tfData.setBounds(270, 20, 100, 25);
        add(tfData);

        JButton btBuscar = new JButton("Buscar");
        btBuscar.setBounds(380, 20, 100, 25);
        add(btBuscar);

        JLabel lbNome = new JLabel("Funcionário:");
        lbNome.setBounds(500, 20, 100, 25);
        add(lbNome);

        tfNome = new JTextField();
        tfNome.setBounds(580, 20, 300, 25);
        tfNome.setEditable(false);
        add(tfNome);

        model = new DefaultTableModel();
        model.addColumn("Entrada");
        model.addColumn("Saída Almoço");
        model.addColumn("Volta Almoço");
        model.addColumn("Saída Final");

        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(30, 70, 1000, 250);
        add(scrollPane);

        JButton btSalvar = new JButton("Salvar");
        btSalvar.setBounds(460, 340, 150, 30);
        add(btSalvar);

        btBuscar.addActionListener(e -> buscarPonto());
        btSalvar.addActionListener(e -> salvarAlteracoes());
    }

    private void buscarPonto() {
        try {
            model.setRowCount(0);
            int matricula = Integer.parseInt(tfMatricula.getText().trim());
            LocalDate data = LocalDate.parse(tfData.getText().trim());

            Connection con = Bd.conectar();
            String sql = "SELECT p.hora_entrada, p.hora_saida_almoco, p.hora_volta_almoco, p.hora_saida_final, f.nome FROM ponto p JOIN funcionario f ON p.matricula = f.matricula WHERE p.matricula = ? AND p.data_registro = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, matricula);
            stmt.setDate(2, Date.valueOf(data));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                tfNome.setText(rs.getString("nome"));

                model.addRow(new Object[]{
                    rs.getTime("hora_entrada") != null ? rs.getTime("hora_entrada").toString() : "",
                    rs.getTime("hora_saida_almoco") != null ? rs.getTime("hora_saida_almoco").toString() : "",
                    rs.getTime("hora_volta_almoco") != null ? rs.getTime("hora_volta_almoco").toString() : "",
                    rs.getTime("hora_saida_final") != null ? rs.getTime("hora_saida_final").toString() : ""
                });
            } else {
                tfNome.setText("");
                JOptionPane.showMessageDialog(this, "Registro de ponto não encontrado.");
            }

            rs.close();
            stmt.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar ponto.");
            e.printStackTrace();
        }
    }

    private void salvarAlteracoes() {
        try {
            int matricula = Integer.parseInt(tfMatricula.getText().trim());
            LocalDate data = LocalDate.parse(tfData.getText().trim());

            String entrada = (String) model.getValueAt(0, 0);
            String saidaAlmoco = (String) model.getValueAt(0, 1);
            String voltaAlmoco = (String) model.getValueAt(0, 2);
            String saidaFinal = (String) model.getValueAt(0, 3);

            Connection con = Bd.conectar();

            String sql = "UPDATE ponto SET hora_entrada = ?, hora_saida_almoco = ?, hora_volta_almoco = ?, hora_saida_final = ? WHERE matricula = ? AND data_registro = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setTime(1, entrada.isEmpty() ? null : Time.valueOf(entrada));
            stmt.setTime(2, saidaAlmoco.isEmpty() ? null : Time.valueOf(saidaAlmoco));
            stmt.setTime(3, voltaAlmoco.isEmpty() ? null : Time.valueOf(voltaAlmoco));
            stmt.setTime(4, saidaFinal.isEmpty() ? null : Time.valueOf(saidaFinal));
            stmt.setInt(5, matricula);
            stmt.setDate(6, Date.valueOf(data));

            int atualizado = stmt.executeUpdate();

            if (atualizado > 0) {
                JOptionPane.showMessageDialog(this, "Ponto alterado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(this, "Registro não encontrado.");
            }

            stmt.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar alterações.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TelaEditarPonto().setVisible(true);
        });
    }
}
