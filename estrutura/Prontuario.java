package estrutura;

public class Prontuario {
    private Paciente paciente;
    private String diagnostico;
    private String observacoes;

    public Prontuario(Paciente paciente, String diagnostico, String observacoes) {
        this.paciente = paciente;
        this.diagnostico = diagnostico;
        this.observacoes = observacoes;
    }

    public Paciente getPaciente() { return paciente; }
    public String getDiagnostico() { return diagnostico; }
    public String getObservacoes() { return observacoes; }

    @Override
    public String toString() {
        return "Paciente: " + paciente.getNome() + "\nDiagnostico: " + diagnostico + "\nObservacoes: " + observacoes;
    }

    public void exibirProntuario() {
        System.out.println("\n--- PRONTUÁRIO ---");
        System.out.println("Paciente: " + paciente.getNome());
        System.out.println("CPF: " + paciente.getCpf());
        System.out.println("\nDiagnóstico:");
        System.out.println(diagnostico);
        System.out.println("\nObservações:");
        System.out.println(observacoes);
        System.out.println("------------------");
    }
}
