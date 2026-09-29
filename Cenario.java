public enum Cenario {
    IDEAL("Ideal", 15000.0, 5000.0, 1, 0.25), APOCALIPTICO("Apocaliptico", 2500.0, 1000.0, 5, 1.5);

    private final String nome;
    private final double orcamentoInicial;
    private final double estoqueCobreInicial;
    private final int desgasteMaquinaBase;
    private final double fatorFalha;

    Cenario(String nome, double orcamentoInicial, double estoqueCobreInicial, int desgasteMaquinaBase, double fatorFalha) {
        this.nome = nome;
        this.orcamentoInicial = orcamentoInicial;
        this.estoqueCobreInicial = estoqueCobreInicial;
        this.desgasteMaquinaBase = desgasteMaquinaBase;
        this.fatorFalha = fatorFalha;
    }

    public String getNome() {
        return nome;
    }

    public double getOrcamentoInicial() {
        return orcamentoInicial;
    }

    public double getEstoqueCobreInicial() {
        return estoqueCobreInicial;
    }

    public int getDesgasteMaquinaBase() {
        return desgasteMaquinaBase;
    }

    public double getFatorFalha() {
        return fatorFalha;
    }
}
