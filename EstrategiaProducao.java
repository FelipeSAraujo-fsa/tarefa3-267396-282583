import java.util.ArrayList;
import java.util.List;

public interface EstrategiaProducao {

    Demanda selecionarDemanda(List<Demanda> demandas, double orcamentoDisponivel);

    String getNomeEstrategia();

    default List<Demanda> filtrarElegiveis(List<Demanda> demandas) {
        List<Demanda> elegiveis = new ArrayList<>();
        for (Demanda demanda : demandas) {
            if (demanda.isElegivel()) {
                elegiveis.add(demanda);
            }
        }
        return elegiveis;
    }
}
