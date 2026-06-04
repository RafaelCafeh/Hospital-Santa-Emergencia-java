package estrutura;

import interfaces.Agendavel;

public class Medico extends Pessoa implements Agendavel {
    private String especialidade;

    public Medico(String nome, String cpf, int idade) {
        super(nome, cpf, idade);
        this.especialidade = "Clinico Geral";
    }

    public Medico(String nome, String cpf, int idade, String especialidade) {
        super(nome, cpf, idade);
        this.especialidade = especialidade;
    }

    public String getEspecialidade() { return especialidade; }



    @Override
    public String toString() {
        return "Medico: " + getNome() + " | Especialidade: " + especialidade;
    }

    @Override
    public void agendarConsulta() {
        System.out.println("Consulta agendada.");
    }
}
