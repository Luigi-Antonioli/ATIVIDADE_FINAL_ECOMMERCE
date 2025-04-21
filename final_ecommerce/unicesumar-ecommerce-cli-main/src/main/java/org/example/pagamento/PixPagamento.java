package org.example.pagamento;

import java.util.UUID;

public class PixPagamento implements Pagamento {

    @Override
    public String pagar(double valor) {
        return "Pagamento confirmado com sucesso via PIX. Chave de Autenticação: 87127372-5642-7121-12345678";
    }

    @Override
    public String getTipo() {
        return "PIX";
    }
}