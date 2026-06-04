package db;

import estrutura.Consulta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de Consulta — CRUD completo via Stored Procedures MySQL.
 *
 * CREATE  → sp_inserir_consulta
 * READ    → sp_listar_consultas / sp_consultas_por_paciente
 * UPDATE  → sp_atualizar_status_consulta
 * DELETE  → sp_cancelar_consulta (soft-delete: muda status para CANCELADA)
 */
public class ConsultaDAO {

    // ----------------------------------------------------------------
    // CREATE
    // ----------------------------------------------------------------
    public void inserir(Consulta c) throws SQLException {
        String sql = "{CALL sp_inserir_consulta(?, ?, ?)}";
        try (CallableStatement cs = ConexaoDB.getConexao().prepareCall(sql)) {
            cs.setString(1, c.getPaciente().getCpf());
            cs.setString(2, c.getMedico().getCpf());
            // getData() retorna "dd/mm/aaaa" — converte para java.sql.Date
            cs.setDate  (3, parsearData(c.getData()));
            cs.execute();
            System.out.println("[DB] Consulta inserida: " + c);
        }
    }

    // ----------------------------------------------------------------
    // READ — listar todas
    // ----------------------------------------------------------------
    public List<String> listarTodas() throws SQLException {
        List<String> lista = new ArrayList<>();
        String sql = "{CALL sp_listar_consultas()}";
        try (CallableStatement cs = ConexaoDB.getConexao().prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                lista.add(formatarLinha(rs));
            }
        }
        return lista;
    }

    // ----------------------------------------------------------------
    // READ — por CPF do paciente
    // ----------------------------------------------------------------
    public List<String> listarPorPaciente(String cpf) throws SQLException {
        List<String> lista = new ArrayList<>();
        String sql = "{CALL sp_consultas_por_paciente(?)}";
        try (CallableStatement cs = ConexaoDB.getConexao().prepareCall(sql)) {
            cs.setString(1, cpf);
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    lista.add(formatarLinha(rs));
                }
            }
        }
        return lista;
    }

    // ----------------------------------------------------------------
    // UPDATE — alterar status (AGENDADA / REALIZADA / CANCELADA)
    // ----------------------------------------------------------------
    public void atualizarStatus(int id, String novoStatus) throws SQLException {
        String sql = "{CALL sp_atualizar_status_consulta(?, ?)}";
        try (CallableStatement cs = ConexaoDB.getConexao().prepareCall(sql)) {
            cs.setInt   (1, id);
            cs.setString(2, novoStatus);
            cs.execute();
            System.out.println("[DB] Consulta #" + id + " → status '" + novoStatus + "'.");
        }
    }

    // ----------------------------------------------------------------
    // DELETE (soft) — marca como CANCELADA
    // ----------------------------------------------------------------
    public void cancelar(int id) throws SQLException {
        String sql = "{CALL sp_cancelar_consulta(?)}";
        try (CallableStatement cs = ConexaoDB.getConexao().prepareCall(sql)) {
            cs.setInt(1, id);
            cs.execute();
            System.out.println("[DB] Consulta #" + id + " cancelada.");
        }
    }

    // ----------------------------------------------------------------
    // Auxiliares
    // ----------------------------------------------------------------

    /** Converte "dd/mm/aaaa" → java.sql.Date */
    private java.sql.Date parsearData(String data) {
        // data no formato dd/mm/aaaa
        String[] partes = data.split("/");
        if (partes.length == 3) {
            String iso = partes[2] + "-" + partes[1] + "-" + partes[0]; // yyyy-MM-dd
            return java.sql.Date.valueOf(iso);
        }
        // tenta interpretar diretamente (yyyy-MM-dd)
        return java.sql.Date.valueOf(data);
    }

    private String formatarLinha(ResultSet rs) throws SQLException {
        return String.format("ID=%d | %s (CPF:%s) c/ Dr(a). %s [%s] em %s — %s",
            rs.getInt   ("id"),
            rs.getString("paciente_nome"),
            rs.getString("paciente_cpf"),
            rs.getString("medico_nome"),
            rs.getString("especialidade"),
            rs.getDate  ("data"),
            rs.getString("status")
        );
    }
}