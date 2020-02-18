package sorocaba.peteca.com.simuladorcircuito;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import sorocaba.peteca.com.circuitogerador.Circuito;

public class SimuladorCircuito extends LinearLayout {
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
        Circuito circuito = view.findViewById(R.id.circuito);
    }
}
