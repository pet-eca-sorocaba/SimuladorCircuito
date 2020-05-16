package sorocaba.peteca.com.simuladorcircuito.circuitogerador;

import android.graphics.Path;

public class Item {
    public int numeroItem;
    public Ponto pontoUm, pontoDois;
    public Path path;

    public Item(int numeroItem, Path path, Ponto pontoUm, Ponto pontoDois) {
        this.numeroItem = numeroItem;
        this.path = path;
        this.pontoUm = pontoUm;
        this.pontoDois = pontoDois;
    }
}