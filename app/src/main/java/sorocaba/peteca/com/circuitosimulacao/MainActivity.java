package sorocaba.peteca.com.circuitosimulacao;

import androidx.appcompat.app.AppCompatActivity;
import sorocaba.peteca.com.simuladorcircuito.IntefaceSimulador;
import sorocaba.peteca.com.simuladorcircuito.SimuladorCircuito;
import sorocaba.peteca.com.simuladorcircuito.circuitogerador.Circuito;
import sorocaba.peteca.com.simuladorcircuito.circuitogerador.Ponto;
import sorocaba.peteca.com.simuladorcircuito.graficosgerador.Serie;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements IntefaceSimulador {
    double[] valores, valoresY;
    SimuladorCircuito simulador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        double[] valores_x = new double[255];
        valores = new double[255];
        valoresY = new double[255];
        final boolean[] animar = {false};

        for (int i = 0; i < 255; i++) {
            valores_x[i] = (2 * Math.PI * i) / 255;
            valores[i] = Math.sin(valores_x[i]);
            valoresY[i] = Math.cos(valores_x[i]);
        }

        setContentView(R.layout.activity_main);
        simulador = findViewById(R.id.simulador);
        simulador.addSerie(new Serie(valoresY, 250), 2);
        simulador.setNomesEixoY("V", "A");
        simulador.setNomesEixoX("ωt", "ωt");
        simulador.setCursorConfig(Color.BLUE, 3);
        simulador.setCursorStatus(true);
        simulador.setPeriodos(1);
        simulador.setEixosWidth(5);
        simulador.setEixosHeigthMarcacoes(0.05f);
        simulador.setEixosTextSize(1.5f);
        simulador.setEixosSubTextSize(1.3f);
        simulador.setBeta(true);
        simulador.setGraficoGrade(true);
        simulador.setColorTensaoUm(Color.GREEN);
        simulador.setColorTensaoDois(Color.BLUE);
        simulador.setColorTensaoTres(Color.BLACK);
        simulador.setColorCorrente(Color.RED);
        simulador.setEspessuraDados(5);

        simulador.setSimuladorListener(this);
        simulador.setCircuitoColor(Color.BLUE);
        simulador.setCircuitoWidth(4);
        simulador.setCircuitoGrade(true);
        simulador.setAnimacaoTime(2000);

        Button button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animar[0] = !animar[0];
                if (animar[0]) {
                    simulador.startAnimacao();
                } else {
                    simulador.stopAnimacao();
                }

            }
        });
    }

    @Override
    public void componenteClickado(int componente) {
        if (componente == 1) {
            simulador.addSerie(new Serie(valores, 250), 1);
            simulador.addSerie(new Serie(valores, 250), 2);
        } else {
            simulador.addSerie(new Serie(valoresY, 15), 1);
            simulador.addSerie(new Serie(valoresY, 15), 2);
        }
    }

    @Override
    public int carregaCircuito(Circuito circuito) {
        circuito.componente(new Ponto(8, 7), new Ponto(8, 3), 5, 1);
        circuito.trilha(new Ponto(8, 3), new Ponto(8, 2), new Ponto(9, 2));
        circuito.componente(new Ponto(9, 2), new Ponto(13, 2), 5, 2);
        circuito.trilha(new Ponto(13, 2), new Ponto(14, 2), new Ponto(14, 3));
        circuito.componente(new Ponto(14, 3), new Ponto(14, 7), 5, 3);
        circuito.trilha(new Ponto(14, 7), new Ponto(14, 8), new Ponto(13, 8));
        circuito.componente(new Ponto(13, 8), new Ponto(9, 8), 5, 4);
        circuito.trilha(new Ponto(9, 8), new Ponto(8, 8), new Ponto(8, 7));
        return 1;
    }

    @Override
    public void animacaoCircuito(Circuito circuito) {

    }


}
