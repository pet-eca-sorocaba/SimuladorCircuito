package sorocaba.peteca.com.simuladorcircuito;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import sorocaba.peteca.com.simuladorcircuito.circuitogerador.Circuito;
import sorocaba.peteca.com.simuladorcircuito.graficosgerador.Grafico;

public class SimuladorCircuito extends LinearLayout {
    private Grafico graficoUm;
    private Grafico graficoDois;
    private Circuito circuito;

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
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        graficoUm.iniciar();
        graficoDois.iniciar();
    }
}
