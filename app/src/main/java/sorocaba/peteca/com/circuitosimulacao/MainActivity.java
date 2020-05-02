package sorocaba.peteca.com.circuitosimulacao;

import androidx.appcompat.app.AppCompatActivity;
import sorocaba.peteca.com.simuladorcircuito.SimuladorCircuito;
import sorocaba.peteca.com.simuladorcircuito.graficosgerador.Serie;

import android.graphics.Color;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        double[] valores_x = new double[255];
        double[] valores = new double[255];
        double[] valoresY = new double[255];

        for (int i = 0; i < 255; i++) {
            valores_x[i] = (2 * Math.PI * i) / 255;
            valores[i] = Math.sin(valores_x[i]);
            valoresY[i] = Math.cos(valores_x[i]);
        }

        setContentView(R.layout.activity_main);
        SimuladorCircuito simulador = findViewById(R.id.simulador);
        simulador.addSerie(new Serie(valores, 250), new Serie(valoresY), 1);
        simulador.addSerie(new Serie(valoresY, 250), 2);
        simulador.setNomesEixoY("V", "A");
        simulador.setNomesEixoX("ωt", "ωt");
        simulador.setCursorConfig(Color.BLUE, 3);
        simulador.setCursorStatus(true);
        //simulador.setPeriodos(1);
        simulador.setEixosWidth(5);
        simulador.setEixosHeigthMarcacoes(0.05f);
        simulador.setEixosTextSize(1.5f);
        simulador.setEixosSubTextSize(1.3f);
        simulador.setBeta(true);
        simulador.setGradeStatus(true);
        simulador.setColorTensaoUm(Color.YELLOW);
        simulador.setColorTensaoDois(Color.BLUE);
        simulador.setColorTensaoTres(Color.BLACK);
        simulador.setColorCorrente(Color.RED);

//        circuito.iniciar(9);
//        circuito.grade(3);
//        circuito.add("Fonte", circuito.Ponto(4,5), circuito.Ponto(6,5), paint, 1);
//        circuito.add("Trilha", circuito.Ponto(4,5), circuito.Ponto(2,5), paint);
//        circuito.add("Trilha", circuito.Ponto(2,5), circuito.Ponto(2,11), paint);
//        circuito.add("Tiristor", circuito.Ponto(2,11), circuito.Ponto(2,13), paint, 4);
//        circuito.add("Trilha", circuito.Ponto(2,13), circuito.Ponto(2,18), paint);
//        circuito.add("Trilha", circuito.Ponto(2,18), circuito.Ponto(4,18), paint);
//        circuito.add("CargaR", circuito.Ponto(4,18), circuito.Ponto(6,18), paint, 10);
//        circuito.add("Trilha", circuito.Ponto(6,18), circuito.Ponto(8,18), paint);
//        circuito.add("Trilha", circuito.Ponto(8,18), circuito.Ponto(8,5), paint);
//        circuito.add("Trilha", circuito.Ponto(8,5), circuito.Ponto(6,5), paint);
    }
}
