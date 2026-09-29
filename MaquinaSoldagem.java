public class MaquinaSoldagem extends Maquina {
    private static final double PROBABILIDADE_FALHA = 0.3;
    private static final double AUMENTO_FALHA = 0.3;

    public MaquinaSoldagem(String nome, Cenario cenario) {
        super(nome, PROBABILIDADE_FALHA, cenario);
    }

    @Override
    public String getTipo() {
        return "Solda";
    }

    @Override
    protected void processar(PlacaCircuito placa) {
        if (verificarFalha()) {
            placa.aumentarProbabilidadeFalha(AUMENTO_FALHA);
            System.out.println("        [ERRO] " + getNome() + ": solda fria em " + placa.getNome() + "!");
        }
    }
}
