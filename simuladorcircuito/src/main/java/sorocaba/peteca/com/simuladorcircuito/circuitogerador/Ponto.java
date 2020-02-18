package sorocaba.peteca.com.simuladorcircuito.circuitogerador;

public class Ponto {
    private int x;
    private int y;

    public Ponto(int valor_x, int valor_y) {
        x = valor_x;
        y = valor_y;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
}