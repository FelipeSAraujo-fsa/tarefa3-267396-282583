import java.util.ArrayList;
import java.util.List;

public interface EstrategiaProduc {

    Demanda selecDemanda(List<Demanda> demandas, double orcamentoDisp);

    String getNomeEstrategia();

    default List<Demanda> filtrarEleg(List<Demanda> demandas) {
        List<Demanda> eleg = new ArrayList<>();
        for (Demanda d : demandas) {
            if (d.isEleg()) {
                eleg.add(d);
            }
        }
        return eleg;
    }
}