package RH;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.awt.Font;

public class TelaFolhaPagamento extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField tfMatricula, tfNome, tfAno;
    private JComboBox<String> cbMes;
    private JTextArea detalhesArea;
    private JButton btnBuscar, btnGerar;

    public TelaFolhaPagamento(Funcionario usuarioLogado) {
        setTitle("Folha de Pagamento");
        setBounds(300, 150, 700, 500);
        getContentPane().setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel lbMatricula = new JLabel("Matrícula:");
        lbMatricula.setBounds(30, 20, 70, 25);
        getContentPane().add(lbMatricula);

        tfMatricula = new JTextField();
        tfMatricula.setBounds(100, 20, 100, 25);
        getContentPane().add(tfMatricula);

        JLabel lbNome = new JLabel("Funcionário:");
        lbNome.setBounds(220, 20, 80, 25);
        getContentPane().add(lbNome);

        tfNome = new JTextField();
        tfNome.setBounds(300, 20, 250, 25);
        tfNome.setEditable(false);
        getContentPane().add(tfNome);

        JLabel lbMes = new JLabel("Mês:");
        lbMes.setBounds(30, 60, 40, 25);
        getContentPane().add(lbMes);

        cbMes = new JComboBox<>(new String[]{
            "01", "02", "03", "04", "05", "06",
            "07", "08", "09", "10", "11", "12"
        });
        cbMes.setBounds(70, 60, 60, 25);
        getContentPane().add(cbMes);

        JLabel lbAno = new JLabel("Ano:");
        lbAno.setBounds(150, 60, 40, 25);
        getContentPane().add(lbAno);

        tfAno = new JTextField();
        tfAno.setBounds(190, 60, 80, 25);
        getContentPane().add(tfAno);

        btnBuscar = new JButton("Ver Folha");
        btnBuscar.setBounds(300, 60, 100, 25);
        getContentPane().add(btnBuscar);

        btnGerar = new JButton("Gerar Folha");
        btnGerar.setBounds(420, 60, 120, 25);
        btnGerar.setEnabled(usuarioLogado.getPerfil() == Perfil.ADMIN);
        getContentPane().add(btnGerar);

        detalhesArea = new JTextArea();
        detalhesArea.setFont(new Font("Verdana", Font.PLAIN, 14));
        detalhesArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(detalhesArea);
        scroll.setBounds(30, 100, 630, 330);
        getContentPane().add(scroll);

        btnBuscar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buscarFolha();
            }
        });

        btnGerar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gerarFolha();
            }
        });
    }

    private void buscarFolha() {
        try {
            int matricula = Integer.parseInt(tfMatricula.getText());
            int ano = Integer.parseInt(tfAno.getText());
            int mes = Integer.parseInt((String) cbMes.getSelectedItem());

            FuncionarioDAO fdao = new FuncionarioDAO();
            Funcionario func = fdao.buscarPorMatricula(matricula);

            if (func == null) {
                tfNome.setText("");
                detalhesArea.setText("Funcionário não encontrado.");
                return;
            }

            tfNome.setText(func.getNome());

            FolhaPagamentoDAO dao = new FolhaPagamentoDAO();
            FolhaPagamento folha = dao.buscarPorFuncionarioMesAno(matricula, mes, ano);

            if (folha != null) {
                detalhesArea.setText("Folha de pagamento encontrada:\n\n");
                detalhesArea.append("Data: " + folha.getData() + "\n");
                detalhesArea.append("Salário Bruto: R$ " + folha.getSalarioBruto() + "\n\n");

                detalhesArea.append("Benefícios:\n");
                for (Beneficios b : Beneficios.values()) {
                    double valor = b.calcularValor(folha.getSalarioBruto());
                    detalhesArea.append(" - " + b.name() + " (" + b.getPercentual() + "%): R$ " + String.format("%.2f", valor) + "\n");
                }

                detalhesArea.append("\nDescontos:\n");
                for (Descontos d : Descontos.values()) {
                    double valor = d.calcularValor(folha.getSalarioBruto());
                    detalhesArea.append(" - " + d.name() + " (" + d.getPercentual() + "%): R$ " + String.format("%.2f", valor) + "\n");
                }

                detalhesArea.append("\nSalário Líquido: R$ " + String.format("%.2f", folha.getSalarioLiquido()));
            } else {
                detalhesArea.setText("Nenhuma folha encontrada para este mês/ano.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Digite valores numéricos válidos para matrícula e ano.");
        }
    }

    private void gerarFolha() {
        try {
            int matricula = Integer.parseInt(tfMatricula.getText());
            int ano = Integer.parseInt(tfAno.getText());

            FuncionarioDAO fdao = new FuncionarioDAO();
            Funcionario func = fdao.buscarPorMatricula(matricula);

            if (func == null) {
                JOptionPane.showMessageDialog(this, "Funcionário não encontrado.");
                tfNome.setText("");
                return;
            }

            tfNome.setText(func.getNome());
            int mes = Integer.parseInt((String) cbMes.getSelectedItem());

            Calendar cal = Calendar.getInstance();
            cal.set(ano, mes - 1, 1);
            Date dataFolha = cal.getTime();

            List<Beneficios> beneficios = Arrays.asList(
                Beneficios.PLANO_SAUDE, 
                Beneficios.VALE_REFEICAO, 
                Beneficios.VALE_ALIMENTACAO
            );

            List<Descontos> descontos = Arrays.asList(
                Descontos.VALE_TRANSPORTE, 
                Descontos.INSS, 
                Descontos.FGTS, 
                Descontos.FALTAS, 
                Descontos.HORAS_NEGATIVAS
            );

            FolhaPagamento folha = new FolhaPagamento(dataFolha, func, descontos, beneficios, func.getSalarioBase());
            FolhaPagamentoDAO dao = new FolhaPagamentoDAO();
            boolean sucesso = dao.inserir(folha);

            if (sucesso) {
                detalhesArea.setText("Folha gerada com sucesso para " + func.getNome() + "\n\n");
                detalhesArea.append("Salário Base: R$ " + String.format("%.2f", func.getSalarioBase()) + "\n");

                detalhesArea.append("\nBenefícios:\n");
                for (Beneficios b : beneficios) {
                    double valor = b.calcularValor(func.getSalarioBase());
                    detalhesArea.append(" - " + b.name() + " (" + b.getPercentual() + "%): R$ " + String.format("%.2f", valor) + "\n");
                }

                detalhesArea.append("\nDescontos:\n");
                for (Descontos d : descontos) {
                    double valor = d.calcularValor(func.getSalarioBase());
                    detalhesArea.append(" - " + d.name() + " (" + d.getPercentual() + "%): R$ " + String.format("%.2f", valor) + "\n");
                }

                detalhesArea.append("\nSalário Líquido: R$ " + String.format("%.2f", folha.getSalarioLiquido()));
            } else {
                detalhesArea.setText("Erro ao salvar folha no banco.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Digite valores numéricos válidos para matrícula e ano.");
        }
    }
}