package sorocaba.peteca.com.simuladorcircuito;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;


class Resultados extends ConstraintLayout {
    private TextView textoTensao, textoCorrente, textoAngulo;

    public Resultados(Context context) {
        super(context);
        init(context);
    }
    public Resultados(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    private void init(Context context) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.layout_resultados, this, true);
        textoTensao = view.findViewById(R.id.texto_tensao);
        textoCorrente = view.findViewById(R.id.texto_corrente);
        textoAngulo = view.findViewById(R.id.texto_angulo);
    }

    // CLASSE QUE CONTEM TODAS AS INFORMAÇOES SOBRE O MÉTODO
    //TODO: POR ENQUANTO, FICA MEIO CAOTICO O MODO DE COMO IMPLEMENTAR E, JÁ PENSANDO
    // EM COMO FAZER AS "FIACAO" PARA FINALIZAR O APLICATIVO.
    public void setColorTensao(int colorTensao) {
        textoTensao.setTextColor(colorTensao);
    }
    public void setColorCorrente(int colorTensao) {
        textoCorrente.setTextColor(colorTensao);
    }
    public void setColorAngulo(int colorTensao) {
        textoAngulo.setTextColor(colorTensao);
    }

    public void atualizarDados(double tensao, double corrente, double angulo) {
        DecimalFormat df;
        df = new DecimalFormat("0.000");

        textoTensao.setText("V = " + df.format(tensao) + " V");
        textoCorrente.setText("I = " + df.format(corrente) + " A");
        textoAngulo.setText("ωt = " + df.format(angulo) + " rad");
    }

    public void setStatus (boolean status) {
        if (status) {
            textoTensao.setVisibility(VISIBLE);
            textoCorrente.setVisibility(VISIBLE);
            textoAngulo.setVisibility(VISIBLE);
        } else {
            textoTensao.setVisibility(INVISIBLE);
            textoCorrente.setVisibility(INVISIBLE);
            textoAngulo.setVisibility(INVISIBLE);
        }
    }

}
