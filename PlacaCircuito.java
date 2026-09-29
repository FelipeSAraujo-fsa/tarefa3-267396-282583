public abstract class PlacaCircuito implements Auditavel {
    private static final double LIMIAR_RISCO = 0.4;

    private final int id;
    private final int lote;
    private final TipoPlaca tipo;
    private final double qualidadeBase;
    private double probabilidadeFalhaAcumulada = 0.0;

    protected PlacaCircuito(int id, int lote, TipoPlaca tipo, double qualidadeBase) {
        this.id = id;
        this.lote = lote;
        this.tipo = tipo;
        this.qualidadeBase = qualidadeBase;
    }

    public abstract void processar();

    public abstract int calcularTempoProducao();

    public int getId() {
        return id;
    }

    public int getLote() {
        return lote;
    }

    public TipoPlaca getTipo() {
        return tipo;
    }

    public String getNome() {
        return tipo.getNome();
    }

    public double getProbabilidadeFalhaAcumulada() {
        return probabilidadeFalhaAcumulada;
    }

    public double getQualidade() {
        return qualidadeBase * (1.0 - probabilidadeFalhaAcumulada);
    }

    public void aumentarProbabilidadeFalha(double incremento) {
        this.probabilidadeFalhaAcumulada = Math.min(1.0, this.probabilidadeFalhaAcumulada + incremento);
    }

    @Override
    public String gerarRelatorioDiagnostico() {
        return String.format("Placa #%d (%s) | Lote #%d | Qualidade: %.1f%% | Risco de falha: %.1f%% | Situacao: %s",
                id, getNome(), lote, getQualidade() * 100, probabilidadeFalhaAcumulada * 100,
                precisaManutencao() ? "RISCO ALTO - retrabalho recomendado" : "Conforme");
    }

    @Override
    public boolean precisaManutencao() {
        return probabilidadeFalhaAcumulada > LIMIAR_RISCO;
    }

    @Override
    public String toString() {
        return String.format("#%d %s (lote %d) qualidade=%.1f%%", id, getNome(), lote, getQualidade() * 100);
    }
}
