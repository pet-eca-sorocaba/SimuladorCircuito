package sorocaba.peteca.com.simuladorcircuito;

import sorocaba.peteca.com.simuladorcircuito.circuitogerador.Circuito;

public interface IntefaceSimulador {
    void componenteClickado(int componente);
    int carregaCircuito(Circuito circuito);
    void animacaoCircuito(Circuito circuito);
}
