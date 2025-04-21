package org.example.pagamento;

import java.util.UUID;

public class CartaoCreditoPagamento implements Pagamento {

    @Override
    public String pagar(double valor) {
        // Lógica para realizar o pagamento com cartão de crédito
        return "Pagamento confirmado com sucesso via Cartão de Crédito. Transação nº 71221";
    }

    @Override
    public String getTipo() {
        return "Cartão de Crédito";
    }
}