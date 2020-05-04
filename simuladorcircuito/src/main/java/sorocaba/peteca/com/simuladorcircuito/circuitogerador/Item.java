package sorocaba.peteca.com.simuladorcircuito.circuitogerador;

import android.graphics.Path;

public class Item {
    public int numeroItem;
    public Ponto ponto;
    public Path path;

    public Item(int numeroItem, Path path, Ponto ponto) {
        this.numeroItem = numeroItem;
        this.path = path;
        this.ponto = ponto;
    }
}