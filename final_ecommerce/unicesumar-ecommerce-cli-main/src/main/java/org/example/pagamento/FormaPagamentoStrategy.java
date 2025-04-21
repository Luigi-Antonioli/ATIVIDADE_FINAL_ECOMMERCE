package org.example.pagamento;

public interface FormaPagamentoStrategy {
    String pagar(double valor);
}