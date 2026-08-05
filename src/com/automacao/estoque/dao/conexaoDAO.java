package com.automacao.estoque.dao;

// Aqui eu poderia fazer "import java.sql*;" pois o "*" importa todas as classes do pacote sql.
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class conexaoDAO {
    private static String URL = "jdbc:postgresql://localhost:5432/autostock";
    private static String usuario = "postgres";
    private static String senha = "brunox123";

    public static Connection abrirConexao() throws SQLException{
        return DriverManager.getConnection(URL, usuario, senha);

    }

}