public class PlacaFaceUnica extends PlacaCircuito {
    private static final double QUALIDADE = 0.5;
    private static final int TEMPO_PRODUCAO = 12;

    public PlacaFaceUnica(int id, int lote) {
        super(id, lote, TipoPlaca.FACE_UNICA, QUALIDADE);
    }

    @Override
    public void processar() {
        System.out.println("Gravando trilhas simples em " + getNome() + "...");
    }

    @Override
    public int calcularTempoProducao() {
        return TEMPO_PRODUCAO;
    }
}
