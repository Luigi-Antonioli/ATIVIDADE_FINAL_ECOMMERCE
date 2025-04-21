package org.example.entidades;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Venda {
    private UUID id;
    private Usuario usuario;
    private List<Produto> produtos;
    private double valorTotal;
    private String formaPagamento;
    private LocalDateTime dataHora;

    public Venda(Usuario usuario, List<Produto> produtos, double valorTotal, String formaPagamento) {
        this.id = UUID.randomUUID();
        this.usuario = usuario;
        this.produtos = produtos;
        this.valorTotal = valorTotal;
        this.formaPagamento = formaPagamento;
        this.dataHora = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }
}