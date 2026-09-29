import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GerenciadorProducao {
    private EstrategiaProducao estrategiaAtual;
    private final Cenario cenarioAtivo;
    private final Cobre estoqueCobre;
    private double budgetAtual;
    private int contadorLotes = 0;
    private int contadorPlacas = 0;

    private final List<Demanda> demandas = new ArrayList<>();
    private final List<PlacaCircuito> armazem = new ArrayList<>();
    private final List<Maquina> maquinas = new ArrayList<>();

    public GerenciadorProducao(EstrategiaProducao estrategia, Cenario cenario, Cobre estoqueCobre) {
        if (estrategia == null) {
            throw new IllegalArgumentException("A estratégia inicial e obrigatória.");
        }
        this.estrategiaAtual = estrategia;
        this.cenarioAtivo = cenario;
        this.estoqueCobre = estoqueCobre;
        this.budgetAtual = cenario.getOrcamentoInicial();
        maquinas.add(new MaquinaLaminadora("Laminadora", cenario));
        maquinas.add(new MaquinaSoldagem("Soldadora", cenario));
        maquinas.add(new MaquinaInspecaoOptica("Inspetor", cenario));
    }

    public void setEstrategia(EstrategiaProducao novaEstrategia) {
        if (novaEstrategia == null) {
            throw new IllegalArgumentException("A estrategia nao pode ser nula.");
        }
        this.estrategiaAtual = novaEstrategia;
    }

    public String getNomeEstrategiaAtual() {
        return estrategiaAtual.getNomeEstrategia();
    }

    public Cenario getCenarioAtivo() {
        return cenarioAtivo;
    }

    public double getBudgetAtual() {
        return budgetAtual;
    }

    public void adicionarDemanda(Demanda demanda) {
        demandas.add(demanda);
    }

    public List<Demanda> getDemandas() {
        return Collections.unmodifiableList(demandas);
    }

    public Demanda preverProximaDemanda() {
        return estrategiaAtual.selecionarDemanda(getDemandas(), budgetAtual);
    }

    public void executarProximaProducao() {
        Demanda demanda = preverProximaDemanda();
        if (demanda == null) {
            System.out.println("Nenhuma demanda elegivel para a estratégia [" + getNomeEstrategiaAtual() + "] com o orcamento atual.");
            return;
        }
        System.out.println("Estratégia [" + getNomeEstrategiaAtual() + "] escolheu: " + demanda);
        processarDemanda(demanda);
    }

    public void fabricarDemandaEspecifica(Demanda demanda) {
        if (!demanda.isElegivel()) {
            System.out.println("Esta demanda nao pode ser fabricada (status: " + demanda.getStatus().getDescricao() + ").");
            return;
        }
        processarDemanda(demanda);
    }

    private void processarDemanda(Demanda demanda) {
        int total = demanda.getQuantidadeRestante();
        int produzidas = 0;
        int lote = 0;
        int tempoTotal = 0;

        if (linhaParada()) {
            return;
        }
        System.out.println("A linha de montagem foi iniciada! Iniciando: " + demanda.getTipo().getNome());
        demanda.iniciarProducao();

        while (demanda.getQuantidadeRestante() > 0) {
            if (linhaParada()) {
                demanda.interromperProducao();
                break;
            }
            String falta = verificarFaltaDeRecursos(demanda);
            if (falta != null) {
                demanda.cancelar();
                System.out.println("Demanda cancelada: " + falta + ".");
                break;
            }
            if (lote == 0) {
                lote = ++contadorLotes;
            }
            produzidas++;
            tempoTotal += produzirUnidade(demanda, lote, produzidas, total);
        }

        if (produzidas > 0) {
            System.out.printf("Resultado: %d placa(s) guardada(s) no armazem (lote #%d) | tempo de linha: %d s%n",
                    produzidas, lote, tempoTotal);
        }
        if (demanda.getStatus() == StatusDemanda.CONCLUIDA) {
            System.out.println("Fim de turno: demanda concluida com sucesso!");
        }
    }

    private int produzirUnidade(Demanda demanda, int lote, int numero, int total) {
        PlacaCircuito placa = demanda.getTipo().criarPlaca(++contadorPlacas, lote);
        budgetAtual -= demanda.getCustoPorPlaca();
        estoqueCobre.consumir(demanda.getCobrePorPlaca());

        System.out.printf("  [%d/%d] ", numero, total);
        placa.processar();
        for (Maquina maquina : maquinas) {
            maquina.operar(placa);
        }

        armazem.add(placa);
        demanda.registrarProducao(1);
        return placa.calcularTempoProducao();
    }

    private boolean linhaParada() {
        for (Maquina maquina : maquinas) {
            if (maquina.estaQuebrada()) {
                System.out.println("A linha parou: " + maquina.getNome() + " quebrou! Repare em Auditoria > Reparar maquinas.");
                return true;
            }
        }
        return false;
    }

    private String verificarFaltaDeRecursos(Demanda demanda) {
        if (budgetAtual < demanda.getCustoPorPlaca()) {
            return "orçamento insuficiente";
        }
        if (!estoqueCobre.verificarDisponibilidade(demanda.getCobrePorPlaca())) {
            return "cobre insuficiente no estoque";
        }
        return null;
    }

    public boolean comprarMateriaPrima(double quantidade) {
        if (quantidade <= 0) {
            System.out.println("Quantidade inválida.");
            return false;
        }
        double custo = quantidade * estoqueCobre.getCustoPorUnidade();
        if (custo > budgetAtual) {
            System.out.printf("Orçamento insuficiente: a compra custa R$ %.2f e o dinheiro atual é R$ %.2f.%n", custo, budgetAtual);
            return false;
        }
        budgetAtual -= custo;
        estoqueCobre.adicionarEstoque(quantidade);
        System.out.printf("Compra realizada: %.1f %s de cobre por R$ %.2f.%n", quantidade, estoqueCobre.getUnidade(), custo);
        return true;
    }

    public void repararMaquinas() {
        int necessitam = 0;
        int reparadas = 0;
        for (Maquina maquina : maquinas) {
            if (!maquina.precisaManutencao()) {
                continue;
            }
            necessitam++;
            if (budgetAtual < Maquina.CUSTO_REPARO) {
                System.out.println("Orçamento insuficiente para reparar " + maquina.getNome() + ".");
                continue;
            }
            budgetAtual -= Maquina.CUSTO_REPARO;
            maquina.reparar();
            reparadas++;
            System.out.printf("Reparo concluido: %s voltou a 100%% de saúde (R$ %.2f).%n", maquina.getNome(), Maquina.CUSTO_REPARO);
        }
        if (necessitam == 0) {
            System.out.println("Todas as maquinas em bom estado. Nenhum reparo necessario.");
        } else {
            System.out.println("Reparos realizados: " + reparadas + " de " + necessitam + ".");
        }
    }

    public void exibirEstoqueMateriaPrima() {
        System.out.println("\n--- ESTOQUE DE MATÉRIA-PRIMA ---");
        System.out.println(estoqueCobre);
    }

    public void exibirArmazem() {
        System.out.println("\n--- ARMAZEM DE PRODUTOS ACABADOS ---");
        if (armazem.isEmpty()) {
            System.out.println("Armazem vazio. Nenhum produto acabado em estoque.");
            return;
        }

        Map<Integer, List<PlacaCircuito>> lotes = new LinkedHashMap<>();
        Map<TipoPlaca, Integer> totaisPorTipo = new EnumMap<>(TipoPlaca.class);
        for (PlacaCircuito placa : armazem) {
            lotes.computeIfAbsent(placa.getLote(), chave -> new ArrayList<>()).add(placa);
            totaisPorTipo.merge(placa.getTipo(), 1, Integer::sum);
        }

        System.out.println(String.format("%-6s %-22s %5s %11s %9s  %s", "LOTE", "PRODUTO", "QTD", "QUALIDADE", "EM RISCO", "STATUS"));
        for (Map.Entry<Integer, List<PlacaCircuito>> entrada : lotes.entrySet()) {
            List<PlacaCircuito> placas = entrada.getValue();
            double somaQualidade = 0;
            int emRisco = 0;
            for (PlacaCircuito placa : placas) {
                somaQualidade += placa.getQualidade();
                if (placa.precisaManutencao()) {
                    emRisco++;
                }
            }
            System.out.println(String.format("#%-5d %-22s %5d %10.1f%% %9d  %s",
                    entrada.getKey(), placas.get(0).getNome(), placas.size(),
                    somaQualidade / placas.size() * 100, emRisco,
                    emRisco > 0 ? "RISCO ALTO" : "RISCO BAIXO"));
        }

        System.out.println("\nTotal por tipo:");
        for (Map.Entry<TipoPlaca, Integer> entrada : totaisPorTipo.entrySet()) {
            System.out.println("  " + entrada.getKey().getNome() + ": " + entrada.getValue() + " unidade(s)");
        }
        System.out.println("Total no armazem: " + armazem.size() + " placa(s)");
    }

    public void gerarAuditoriaGeral() {
        List<Auditavel> todos = new ArrayList<>(maquinas);
        todos.addAll(armazem);
        imprimirAuditoria("RELATÓRIO DE AUDITORIA GERAL", todos);
    }

    public void gerarAuditoriaMaquinas() {
        imprimirAuditoria("AUDITÓRIA DAS MAQUINAS", maquinas);
    }

    public void gerarAuditoriaProdutos() {
        imprimirAuditoria("AUDITÓRIA DOS PRODUTOS", armazem);
    }

    private void imprimirAuditoria(String titulo, List<? extends Auditavel> itens) {
        System.out.println("\n--- " + titulo + " ---");
        if (itens.isEmpty()) {
            System.out.println("Nenhum componente para auditar.");
            return;
        }
        int alertas = 0;
        for (Auditavel item : itens) {
            boolean atencao = item.precisaManutencao();
            System.out.println((atencao ? "[!]  " : "[ok] ") + item.gerarRelatorioDiagnostico());
            if (atencao) {
                alertas++;
            }
        }
        System.out.println("Componentes auditados: " + itens.size() + " | Requerem atenção: " + alertas);
    }
}
