import java.util.List;
import java.util.Scanner;

public class Main {
    private static final int LARGURA = 62;
    private static final String LINHA_DUPLA = repetir('=', LARGURA);
    private static final String LINHA_SIMPLES = repetir('-', LARGURA);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Cenario cenario = escolherCenario(scanner);
        Cobre estoqueCobre = new Cobre(1, cenario.getEstoqueCobreInicial(), 0.5);
        GerenciadorProducao gerenciador = new GerenciadorProducao(new EstrategiaOrdemChegada(), cenario, estoqueCobre);

        boolean executando = true;
        while (executando) {
            exibirMenuPrincipal(gerenciador);
            int opcao = lerInteiro(scanner, "ESCOLHA: ");
            switch (opcao) {
                case 1:
                    menuDemandas(scanner, gerenciador);
                    break;
                case 2:
                    menuFabricacao(scanner, gerenciador);
                    break;
                case 3:
                    menuConsultas(scanner, gerenciador);
                    break;
                case 4:
                    menuCompraMateriaPrima(scanner, gerenciador);
                    break;
                case 5:
                    menuEstrategia(scanner, gerenciador);
                    break;
                case 6:
                    menuAuditoria(scanner, gerenciador);
                    break;
                case 0:
                    System.out.println("\nDesligando as maquinas e apagando as luzes da fabrica... Fechado!");
                    executando = false;
                    break;
                default:
                    opcaoInvalida();
            }
        }
        scanner.close();
    }

    private static Cenario escolherCenario(Scanner scanner) {
        System.out.println(LINHA_DUPLA);
        System.out.println(caixa(centralizar("BEM-VINDO A FABRICA DE PLACAS DE CIRCUITO")));
        System.out.println(LINHA_DUPLA);
        System.out.println("Selecione o cenario de operacao desta sessao:");
        System.out.println("  1 - IDEAL        (orcamento farto, desgaste reduzido, maquinas confiaveis)");
        System.out.println("  2 - APOCALIPTICO (orcamento restrito, desgaste acelerado, falhas frequentes)");

        Cenario escolhido = null;
        while (escolhido == null) {
            int opcao = lerInteiro(scanner, "Escolha (1 ou 2): ");
            if (opcao == 1) {
                escolhido = Cenario.IDEAL;
            } else if (opcao == 2) {
                escolhido = Cenario.APOCALIPTICO;
            } else {
                opcaoInvalida();
            }
        }
        System.out.printf("%nCenario %s ativado: budget inicial R$ %.2f | cobre inicial %.0f cm^2 | desgaste base %d ponto(s) por uso | fator de falha x%.2f%n",
                escolhido.getNome(), escolhido.getOrcamentoInicial(), escolhido.getEstoqueCobreInicial(),
                escolhido.getDesgasteMaquinaBase(), escolhido.getFatorFalha());
        return escolhido;
    }

    private static void exibirMenuPrincipal(GerenciadorProducao gerenciador) {
        System.out.println();
        System.out.println(LINHA_DUPLA);
        System.out.println(caixa(centralizar("[FABRICA DE PLACAS DE CIRCUITO - PCBs]")));
        System.out.println(LINHA_DUPLA);
        System.out.println(caixa("ESTRATEGIA ATUAL: [" + gerenciador.getNomeEstrategiaAtual() + "]"));
        System.out.println(caixa("CENARIO ATIVO:    [" + gerenciador.getCenarioAtivo().getNome() + "]"));
        System.out.println(caixa(String.format("BUDGET ATUAL:     R$ %.2f", gerenciador.getBudgetAtual())));
        System.out.println(LINHA_DUPLA);
        secao("PRODUCAO");
        System.out.println("  1 - Demandas (cadastrar, listar, cancelar)");
        System.out.println("  2 - Fabricacao (processar demandas)");
        secao("CONSULTAS");
        System.out.println("  3 - Consultar (armazem e estoque de materia-prima)");
        secao("SUPRIMENTOS");
        System.out.println("  4 - Comprar materia-prima");
        secao("ESTRATEGIA");
        System.out.println("  5 - Gerenciar estrategia de producao");
        secao("AUDITORIA");
        System.out.println("  6 - Auditoria e manutencao");
        System.out.println(LINHA_SIMPLES);
        System.out.println("  0 - SAIR");
    }

    private static void menuDemandas(Scanner scanner, GerenciadorProducao gerenciador) {
        boolean voltar = false;
        while (!voltar) {
            cabecalhoSubmenu("DEMANDAS");
            System.out.println("  1 - Cadastrar demanda");
            System.out.println("  2 - Listar demandas");
            System.out.println("  3 - Cancelar demanda");
            System.out.println("  0 - Voltar");
            int opcao = lerInteiro(scanner, "ESCOLHA: ");
            switch (opcao) {
                case 1:
                    cadastrarDemanda(scanner, gerenciador);
                    break;
                case 2:
                    listarDemandas(gerenciador.getDemandas());
                    break;
                case 3:
                    cancelarDemanda(scanner, gerenciador);
                    break;
                case 0:
                    voltar = true;
                    break;
                default:
                    opcaoInvalida();
            }
        }
    }

    private static void cadastrarDemanda(Scanner scanner, GerenciadorProducao gerenciador) {
        TipoPlaca[] tipos = TipoPlaca.values();
        System.out.println("\nTipos de placa disponiveis:");
        for (int i = 0; i < tipos.length; i++) {
            System.out.printf("  %d - %s (R$ %.2f/un, %.0f cm^2 de cobre/un)%n",
                    i + 1, tipos[i].getNome(), tipos[i].getCustoPorPlaca(), tipos[i].getCobrePorPlaca());
        }
        int tipo = lerInteiro(scanner, "Tipo de placa (0 para voltar): ");
        if (tipo == 0) {
            return;
        }
        if (tipo < 1 || tipo > tipos.length) {
            opcaoInvalida();
            return;
        }
        int quantidade = lerInteiro(scanner, "Quantidade de placas: ");
        if (quantidade <= 0) {
            System.out.println("A quantidade deve ser maior que zero.");
            return;
        }
        Demanda demanda = new Demanda(tipos[tipo - 1], quantidade);
        gerenciador.adicionarDemanda(demanda);
        System.out.println("Demanda registrada: " + demanda);
    }

    private static void listarDemandas(List<Demanda> demandas) {
        System.out.println("\n--- DEMANDAS CADASTRADAS ---");
        if (demandas.isEmpty()) {
            System.out.println("Nenhuma demanda cadastrada.");
            return;
        }
        for (int i = 0; i < demandas.size(); i++) {
            System.out.printf("  %d - %s%n", i + 1, demandas.get(i));
        }
    }

    private static void cancelarDemanda(Scanner scanner, GerenciadorProducao gerenciador) {
        List<Demanda> demandas = gerenciador.getDemandas();
        listarDemandas(demandas);
        if (demandas.isEmpty()) {
            return;
        }
        int indice = lerInteiro(scanner, "Número da demanda a cancelar (0 para voltar): ");
        if (indice == 0) {
            return;
        }
        if (indice < 1 || indice > demandas.size()) {
            opcaoInvalida();
            return;
        }
        try {
            demandas.get(indice - 1).cancelar();
            System.out.println("Demanda cancelada.");
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void menuFabricacao(Scanner scanner, GerenciadorProducao gerenciador) {
        boolean voltar = false;
        while (!voltar) {
            cabecalhoSubmenu("FABRICAÇÃO");
            System.out.println("  1 - Processar proxima demanda (usa a estrategia ativa)");
            System.out.println("  2 - Fabricar demanda específica");
            System.out.println("  0 - Voltar");
            int opcao = lerInteiro(scanner, "ESCOLHA: ");
            switch (opcao) {
                case 1:
                    System.out.println();
                    gerenciador.executarProximaProducao();
                    break;
                case 2:
                    fabricarDemandaEspecifica(scanner, gerenciador);
                    break;
                case 0:
                    voltar = true;
                    break;
                default:
                    opcaoInvalida();
            }
        }
    }

    private static void fabricarDemandaEspecifica(Scanner scanner, GerenciadorProducao gerenciador) {
        List<Demanda> demandas = gerenciador.getDemandas();
        listarDemandas(demandas);
        if (demandas.isEmpty()) {
            return;
        }
        int indice = lerInteiro(scanner, "Número da demanda para ser fabricada (0 para voltar): ");
        if (indice == 0) {
            return;
        }
        if (indice < 1 || indice > demandas.size()) {
            opcaoInvalida();
            return;
        }
        System.out.println();
        gerenciador.fabricarDemandaEspecifica(demandas.get(indice - 1));
    }

    private static void menuConsultas(Scanner scanner, GerenciadorProducao gerenciador) {
        boolean voltar = false;
        while (!voltar) {
            cabecalhoSubmenu("CONSULTAR");
            System.out.println("  1 - Ver armazem (produtos acabados)");
            System.out.println("  2 - Ver estoque de matéria-prima");
            System.out.println("  0 - Voltar");
            int opcao = lerInteiro(scanner, "ESCOLHA: ");
            switch (opcao) {
                case 1:
                    gerenciador.exibirArmazem();
                    break;
                case 2:
                    gerenciador.exibirEstoqueMateriaPrima();
                    break;
                case 0:
                    voltar = true;
                    break;
                default:
                    opcaoInvalida();
            }
        }
    }

    private static void menuCompraMateriaPrima(Scanner scanner, GerenciadorProducao gerenciador) {
        cabecalhoSubmenu("COMPRAR MATERIA-PRIMA");
        gerenciador.exibirEstoqueMateriaPrima();
        System.out.printf("Dinheiro atual: R$ %.2f%n", gerenciador.getBudgetAtual());
        double quantidade = lerDecimal(scanner, "Quantidade de cobre a comprar em cm^2 (0 para voltar): ");
        if (quantidade <= 0) {
            return;
        }
        gerenciador.comprarMateriaPrima(quantidade);
    }

    private static void menuEstrategia(Scanner scanner, GerenciadorProducao gerenciador) {
        boolean voltar = false;
        while (!voltar) {
            cabecalhoSubmenu("GERENCIAR ESTRATÉGIA");
            System.out.println("Estrategia ativa: " + gerenciador.getNomeEstrategiaAtual());
            System.out.println("  1 - Ordem de Chegada (FIFO)");
            System.out.println("  2 - Lote de Maior Demanda");
            System.out.println("  3 - Maximizar Produção de Placas");
            System.out.println("  4 - Ver próxima demanda prevista");
            System.out.println("  0 - Voltar");
            int opcao = lerInteiro(scanner, "ESCOLHA: ");
            switch (opcao) {
                case 1:
                    trocarEstrategia(gerenciador, new EstrategiaOrdemChegada());
                    break;
                case 2:
                    trocarEstrategia(gerenciador, new EstrategiaMaiorDemanda());
                    break;
                case 3:
                    trocarEstrategia(gerenciador, new EstrategiaMaxPlaca());
                    break;
                case 4:
                    exibirPrevisao(gerenciador);
                    break;
                case 0:
                    voltar = true;
                    break;
                default:
                    opcaoInvalida();
            }
        }
    }

    private static void trocarEstrategia(GerenciadorProducao gerenciador, EstrategiaProducao estrategia) {
        gerenciador.setEstrategia(estrategia);
        System.out.println("Estratégia alterada para [" + gerenciador.getNomeEstrategiaAtual() + "].");
        exibirPrevisao(gerenciador);
    }

    private static void exibirPrevisao(GerenciadorProducao gerenciador) {
        Demanda proxima = gerenciador.preverProximaDemanda();
        if (proxima == null) {
            System.out.println("Com esta estratégia, nenhuma demanda esta elegivel no momento.");
        } else {
            System.out.println("Próxima demanda prevista: " + proxima);
        }
    }

    private static void menuAuditoria(Scanner scanner, GerenciadorProducao gerenciador) {
        boolean voltar = false;
        while (!voltar) {
            cabecalhoSubmenu("AUDITORIA E MANUTENÇÃO");
            System.out.println("  1 - Relatório geral");
            System.out.println("  2 - Detalhar maquinas");
            System.out.println("  3 - Detalhar produtos");
            System.out.printf("  4 - Reparar maquinas (R$ %.2f por maquina)%n", Maquina.CUSTO_REPARO);
            System.out.println("  0 - Voltar");
            int opcao = lerInteiro(scanner, "ESCOLHA: ");
            switch (opcao) {
                case 1:
                    gerenciador.gerarAuditoriaGeral();
                    break;
                case 2:
                    gerenciador.gerarAuditoriaMaquinas();
                    break;
                case 3:
                    gerenciador.gerarAuditoriaProdutos();
                    break;
                case 4:
                    gerenciador.repararMaquinas();
                    break;
                case 0:
                    voltar = true;
                    break;
                default:
                    opcaoInvalida();
            }
        }
    }

    private static void secao(String titulo) {
        System.out.println(LINHA_SIMPLES);
        System.out.println(" [" + titulo + "]");
    }

    private static void cabecalhoSubmenu(String titulo) {
        System.out.println();
        System.out.println(LINHA_SIMPLES);
        System.out.println(" [ " + titulo + " ]");
        System.out.println(LINHA_SIMPLES);
    }

    private static void opcaoInvalida() {
        System.out.println("Opção inválida! Tente novamente.");
    }

    private static String caixa(String texto) {
        return String.format("| %-" + (LARGURA - 4) + "s |", texto);
    }

    private static String centralizar(String texto) {
        return repetir(' ', Math.max(0, (LARGURA - 4 - texto.length()) / 2)) + texto;
    }

    private static String repetir(char caractere, int vezes) {
        return new String(new char[vezes]).replace('\0', caractere);
    }

    private static String lerLinha(Scanner scanner, String mensagem) {
        System.out.print(mensagem);
        if (!scanner.hasNextLine()) {
            System.out.println("\nEntrada encerrada. Até logo!");
            System.exit(0);
        }
        return scanner.nextLine().trim();
    }

    private static int lerInteiro(Scanner scanner, String mensagem) {
        while (true) {
            String texto = lerLinha(scanner, mensagem);
            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida! Digite um número inteiro.");
            }
        }
    }

    private static double lerDecimal(Scanner scanner, String mensagem) {
        while (true) {
            String texto = lerLinha(scanner, mensagem).replace(',', '.');
            try {
                double valor = Double.parseDouble(texto);
                if (Double.isNaN(valor) || Double.isInfinite(valor)) {
                    throw new NumberFormatException();
                }
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida! Digite um número.");
            }
        }
    }
}