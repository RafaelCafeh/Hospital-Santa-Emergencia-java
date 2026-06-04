package estrutura;

import interfaces.Autenticavel;

public class Recepcionista extends Pessoa implements Autenticavel {
    private String usuario;
    private String senha;

    public Recepcionista(String nome, String cpf, int idade, String usuario, String senha) {
        super(nome, cpf, idade);
        this.usuario = usuario;
        this.senha = senha;
    }

    public String getUsuario() { return usuario; }



    @Override
    public String toString() {
        return "Recepcionista: " + getNome() + " | Usuario: " + usuario;
    }

    @Override
    public boolean login(String usuario, String senha) {
        return this.usuario.equals(usuario) && this.senha.equals(senha);
    }
}
