package sorocaba.peteca.com.simuladorcircuito.graficosgerador;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class Grafico extends View {

    // Variaveis relacionados com o gráfico sem modificações
    private float alturaPontoX, alturaPontoY, alturaTotal, cursor, betaX;
    private float larguraPontoX, larguraPontoY, larguraTotal;
    private Path pathEixos, pathGrade, pathEscala, pathCursor, pathDados, pathDadosDois, pathDadosTres;
    private Paint paintEixos, paintGrade, paintTextos, paintCursor, paintDados, paintDadosDois, paintDadosTres;
    private Rect rect;
    private String[] periodosString = {"π/2", "π", "3π/2","2π"};

    // Variaveis disponiveis para o usuario fazer modificações
    private String nomeEixoX = "", nomeEixoY = "";
    private int periodos = 4, periodosReais = 1;
    private boolean cursorStatus = false, gradeStatus = false, betaAtivado = false;
    private float tamanhoMarcacoes = 0.05f, tamanhoText = 1.5f, subtamanhoText = 1.3f;
    private Serie serie, serieDois, serieTres;

    //region TA OK
    public Grafico(Context context) {
        super(context);
        init();
    }
    public Grafico(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init() {
        paintEixos = new Paint();
        paintEixos.setStrokeWidth(5);
        paintEixos.setStyle(Paint.Style.FILL_AND_STROKE);
        paintEixos.setColor(Color.BLACK);
        paintEixos.setAntiAlias(true);

        paintGrade = new Paint();
        paintGrade.setStrokeWidth(2);
        paintGrade.setStyle(Paint.Style.STROKE);
        paintGrade.setColor(Color.LTGRAY);
        paintGrade.setPathEffect(new DashPathEffect(new float[]{5, 10, 15, 20}, 0));
        paintGrade.setAntiAlias(true);

        paintTextos = new Paint();
        paintTextos.setTextAlign(Paint.Align.CENTER);
        paintTextos.setAntiAlias(true);

        paintCursor = new Paint();
        paintCursor.setStrokeWidth(3);
        paintCursor.setStyle(Paint.Style.STROKE);
        paintCursor.setColor(Color.DKGRAY);
        paintCursor.setAntiAlias(true);

        paintDados = new Paint();
        paintDados.setStrokeWidth(5);
        paintDados.setStyle(Paint.Style.STROKE);
        paintDados.setColor(Color.RED);
        paintDados.setAntiAlias(true);

        paintDadosDois = new Paint();
        paintDadosDois.setStrokeWidth(3);
        paintDadosDois.setStyle(Paint.Style.STROKE);
        paintDadosDois.setColor(Color.GREEN);
        paintDadosDois.setAntiAlias(true);

        paintDadosTres = new Paint();
        paintDadosTres.setStrokeWidth(3);
        paintDadosTres.setStyle(Paint.Style.STROKE);
        paintDadosTres.setColor(Color.YELLOW);
        paintDadosTres.setAntiAlias(true);

        pathEixos = new Path();
        pathGrade = new Path();
        pathCursor = new Path();
        pathEscala = new Path();
        pathDados = new Path();
        pathDadosDois = new Path();
        pathDadosTres = new Path();

        rect = new Rect();
    }

    private void atualizaEscala() {
        pathEscala.reset();
        for (int i = 1; i <= periodos; i++) {
            pathEscala.moveTo((i * (larguraPontoX - larguraPontoY) / periodos) + larguraPontoY, alturaPontoX * (1 - tamanhoMarcacoes));
            pathEscala.lineTo((i * (larguraPontoX - larguraPontoY) / periodos) + larguraPontoY, alturaPontoX * (1 + tamanhoMarcacoes));
        }
        atualizaGrade();
    }
    private void atualizaTextosEixos(Canvas canvas) {
        paintTextos.setColor(Color.BLACK);
        paintTextos.setTextSize(tamanhoText * alturaPontoY);
        canvas.drawText(nomeEixoY, larguraPontoY / 2, alturaPontoY * (1 + tamanhoText / 2), paintTextos);
        canvas.drawText(nomeEixoX, (larguraPontoX + larguraTotal) / 2, alturaPontoX + tamanhoText / 4 * alturaPontoY, paintTextos);
        if (betaAtivado)
            canvas.drawText("β", betaX, alturaPontoX * (1 - tamanhoMarcacoes), paintTextos);
        paintTextos.setColor(Color.BLACK);
        paintTextos.setTextSize(subtamanhoText * alturaPontoY);
        for (int i = 1; i <= periodos; i++)
            canvas.drawText(periodosString[i - 1], (i * (larguraPontoX - larguraPontoY) / periodos) + larguraPontoY, alturaPontoX * (1 + tamanhoMarcacoes) + subtamanhoText * alturaPontoY, paintTextos);
    }
    private void atualizaDados() {
        pathDados.reset();
        pathDadosDois.reset();
        pathDadosTres.reset();
        if (serie != null) {
            int max = serie.max;
            betaX = (((larguraPontoX - larguraPontoY)*serie.beta)/(serie.tamanho*periodosReais) + larguraPontoY);
            for (int periodo = 0; periodo < (periodosReais); periodo++) {
                float esquerda = ((larguraPontoX - larguraPontoY) * periodo) / periodosReais + larguraPontoY;
                float direita = ((larguraPontoX - larguraPontoY) * (periodo+1)) / periodosReais + larguraPontoY;
                pathDados.moveTo(esquerda, (float) ((serie.valor[0] / serie.valor[max]) * (alturaPontoY-alturaPontoX)) + alturaPontoX);
                for (int i = 1; i < serie.tamanho; i++)
                    pathDados.lineTo((i * (direita - esquerda)/serie.tamanho) + esquerda, (float) ((serie.valor[i] / serie.valor[max])*(alturaPontoY-alturaPontoX)) + alturaPontoX);
                if (serieDois != null) {
                    int maxDois = serieDois.max;
                    pathDadosDois.moveTo(esquerda, (float) ((serieDois.valor[0] / serieDois.valor[maxDois]) * (-1 * alturaPontoX + alturaPontoY)) + alturaPontoX);
                    for (int i = 1; i < serie.tamanho; i++)
                        pathDadosDois.lineTo((i * (direita - esquerda)/serieDois.tamanho) + esquerda, (float) ((serieDois.valor[i] / serieDois.valor[maxDois])*(alturaPontoY-alturaPontoX)) + alturaPontoX);
                }
                if (serieTres != null) {
                    int maxTres = serieTres.max;
                    pathDadosTres.moveTo(esquerda, (float) ((serie.valor[0] / serieTres.valor[maxTres]) * (-1 * alturaPontoX + alturaPontoY)) + alturaPontoX);
                    for (int i = 1; i < serie.tamanho; i++)
                        pathDadosTres.lineTo((i * (direita - esquerda)/ serieTres.tamanho) + esquerda, (float) ((serieTres.valor[i] / serieTres.valor[maxTres])*(alturaPontoY-alturaPontoX)) + alturaPontoX);
                }
            }
        }
        invalidate();
    }
    private void atualizaGrade() {
        pathGrade.reset();
        for (int i = 0; i < 5; i++) {
            if (i != 2) {
                pathGrade.moveTo(larguraPontoY, (alturaTotal - 2 * alturaPontoY) * i / 4.0f + alturaPontoY);
                pathGrade.lineTo(larguraPontoX, (alturaTotal - 2 * alturaPontoY) * i / 4.0f + alturaPontoY);
            }
        }
        for (int i = 1; i <= (periodos * 2); i++) {
            pathGrade.moveTo((larguraPontoX - larguraPontoY) * i / (2 * periodos) + larguraPontoY, alturaPontoY);
            pathGrade.lineTo((larguraPontoX - larguraPontoY) * i / (2 * periodos) + larguraPontoY, alturaTotal - alturaPontoY);
        }
    }

    public void changeCursor(MotionEvent event) {
        cursor = event.getX();
        if ((cursor > larguraPontoY) && (cursor < larguraPontoX)) {
            pathCursor.reset();
            pathCursor.moveTo(cursor, 0);
            pathCursor.lineTo(cursor, alturaTotal);
            invalidate();
        }
    }
    private void changeCursor() {
        if ((cursor > larguraPontoY) && (cursor < larguraPontoX)) {
            pathCursor.reset();
            pathCursor.moveTo(cursor, 0);
            pathCursor.lineTo(cursor, alturaTotal);
            invalidate();
        }
    }

    public void addSerie(Serie serie) {
        this.serie = serie;
        atualizaDados();
    }
    public void addSerie(Serie serie, Serie serieDois) {
        this.serie = serie;
        this.serieDois = serieDois;
        atualizaDados();
    }
    public void addSerie(Serie serie, Serie serieDois, Serie serieTres) {
        this.serie = serie;
        this.serieDois = serieDois;
        this.serieTres = serieTres;
        atualizaDados();
    }
    public void removeSerie() {
        this.serie = null;
        this.serieDois = null;
        this.serieTres = null;
    }
    //endregion

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paintEixos.setColor(Color.WHITE);
        canvas.drawRect(rect, paintEixos);
        paintEixos.setColor(Color.BLACK);
        canvas.drawPath(pathEixos, paintEixos);
        if (gradeStatus)
            canvas.drawPath(pathGrade, paintGrade);
        if (serieDois != null)
            canvas.drawPath(pathDadosDois, paintDadosDois);
        if (serieTres != null)
            canvas.drawPath(pathDadosTres, paintDadosTres);
        if (serie != null)
            canvas.drawPath(pathDados, paintDados);
        canvas.drawPath(pathEscala, paintEixos);
        atualizaTextosEixos(canvas);
        if (cursorStatus) {
            canvas.drawPath(pathCursor, paintCursor);
        }
    }
    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        alturaTotal = height;
        larguraTotal = width;
        alturaPontoY = 0.05f * alturaTotal;
        larguraPontoY = 0.04f * larguraTotal;
        alturaPontoX = 0.50f * alturaTotal;
        larguraPontoX = 0.94f * larguraTotal;

        pathEixos.reset();
        pathGrade.reset();

        pathEixos.moveTo(larguraPontoY, alturaPontoY);
        pathEixos.lineTo(larguraPontoY, alturaTotal - alturaPontoY);
        pathEixos.moveTo(larguraPontoY, alturaPontoX);
        pathEixos.lineTo(larguraPontoX, alturaPontoX);

        pathCursor.reset();
        pathCursor.moveTo(1.2f * larguraPontoY, 0);
        pathCursor.lineTo(1.2f * larguraPontoY, alturaTotal);

        rect = new Rect(0, 0, width, height);
        atualizaEscala();
        atualizaGrade();
        atualizaDados();
        super.onSizeChanged(width, height, oldw, oldh);
    }

    //region getters et setters
    public void setNomeEixoX(String nomeEixoX) {
        this.nomeEixoX = nomeEixoX;
    }

    public void setNomeEixoY(String nomeEixoY) {
        this.nomeEixoY = nomeEixoY;
    }

    public float getCursor() {
        return cursor;
    }

    public void setCursor(float cursor) {
        this.cursor = cursor;
        changeCursor();
    }

    public void setPeriodos(int periodos) {
        this.periodosReais = periodos;
        if (periodos != 3) {
            periodosString = new String[4];
            switch (periodos) {
                case 1:
                    periodosString[0] = "π/2";
                    periodosString[1] = "π";
                    periodosString[2] = "3π/2";
                    periodosString[3] = "2π";
                    break;
                case 2:
                    periodosString[0] = "π";
                    periodosString[1] = "2π";
                    periodosString[2] = "3π";
                    periodosString[3] = "4π";
                    break;
                case 4:
                    periodosString[0] = "2π";
                    periodosString[1] = "4π";
                    periodosString[2] = "6π";
                    periodosString[3] = "8π";
                    break;
            }
            this.periodos = 4;
        } else {
            periodosString = new String[6];
            periodosString[0] = "π";
            periodosString[1] = "2π";
            periodosString[2] = "3π";
            periodosString[3] = "4π";
            periodosString[4] = "5π";
            periodosString[5] = "6π";
            this.periodos = 6;
        }
        atualizaEscala();
        atualizaDados();
    }

    public void setGradeStatus(boolean gradeStatus) {
        this.gradeStatus = gradeStatus;
    }

    public void setEixosWidth(int espessura) {
        paintEixos.setStrokeWidth(espessura);

    }

    public void setEixosHeigthMarcacoes(float altura) {
        this.tamanhoMarcacoes = altura;
    }

    public void setEixosTextSize(float altura) {
        this.tamanhoText = altura;
    }

    public void setEixosSubTextSize(float altura) {
        this.subtamanhoText = altura;
    }

    public void setCursorColor(int corCursor) {
        paintCursor.setColor(corCursor);
    }

    public void setCursorStatus(boolean cursorStatus) {
        this.cursorStatus = cursorStatus;
    }

    public void setCursorWidth(int espessura) {
        paintCursor.setStrokeWidth(espessura);
    }

    public void setBeta(boolean betaAtivado) {
        this.betaAtivado = betaAtivado;
    }

    public void setColorPrincipal(int color) {
        paintDados.setColor(color);
    }
    public void setColorSecundario(int color) {
        paintDadosDois.setColor(color);
    }
    public void setColorTerciario(int color) {
        paintDadosTres.setColor(color);
    }

    //endregion
}
