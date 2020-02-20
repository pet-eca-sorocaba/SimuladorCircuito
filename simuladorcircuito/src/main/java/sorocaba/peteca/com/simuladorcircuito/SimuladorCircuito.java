package sorocaba.peteca.com.simuladorcircuito;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import sorocaba.peteca.com.simuladorcircuito.circuitogerador.Circuito;
import sorocaba.peteca.com.simuladorcircuito.graficosgerador.Grafico;

public class SimuladorCircuito extends LinearLayout {
    private Grafico graficoUm;
    private Grafico graficoDois;
    private Circuito circuito;
    private Resultados resultados;

    public SimuladorCircuito(Context context) {
        super(context);
        init(context);
    }
    public SimuladorCircuito(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.layout_principal, this, true);
        graficoUm = findViewById(R.id.graficoUm);
        graficoDois = findViewById(R.id.graficoDois);
        circuito = view.findViewById(R.id.circuito);

        circuito.post(new Runnable() {
            @Override
            public void run() {
                circuito.iniciar(4);
                circuito.grade(3);
            }
        });

        graficoUm.setNomeEixoY("V"); // DEIXAR MODIFICAVEL
        graficoUm.setNomeEixoX("ωt"); // DEIXAR MODIFICAVEL
        graficoDois.setNomeEixoY("A"); // DEIXAR MODIFICAVEL
        graficoDois.setNomeEixoX("ωt"); // DEIXAR MODIFICAVEL

        graficoUm.setCursorColor(Color.BLUE); // DEIXAR MODIFICAVEL
        graficoUm.setCursorStatus(true); // DEIXAR MODIFICAVEL
        graficoUm.setCursorWidth(4);// DEIXAR MODIFICAVEL
        graficoDois.setCursorColor(Color.BLUE); // DEIXAR MODIFICAVEL
        graficoDois.setCursorStatus(true); // DEIXAR MODIFICAVEL
        graficoDois.setCursorWidth(4); // DEIXAR MODIFICAVEL

        graficoUm.setPeriodos(3); // DEIXAR MODIFICAVEL
        graficoDois.setPeriodos(3); // DEIXAR MODIFICAVEL

        graficoUm.setEixosWidth(5); // DEIXAR MODIFICAVEL
        graficoUm.setEixosHeigthMarcacoes(0.05f); // DEIXAR MODIFICAVEL

        graficoDois.setEixosWidth(5); // DEIXAR MODIFICAVEL
        graficoDois.setEixosHeigthMarcacoes(0.05f); // DEIXAR MODIFICAVEL

        graficoUm.setEixosTextSize(1.5f); // DEIXAR MODIFICAVEL
        graficoDois.setEixosTextSize(1.5f); // DEIXAR MODIFICAVEL

        graficoUm.setEixosSubTextSize(1.4f); // DEIXAR MODIFICAVEL
        graficoDois.setEixosSubTextSize(1.4f); // DEIXAR MODIFICAVEL

        graficoUm.setBeta(true); // DEIXAR MODIFICAVEL
        graficoDois.setBeta(true); // DEIXAR MODIFICAVEL

        graficoUm.setGradeStatus(true); // DEIXAR MODIFICAVEL
        graficoDois.setGradeStatus(true); // DEIXAR MODIFICAVEL

        graficoUm.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                graficoUm.changeCursor(event);
                graficoDois.setCursor(graficoUm.getCursor());
                return true;
            }
        });

        graficoDois.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                graficoDois.changeCursor(event);
                graficoUm.setCursor(graficoDois.getCursor());
                return true;
            }
        });
        final int[] val = {1};
        circuito.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                graficoUm.setPeriodos(val[0]%4 + 1);
                graficoDois.setPeriodos((val[0] +1)%4 + 1);
                val[0] = val[0] + 1;
                graficoUm.setCursorWidth(val[0]);// DEIXAR MODIFICAVEL
                graficoDois.setCursorWidth(val[0]); // DEIXAR MODIFICAVEL
            }
        });
    }
}

//    private DecimalFormat df;
//        df = new DecimalFormat("0.000");
