package org.example.pagamento;

import java.util.UUID;

public class BoletoPagamento implements Pagamento {

    @Override
    public String pagar(double valor) {
        // Lógica para gerar o pagamento com boleto
        // Simulando um processo de pagamento
        return "Pagamento confirmado com sucesso via Boleto. Código do boleto: 17654298371";
    }

    @Override
    public String getTipo() {
        return "Boleto";
    }
}