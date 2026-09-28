import java.util.List;

public class EstrategiaOrdemChegada implements EstrategiaProduc {
    @Override
    public demanda selecionarDemanda(List<demanda> demandas, double orcamentoDisp) {
        for (demanda d : demandas) {
            if (d.getStatus() == StatusDemanda.PENDENTE) {
                return d;
            }
        }
        return null;
    }

    @Override
    public String getNomeEstrategia() {
        return "Ordem de Chegada (FIFO)";
    }
}