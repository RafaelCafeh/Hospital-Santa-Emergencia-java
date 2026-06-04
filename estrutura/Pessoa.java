package estrutura;

public abstract class Pessoa {
    private String nome;
    private String cpf;
    private int idade;

    public Pessoa(String nome, String cpf, int idade) {
        this.nome = nome;
        this.cpf = cpf;
        this.idade = idade;
    }

    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public int getIdade() { return idade; }

    public static boolean validarCPF(String cpf) {
        if (cpf == null) return false;
        String numeros = cpf.replaceAll("[^0-9]", "");
        return numeros.length() == 11;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !(obj instanceof Pessoa)) return false;
        Pessoa outra = (Pessoa) obj;
        return this.cpf != null && this.cpf.equals(outra.cpf);
    }

    public void exibirDados() {
        System.out.println(this.toString());
    }
}
