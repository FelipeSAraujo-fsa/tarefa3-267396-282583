public enum StatDemanda {
    PENDENTE("Pendente"), EM_PRODUCAO("Em producao"), CONCLUIDA("Concluida"), CANCELADA("Cancelada");

    private final String descricao;

    StatusDemanda(String descricao) {
        this.descricao = descricao;
    }


    public String getDescricao() {
        return descricao;
    }

    public boolean isFinal() {
        return this == CONCLUIDA || this == CANCELADA;
    }
}