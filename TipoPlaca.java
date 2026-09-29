public enum TipoPlaca {
    FACE_UNICA("Placa Face Unica", 50.0, 35.0),
    DUPLA_FACE("Placa Dupla Face", 90.0, 70.0),
    MULTILAYER_PRO("Placa Multilayer Pro", 150.0, 120.0);

    private final String nome;
    private final double custoPorPlaca;
    private final double cobrePorPlaca;

    TipoPlaca(String nome, double custoPorPlaca, double cobrePorPlaca) {
        this.nome = nome;
        this.custoPorPlaca = custoPorPlaca;
        this.cobrePorPlaca = cobrePorPlaca;
    }

    public String getNome() {
        return nome;
    }

    public double getCustoPorPlaca() {
        return custoPorPlaca;
    }

    public double getCobrePorPlaca() {
        return cobrePorPlaca;
    }

    public PlacaCircuito criarPlaca(int id, int lote) {
        switch (this) {
            case FACE_UNICA:
                return new PlacaFaceUnica(id, lote);
            case DUPLA_FACE:
                return new PlacaFaceDupla(id, lote);
            case MULTILAYER_PRO:
                return new PlacaMultilayerPro(id, lote);
            default:
                throw new IllegalStateException("Tipo de placa desconhecido: " + this);
        }
    }
}
