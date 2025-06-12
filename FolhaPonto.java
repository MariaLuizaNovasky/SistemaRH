package RH;

import java.time.Duration;
import java.util.List;

public class FolhaPonto {
    private int id;
    private int mesReferencia;
    private int anoReferencia;
    private double horasTrabalhadas;
    private double horasExtras;
    private double horasNegativas;
    private double saldoHoras;
    private Funcionario funcionario;
	private List<RegistrarPonto> registros; 
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMesReferencia() { return mesReferencia; }
    public void setMesReferencia(int mesReferencia) { this.mesReferencia = mesReferencia; }

    public int getAnoReferencia() { return anoReferencia; }
    public void setAnoReferencia(int anoReferencia) { this.anoReferencia = anoReferencia; }

    public double getHorasTrabalhadas() { return horasTrabalhadas; }
    public void setHorasTrabalhadas(double horasTrabalhadas) { this.horasTrabalhadas = horasTrabalhadas; }

    public double getHorasExtras() { return horasExtras; }
    public void setHorasExtras(double horasExtras) { this.horasExtras = horasExtras; }

    public double getHorasNegativas() { return horasNegativas; }
    public void setHorasNegativas(double horasNegativas) { this.horasNegativas = horasNegativas; }

    public double getSaldoHoras() { return saldoHoras; }
    public void setSaldoHoras(double saldoHoras) { this.saldoHoras = saldoHoras; }

    public Funcionario getFuncionario() { return funcionario; }
    public void setFuncionario(Funcionario funcionario) { this.funcionario = funcionario; }

    public void calcularTotais(List<RegistrarPonto> registros, Duration jornadaEsperada) {
        Duration totalTrabalhada = Duration.ZERO;
        Duration totalExtras = Duration.ZERO;
        Duration totalNegativas = Duration.ZERO;

        for (RegistrarPonto rp : registros) {
            if (!rp.isPontoCompleto()) continue;

            Duration totalDia = rp.calcularTotalHorasTrabalhadas();
            Duration diferenca = rp.calcularDiferencaJornada(jornadaEsperada);

            totalTrabalhada = totalTrabalhada.plus(totalDia);

            if (diferenca.isNegative()) {
                totalNegativas = totalNegativas.plus(diferenca.abs());
            } else {
                totalExtras = totalExtras.plus(diferenca);
            }
        }

        this.horasTrabalhadas = totalTrabalhada.toMinutes() / 60.0;
        this.horasExtras = totalExtras.toMinutes() / 60.0;
        this.horasNegativas = totalNegativas.toMinutes() / 60.0;
        this.saldoHoras = this.horasExtras - this.horasNegativas;
    }

    public String getResumoPontoMensal() {
        return String.format(
            "Funcionário: %d\nMês/Ano: %02d/%d\nHoras Trabalhadas: %.2f\nHoras Extras: %.2f\nHoras Negativas: %.2f\nSaldo de Horas: %.2f",
            funcionario.getMatricula(),
            mesReferencia,
            anoReferencia,
            horasTrabalhadas,
            horasExtras,
            horasNegativas,
            saldoHoras
        );
    }
    public List<RegistrarPonto> getRegistros() {
        return registros;
    }

    public void setRegistros(List<RegistrarPonto> registros) {
        this.registros = registros;
    }
}
