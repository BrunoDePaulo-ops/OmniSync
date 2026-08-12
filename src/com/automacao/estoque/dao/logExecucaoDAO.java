package com.automacao.estoque.dao;

import com.automacao.estoque.model.logExecucao;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class logExecucaoDAO {

    public logExecucaoDAO() {}

    public void gravarLogs(logExecucao logExecucao) throws SQLException{
        String sqlGravar = "INSERT INTO registros (tipo, mensagem, detalhes, sucesso) VALUES (?, ?, ?, ?)";

        try(Connection conexao = conexaoDAO.abrirConexao(); PreparedStatement pstmt = conexao.prepareStatement(sqlGravar, Statement.RETURN_GENERATED_KEYS);){
            pstmt.setString(1, logExecucao.getTipo());
            pstmt.setString(2, logExecucao.getMensagem());
            pstmt.setString(3, logExecucao.getDetalhes());
            pstmt.setBoolean(4,logExecucao.getSucesso());
            int linhas_afetadas = pstmt.executeUpdate();

            if(linhas_afetadas > 0){
                try(ResultSet rs = pstmt.getGeneratedKeys()){
                
                    if(rs.next()){
                        long idMov = rs.getLong(1);
                        // Está setando o id diretamente na classe não no construtor.
                        logExecucao.setId(idMov);
                    }
                }
                System.out.println(" ✅ Log gravado com sucesso no disco");
            }else{
                System.out.println(" ⚠️ Nenhuma linha foi adicionada.");   
            }

        }

    }

    public List<logExecucao> listarTodos() throws SQLException{
        List<logExecucao> lista = new ArrayList<>();

        String sqlBuscar = "SELECT * FROM registros";

        try(Connection conexao = conexaoDAO.abrirConexao(); Statement stmt = conexao.createStatement()){
            
            try(ResultSet rs = stmt.executeQuery(sqlBuscar)){

                while(rs.next()){
                    logExecucao registro = new logExecucao( 
                        rs.getLong("id"),
                        rs.getString("tipo"),
                        rs.getString("mensagem"), 
                        rs.getString("detalhes"), 
                        rs.getTimestamp("data_registro").toLocalDateTime(),
                        rs.getBoolean("sucesso")
                    );
                    lista.add(registro);
                }
            }
    
        }
        return lista;
    }

}
