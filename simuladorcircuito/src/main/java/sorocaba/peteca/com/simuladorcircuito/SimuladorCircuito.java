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
import sorocaba.peteca.com.simuladorcircuito.graficosgerador.Serie;

public class SimuladorCircuito extends LinearLayout implements Circuito.InterfaceCircuito{
    private Grafico graficoUm;
    private Grafico graficoDois;
    private Circuito circuito;
    private Resultados resultados;

    IntefaceSimulador intefaceSimulador;

    public void setSimuladorListener(IntefaceSimulador intefaceSimulador) {
        this.intefaceSimulador = intefaceSimulador;
    }

    public interface IntefaceSimulador {
        void componenteClickado(int componente);
        int carregaCircuito(Circuito circuito);
    }


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
        graficoUm = view.findViewById(R.id.graficoUm);
        graficoDois = view.findViewById(R.id.graficoDois);
        resultados = view.findViewById(R.id.resultados);
        circuito = view.findViewById(R.id.circuito);
        circuito.setCircuitoListener(this);

        resultados.atualizarDados(125.351531512, 125.23424252, 76.33151355);

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

        circuito.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                circuito.ToqueNaTela(event);
                intefaceSimulador.componenteClickado(circuito.selecionado());
                return true;
            }
        });
    }

    @Override
    public int circuitoPronto() {
        int componente = intefaceSimulador.carregaCircuito(circuito);
        intefaceSimulador.componenteClickado(componente);
        return componente;
    }

    //region getters e setters
    public void setNomesEixoY(String nome, String nomeDois) {
        graficoUm.setNomeEixoY(nome);
        graficoDois.setNomeEixoY(nomeDois);
    }
    public void setNomesEixoX(String nome, String nomeDois) {
        graficoUm.setNomeEixoX(nome);
        graficoDois.setNomeEixoX(nomeDois);
    }

    public void setCursorConfig (int color, int width) {
        graficoUm.setCursorColor(color);
        graficoUm.setCursorWidth(width);
        graficoDois.setCursorColor(color);
        graficoDois.setCursorWidth(width);
        resultados.setColorAngulo(color);
    }
    public void setCursorStatus (boolean status) {
        graficoUm.setCursorStatus(status);
        graficoDois.setCursorStatus(status);
        resultados.setStatus(status);
    }

    public void setPeriodos(int periodos) {
        graficoUm.setPeriodos(periodos);
        graficoDois.setPeriodos(periodos);
    }

    public void setEixosWidth(int width) {
        graficoUm.setEixosWidth(width);
        graficoDois.setEixosWidth(width);
    }

    public void setEixosHeigthMarcacoes(float width) {
        graficoUm.setEixosHeigthMarcacoes(width);
        graficoDois.setEixosHeigthMarcacoes(width);
    }

    public void setEixosTextSize(float width) {
        graficoUm.setEixosTextSize(width);
        graficoDois.setEixosTextSize(width);
    }
    public void setEixosSubTextSize(float width) {
        graficoUm.setEixosSubTextSize(width);
        graficoDois.setEixosSubTextSize(width);
    }

    public void setBeta(boolean status) {
        graficoUm.setBeta(status);
        graficoDois.setBeta(status);
    }

    public void setGradeStatus(boolean status) {
        graficoUm.setGradeStatus(status);
        graficoDois.setGradeStatus(status);
    }

    public void setColorTensaoUm(int color) {
        graficoUm.setColorPrincipal(color);
        circuito.setColorPrincipal(color);
        resultados.setColorTensao(color);
    }
    public void setColorTensaoDois(int color) {
        graficoUm.setColorSecundario(color);
        circuito.setColorSecundario(color);
    }
    public void setColorTensaoTres(int color) {
        graficoUm.setColorTerciario(color);
        circuito.setColorTerciario(color);
    }
    public void setColorCorrente(int color) {
        graficoDois.setColorPrincipal(color);
        resultados.setColorCorrente(color);
    }

    public void addSerie(Serie serie, int grafico) {
        if (grafico == 1) {
            graficoUm.addSerie(serie);
        } else if (grafico == 2) {
            graficoDois.addSerie(serie);
        }
    }
    public void addSerie(Serie serie, Serie serieDois, int grafico) {
        if (grafico == 1) {
            graficoUm.addSerie(serie, serieDois);
        } else if (grafico == 2) {
            graficoDois.addSerie(serie, serieDois);
        }
    }
    public void addSerie(Serie serie, Serie serieDois, Serie serieTres, int grafico) {
        if (grafico == 1) {
            graficoUm.addSerie(serie, serieDois, serieTres);
        } else if (grafico == 2) {
            graficoDois.addSerie(serie, serieDois, serieTres);
        }
    }
    public void removeSerie(int grafico) {
        if (grafico == 1) {
            graficoUm.removeSerie();
        } else if (grafico == 2) {
            graficoDois.removeSerie();
        }
    }

    public void setCircuitoWidth(int width) {
        circuito.setStrokeWidth(width);
    }
    public void setCircuitoColor(int color) {
        circuito.setColor(color);
    }

    public void setRaioGrade(int raioGrade) {
        circuito.setRaioGrade(raioGrade);
    }
    public void setStatusGrade(boolean statusGrade) {
        circuito.setStatusGrade(statusGrade);
    }

    public String getCircuitoDimensoes () {
        return circuito.dimensoes();
    }
    //endregion
}

//    private DecimalFormat df;
//        df = new DecimalFormat("0.000");
