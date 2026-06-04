package db;

public class DuplicatedCpfException extends RuntimeException {
    private String cpf;

    public DuplicatedCpfException(String cpf) {
        super("CPF já cadastrado: " + cpf);
        this.cpf = cpf;
    }

    public String getCpf() {
        return cpf;
    }
}
