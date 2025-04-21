package org.example.pagamento;

public interface Pagamento {
    String pagar(double valor);
    String getTipo();
}