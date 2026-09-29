import java.util.List;

public class EstrategiaOrdemChegada implements EstrategiaProducao {

    @Override
    public Demanda selecionarDemanda(List<Demanda> demandas, double orcamentoDisponivel) {
        List<Demanda> elegiveis = filtrarElegiveis(demandas);
        return elegiveis.isEmpty() ? null : elegiveis.get(0);
    }

    @Override
    public String getNomeEstrategia() {
        return "Ordem de Chegada (FIFO)";
    }
}
