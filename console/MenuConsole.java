package console;

import java.util.List;
import java.util.Scanner;
import estrutura.*;
import interfaces.Autenticavel;
import db.DatabaseException;
import db.DuplicatedCpfException;

public class MenuConsole {
    private Hospital hospital;
    private Scanner sc;

    public MenuConsole(Hospital hospital) {
        this.hospital = hospital;
        this.sc = new Scanner(System.in);
    }

    private static final int W = 52;
    private static final String TOPO = "+" + "-".repeat(W - 2) + "+";
    private static final String SEP  = "|" + "=".repeat(W - 2) + "|";
    private static final String LAT  = "|";

    private void titulo(String texto) {
        System.out.println("\n--- " + texto + " ---");
    }

    private void sucesso(String msg) {
        System.out.println(msg);
    }

    private void erro(String msg) {
        System.out.println("(ERRO) " + msg);
    }

    private void printLinha(String texto) {
        System.out.printf("%s  %-46s  %s%n", LAT, texto, LAT);
    }

    public void iniciar() {
        System.out.println("\n=== HOSPITAL SANTA EMERGENCIA ===");
        System.out.println("Aqui sua saude e nossa prioridade!");

        int opcao = -1;
        do {
            System.out.println();
            System.out.println(TOPO);
            printLinha("MENU PRINCIPAL");
            System.out.println(SEP);
            printLinha("1 - Cadastrar Paciente");
            printLinha("2 - Buscar Paciente");
            printLinha("3 - Listar Pacientes");
            printLinha("4 - Cadastrar Medico");
            printLinha("5 - Listar Medicos");
            printLinha("6 - Cadastrar Enfermeiro");
            printLinha("7 - Listar Enfermeiros");
            printLinha("8 - Agendar Consulta");
            printLinha("9 - Listar Consultas");
            printLinha("10 - Cancelar Consulta");
            printLinha("11 - Criar Prontuario");
            printLinha("12 - Ver Prontuario");
            printLinha("13 - Listar Funcionarios");
            System.out.println(SEP);
            printLinha("0 - Sair");
            System.out.println(TOPO);
            System.out.print("Escolha uma opcao: ");

            try {
                if (!sc.hasNextLine()) {
                    System.out.println("Saindo...");
                    break;
                }
                opcao = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                erro("Opcao invalida. Digite um numero.");
                continue;
            }

            try {
                switch (opcao) {
                    case 1:  cadastrarPaciente();          break;
                    case 2:  buscarPaciente();             break;
                    case 3:  hospital.listarPacientes();   break;
                    case 4:  cadastrarMedico();            break;
                    case 5:  hospital.listarMedicos();     break;
                    case 6:  cadastrarEnfermeiro();        break;
                    case 7:  hospital.listarEnfermeiros(); break;
                    case 8:  agendarConsulta();            break;
                    case 9:  hospital.listarConsultas();   break;
                    case 10: cancelarConsulta();           break;
                    case 11: criarProntuario();            break;
                    case 12: verProntuario();              break;
                    case 13: hospital.listarTodosFuncionarios(); break;
                    case 0:  System.out.println("Saindo...");   break;
                    default: erro("Opcao incorreta.");
                }
            } catch (DatabaseException e) {
                erro("Erro no banco de dados. Verifique a conexao.");
            }
        } while (opcao != 0);
    }

    // --- Login ---

    private boolean realizarLogin() {
        titulo("LOGIN");
        for (int tentativa = 1; tentativa <= 3; tentativa++) {
            System.out.print("Usuario: ");
            String usuario = sc.nextLine().trim();
            System.out.print("Senha: ");
            String senha = sc.nextLine().trim();

            Autenticavel auth = hospital.getRecepcionista();
            if (auth.login(usuario, senha)) {
                return true;
            }
            erro("Credenciais invalidas. Tentativa " + tentativa + "/3");
        }
        return false;
    }

    // --- Paciente ---

    private void cadastrarPaciente() {
        titulo("CADASTRO DE PACIENTE");

        String nome = lerCampoObrigatorio("Nome");
        if (nome == null) return;

        String cpf = lerCampoObrigatorio("CPF");
        if (cpf == null) return;
        if (!Pessoa.validarCPF(cpf)) {
            erro("CPF invalido. Deve conter 11 digitos numericos.");
            return;
        }

        int idade = lerIdade();
        if (idade == -1) return;

        System.out.print("Convenio (Enter para SUS): ");
        String convenio = sc.nextLine().trim();
        if (convenio.isEmpty()) convenio = "SUS";

        Paciente novoPaciente = new Paciente(nome, cpf, idade, convenio);
        try {
            hospital.cadastrarPaciente(novoPaciente);
            sucesso("Paciente cadastrado com sucesso!");
        } catch (DuplicatedCpfException e) {
            erro("CPF ja cadastrado no sistema.");
        } catch (DatabaseException e) {
            erro("Erro no banco de dados. Verifique a conexao.");
        }
    }

    private void buscarPaciente() {
        titulo("BUSCAR PACIENTE");
        System.out.print("CPF: ");
        String cpf = sc.nextLine().trim();
        Paciente p = hospital.buscarPacienteCPF(cpf);
        if (p != null) {
            p.exibirDados();
        } else {
            erro("Paciente nao encontrado.");
        }
    }

    // --- Medico ---

    private void cadastrarMedico() {
        titulo("CADASTRO DE MEDICO");

        String nome = lerCampoObrigatorio("Nome");
        if (nome == null) return;

        String cpf = lerCampoObrigatorio("CPF");
        if (cpf == null) return;
        if (!Pessoa.validarCPF(cpf)) {
            erro("CPF invalido. Deve conter 11 digitos numericos.");
            return;
        }

        int idade = lerIdade();
        if (idade == -1) return;

        String especialidade = lerCampoObrigatorio("Especialidade");
        if (especialidade == null) return;

        Medico novoMedico = new Medico(nome, cpf, idade, especialidade);
        hospital.cadastrarMedico(novoMedico);
        sucesso("Medico cadastrado com sucesso!");
    }

    // --- Enfermeiro ---

    private void cadastrarEnfermeiro() {
        titulo("CADASTRO DE ENFERMEIRO");

        String nome = lerCampoObrigatorio("Nome");
        if (nome == null) return;

        String cpf = lerCampoObrigatorio("CPF");
        if (cpf == null) return;
        if (!Pessoa.validarCPF(cpf)) {
            erro("CPF invalido. Deve conter 11 digitos numericos.");
            return;
        }

        int idade = lerIdade();
        if (idade == -1) return;

        String setor = lerCampoObrigatorio("Setor");
        if (setor == null) return;

        Enfermeiro novoEnfermeiro = new Enfermeiro(nome, cpf, idade, setor);
        hospital.cadastrarEnfermeiro(novoEnfermeiro);
        sucesso("Enfermeiro cadastrado com sucesso!");
    }

    // --- Consulta ---

    private void agendarConsulta() {
        titulo("AGENDAR CONSULTA");

        if (hospital.getPacientes().isEmpty()) {
            erro("Nenhum paciente cadastrado. Cadastre um paciente primeiro.");
            return;
        }

        List<Medico> medicos = hospital.getMedicos();
        if (medicos.isEmpty()) {
            erro("Nenhum medico cadastrado. Cadastre um medico primeiro.");
            return;
        }

        System.out.print("CPF do paciente: ");
        String cpf = sc.nextLine().trim();
        Paciente paciente = hospital.buscarPacienteCPF(cpf);
        if (paciente == null) {
            erro("Paciente nao encontrado.");
            return;
        }

        System.out.println("Medicos disponiveis:");
        for (int i = 0; i < medicos.size(); i++) {
            System.out.println("[" + i + "] " + medicos.get(i).getNome() + " - " + medicos.get(i).getEspecialidade());
        }

        System.out.print("Numero do medico: ");
        try {
            int indiceMedico = Integer.parseInt(sc.nextLine().trim());
            if (indiceMedico < 0 || indiceMedico >= medicos.size()) {
                erro("Indice invalido.");
                return;
            }
            Medico medico = medicos.get(indiceMedico);

            String data = lerCampoObrigatorio("Data (dd/mm/aaaa)");
            if (data == null) return;

            Consulta consulta = new Consulta(paciente, medico, data);
            hospital.cadastrarConsulta(consulta);
            medico.agendarConsulta();
            sucesso("Consulta agendada com sucesso!");
        } catch (NumberFormatException e) {
            erro("Digite um numero valido.");
        }
    }

    private void cancelarConsulta() {
        if (!hospital.temConsultas()) {
            System.out.println("Nenhuma consulta agendada.");
            return;
        }

        System.out.println();
        hospital.listarConsultas();
        System.out.print("ID da consulta a cancelar: ");
        try {
            int id = Integer.parseInt(sc.nextLine().trim());
            hospital.cancelarConsulta(id);
            sucesso("Consulta cancelada com sucesso!");
        } catch (NumberFormatException e) {
            erro("Digite um numero valido.");
        }
    }

    // --- Prontuario ---

    private void criarProntuario() {
        titulo("CRIAR PRONTUARIO");

        System.out.print("CPF do paciente: ");
        String cpf = sc.nextLine().trim();
        Paciente paciente = hospital.buscarPacienteCPF(cpf);
        if (paciente == null) {
            erro("Paciente nao encontrado.");
            return;
        }

        String diagnostico = lerCampoObrigatorio("Diagnostico");
        if (diagnostico == null) return;

        String observacoes = lerCampoObrigatorio("Observacoes");
        if (observacoes == null) return;

        Prontuario prontuario = new Prontuario(paciente, diagnostico, observacoes);
        hospital.salvarProntuario(cpf, prontuario);
        sucesso("Prontuario criado com sucesso!");
    }

    private void verProntuario() {
        titulo("VER PRONTUARIO");

        System.out.print("CPF do paciente: ");
        String cpf = sc.nextLine().trim();
        Paciente paciente = hospital.buscarPacienteCPF(cpf);
        if (paciente == null) {
            erro("Paciente nao encontrado.");
            return;
        }

        Prontuario p = hospital.getProntuario(cpf);
        if (p == null) {
            erro("Paciente nao possui prontuario.");
            return;
        }

        p.exibirProntuario();
    }

    // --- Utilitarios ---

    private int lerIdade() {
        while (true) {
            System.out.print("Idade: ");
            try {
                if (!sc.hasNextLine()) {
                    erro("Cadastro cancelado.");
                    return -1;
                }
                int idade = Integer.parseInt(sc.nextLine().trim());
                if (idade <= 0) {
                    erro("Idade deve ser maior que zero.");
                } else {
                    return idade;
                }
            } catch (NumberFormatException e) {
                erro("Idade invalida. Digite um numero.");
            }
        }
    }

    private String lerCampoObrigatorio(String campo) {
        while (true) {
            System.out.print(campo + ": ");
            if (!sc.hasNextLine()) {
                erro("Cadastro cancelado.");
                return null;
            }

            String valor = sc.nextLine().trim();
            if (!valor.isEmpty()) {
                return valor;
            }

            erro(campo + " nao pode ficar vazio.");
        }
    }
}
