package RH;

import java.time.LocalDate;
import java.util.Scanner;

public class CadastroFuncionario {

    public static void cadastrarFuncionario() {
        try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("\n=== Cadastro de Funcionário ===");

			try {
			    System.out.print("Informe o nome: ");
			    String nome = scanner.nextLine();

			    System.out.print("Informe o CPF: ");
			    String cpf = scanner.nextLine();

			    System.out.print("Informe o sexo (MASCULINO / FEMININO / OUTRO): ");
			    Sexo sexo = Sexo.valueOf(scanner.nextLine().toUpperCase());

			    System.out.print("Informe o estado civil (SOLTEIRO / CASADO / DIVORCIADO / VIUVO): ");
			    EstadoCivil estadoCivil = EstadoCivil.valueOf(scanner.nextLine().toUpperCase());

			    System.out.print("Informe a data de nascimento (AAAA-MM-DD): ");
			    LocalDate dataNascimento = LocalDate.parse(scanner.nextLine());

			    System.out.print("Informe a data de admissão (AAAA-MM-DD): ");
			    LocalDate dataAdmissao = LocalDate.parse(scanner.nextLine());

			    System.out.print("Informe a data de demissão (deixe vazio se ainda está ativo): ");
			    String demissaoInput = scanner.nextLine();
			    LocalDate dataDemissao = demissaoInput.isEmpty() ? null : LocalDate.parse(demissaoInput);

			    System.out.print("Informe o cargo: ");
			    String cargo = scanner.nextLine();

			    System.out.print("Informe o perfil (ADMIN / OPERACIONAL / USUARIO / GESTOR): ");
			    Perfil perfil = Perfil.valueOf(scanner.nextLine().toUpperCase());

			    System.out.print("Informe o salário base: ");
			    double salarioBase = Double.parseDouble(scanner.nextLine());

			    System.out.print("Informe a senha: ");
			    String senha = scanner.nextLine();

			    boolean status = (dataDemissao == null);

			    
			    Funcionario funcionario = new Funcionario(
			        nome, cpf, sexo, estadoCivil, dataNascimento,
			        dataAdmissao, dataDemissao, cargo, perfil, salarioBase, status
			    );

			    funcionario.setSenha(senha); 

			    FuncionarioDAO dao = new FuncionarioDAO();
			    boolean sucesso = dao.salvar(funcionario);

			    if (sucesso) {
			        System.out.println("\nFuncionário cadastrado com sucesso!");
			        System.out.println("Matrícula gerada: " + funcionario.getMatricula());
			    } else {
			        System.out.println("\nErro ao cadastrar funcionário.");
			    }
			} catch (Exception e) {
			    System.out.println("Erro durante o cadastro: " + e.getMessage());
			}
			
		}
    }
}
