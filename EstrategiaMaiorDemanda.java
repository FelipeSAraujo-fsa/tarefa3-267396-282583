import java.util.List;

public class EstrategiaMaiorDemanda implements EstrategiaProducao {

    @Override
    public Demanda selecionarDemanda(List<Demanda> demandas, double orcamentoDisponivel) {
        Demanda maior = null;
        for (Demanda demanda : filtrarElegiveis(demandas)) {
            if (maior == null || demanda.getQuantidadeRestante() > maior.getQuantidadeRestante()) {
                maior = demanda;
            }
        }
        return maior;
    }

    @Override
    public String getNomeEstrategia() {
        return "Lote de Maior Demanda";
    }
}
