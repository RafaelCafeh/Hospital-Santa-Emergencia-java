package db;

import java.sql.*;

/**
 * Gerencia a conexão com o MySQL.
 * Coloque o MySQL Connector/J no classpath:
 * javac -cp ".;mysql-connector-j-9.x.x.jar" ...
 * java -cp ".;mysql-connector-j-9.x.x.jar" Main
 */
public class ConexaoDB {

    private static final String URL = "jdbc:mysql://localhost:3306/hospital_db?useSSL=false&serverTimezone=America/Sao_Paulo";
    private static final String USUARIO = "root"; // altere se necessário
    private static final String SENHA = "sql@123"; // altere se necessário

    private static Connection conexao;

    private ConexaoDB() {
    }

    /** Retorna (ou abre) a conexão singleton. */
    public static Connection getConexao() throws SQLException {
        if (conexao == null || conexao.isClosed()) {
            conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
        }
        return conexao;
    }

    /** Fecha a conexão ao encerrar a aplicação. */
    public static void fecharConexao() {
        if (conexao != null) {
            try {
                conexao.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }
}
