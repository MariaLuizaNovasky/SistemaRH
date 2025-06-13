package RH;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MenuPrincipal extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6862201990692350879L;
	private JButton btnRegistrarPonto;
    private JButton btnCadastrarFuncionario;
    private JButton btnFolhaRegistros;
    private JButton btnFolhaPagamento;
    private JButton btnSair;

    private Funcionario usuarioLogado;

    public MenuPrincipal(Funcionario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
        inicializarComponentes();
        definirEventos();
    }

    private void inicializarComponentes() {
        setTitle("Menu Principal");
        setSize(400, 300);
        setLayout(new GridLayout(6, 1, 10, 10)); // Atualizado para 6 linhas
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        btnRegistrarPonto = new JButton("Registrar ponto");
        btnCadastrarFuncionario = new JButton("Cadastrar funcionário");
        btnFolhaRegistros = new JButton("Folha de Registros");
        btnFolhaPagamento = new JButton("Folha de Pagamento"); // Nome corrigido
        btnSair = new JButton("Sair");

        add(new JLabel("Bem-vindo, " + usuarioLogado.getNome(), SwingConstants.CENTER));
        add(btnRegistrarPonto);

        if (usuarioLogado.getPerfil() == Perfil.ADMIN) {
            add(btnCadastrarFuncionario);
        }

        add(btnFolhaRegistros);
        add(btnFolhaPagamento);
        add(btnSair);
    }

    private void definirEventos() {
        btnRegistrarPonto.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TelaRegistrarPonto telaRegistro = new TelaRegistrarPonto();
                telaRegistro.setLocationRelativeTo(null);
                telaRegistro.setVisible(true);
            }
        });

        btnCadastrarFuncionario.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (usuarioLogado.getPerfil() == Perfil.ADMIN) {
                    TelaCadastroFuncionario tela = new TelaCadastroFuncionario();
                    tela.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Acesso negado! Você não tem permissão para cadastrar funcionários.");
                }
            }
        });

        btnFolhaRegistros.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TelaFolhaPonto telaFolha = new TelaFolhaPonto();
                telaFolha.setVisible(true);
            }
        });

        btnFolhaPagamento.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TelaFolhaPagamento telaFolhaPagamento = new TelaFolhaPagamento(usuarioLogado);
                telaFolhaPagamento.setLocationRelativeTo(null);
                telaFolhaPagamento.setVisible(true);
            }
        });


        btnSair.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    public static void abrir(Funcionario usuarioLogado) {
        MenuPrincipal menu = new MenuPrincipal(usuarioLogado);
        menu.setVisible(true);
    }
}

