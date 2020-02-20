package sorocaba.peteca.com.simuladorcircuito.graficosgerador;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class Grafico extends View {

    // Variaveis relacionados com o gráfico sem modificações
    private float alturaPontoX, alturaPontoY, alturaTotal, cursor;
    private float larguraPontoX, larguraPontoY, larguraTotal;
    private Path pathEixos, pathEscala, pathGrade, pathCursor;
    private Paint paintEixos, paintGrade, paintTextos;

    // Variaveis disponiveis para o usuario fazer modificações
    private String nomeEixoX = "", nomeEixoY = "";
    private int periodos = 4;

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

        paintGrade = new Paint();
        paintGrade.setStrokeWidth(3);
        paintGrade.setStyle(Paint.Style.STROKE);
        paintGrade.setColor(Color.DKGRAY);

        paintTextos = new Paint();

        paintEixos.setAntiAlias(true);
        paintGrade.setAntiAlias(true);
        paintTextos.setAntiAlias(true);

        pathEixos = new Path();
        pathGrade = new Path();
        pathCursor = new Path();
        pathEscala = new Path();
    }

    public void atualizar() {
        invalidate();
    } //TODO: TALVEZ EU VA TIRA-LA

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paintEixos.setColor(Color.WHITE);
        canvas.drawRect(new Rect(0, 0, getWidth(), getHeight()), paintEixos);
        if (true) {
            canvas.drawPath(pathGrade, paintGrade);
            atualizaTextosEixos(canvas);
        }
        //paintEixos.setStyle(Paint.Style.STROKE);
        paintEixos.setColor(Color.BLACK);
        canvas.drawPath(pathEixos, paintEixos);
        canvas.drawPath(pathEscala, paintEixos);
        canvas.drawPath(pathCursor, paintEixos);
   }

   //TODO: ARRUMAR ESSES ATUALIZA, TENTAR DEIXAR O MAIS ENXUTO POSSÍVEL
    public void atualizaEscala() {
        pathEscala.reset();
        for (int i = 1; i <= periodos; i++) {
            pathEscala.moveTo((i*(larguraPontoX-larguraPontoY)/periodos)+larguraPontoY, alturaPontoX * 0.95f);
            pathEscala.lineTo((i*(larguraPontoX-larguraPontoY)/periodos)+larguraPontoY, alturaPontoX * 1.05f);
        }
    }
    private void atualizaTextosEixos(Canvas canvas) {
        paintTextos.setTextSize(1.2f * alturaPontoY); //TODO: COLOCAR ESSE SIZE COMO PARAMETRO, JE M'ENFICHE !!!
        paintTextos.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(nomeEixoY, larguraPontoY/2, 1.6f * alturaPontoY, paintTextos);
        paintTextos.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(nomeEixoX, (larguraTotal), alturaPontoX + 0.6f * alturaPontoY, paintTextos);

        for (int i = 1; i <= periodos; i++) {
            //π
            canvas.drawText(i + "π", (i*(larguraPontoX-larguraPontoY)/periodos)+larguraPontoY, alturaPontoX * 1.08f, paintTextos);
        }
    }

    public void addSerie() {

    }
    public void removeSerie() {

    }

    public void changeCursor(MotionEvent event) {
        cursor = event.getX();
        if((cursor > larguraPontoY) && (cursor < larguraPontoX)) {
            pathCursor.reset();
            pathCursor.moveTo(cursor, 0);
            pathCursor.lineTo(cursor, alturaTotal);
            invalidate();
        }
    }
    public void changeCursor() {
        if((cursor > larguraPontoY) && (cursor < larguraPontoX)) {
            pathCursor.reset();
            pathCursor.moveTo(cursor, 0);
            pathCursor.lineTo(cursor, alturaTotal);
            invalidate();
        }
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        alturaTotal = height;
        larguraTotal = width;
        alturaPontoY = 0.05f * alturaTotal;
        larguraPontoY = 0.05f * larguraTotal;
        alturaPontoX = 0.50f * alturaTotal;
        larguraPontoX = 0.95f * larguraTotal;

        pathEixos.reset();
        pathGrade.reset();

        pathEixos.moveTo(larguraPontoY, alturaPontoY);
        pathEixos.lineTo(larguraPontoY, 2 * alturaPontoX - alturaPontoY);
        pathEixos.moveTo(larguraPontoY, alturaPontoX);
        pathEixos.lineTo(larguraPontoX, alturaPontoX);

        atualizaEscala();
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
    public int getPeriodos() {
        return periodos;
    }
    public void setPeriodos(int periodos) {
        if (periodos != 3)
            this.periodos = 4;
        else
            this.periodos = 6;
        atualizaEscala();
    }
//endregion
}
