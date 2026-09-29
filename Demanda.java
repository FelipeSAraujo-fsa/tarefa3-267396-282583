public class Demanda {
    private final TipoPlaca tipo;
    private final int quantidadeSolicitada;
    private int quantidadeProduzida = 0;
    private StatusDemanda status = StatusDemanda.PENDENTE;

    public Demanda(TipoPlaca tipo, int quantidadeSolicitada) {
        if (tipo == null) {
            throw new IllegalArgumentException("O tipo de placa e obrigatório.");
        }
        if (quantidadeSolicitada <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }
        this.tipo = tipo;
        this.quantidadeSolicitada = quantidadeSolicitada;
    }

    public TipoPlaca getTipo() {
        return tipo;
    }

    public StatusDemanda getStatus() {
        return status;
    }

    public int getQuantidadeSolicitada() {
        return quantidadeSolicitada;
    }

    public int getQuantidadeProduzida() {
        return quantidadeProduzida;
    }

    public int getQuantidadeRestante() {
        return quantidadeSolicitada - quantidadeProduzida;
    }

    public double getCustoPorPlaca() {
        return tipo.getCustoPorPlaca();
    }

    public double getCobrePorPlaca() {
        return tipo.getCobrePorPlaca();
    }

    public boolean isElegivel() {
        return status == StatusDemanda.PENDENTE && getQuantidadeRestante() > 0;
    }

    public double calcularCustoTotalEstimado() {
        return getQuantidadeRestante() * tipo.getCustoPorPlaca();
    }

    public double calcularMateriaPrimaNecessaria() {
        return getQuantidadeRestante() * tipo.getCobrePorPlaca();
    }

    public boolean verificarViabilidade(double orcamentoDisponivel) {
        return calcularCustoTotalEstimado() <= orcamentoDisponivel;
    }

    public void iniciarProducao() {
        if (status != StatusDemanda.PENDENTE) {
            throw new IllegalStateException("É apenas possivel iniciar demandas pendentes (status atual: " + status.getDescricao() + ").");
        }
        status = StatusDemanda.EM_PRODUCAO;
    }

    public void registrarProducao(int quantidade) {
        if (status != StatusDemanda.EM_PRODUCAO) {
            throw new IllegalStateException("So é possivel registrar produção em demandas (status atual: " + status.getDescricao() + ").");
        }
        if (quantidade <= 0 || quantidade > getQuantidadeRestante()) {
            throw new IllegalArgumentException("Quantidade produzida inválida: " + quantidade);
        }
        quantidadeProduzida += quantidade;
        if (getQuantidadeRestante() == 0) {
            concluir();
        }
    }

    public void interromperProducao() {
        if (status != StatusDemanda.EM_PRODUCAO) {
            throw new IllegalStateException("Apenas demandas em produção podem ser interrompidas.");
        }
        status = StatusDemanda.PENDENTE;
    }

    public void concluir() {
        if (status == StatusDemanda.CANCELADA) {
            throw new IllegalStateException("Impossivel concluir uma demanda cancelada.");
        }
        if (status != StatusDemanda.EM_PRODUCAO || getQuantidadeRestante() > 0) {
            throw new IllegalStateException("A demanda ainda nao terminou de ser produzida.");
        }
        status = StatusDemanda.CONCLUIDA;
    }

    public void cancelar() {
        if (status.isFinal()) {
            throw new IllegalStateException("Impossivel cancelar uma demanda " + status.getDescricao().toLowerCase() + ".");
        }
        status = StatusDemanda.CANCELADA;
    }

    @Override
    public String toString() {
        return String.format("%s: %d/%d produzida(s) - Status: [%s]",
                tipo.getNome(), quantidadeProduzida, quantidadeSolicitada, status.getDescricao());
    }
}
