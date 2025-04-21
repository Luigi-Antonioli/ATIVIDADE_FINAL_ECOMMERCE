package org.example.pagamento;

import org.example.pagamento.BoletoPagamento;
import org.example.pagamento.CartaoCreditoPagamento;
import org.example.pagamento.Pagamento;
import org.example.pagamento.PixPagamento;

public class FormaPagamentoFactory {

    public static Pagamento criarPagamento(int tipo) {
        switch (tipo) {
            case 1:
                return new CartaoCreditoPagamento();
            case 2:
                return new BoletoPagamento();
            case 3:
                return new PixPagamento();
            default:
                return null;
        }
    }
}