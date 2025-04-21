package org.example.entidades;

public class Produto {
    private int id;
    private String nome;
    private double preco;

    // Construtor com ID (usado ao buscar do banco)
    public Produto(int id, String nome, double preco) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
    }

    // Construtor sem ID (usado ao cadastrar novo produto)
    public Produto(String nome, double preco) {
        this.nome = nome;
        this.preco = preco;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }
}
