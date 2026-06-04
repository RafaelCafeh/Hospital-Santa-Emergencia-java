package estrutura;

public class Enfermeiro extends Pessoa {
    private String setor;

    public Enfermeiro(String nome, String cpf, int idade, String setor) {
        super(nome, cpf, idade);
        this.setor = setor;
    }

    public String getSetor() { return setor; }



    @Override
    public String toString() {
        return "Enfermeiro: " + getNome() + " | Setor: " + setor;
    }
}
