package org.example.repositorios;

import org.example.entidades.Produto;
import org.example.entidades.Usuario;
import org.example.entidades.Venda;

import java.sql.*;
import java.util.List;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class VendaRepositorio {
    private final Connection connection;

    public VendaRepositorio(Connection connection) {
        this.connection = connection;
    }

    public void criarTabela() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS vendas (
                id TEXT PRIMARY KEY,
                usuario_email TEXT,
                produtos TEXT,
                valor_total REAL,
                forma_pagamento TEXT,
                data_hora TEXT
            );
        """;
        connection.createStatement().execute(sql);
    }

    public void salvarVenda(Venda venda) {
        String sql = "INSERT INTO vendas (id, usuario_email, produtos, valor_total, forma_pagamento, data_hora) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, venda.getId().toString());
            stmt.setString(2, venda.getUsuario().getEmail());
            stmt.setString(3, getProdutosFormatados(venda));
            stmt.setDouble(4, venda.getValorTotal());
            stmt.setString(5, venda.getFormaPagamento());
            stmt.setString(6, venda.getDataHora().toString());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar a venda: " + e.getMessage());
        }
    }

    private String getProdutosFormatados(Venda venda) {
        StringBuilder sb = new StringBuilder();
        for (Produto produto : venda.getProdutos()) {
            sb.append(produto.getNome()).append(" (R$ ").append(produto.getPreco()).append("), ");
        }
        return sb.toString().replaceAll(", $", "");
    }
}