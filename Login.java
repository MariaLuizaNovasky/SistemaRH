package RH;

import java.util.Scanner;

public class Login {

    private FuncionarioDAO funcionarioDAO;
    private Scanner scanner;

    public Login(Scanner scanner) {
        this.funcionarioDAO = new FuncionarioDAO();
        this.scanner = scanner; // Reutiliza o scanner criado no main
    }

    public boolean autenticarUsuario() {
        System.out.println("===== LOGIN =====");
        System.out.print("Usuário: ");
        String Matricula = scanner.nextLine();

        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        boolean autenticado = funcionarioDAO.autenticar(Matricula, senha);

        if (!autenticado) {
            System.out.println("Usuário ou senha inválidos!");
            return false; 
        }

        System.out.println("Login realizado com sucesso!");
        return true;
    }

    public int mostrarMenu() {
        System.out.println("\nEscolha a opção:");
        System.out.println("1 - Registrar ponto");
        System.out.println("2 - Cadastrar funcionários");
        System.out.print("Opção: ");

        int opcao = Integer.parseInt(scanner.nextLine()); // ou tratar com try-catch
        return opcao;
    }
}
