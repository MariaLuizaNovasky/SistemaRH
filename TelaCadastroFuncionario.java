package RH;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;

public class TelaCadastroFuncionario extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4237051599414045887L;
	private JTextField tfNome, tfCpf, tfDataNasc, tfAdmissao, tfDemissao, tfCargo, tfSalario, tfSenha;
    private JComboBox<String> cbSexo, cbEstadoCivil, cbPerfil;
    private JButton btSalvar, btCancelar;

    public TelaCadastroFuncionario() {
        setTitle("Cadastro de Funcionário");
        setBounds(100, 100, 400, 450);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel lbNome = new JLabel("Nome:");
        lbNome.setBounds(20, 20, 100, 25);
        add(lbNome);
        tfNome = new JTextField();
        tfNome.setBounds(150, 20, 200, 25);
        add(tfNome);

        JLabel lbCpf = new JLabel("CPF:");
        lbCpf.setBounds(20, 55, 100, 25);
        add(lbCpf);
        tfCpf = new JTextField();
        tfCpf.setBounds(150, 55, 200, 25);
        add(tfCpf);

        JLabel lbSexo = new JLabel("Sexo:");
        lbSexo.setBounds(20, 90, 100, 25);
        add(lbSexo);
        cbSexo = new JComboBox<>(new String[]{"MASCULINO", "FEMININO", "OUTRO"});
        cbSexo.setBounds(150, 90, 200, 25);
        add(cbSexo);

        JLabel lbEstadoCivil = new JLabel("Estado Civil:");
        lbEstadoCivil.setBounds(20, 125, 100, 25);
        add(lbEstadoCivil);
        cbEstadoCivil = new JComboBox<>(new String[]{"SOLTEIRO", "CASADO", "DIVORCIADO", "VIUVO"});
        cbEstadoCivil.setBounds(150, 125, 200, 25);
        add(cbEstadoCivil);

        JLabel lbDataNasc = new JLabel("Nascimento (AAAA-MM-DD):");
        lbDataNasc.setBounds(20, 160, 200, 25);
        add(lbDataNasc);
        tfDataNasc = new JTextField();
        tfDataNasc.setBounds(220, 160, 130, 25);
        add(tfDataNasc);

        JLabel lbAdmissao = new JLabel("Admissão (AAAA-MM-DD):");
        lbAdmissao.setBounds(20, 195, 200, 25);
        add(lbAdmissao);
        tfAdmissao = new JTextField();
        tfAdmissao.setBounds(220, 195, 130, 25);
        add(tfAdmissao);

        JLabel lbDemissao = new JLabel("Demissão (se houver):");
        lbDemissao.setBounds(20, 230, 200, 25);
        add(lbDemissao);
        tfDemissao = new JTextField();
        tfDemissao.setBounds(220, 230, 130, 25);
        add(tfDemissao);

        JLabel lbCargo = new JLabel("Cargo:");
        lbCargo.setBounds(20, 265, 100, 25);
        add(lbCargo);
        tfCargo = new JTextField();
        tfCargo.setBounds(150, 265, 200, 25);
        add(tfCargo);

        JLabel lbPerfil = new JLabel("Perfil:");
        lbPerfil.setBounds(20, 300, 100, 25);
        add(lbPerfil);
        cbPerfil = new JComboBox<>(new String[]{"ADMIN", "OPERACIONAL", "USUARIO", "GESTOR"});
        cbPerfil.setBounds(150, 300, 200, 25);
        add(cbPerfil);

        JLabel lbSalario = new JLabel("Salário base:");
        lbSalario.setBounds(20, 335, 100, 25);
        add(lbSalario);
        tfSalario = new JTextField();
        tfSalario.setBounds(150, 335, 200, 25);
        add(tfSalario);

        JLabel lbSenha = new JLabel("Senha:");
        lbSenha.setBounds(20, 370, 100, 25);
        add(lbSenha);
        tfSenha = new JTextField();
        tfSenha.setBounds(150, 370, 200, 25);
        add(tfSenha);

        btSalvar = new JButton("Salvar");
        btSalvar.setBounds(80, 410, 100, 25);
        add(btSalvar);

        btCancelar = new JButton("Cancelar");
        btCancelar.setBounds(200, 410, 100, 25);
        add(btCancelar);

        definirEventos();
    }

    private void definirEventos() {
        btSalvar.addActionListener(e -> {
            try {
                String nome = tfNome.getText();
                String cpf = tfCpf.getText();
                Sexo sexo = Sexo.valueOf(cbSexo.getSelectedItem().toString());
                EstadoCivil estadoCivil = EstadoCivil.valueOf(cbEstadoCivil.getSelectedItem().toString());
                LocalDate nascimento = LocalDate.parse(tfDataNasc.getText());
                LocalDate admissao = LocalDate.parse(tfAdmissao.getText());
                String demissaoInput = tfDemissao.getText();
                LocalDate demissao = demissaoInput.isEmpty() ? null : LocalDate.parse(demissaoInput);
                String cargo = tfCargo.getText();
                Perfil perfil = Perfil.valueOf(cbPerfil.getSelectedItem().toString());
                double salario = Double.parseDouble(tfSalario.getText());
                String senha = tfSenha.getText();
                boolean status = demissao == null;

                Funcionario funcionario = new Funcionario(
                    nome, cpf, sexo, estadoCivil, nascimento,
                    admissao, demissao, cargo, perfil, salario, status
                );
                funcionario.setSenha(senha);

                FuncionarioDAO dao = new FuncionarioDAO();
                boolean sucesso = dao.salvar(funcionario);

                if (sucesso) {
                    JOptionPane.showMessageDialog(this, "Funcionário cadastrado com sucesso!\nMatrícula: " + funcionario.getMatricula());
                    dispose(); 
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao salvar funcionário.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });

        btCancelar.addActionListener(e -> dispose());
    }

    public static void abrir() {
        TelaCadastroFuncionario tela = new TelaCadastroFuncionario();
        tela.setVisible(true);
    }
}
