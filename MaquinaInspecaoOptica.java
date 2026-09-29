public class MaquinaInspecaoOptica extends Maquina {
    private static final double PROBABILIDADE_FALHA = 0.05;
    private static final double AUMENTO_FALHA = 0.1;

    public MaquinaInspecaoOptica(String nome, Cenario cenario) {
        super(nome, PROBABILIDADE_FALHA, cenario);
    }

    @Override
    public String getTipo() {
        return "Inspeção Óptica Automática";
    }

    @Override
    protected void processar(PlacaCircuito placa) {
        if (verificarFalha()) {
            placa.aumentarProbabilidadeFalha(AUMENTO_FALHA);
            System.out.println("        [ERRO] " + getNome() + ": um defeito passou despercebido em " + placa.getNome() + "!");
        }
    }
}
