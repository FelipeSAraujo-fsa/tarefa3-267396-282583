import java.util.List;

public class EstrategiaMaxPlaca implements EstrategiaProducao {

    @Override
    public Demanda selecionarDemanda(List<Demanda> demandas, double orcamentoDisponivel) {
        Demanda melhor = null;
        for (Demanda demanda : filtrarElegiveis(demandas)) {
            if (demanda.verificarViabilidade(orcamentoDisponivel)
                    && (melhor == null || demanda.getQuantidadeRestante() > melhor.getQuantidadeRestante())) {
                melhor = demanda;
            }
        }
        return melhor;
    }

    @Override
    public String getNomeEstrategia() {
        return "Maximizar Produção de Placas";
    }
}
