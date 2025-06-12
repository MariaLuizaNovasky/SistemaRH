package RH;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.sql.*;
import java.time.Duration;
import java.util.List;

public class TelaFolhaPonto extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 673970797559038147L;
	private JTextField tfMatricula, tfAno, tfNome;
    private JComboBox<String> cbMes;
    private JTable table;
    private DefaultTableModel model;
    private JButton btBuscar;

    public TelaFolhaPonto() {
        setTitle("Folha Detalhada de Ponto");
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        inicializarComponentes();
        definirEventos();
    }

    private void inicializarComponentes() {
        JLabel lbMatricula = new JLabel("Matrícula:");
        lbMatricula.setBounds(30, 20, 80, 25);
        add(lbMatricula);

        tfMatricula = new JTextField();
        tfMatricula.setBounds(100, 20, 80, 25);
        add(tfMatricula);

        JLabel lbNome = new JLabel("Funcionário:");
        lbNome.setBounds(200, 20, 80, 25);
        add(lbNome);

        tfNome = new JTextField();
        tfNome.setBounds(280, 20, 200, 25);
        tfNome.setEditable(false);
        add(tfNome);

        JLabel lbMes = new JLabel("Mês:");
        lbMes.setBounds(500, 20, 40, 25);
        add(lbMes);

        cbMes = new JComboBox<>(new String[]{"01","02","03","04","05","06","07","08","09","10","11","12"});
        cbMes.setBounds(540, 20, 60, 25);
        add(cbMes);

        JLabel lbAno = new JLabel("Ano:");
        lbAno.setBounds(620, 20, 40, 25);
        add(lbAno);

        tfAno = new JTextField();
        tfAno.setBounds(660, 20, 80, 25);
        add(tfAno);

        btBuscar = new JButton("Buscar");
        btBuscar.setBounds(760, 20, 100, 25);
        add(btBuscar);

        String[] colunas = {"Data", "Entrada", "Saída Almoço", "Volta Almoço", "Saída Final", "Trabalhadas", "Extras", "Negativas"};
        model = new DefaultTableModel(colunas, 0) {
            private static final long serialVersionUID = 1L;
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(30, 70, 920, 350);
        add(scroll);
    }

    private void definirEventos() {
        btBuscar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buscarDados();
            }
        });
    }

    private void buscarDados() {
        try {
            model.setRowCount(0);
            int matricula = Integer.parseInt(tfMatricula.getText());
            int mes = Integer.parseInt(cbMes.getSelectedItem().toString());
            int ano = Integer.parseInt(tfAno.getText());

            try (Connection con = Bd.getConexao()) {
                PreparedStatement ps = con.prepareStatement("SELECT nome FROM funcionario WHERE matricula = ?");
                ps.setInt(1, matricula);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) tfNome.setText(rs.getString("nome"));
                else tfNome.setText("Não encontrado");
            } catch (Exception ex) {
                tfNome.setText("Erro ao buscar");
            }

            RegistrarPontoDAO dao = new RegistrarPontoDAO();
            List<RegistrarPonto> lista = dao.listarPorMesEAno(matricula, mes, ano);

            if (lista.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nenhum ponto encontrado.");
                return;
            }

            FolhaPonto folha = new FolhaPonto();
            Funcionario func = new Funcionario();
            func.setMatricula(matricula);
            folha.setFuncionario(func);
            folha.setMesReferencia(mes);
            folha.setAnoReferencia(ano);
            folha.calcularTotais(lista, Duration.ofHours(8));

            for (RegistrarPonto p : lista) {
                Duration dif = p.calcularDiferencaJornada(Duration.ofHours(8));
                model.addRow(new Object[]{
                    p.getData_registro(),
                    p.getHora_entrada(),
                    p.getHora_saida_almoco(),
                    p.getHora_volta_almoco(),
                    p.getHora_saida_final(),
                    formatar(p.calcularTotalHorasTrabalhadas()),
                    dif.isNegative() ? "" : formatar(dif),
                    dif.isNegative() ? formatar(dif.abs()) : ""
                });
            }

            model.addRow(new Object[]{
                "TOTAL MENSAL", "", "", "", "",
                String.format("%.2f h", folha.getHorasTrabalhadas()),
                String.format("%.2f h", folha.getHorasExtras()),
                String.format("%.2f h", folha.getHorasNegativas())
            });

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao buscar: " + ex.getMessage());
        }
    }

    private String formatar(Duration d) {
        long h = d.toHours();
        long m = d.minusHours(h).toMinutes();
        return String.format("%02dh%02dmin", h, m);
    }

    public static void main(String[] args) {
        new TelaFolhaPonto().setVisible(true);
    }
}
