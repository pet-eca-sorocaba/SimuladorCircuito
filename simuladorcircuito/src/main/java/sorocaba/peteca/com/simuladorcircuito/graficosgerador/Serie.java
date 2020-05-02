package sorocaba.peteca.com.simuladorcircuito.graficosgerador;

public class Serie {
    public double[] valor;
    public int beta = 0, max, tamanho;

    public Serie(double[] valorY) {
        this.valor = valorY;
        max = max();
        tamanho = valorY.length;
    }

    public Serie(double[] valorY, int beta) {
        this.valor = valorY;
        this.max = max();
        this.tamanho = valorY.length;
        this.beta = beta;
    }

    private int max() {
        int index = 0;
        double valorMax = Math.abs(valor[0]);
        for (int i = 1; i < valor.length; i++) {
            if (Math.abs(valorMax) < Math.abs(valor[i])) {
                valorMax = Math.abs(valor[i]);
                index = i;
            }
        }
        return index;
    }


}
