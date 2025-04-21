package org.example;

import org.example.pagamento.FormaPagamentoFactory;
import org.example.pagamento.Pagamento;
import org.example.repositorios.ProdutoRepositorio;
import org.example.repositorios.UsuarioRepositorio;
import org.example.repositorios.VendaRepositorio;
import org.example.entidades.Produto;
import org.example.entidades.Usuario;
import org.example.entidades.Venda;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        String url = "jdbc:sqlite:ecommerce.db";

        try (Connection connection = DriverManager.getConnection(url)) {
            UsuarioRepositorio usuarioRepo = new UsuarioRepositorio(connection);
            ProdutoRepositorio produtoRepo = new ProdutoRepositorio(connection);
            VendaRepositorio vendaRepo = new VendaRepositorio(connection);

            usuarioRepo.criarTabela();
            produtoRepo.criarTabela();
            vendaRepo.criarTabela();

            Scanner scanner = new Scanner(System.in);
            int opcao;

            do {
                System.out.println("\n===== MENU =====");
                System.out.println("1. Cadastrar Usuário");
                System.out.println("2. Cadastrar Produto");
                System.out.println("3. Listar Usuários");
                System.out.println("4. Listar Produtos");
                System.out.println("5. Registrar Venda");
                System.out.println("6. Limpar Tabelas");
                System.out.println("7. Sair");
                System.out.print("Escolha uma opção: ");
                opcao = Integer.parseInt(scanner.nextLine());

                switch (opcao) {
                    case 1 -> {
                        System.out.print("Nome: ");
                        String nome = scanner.nextLine();
                        System.out.print("Email: ");
                        String email = scanner.nextLine();
                        usuarioRepo.cadastrar(new Usuario(nome, email));
                        System.out.println("Usuário cadastrado com sucesso!");
                    }
                    case 2 -> {
                        System.out.print("Nome do Produto: ");
                        String nome = scanner.nextLine();
                        System.out.print("Preço: ");
                        double preco = Double.parseDouble(scanner.nextLine());
                        produtoRepo.cadastrar(new Produto(nome, preco));
                        System.out.println("Produto cadastrado com sucesso!");
                    }
                    case 3 -> {
                        System.out.println("=== Usuários Cadastrados ===");
                        usuarioRepo.listarTodos().forEach(u ->
                                System.out.println("- " + u.getNome() + " (" + u.getEmail() + ")"));
                    }
                    case 4 -> {
                        System.out.println("=== Produtos Cadastrados ===");
                        produtoRepo.listarTodos().forEach(p ->
                                System.out.println("- [" + p.getId() + "] " + p.getNome() + " - R$" + p.getPreco()));
                    }
                    case 5 -> registrarVenda(scanner, usuarioRepo, produtoRepo, vendaRepo);
                    case 6 -> {
                        System.out.println("Tem certeza que deseja limpar todas as tabelas? (S/N)");
                        String confirmacao = scanner.nextLine().toUpperCase();
                        if (confirmacao.equals("S")) {
                            try {
                                usuarioRepo.limparTabela();
                                produtoRepo.limparTabela();
                                System.out.println("Tabelas limpas com sucesso!");
                            } catch (SQLException e) {
                                System.out.println("Erro ao limpar as tabelas: " + e.getMessage());
                            }
                        } else {
                            System.out.println("Operação cancelada.");
                        }
                    }
                    case 7 -> {
                        System.out.println("Saindo do sistema...");
                        return;  // Encerra o programa
                    }
                    default -> System.out.println("Opção inválida. Tente novamente.");
                }
            } while (opcao != 7); // O loop continuará até que o usuário escolha sair (opção 7)

        } catch (SQLException e) {
            System.out.println("Erro no banco de dados: " + e.getMessage());
        }
    }

    public static void registrarVenda(Scanner scanner, UsuarioRepositorio usuarioRepo,
                                      ProdutoRepositorio produtoRepo, VendaRepositorio vendaRepo) throws SQLException {
        System.out.print("Digite o Email do usuário: ");
        String email = scanner.nextLine();
        Optional<Usuario> usuarioOpt = usuarioRepo.buscarPorEmail(email);

        if (usuarioOpt.isEmpty()) {
            System.out.println("Usuário não encontrado.");
            return;
        }

        Usuario usuario = usuarioOpt.get();
        System.out.println("Usuário encontrado: " + usuario.getNome());

        System.out.print("Digite os IDs dos produtos (separados por vírgula): ");
        String[] ids = scanner.nextLine().split(",");

        List<Produto> produtosSelecionados = new ArrayList<>();
        for (String idStr : ids) {
            try {
                int id = Integer.parseInt(idStr.trim());
                Optional<Produto> produtoOpt = produtoRepo.buscarPorId(id);
                produtoOpt.ifPresentOrElse(produtosSelecionados::add,
                        () -> System.out.println("Produto com ID " + id + " não encontrado."));
            } catch (NumberFormatException e) {
                System.out.println("ID inválido: " + idStr);
            }
        }

        if (produtosSelecionados.isEmpty()) {
            System.out.println("Nenhum produto válido selecionado. Venda cancelada.");
            return;
        }

        System.out.println("Produtos encontrados:");
        double total = 0;
        for (Produto p : produtosSelecionados) {
            System.out.println("- " + p.getNome() + " (R$ " + p.getPreco() + ")");
            total += p.getPreco();
        }

        System.out.println("\nEscolha a forma de pagamento:");
        System.out.println("1 - Cartão de Crédito");
        System.out.println("2 - Boleto");
        System.out.println("3 - PIX");
        System.out.print("Opção: ");
        int opcaoPagamento = Integer.parseInt(scanner.nextLine());

        Pagamento pagamento = FormaPagamentoFactory.criarPagamento(opcaoPagamento);
        if (pagamento == null) {
            System.out.println("Forma de pagamento inválida. Venda cancelada.");
            return;
        }

        System.out.println("Aguarde, efetuando pagamento...");
        String comprovante = pagamento.pagar(total);
        System.out.println("Pagamento confirmado com sucesso: " + comprovante);

        Venda venda = new Venda(usuario, produtosSelecionados, total, pagamento.getTipo());
        vendaRepo.salvarVenda(venda);

        System.out.println("\nResumo da venda:");
        System.out.println("Cliente: " + usuario.getNome());
        for (Produto p : produtosSelecionados) {
            System.out.println("- " + p.getNome());
        }
        System.out.println("Valor total: R$ " + total);
        System.out.println("Pagamento: " + pagamento.getTipo());
        System.out.println("Venda registrada com sucesso!");
    }
}
