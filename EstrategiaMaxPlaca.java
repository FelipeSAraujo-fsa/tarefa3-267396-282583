import java.util.List;

public class EstrategiaMaxPlaca implements EstrategiaProduc {
    @Override
    public demanda selecionarDemanda(List<demanda> demandas, double orcamentoDisp) {
        demanda melhorCustoBeneficio = null;
        for (demanda d : demandas) {
            if (d.getStatus() == StatusDemanda.PENDENTE && d.verificarViabilidade(orcamentoDisponivel)) {
                if (melhorCustoBeneficio == null || d.getQuantidadePlacas() > melhorCustoBeneficio.getQuantidadePlacas()) {
                    melhorCustoBeneficio = d;
                }
            }
        }
        return melhorCustoBeneficio;
    }

    @Override
    public String getNomeEstrategia() {
        return "Maximizar Produção de Placas";
    }
}