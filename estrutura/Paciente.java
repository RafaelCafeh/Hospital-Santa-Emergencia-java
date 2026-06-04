package estrutura;

public class Paciente extends Pessoa {
    private String convenio;
    private Prontuario prontuario;

    public Paciente(String nome, String cpf, int idade) {
        super(nome, cpf, idade);
    }

    public Paciente(String nome, String cpf, int idade, String convenio) {
        super(nome, cpf, idade);
        this.convenio = convenio;
    }

    public String getConvenio() { return convenio; }

    public void setProntuario(Prontuario prontuario) {
        this.prontuario = prontuario;
    }

    public Prontuario getProntuario() {
        return prontuario;
    }



    @Override
    public String toString() {
        return "Paciente: " + getNome() + " | CPF: " + getCpf() + " | Convenio: " + convenio;
    }
}
