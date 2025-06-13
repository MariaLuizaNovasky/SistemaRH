package RH;

public enum Descontos {
    INSS(11.0),
    FGTS(8.0),
    FALTAS(2.0),
    HORAS_NEGATIVAS(1.0),
    VALE_TRANSPORTE(6.0);

    private final double percentual;

    Descontos(double percentual) {
        this.percentual = percentual;
    }

    public double getPercentual() {
        return percentual;
    }

    public double calcularValor(double salarioBruto) {
        return salarioBruto * (percentual / 100);
    }
}
