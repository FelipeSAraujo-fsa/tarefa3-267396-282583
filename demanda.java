public class demanda {
    private final String tipoProduto;
    private int quantidadeProdutos;
    private StatDemanda status;
    private final double custoPorPlaca;

    public demanda(String tipoProduto, int quantidadeProdutos, double custoPorPlaca) {
        this.tipoProduto = tipoProduto;
        this.quantidadeProdutos = quantidadeProdutos;
        this.custoPorPlaca = custoPorPlaca;
        this.status = quantidadeProdutos <= 0 ? StatDemanda.CONCLUIDA : StatDemanda.PENDENTE;
    }

    public String getTipoProduto() { return tipoProduto; }
    public int getQuantidadeProdutos() { return quantidadeProdutos; }
    public StatDemanda getStatus() { return status; }
    
    public void setStatus(StatDemanda novoStatus) {
        if (this.status == StatDemanda.CANCELADA && novoStatus == StatDemanda.CONCLUIDA) {
            throw new IllegalStateException("IMpossivel concluir uma demanda cancelada.");
        }
        this.status = novoStatus;
    }

    public boolean verificarViabilidade(double orcamentoDisponivel) {
        return (quantidadeProdutos * custoPorPlaca) <= orcamentoDisponivel;
    }

    public double calcularMateriaPrimaNecessaria(double cobrePorUnidade) {
        return quantidadeProdutos * cobrePorUnidade;
    }

    public void atender(int quantidadeEntregue) {
        this.quantidadeProdutos = Math.max(0, this.quantidadeProdutos - quantidadeEntregue);
        if (this.quantidadeProdutos == 0) {
            this.status = StatDemanda.CONCLUIDA;
        }
    }

    @Override
    public String toString() {
        return String.format("%s: %d unidade(s) - Status: [%s]", 
                tipoProduto, quantidadeProdutos, status.getDescricao());
    }
}