public class PlacaFaceDupla extends PlacaCircuito {
    private static final double QUALIDADE = 0.7;
    private static final int TEMPO_PRODUCAO = 25;

    public PlacaFaceDupla(int id, int lote) {
        super(id, lote, TipoPlaca.DUPLA_FACE, QUALIDADE);
    }

    @Override
    public void processar() {
        System.out.println("Gravando trilhas nas duas faces de " + getNome() + "...");
    }

    @Override
    public int calcularTempoProducao() {
        return TEMPO_PRODUCAO;
    }
}
