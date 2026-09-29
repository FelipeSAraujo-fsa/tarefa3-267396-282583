public class MaquinaLaminadora extends Maquina {
    private static final double PROBABILIDADE_FALHA = 0.2;
    private static final double AUMENTO_FALHA = 0.2;

    public MaquinaLaminadora(String nome, Cenario cenario) {
        super(nome, PROBABILIDADE_FALHA, cenario);
    }

    @Override
    public String getTipo() {
        return "Laminadora";
    }

    @Override
    protected void processar(PlacaCircuito placa) {
        if (verificarFalha()) {
            placa.aumentarProbabilidadeFalha(AUMENTO_FALHA);
            System.out.println("        [ERRO] " + getNome() + ": o cobre laminou incorretamente a " + placa.getNome() + "!");
        }
    }
}
