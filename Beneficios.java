package RH;

public enum Beneficios {
    VALE_ALIMENTACAO(150), 
    VALE_REFEICAO(200), 
    PLANO_SAUDE(300);

    private final double valor;

    Beneficios(double valor) {
        this.valor = valor;
    }

    public double getValor() {
        return valor;
    }
}
