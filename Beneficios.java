package RH;

public enum Beneficios {
    VALE_REFEICAO(5.0),
    VALE_ALIMENTACAO(6.0),
    PLANO_SAUDE(8.0);

    private final double percentual;

    Beneficios(double percentual) {
        this.percentual = percentual;
    }

    public double getPercentual() {
        return percentual;
    }

    public double calcularValor(double salarioBruto) {
        return salarioBruto * (percentual / 100);
    }
}
