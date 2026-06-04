package db;

import estrutura.Enfermeiro;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de Enfermeiro — CRUD completo via Stored Procedures MySQL.
 *
 * CREATE  → sp_inserir_enfermeiro
 * READ    → sp_listar_enfermeiros
 * UPDATE  → sp_atualizar_enfermeiro
 * DELETE  → sp_remover_enfermeiro
 */
public class EnfermeiroDAO {

    // ----------------------------------------------------------------
    // CREATE
    // ----------------------------------------------------------------
    public void inserir(Enfermeiro e) throws SQLException {
        String sql = "{CALL sp_inserir_enfermeiro(?, ?, ?, ?)}";
        try (CallableStatement cs = ConexaoDB.getConexao().prepareCall(sql)) {
            cs.setString(1, e.getNome());
            cs.setString(2, e.getCpf());
            cs.setInt   (3, e.getIdade());
            cs.setString(4, e.getSetor());
            cs.execute();
            System.out.println("[DB] Enfermeiro '" + e.getNome() + "' inserido com sucesso.");
        }
    }

    // ----------------------------------------------------------------
    // READ — listar todos
    // ----------------------------------------------------------------
    public List<Enfermeiro> listarTodos() throws SQLException {
        List<Enfermeiro> lista = new ArrayList<>();
        String sql = "{CALL sp_listar_enfermeiros()}";
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
    public void atualizar(Enfermeiro e) throws SQLException {
        String sql = "{CALL sp_atualizar_enfermeiro(?, ?, ?, ?)}";
        try (CallableStatement cs = ConexaoDB.getConexao().prepareCall(sql)) {
            cs.setString(1, e.getCpf());
            cs.setString(2, e.getNome());
            cs.setInt   (3, e.getIdade());
            cs.setString(4, e.getSetor());
            cs.execute();
            System.out.println("[DB] Enfermeiro CPF '" + e.getCpf() + "' atualizado.");
        }
    }

    // ----------------------------------------------------------------
    // DELETE
    // ----------------------------------------------------------------
    public void remover(String cpf) throws SQLException {
        String sql = "{CALL sp_remover_enfermeiro(?)}";
        try (CallableStatement cs = ConexaoDB.getConexao().prepareCall(sql)) {
            cs.setString(1, cpf);
            cs.execute();
            System.out.println("[DB] Enfermeiro CPF '" + cpf + "' removido.");
        }
    }

    // ----------------------------------------------------------------
    // Auxiliar — mapeia ResultSet → Enfermeiro
    // ----------------------------------------------------------------
    private Enfermeiro mapear(ResultSet rs) throws SQLException {
        return new Enfermeiro(
            rs.getString("nome"),
            rs.getString("cpf"),
            rs.getInt   ("idade"),
            rs.getString("setor")
        );
    }
}