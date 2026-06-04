package db;

import estrutura.Paciente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de Paciente — CRUD completo via Stored Procedures MySQL.
 *
 * CREATE  → sp_inserir_paciente
 * READ    → sp_buscar_paciente_cpf / sp_listar_pacientes
 * UPDATE  → sp_atualizar_paciente
 * DELETE  → sp_remover_paciente
 */
public class PacienteDAO {

    // ----------------------------------------------------------------
    // CREATE
    // ----------------------------------------------------------------
    public void inserir(Paciente p) throws SQLException {
        String sql = "{CALL sp_inserir_paciente(?, ?, ?, ?)}";
        try (CallableStatement cs = ConexaoDB.getConexao().prepareCall(sql)) {
            cs.setString(1, p.getNome());
            cs.setString(2, p.getCpf());
            cs.setInt   (3, p.getIdade());
            cs.setString(4, p.getConvenio() != null ? p.getConvenio() : "SUS");
            cs.execute();
            System.out.println("[DB] Paciente '" + p.getNome() + "' inserido com sucesso.");
        }
    }

    // ----------------------------------------------------------------
    // READ — buscar por CPF
    // ----------------------------------------------------------------
    public Paciente buscarPorCpf(String cpf) throws SQLException {
        String sql = "{CALL sp_buscar_paciente_cpf(?)}";
        try (CallableStatement cs = ConexaoDB.getConexao().prepareCall(sql)) {
            cs.setString(1, cpf);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }
        return null;
    }

    // ----------------------------------------------------------------
    // READ — listar todos
    // ----------------------------------------------------------------
    public List<Paciente> listarTodos() throws SQLException {
        List<Paciente> lista = new ArrayList<>();
        String sql = "{CALL sp_listar_pacientes()}";
        try (CallableStatement cs = ConexaoDB.getConexao().prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    // ----------------------------------------------------------------
    // UPDATE
    // ----------------------------------------------------------------
    public void atualizar(Paciente p) throws SQLException {
        String sql = "{CALL sp_atualizar_paciente(?, ?, ?, ?)}";
        try (CallableStatement cs = ConexaoDB.getConexao().prepareCall(sql)) {
            cs.setString(1, p.getCpf());
            cs.setString(2, p.getNome());
            cs.setInt   (3, p.getIdade());
            cs.setString(4, p.getConvenio() != null ? p.getConvenio() : "SUS");
            cs.execute();
            System.out.println("[DB] Paciente CPF '" + p.getCpf() + "' atualizado.");
        }
    }

    // ----------------------------------------------------------------
    // DELETE
    // ----------------------------------------------------------------
    public void remover(String cpf) throws SQLException {
        String sql = "{CALL sp_remover_paciente(?)}";
        try (CallableStatement cs = ConexaoDB.getConexao().prepareCall(sql)) {
            cs.setString(1, cpf);
            cs.execute();
            System.out.println("[DB] Paciente CPF '" + cpf + "' removido.");
        }
    }

    // ----------------------------------------------------------------
    // Auxiliar — mapeia ResultSet → Paciente
    // ----------------------------------------------------------------
    private Paciente mapear(ResultSet rs) throws SQLException {
        return new Paciente(
            rs.getString("nome"),
            rs.getString("cpf"),
            rs.getInt   ("idade"),
            rs.getString("convenio")
        );
    }
}