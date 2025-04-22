package org.example.repositorios;

import org.example.entidades.Produto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProdutoRepositorio {
    private final Connection connection;

    public ProdutoRepositorio(Connection connection) {
        this.connection = connection;
    }
// FUNÇÃO PARA LIMPAR AS TABELAS
    public void limparTabela() {
        try (Statement stmt = connection.createStatement()) {

            stmt.executeUpdate("DELETE FROM produtos");

            stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='produtos'");

            System.out.println("Tabela de produtos limpa e ID resetado.");
        } catch (SQLException e) {
            System.out.println("Erro ao limpar tabela de produtos: " + e.getMessage());
        }
    }

// FUNÇÃO PARA CRIAR A TABELA DO ZERO
    public void criarTabela() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS produtos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT NOT NULL,
                preco REAL NOT NULL
            )
        """;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

// FUNÇÃO QUE CADASTRA O NOVO USUÁRIO, ATRIBUINDO OS DEVIDOS VALORES
    public void cadastrar(Produto produto) {
        String sql = "INSERT INTO produtos (nome, preco) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar produto: " + e.getMessage());
        }
    }

// FUNÇÃO PARA MOSTRAR TODOS OS PRODUTOS DA TABELA
    public List<Produto> listarTodos() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Produto produto = new Produto(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getDouble("preco")
                );
                produtos.add(produto);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar produtos: " + e.getMessage());
        }
        return produtos;
    }

// FUNÇÃO QUE BUSCAR POR ID
    public Optional<Produto> buscarPorId(int id) {
        String sql = "SELECT * FROM produtos WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Produto produto = new Produto(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getDouble("preco")
                );
                return Optional.of(produto);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar produto: " + e.getMessage());
        }
        return Optional.empty();
    }
}
