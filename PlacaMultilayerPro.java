public class PlacaMultilayerPro extends PlacaCircuito {
    private static final double QUALIDADE = 0.9;
    private static final int TEMPO_PRODUCAO = 45;

    public PlacaMultilayerPro(int id, int lote) {
        super(id, lote, TipoPlaca.MULTILAYER_PRO, QUALIDADE);
    }

    @Override
    public void processar() {
        System.out.println("Gravando " + TEMPO_PRODUCAO + "s de trilhas multicamada em " + getNome() + "...");
    }

    @Override
    public int calcularTempoProducao() {
        return TEMPO_PRODUCAO;
    }
}
