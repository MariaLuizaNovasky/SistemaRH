package RH;

import java.util.Date;
import java.util.List;

public class FolhaPagamento {
    private int idFolha;
    private Date data;
    private Funcionario funcionario;
    private List<Descontos> descontos;
    private List<Beneficios> beneficios;
    private double salarioBruto;
    private double salarioLiquido;

    public FolhaPagamento() {}

    public FolhaPagamento(Date data, Funcionario funcionario, List<Descontos> descontos, List<Beneficios> beneficios, double salarioBruto) {
        this.data = data;
        this.funcionario = funcionario;
        this.descontos = descontos;
        this.beneficios = beneficios;
        this.salarioBruto = salarioBruto;
        calcularSalarioLiquido();
    }

    public double calcularTotalDescontos() {
        if (descontos == null) return 0.0;
        return descontos.stream()
                .mapToDouble(d -> d.calcularValor(salarioBruto))
                .sum();
    }

    public double calcularTotalBeneficios() {
        if (beneficios == null) return 0.0;
        return beneficios.stream()
                .mapToDouble(b -> b.calcularValor(salarioBruto))
                .sum();
    }

    public void calcularSalarioLiquido() {
        this.salarioLiquido = salarioBruto + calcularTotalBeneficios() - calcularTotalDescontos();
    }

    public int getIdFolha() {
        return idFolha;
    }

    public void setIdFolha(int idFolha) {
        this.idFolha = idFolha;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public List<Descontos> getDescontos() {
        return descontos;
    }

    public void setDescontos(List<Descontos> descontos) {
        this.descontos = descontos;
        calcularSalarioLiquido(); 
    }

    public List<Beneficios> getBeneficios() {
        return beneficios;
    }

    public void setBeneficios(List<Beneficios> beneficios) {
        this.beneficios = beneficios;
        calcularSalarioLiquido(); 
    }

    public double getSalarioBruto() {
        return salarioBruto;
    }

    public void setSalarioBruto(double salarioBruto) {
        this.salarioBruto = salarioBruto;
        calcularSalarioLiquido(); 
    }

    public double getSalarioLiquido() {
        return salarioLiquido;
    }

    public void setSalarioLiquido(double salarioLiquido) {
        this.salarioLiquido = salarioLiquido;
    }
}
