package RH;

import java.awt.event.*;
import javax.swing.*;

public class TelaLogin extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7726650488916303613L;
	private JTextField tfLogin;
	private JLabel lbSenha;
	private JLabel lbLogin;
	private JButton btLogar;
	private JButton btCancelar;
	private JPasswordField pfSenha;
	private static TelaLogin frame;
	
	public TelaLogin() {
		inicializarComponentes();
		definirEventos();
	}
	
	private void inicializarComponentes() {
	    setTitle("Login no Sistema");
	    setBounds(0, 0, 250, 200);
	    setLayout(null);  

	    tfLogin = new JTextField(5);
	    pfSenha = new JPasswordField(5);
	    lbSenha = new JLabel("Senha:");
	    lbLogin = new JLabel("Login:");
	    btLogar = new JButton("Logar");
	    btCancelar = new JButton("Cancelar");

	    tfLogin.setBounds(100, 30, 120, 25);
	    lbLogin.setBounds(30, 30, 80, 25);
	    lbSenha.setBounds(30, 75, 80, 25);
	    pfSenha.setBounds(100, 75, 120, 25);
	    btLogar.setBounds(20, 120, 100, 25);
	    btCancelar.setBounds(125, 120, 100, 25);

	    add(tfLogin);
	    add(lbSenha);
	    add(lbLogin);
	    add(btLogar);
	    add(btCancelar);
	    add(pfSenha);
	}
	private void definirEventos() {
	    btLogar.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            String login = tfLogin.getText();
	            String senha = String.valueOf(pfSenha.getPassword());

	            LoginDAO loginDAO = new LoginDAO();
	            Funcionario funcionario = loginDAO.verificarLogin(login, senha);

	            if (funcionario != null) {
	                JOptionPane.showMessageDialog(null, "Login realizado com sucesso!\nBem-vindo, " + funcionario.getNome());
	                MenuPrincipal.abrir(funcionario); 
	                
	                frame.dispose(); 
	            } else {
	                JOptionPane.showMessageDialog(null, "Login ou Senha incorretas!");
	            }


	        }
	    });

	    btCancelar.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            System.exit(0);
	        }
	    });
	}
	public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame = new TelaLogin();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null); 
                frame.setVisible(true);
            }
        });
    }
}

 


