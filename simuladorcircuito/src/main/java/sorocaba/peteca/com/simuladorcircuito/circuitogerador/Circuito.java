package sorocaba.peteca.com.simuladorcircuito.circuitogerador;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class Circuito extends View {
    private int divisao = 10, tamanhoMinimo = 0, segundaDivisao = 0, ultimoSelecionado = -1;
    private boolean orientacaoVertical = false;
    private Path pathGrade, pathSelecao;
    private Paint paintFundo, paintGrade, paintSelecao;
    private Desenho desenhar;
    private RectF rectF;

    private List<Path> pathTrilhas = new ArrayList<>();
    private List<Item> itemList = new ArrayList<>();

    // VARIAVEIS QUE O USUARIO PODE MUDAR
    private Paint paintDesenho;
    private int raioGrade = 2;
    private boolean statusGrade = false;

    InterfaceCircuito interfaceCircuito;
    public void setCircuitoListener(InterfaceCircuito interfaceCircuito) {
        this.interfaceCircuito = interfaceCircuito;
    }
    public interface InterfaceCircuito {
        int circuitoPronto();
    }

    public Circuito(Context context) {
        super(context);
        init();
    }
    public Circuito(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init() {
        paintFundo = new Paint();
        paintFundo.setStyle(Paint.Style.FILL);
        paintFundo.setColor(Color.WHITE);

        paintGrade = new Paint();
        paintGrade.setColor(Color.BLACK);
        paintGrade.setStyle(Paint.Style.FILL);

        paintSelecao = new Paint();
        paintSelecao.setStrokeWidth(4);
        paintSelecao.setStyle(Paint.Style.STROKE);
        paintSelecao.setColor(Color.DKGRAY);

        paintDesenho = new Paint();
        paintDesenho.setStrokeWidth(4);
        paintDesenho.setColor(Color.BLUE);
        paintDesenho.setStyle(Paint.Style.STROKE);

        pathGrade = new Path();
        pathSelecao = new Path();
    }

    //region TA OK
    private boolean isValid(Ponto ponto) {
        return (orientacaoVertical) ? (ponto.X > 0 && ponto.X < segundaDivisao) && (ponto.Y > 0 && ponto.Y < divisao) : (ponto.X > 0 && ponto.X < divisao) && (ponto.Y > 0 && ponto.Y < segundaDivisao);
    }

    public void trilha(Ponto pontoUm, Ponto pontoDois) {
        if (isValid(pontoUm) && isValid(pontoDois)) {
            pathTrilhas.add(desenhar.Trilha(pontoUm, pontoDois));
        }
    }

    public void trilha(Ponto pontoUm, Ponto pontoDois, Ponto pontoTres) {
        if (isValid(pontoUm) && isValid(pontoDois)) {
            pathTrilhas.add(desenhar.Trilha(pontoUm, pontoDois, pontoTres));
        }
    }

    public void componente(Ponto pontoUm, Ponto pontoDois, int componente, int numeroItem) {
        if (isValid(pontoUm) && isValid(pontoDois)) {
            Path path = desenhar.componente(pontoUm, pontoDois, componente); // Ele atualiza os valores dos pontos
            if (path != null) {
                pathTrilhas.add(path);
                itemList.add(new Item(numeroItem, path, new Ponto((pontoUm.X + pontoDois.X) / 2, (pontoUm.Y + pontoDois.Y) / 2)));
            }
        }
    }

    public String dimensoes() {
        return (orientacaoVertical ? ("X =" + (segundaDivisao - 1) + " Y=" + (divisao - 1)) : ("X =" + (divisao - 1) + " Y=" + (segundaDivisao - 1)));
    }

    public void ToqueNaTela(MotionEvent event) {
        for (int elemento = 0; elemento < itemList.size(); elemento++) {
            if (event.getX() >= (itemList.get(elemento).ponto.X - tamanhoMinimo) && (event.getX() <= itemList.get(elemento).ponto.X + tamanhoMinimo) &&
                    (event.getY() >= (itemList.get(elemento).ponto.Y - tamanhoMinimo) && (event.getY() <= itemList.get(elemento).ponto.Y + tamanhoMinimo))) {
                DesenhaSelecionado(itemList.get(elemento).ponto);
                ultimoSelecionado = itemList.get(elemento).numeroItem;
            }
        }
        invalidate();
    }

    private void DesenhaSelecionado(Ponto ponto) {
        pathSelecao.reset();
        RectF rectF = new RectF(ponto.X - 1.2f * tamanhoMinimo, ponto.Y - 1.2f * tamanhoMinimo, ponto.X + 1.2f * tamanhoMinimo, ponto.Y + 1.2f * tamanhoMinimo);
        pathSelecao.addRect(rectF, Path.Direction.CCW);
    }

    public int selecionado() {
        return ultimoSelecionado;
    }
    //endregion

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(rectF, paintFundo);
        if(statusGrade)
            canvas.drawPath(pathGrade, paintGrade);

        for (int i = 0; i < pathTrilhas.size(); i++) {
            Path path = this.pathTrilhas.get(i);
            if (path != null)
                canvas.drawPath(path, paintDesenho);
        }

        for (Item item: itemList) {
            if (item.path != null) {
                //TODO: Logica das cores ajustaveis tem que sair daqui
                canvas.drawPath(item.path, paintDesenho);
            }
        }

        if ((ultimoSelecionado > 0) && (pathSelecao != null)) {
            canvas.drawPath(pathSelecao, paintSelecao);
        }
        super.onDraw(canvas);
    }
    @Override
    protected void onSizeChanged(int larguraTotal, int alturaTotal, int oldw, int oldh) {
        pathGrade.reset();
        if (larguraTotal > alturaTotal) { // retangulo deitado
            tamanhoMinimo = alturaTotal / divisao;
            orientacaoVertical = true;
            segundaDivisao = larguraTotal / tamanhoMinimo;

            for (int linha = 1; linha < divisao; linha++) {
                for (int coluna = 1; coluna < segundaDivisao; coluna++) {
                    pathGrade.addCircle(coluna * tamanhoMinimo, linha * tamanhoMinimo, raioGrade, Path.Direction.CCW);
                }
            }

        } else { // retangulo em pé
            tamanhoMinimo = larguraTotal / divisao;
            orientacaoVertical = false;
            segundaDivisao = alturaTotal / tamanhoMinimo;

            for (int linha = 1; linha < segundaDivisao; linha++) {
                for (int coluna = 1; coluna < divisao; coluna++) {
                    pathGrade.addCircle(coluna * tamanhoMinimo, linha * tamanhoMinimo, raioGrade, Path.Direction.CCW);
                }
            }
        }

        desenhar = new Desenho(tamanhoMinimo);

        rectF = new RectF(0, 0, larguraTotal, alturaTotal);
        pathTrilhas.clear();
        ultimoSelecionado = interfaceCircuito.circuitoPronto();
        for (Item item : itemList) {
            if (item.numeroItem == ultimoSelecionado) {
                DesenhaSelecionado(item.ponto);
            }
        }
        super.onSizeChanged(larguraTotal, alturaTotal, oldw, oldh);
    }

    //region getters e setters
    public void setStrokeWidth(int width) {
        paintDesenho.setStrokeWidth(width);
    }
    public void setColor(int color) {
        paintDesenho.setColor(color);
    }
    public void setDivisao(int divisao) {
        this.divisao = divisao;
    }
    public void setColorPrincipal(int color) {
    }//TODO: PRECISAR ACHAR UMA MANEIRA DE DEIXAR AS FONTES COLORIDAS
    public void setColorSecundario(int color) {
    }
    public void setColorTerciario(int color) {
    }
    public void setRaioGrade(int raioGrade) {
        this.raioGrade = raioGrade;
    }
    public void setStatusGrade(boolean statusGrade) {
        this.statusGrade = statusGrade;
    }
    //endregion
}