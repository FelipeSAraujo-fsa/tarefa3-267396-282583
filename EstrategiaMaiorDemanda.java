import java.util.List;

public class EstrategiaMaiorDemanda implements EstrategiaProduc {
    @Override
    public demanda selecionarDemanda(List<demanda> demandas, double orcamentoDisp) {
        demanda maior = null;
        for (demanda d : demandas) {
            if (d.getStatus() == StatusDemanda.PENDENTE) {
                if (maior == null || d.getQuantidadeProdutos() > maior.getQuantidadeProdutos()) {
                    maior = d;
                }
            }
        }
        return maior;
    }

    @Override
    public String getNomeEstrategia() {
        return "Lote de Maior Demanda";
    }
}