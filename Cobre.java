public class Cobre {
    private final int id;
    private final String nome;
    private double quantidade;
    private final String unidade;
    private final double custoPorUnidade;

    public Cobre(int id, double quantidadeInicial, double custoPorUnidade) {
        this.id = id;
        this.nome = "Laminado de Cobre";
        this.quantidade = quantidadeInicial;
        this.unidade = "cm^2";
        this.custoPorUnidade = custoPorUnidade;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public String getUnidade() {
        return unidade;
    }

    public double getCustoPorUnidade() {
        return custoPorUnidade;
    }

    public boolean verificarDisponibilidade(double quantidadeNecessaria) {
        return quantidade >= quantidadeNecessaria;
    }

    public void consumir(double quantidadeConsumida) {
        if (!verificarDisponibilidade(quantidadeConsumida)) {
            throw new IllegalStateException("Estoque de cobre insuficiente!");
        }
        this.quantidade -= quantidadeConsumida;
    }

    public void adicionarEstoque(double quantidadeAdicionada) {
        this.quantidade += quantidadeAdicionada;
    }

    @Override
    public String toString() {
        return String.format("%s: %.1f %s disponiveis (R$ %.2f/%s)", nome, quantidade, unidade, custoPorUnidade, unidade);
    }
}
