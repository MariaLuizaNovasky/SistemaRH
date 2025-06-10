package RH;

public enum Descontos {
    INSS(200), FGTS(150), 
    FALTAS(100), 
    HORAS_NEGATIVAS(80), 
    VALE_TRANSPORTE(120);

    private final double valor;

    Descontos(double valor) {
        this.valor = valor;
    }

    public double getValor() {
        return valor;
    }
}