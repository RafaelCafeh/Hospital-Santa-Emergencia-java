package estrutura;

public class Consulta {
    private Paciente paciente;
    private Medico medico;
    private String data;

    public Consulta(Paciente paciente, Medico medico, String data) {
        this.paciente = paciente;
        this.medico = medico;
        this.data = data;
    }

    public Paciente getPaciente() { return paciente; }
    public Medico getMedico() { return medico; }
    public String getData() { return data; }

    @Override
    public String toString() {
        return paciente.getNome() + " sera atendido por " + medico.getNome() + " em " + data;
    }

    public void exibirConsulta() {
        System.out.println(this.toString());
    }
}
