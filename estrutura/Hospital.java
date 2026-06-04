package estrutura;

import db.*;
import java.sql.SQLException;
import java.util.*;

public class Hospital {
    private String nome;
    private Recepcionista recepcionista;
    private Map<String, Prontuario> prontuarios = new HashMap<>();

    public Hospital(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setRecepcionista(Recepcionista r) {
        this.recepcionista = r;
    }

    public Recepcionista getRecepcionista() {
        return recepcionista;
    }

    // ==================== PACIENTE ====================

    public void cadastrarPaciente(Paciente p) {
        try {
            if (new PacienteDAO().buscarPorCpf(p.getCpf()) != null) {
                throw new DuplicatedCpfException(p.getCpf());
            }
            new PacienteDAO().inserir(p);
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao cadastrar paciente no banco de dados.", e);
        }
    }

    public Paciente buscarPacienteCPF(String cpf) {
        return buscarPaciente(cpf);
    }

    public Paciente buscarPaciente(String cpf) {
        try {
            Paciente p = new PacienteDAO().buscarPorCpf(cpf);
            if (p != null && prontuarios.containsKey(cpf)) {
                p.setProntuario(prontuarios.get(cpf));
            }
            return p;
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar paciente no banco de dados.", e);
        }
    }

    public List<Paciente> buscarPaciente(int idadeMinima) {
        try {
            List<Paciente> todos = new PacienteDAO().listarTodos();
            List<Paciente> filtrados = new ArrayList<>();
            for (Paciente p : todos) {
                if (p.getIdade() >= idadeMinima) {
                    if (prontuarios.containsKey(p.getCpf())) {
                        p.setProntuario(prontuarios.get(p.getCpf()));
                    }
                    filtrados.add(p);
                }
            }
            return filtrados;
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar pacientes no banco de dados.", e);
        }
    }

    public void listarPacientes() {
        List<Paciente> lista = getPacientes();
        if (lista.isEmpty()) {
            System.out.println("Nenhum paciente cadastrado.");
            return;
        }
        System.out.println("\n--- PACIENTES ---");
        for (Paciente p : lista) {
            p.exibirDados();
        }
    }

    public List<Paciente> getPacientes() {
        try {
            List<Paciente> lista = new PacienteDAO().listarTodos();
            for (Paciente p : lista) {
                if (prontuarios.containsKey(p.getCpf())) {
                    p.setProntuario(prontuarios.get(p.getCpf()));
                }
            }
            return lista;
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao listar pacientes no banco de dados.", e);
        }
    }

    // ==================== MEDICO ====================

    public void cadastrarMedico(Medico m) {
        try {
            new MedicoDAO().inserir(m);
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao cadastrar medico no banco de dados.", e);
        }
    }

    public void listarMedicos() {
        List<Medico> lista = getMedicos();
        if (lista.isEmpty()) {
            System.out.println("Nenhum medico cadastrado.");
            return;
        }
        System.out.println("\n--- MEDICOS ---");
        for (int i = 0; i < lista.size(); i++) {
            System.out.println("[" + i + "] " + lista.get(i).toString());
        }
    }

    public List<Medico> getMedicos() {
        try {
            return new MedicoDAO().listarTodos();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao listar medicos no banco de dados.", e);
        }
    }

    // ==================== ENFERMEIRO ====================

    public void cadastrarEnfermeiro(Enfermeiro e) {
        try {
            new EnfermeiroDAO().inserir(e);
        } catch (SQLException ex) {
            throw new DatabaseException("Erro ao cadastrar enfermeiro no banco de dados.", ex);
        }
    }

    public void listarEnfermeiros() {
        List<Enfermeiro> lista = getEnfermeiros();
        if (lista.isEmpty()) {
            System.out.println("Nenhum enfermeiro cadastrado.");
            return;
        }
        System.out.println("\n--- ENFERMEIROS ---");
        for (Enfermeiro e : lista) {
            e.exibirDados();
        }
    }

    public List<Enfermeiro> getEnfermeiros() {
        try {
            return new EnfermeiroDAO().listarTodos();
        } catch (SQLException ex) {
            throw new DatabaseException("Erro ao listar enfermeiros no banco de dados.", ex);
        }
    }

    // ==================== CONSULTA ====================

    public void cadastrarConsulta(Consulta c) {
        try {
            new ConsultaDAO().inserir(c);
        } catch (SQLException ex) {
            throw new DatabaseException("Erro ao cadastrar consulta no banco de dados.", ex);
        }
    }

    public void listarConsultas() {
        try {
            List<String> lista = new ConsultaDAO().listarTodas();
            if (lista.isEmpty()) {
                System.out.println("Nenhuma consulta agendada.");
                return;
            }
            System.out.println("\n--- CONSULTAS ---");
            for (String s : lista) {
                System.out.println(s);
            }
        } catch (SQLException ex) {
            throw new DatabaseException("Erro ao listar consultas no banco de dados.", ex);
        }
    }

    public boolean temConsultas() {
        try {
            return !new ConsultaDAO().listarTodas().isEmpty();
        } catch (SQLException ex) {
            throw new DatabaseException("Erro ao verificar consultas no banco de dados.", ex);
        }
    }

    public void cancelarConsulta(int id) {
        try {
            new ConsultaDAO().cancelar(id);
        } catch (SQLException ex) {
            throw new DatabaseException("Erro ao cancelar consulta no banco de dados.", ex);
        }
    }

    // ==================== FUNCIONARIOS ====================

    public void listarTodosFuncionarios() {
        List<Pessoa> funcionarios = new ArrayList<>();
        if (recepcionista != null) {
            funcionarios.add(recepcionista);
        }
        try {
            funcionarios.addAll(new MedicoDAO().listarTodos());
            funcionarios.addAll(new EnfermeiroDAO().listarTodos());
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao listar funcionarios no banco de dados.", e);
        }

        if (funcionarios.isEmpty()) {
            System.out.println("Nenhum funcionario cadastrado.");
            return;
        }

        System.out.println("\n--- TODOS OS FUNCIONARIOS (POLIMORFISMO) ---");
        for (Pessoa p : funcionarios) {
            System.out.println(p.toString());
        }
    }

    // ==================== PRONTUARIO ====================

    public void salvarProntuario(String cpf, Prontuario prontuario) {
        prontuarios.put(cpf, prontuario);
    }

    public Prontuario getProntuario(String cpf) {
        return prontuarios.get(cpf);
    }
}
