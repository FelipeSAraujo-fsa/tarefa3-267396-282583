import java.util.Random;

public abstract class Maquina implements Auditavel {
    public static final double CUSTO_REPARO = 100.0;

    private static final int SAUDE_MAXIMA = 100;
    private static final int LIMIAR_MANUTENCAO = 30;
    private static final int DESGASTE_ALEATORIO_MAXIMO = 3;
    private static final int SAUDE_MINIMA_NO_CALCULO = 10;

    private final String nome;
    private final double probabilidadeFalhaBase;
    private final Cenario cenario;
    private final Random random = new Random();
    private int saude = SAUDE_MAXIMA;
    private int historicoFalhas = 0;

    protected Maquina(String nome, double probabilidadeFalhaBase, Cenario cenario) {
        this.nome = nome;
        this.probabilidadeFalhaBase = probabilidadeFalhaBase;
        this.cenario = cenario;
    }

    public abstract String getTipo();

    protected abstract void processar(PlacaCircuito placa);

    public final void operar(PlacaCircuito placa) {
        if (estaQuebrada()) {
            throw new IllegalStateException("A maquina " + nome + " está quebrada e precisa de reparo!");
        }
        desgastar();
        processar(placa);
        if (estaQuebrada()) {
            System.out.println("        [ALERTA] A saûde da" + nome + " chegou a zero ");
        }
    }

    private void desgastar() {
        int desgaste = cenario.getDesgasteMaquinaBase() + random.nextInt(DESGASTE_ALEATORIO_MAXIMO + 1);
        saude = Math.max(0, saude - desgaste);
    }

    protected boolean verificarFalha() {
        boolean falhou = random.nextDouble() < calcularProbabilidadeFalha();
        if (falhou) {
            historicoFalhas++;
        }
        return falhou;
    }

    public double calcularProbabilidadeFalha() {
        double fatorSaude = (double) SAUDE_MAXIMA / Math.max(saude, SAUDE_MINIMA_NO_CALCULO);
        return Math.min(1.0, probabilidadeFalhaBase * cenario.getFatorFalha() * fatorSaude);
    }

    public void reparar() {
        this.saude = SAUDE_MAXIMA;
    }

    public boolean estaQuebrada() {
        return saude <= 0;
    }

    public String getNome() {
        return nome;
    }

    public int getSaude() {
        return saude;
    }

    public int getHistoricoFalhas() {
        return historicoFalhas;
    }

    @Override
    public String gerarRelatorioDiagnostico() {
        String situacao;
        if (estaQuebrada()) {
            situacao = "QUEBRADA - reparo obrigatório";
        } else if (precisaManutencao()) {
            situacao = "CRÍTICA - manutencao recomendada";
        } else {
            situacao = "Operacional";
        }
        return String.format("Maquina: %s [%s] | Saude: %d%% | Prob. de falha: %.1f%% | Falhas: %d | Situacao: %s",
                nome, getTipo(), saude, calcularProbabilidadeFalha() * 100, historicoFalhas, situacao);
    }

    @Override
    public boolean precisaManutencao() {
        return saude < LIMIAR_MANUTENCAO;
    }
}
