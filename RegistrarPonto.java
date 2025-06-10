package RH;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public class RegistrarPonto {
    private int matricula;
    private LocalDate data_registro;
    private LocalTime hora_entrada;
    private LocalTime hora_saida_almoco;
    private LocalTime hora_volta_almoco;
    private LocalTime hora_saida_final;
    private Time hora_extra;

    public RegistrarPonto(int matricula) {
        this.matricula = matricula;
        this.data_registro = LocalDate.now(); 
    }

    public int getMatricula() {
        return matricula;
    }

    public LocalDate getData_registro() {
        return data_registro;
    }

    public void setData_registro(LocalDate data_registro) {
        this.data_registro = data_registro;
    }

    public LocalTime getHora_entrada() {
        return hora_entrada;
    }

    public void setHora_entrada(LocalTime hora_entrada) {
        this.hora_entrada = hora_entrada;
    }

    public LocalTime getHora_saida_almoco() {
        return hora_saida_almoco;
    }

    public void setHora_saida_almoco(LocalTime hora_saida_almoco) {
        this.hora_saida_almoco = hora_saida_almoco;
    }

    public LocalTime getHora_volta_almoco() {
        return hora_volta_almoco;
    }

    public void setHora_volta_almoco(LocalTime hora_volta_almoco) {
        this.hora_volta_almoco = hora_volta_almoco;
    }

    public LocalTime getHora_saida_final() {
        return hora_saida_final;
    }

    public void setHora_saida_final(LocalTime hora_saida_final) {
        this.hora_saida_final = hora_saida_final;
    }

    public Time getHora_extra() {
        return hora_extra;
    }

    public void setHora_extra(Time hora_extra) {
        this.hora_extra = hora_extra;
    }

    public void registrarEntrada(LocalTime agora) {
        setHora_entrada(agora);
    }

    public void registrarSaidaAlmoco(LocalTime agora) {
        setHora_saida_almoco(agora);
    }

    public void registrarVoltaAlmoco(LocalTime agora) {
        setHora_volta_almoco(agora);
    }

    public void registrarSaidaFinal(LocalTime agora) {
        setHora_saida_final(agora);
    }


    public Duration calcularTotalHorasTrabalhadas() {
        if (isPontoCompleto()) {
            Duration manha = Duration.between(hora_entrada, hora_saida_almoco);
            Duration tarde = Duration.between(hora_volta_almoco, hora_saida_final);
            return manha.plus(tarde);
        } else {
            return Duration.ZERO;
        }
    }

    public Duration calcularDiferencaJornada(Duration jornadaEsperada) {
        return calcularTotalHorasTrabalhadas().minus(jornadaEsperada);
    }

    public boolean isPontoCompleto() {
        return hora_entrada != null &&
               hora_saida_almoco != null &&
               hora_volta_almoco != null &&
               hora_saida_final != null;
    }

    public String resumoDoDia(Duration jornadaEsperada) {
        if (!isPontoCompleto()) return "Ponto incompleto para " + data_registro;

        Duration total = calcularTotalHorasTrabalhadas();
        Duration diff = calcularDiferencaJornada(jornadaEsperada);

        String status;
        if (diff.isZero()) {
            status = "Jornada OK";
        } else if (diff.isNegative()) {
            status = "Horas faltantes: " + formatar(diff.abs());
        } else {
            status = "Horas extras: " + formatar(diff);
        }

        return String.format(
            "Data: %s\nTotal trabalhado: %s\n%s",
            data_registro,
            formatar(total),
            status
        );
    }

    private String formatar(Duration d) {
        long horas = d.toHours();
        long minutos = d.minusHours(horas).toMinutes();
        return String.format("%02dh%02dmin", horas, minutos);
    }
}
