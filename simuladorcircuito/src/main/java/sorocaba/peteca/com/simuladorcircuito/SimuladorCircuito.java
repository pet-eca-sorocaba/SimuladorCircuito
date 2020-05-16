package sorocaba.peteca.com.simuladorcircuito;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import sorocaba.peteca.com.simuladorcircuito.circuitogerador.Circuito;
import sorocaba.peteca.com.simuladorcircuito.graficosgerador.Grafico;
import sorocaba.peteca.com.simuladorcircuito.graficosgerador.Serie;

public class SimuladorCircuito extends LinearLayout implements Circuito.InterfaceCircuito {
    private Grafico graficoUm;
    private Grafico graficoDois;
    private Circuito circuito;
    private Resultados resultados;
    private boolean animacao = false, cursorStatus = false;
    private long tempoAnimacao = 2000;
    private CountDownTimer counter;

    IntefaceSimulador intefaceSimulador;
    private boolean OndasStatus;

    public void setSimuladorListener(IntefaceSimulador intefaceSimulador) {
        this.intefaceSimulador = intefaceSimulador;
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

        graficoUm.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!animacao) {
                    graficoUm.changeCursor(event);
                    graficoDois.setCursor(graficoUm.getCursor());
                    resultados.atualizarDados(graficoUm.pegaValorAtual(), graficoDois.pegaValorAtual(), graficoUm.pegaAnguloAtual());
                }
                return true;
            }
        });

        graficoDois.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!animacao) {
                    graficoDois.changeCursor(event);
                    graficoUm.setCursor(graficoDois.getCursor());
                    resultados.atualizarDados(graficoUm.pegaValorAtual(), graficoDois.pegaValorAtual(), graficoDois.pegaAnguloAtual());
                }
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

    public void setCursorConfig(int color, int width) {
        graficoUm.setCursorColor(color);
        graficoUm.setCursorWidth(width);
        graficoDois.setCursorColor(color);
        graficoDois.setCursorWidth(width);
        resultados.setColorAngulo(color);
    }

    public void setCursorStatus(boolean status) {
        this.cursorStatus = status;
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

    public void setGraficoGrade(boolean status) {
        graficoUm.setGradeStatus(status);
        graficoDois.setGradeStatus(status);
    }

    public void setColorTensaoUm(int color) {
        graficoUm.setColorPrincipal(color);
        circuito.setColorPrincipal(color);
        resultados.setColorTensao(color);
    }//TODO: TEM PROBLEMA AQUI

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

    public void setEspessuraDados(int width) {
        graficoUm.setEspessuraDados(width);
        graficoDois.setEspessuraDados(width);
    }

    public void addSerie(Serie serie, int grafico) {
        if (grafico == 1) {
            graficoUm.addSerie(serie);
            resultados.setTensaoMaxima(serie.valor[serie.max]);
        } else if (grafico == 2) {
            graficoDois.addSerie(serie);
            resultados.setCorrenteMaxima(serie.valor[serie.max]);
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

    public void setOndasSimultaneas (boolean status) {
        circuito.setCoresAjustaveis(status);
        this.OndasStatus = status;
    }

    public void setCircuitoWidth(int width) {
        circuito.setStrokeWidth(width);
    }

    public void setCircuitoColor(int color) {
        circuito.setColor(color);
    }
    public void setCircuitoAnimColor(int color){
        circuito.setColorAnimacao(color);
    }

    public void setRaioGrade(int raioGrade) {
        circuito.setRaioGrade(raioGrade);
    }

    public void setCircuitoGrade(boolean statusGrade) {
        circuito.setStatusGrade(statusGrade);
    }

    public String getCircuitoDimensoes() {
        return circuito.dimensoes();
    }

    public boolean startAnimacao() { //TODO: PAREI AQUI
        this.animacao = true;
        if ((graficoUm.serie != null) && (graficoDois != null)) {
            graficoUm.startAnimacao();
            graficoDois.startAnimacao();
            circuito.startAnimacao();
            resultados.setStatus(false);
            long tickTime = (3 * graficoUm.periodosReais * tempoAnimacao/(graficoUm.serie.tamanho));
            counter = new CountDownTimer(tempoAnimacao, tickTime) {

                public void onTick(long millisUntilFinished) {
                    long tStart = System.currentTimeMillis();
                    graficoUm.animar();
                    graficoDois.animar();
                    long tEnd = System.currentTimeMillis();
                    Log.d("TEMPO CONSUMIDO", String.valueOf(tEnd - tStart));
                    Log.d("TEMPO em Millis", String.valueOf(millisUntilFinished));
                    //circuito.animar();
                }

                public void onFinish() {
                    graficoUm.animar();
                    graficoDois.animar();
                    //circuito.animar();
                    graficoUm.startAnimacao();
                    graficoDois.startAnimacao();
                    circuito.startAnimacao();
                    resultados.limpar();
                    resultados.setStatus(false);
                    counter.start();
                }
            }.start();
            return true;
        } else {
            stopAnimacao();
            return false;
        }
    }

    public void stopAnimacao() {
        this.animacao = false;
        counter.cancel();
        graficoUm.stopAnimacao();
        graficoDois.stopAnimacao();
        circuito.stopAnimacao();
        if (cursorStatus) resultados.setStatus(true);
    }

    public void setAnimacaoTime(long time) {
        this.tempoAnimacao = time;
    }
    //endregion
}