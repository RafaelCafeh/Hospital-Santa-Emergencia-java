package db;

import estrutura.Medico;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de Médico — CRUD completo via Stored Procedures MySQL.
 *
 * CREATE  → sp_inserir_medico
 * READ    → sp_listar_medicos
 * UPDATE  → sp_atualizar_medico
 * DELETE  → sp_remover_medico
 */
public class MedicoDAO {

    // ----------------------------------------------------------------
    // CREATE
    // ----------------------------------------------------------------
    public void inserir(Medico m) throws SQLException {
        String sql = "{CALL sp_inserir_medico(?, ?, ?, ?)}";
        try (CallableStatement cs = ConexaoDB.getConexao().prepareCall(sql)) {
            cs.setString(1, m.getNome());
            cs.setString(2, m.getCpf());
            cs.setInt   (3, m.getIdade());
            cs.setString(4, m.getEspecialidade());
            cs.execute();
            System.out.println("[DB] Médico '" + m.getNome() + "' inserido com sucesso.");
        }
    }

    // ----------------------------------------------------------------
    // READ — listar todos
    // ----------------------------------------------------------------
    public List<Medico> listarTodos() throws SQLException {
        List<Medico> lista = new ArrayList<>();
        String sql = "{CALL sp_listar_medicos()}";
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
    public void atualizar(Medico m) throws SQLException {
        String sql = "{CALL sp_atualizar_medico(?, ?, ?, ?)}";
        try (CallableStatement cs = ConexaoDB.getConexao().prepareCall(sql)) {
            cs.setString(1, m.getCpf());
            cs.setString(2, m.getNome());
            cs.setInt   (3, m.getIdade());
            cs.setString(4, m.getEspecialidade());
            cs.execute();
            System.out.println("[DB] Médico CPF '" + m.getCpf() + "' atualizado.");
        }
    }

    // ----------------------------------------------------------------
    // DELETE
    // ----------------------------------------------------------------
    public void remover(String cpf) throws SQLException {
        String sql = "{CALL sp_remover_medico(?)}";
        try (CallableStatement cs = ConexaoDB.getConexao().prepareCall(sql)) {
            cs.setString(1, cpf);
            cs.execute();
            System.out.println("[DB] Médico CPF '" + cpf + "' removido.");
        }
    }

    // ----------------------------------------------------------------
    // Auxiliar — mapeia ResultSet → Medico
    // ----------------------------------------------------------------
    private Medico mapear(ResultSet rs) throws SQLException {
        return new Medico(
            rs.getString("nome"),
            rs.getString("cpf"),
            rs.getInt   ("idade"),
            rs.getString("especialidade")
        );
    }
}