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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Circuito extends View {
    private int divisao = 10, tamanhoMinimo = 0, segundaDivisao = 0, ultimoSelecionado = -1;
    private boolean orientacaoVertical = false;
    private Path pathGrade, pathSelecao, pathAnimacao;
    private Paint paintFundo, paintGrade, paintSelecao, paintAnimacao;
    private Desenho desenhar;
    private RectF rectFundo;
    private boolean coresAjustaveis = false;

    private List<Path> pathTrilhas = new ArrayList<>();
    private List<Item> itemList = new ArrayList<>();

    // VARIAVEIS QUE O USUARIO PODE MUDAR
    private Paint paintDesenho;
    private int raioGrade = 2;
    private boolean statusGrade = false, animacao = false;

    InterfaceCircuito interfaceCircuito;
    private int colorNormal, colorPrincipal, colorSecundario, colorTerciario;
    private int itemcolorPrincipal, itemcolorSecundario, itemcolorTerciario;

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

        paintAnimacao = new Paint();
        paintAnimacao.setColor(Color.BLUE);
        paintAnimacao.setStyle(Paint.Style.FILL_AND_STROKE);

        pathGrade = new Path();
        pathSelecao = new Path();
        pathAnimacao = new Path();
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
                itemList.add(new Item(numeroItem, path, pontoUm, pontoDois));
            }
        }
    }

    public String dimensoes() {
        return (orientacaoVertical ? ("X =" + (segundaDivisao - 1) + " Y=" + (divisao - 1)) : ("X =" + (divisao - 1) + " Y=" + (segundaDivisao - 1)));
    }

    public void ToqueNaTela(MotionEvent event) {
        for (int elemento = 0; elemento < itemList.size(); elemento++) {
            Orientacao orientacao = desenhar.verificarOrientacao(itemList.get(elemento).pontoUm, itemList.get(elemento).pontoDois);
            if (orientacao == Orientacao.CIMA) {
                if ((event.getX() >= itemList.get(elemento).pontoUm.X - tamanhoMinimo) && (event.getX() <= itemList.get(elemento).pontoUm.X + tamanhoMinimo) &&
                        (event.getY() >= itemList.get(elemento).pontoDois.Y) && (event.getY() <= itemList.get(elemento).pontoUm.Y)) {
                    DesenhaSelecionado(itemList.get(elemento).pontoUm, itemList.get(elemento).pontoDois, orientacao);
                    ultimoSelecionado = itemList.get(elemento).numeroItem;
                }
            } else if (orientacao == Orientacao.BAIXO) {
                if ((event.getX() >= itemList.get(elemento).pontoUm.X - tamanhoMinimo) && (event.getX() <= itemList.get(elemento).pontoUm.X + tamanhoMinimo) &&
                        (event.getY() >= itemList.get(elemento).pontoUm.Y) && (event.getY() <= itemList.get(elemento).pontoDois.Y)) {
                    DesenhaSelecionado(itemList.get(elemento).pontoUm, itemList.get(elemento).pontoDois, orientacao);
                    ultimoSelecionado = itemList.get(elemento).numeroItem;
                }
            } else if (orientacao == Orientacao.ESQUERDA) {
                if ((event.getX() <= itemList.get(elemento).pontoDois.X) && (event.getX() >= itemList.get(elemento).pontoUm.X)&&
                        (event.getY() >= (itemList.get(elemento).pontoUm.Y - tamanhoMinimo) && (event.getY() <= itemList.get(elemento).pontoUm.Y + tamanhoMinimo))) {
                    DesenhaSelecionado(itemList.get(elemento).pontoUm, itemList.get(elemento).pontoDois, orientacao);
                    ultimoSelecionado = itemList.get(elemento).numeroItem;
                }
            } else if (orientacao == Orientacao.DIREITA) {
                if ((event.getX() <= itemList.get(elemento).pontoUm.X) && (event.getX() >= itemList.get(elemento).pontoDois.X) &&
                        (event.getY() >= (itemList.get(elemento).pontoUm.Y - tamanhoMinimo) && (event.getY() <= itemList.get(elemento).pontoUm.Y + tamanhoMinimo))) {
                    DesenhaSelecionado(itemList.get(elemento).pontoUm, itemList.get(elemento).pontoDois, orientacao);
                    ultimoSelecionado = itemList.get(elemento).numeroItem;
                }
            }
        }
        invalidate();
    }

    private void DesenhaSelecionado(Ponto pontoUm, Ponto pontoDois, Orientacao orientacao) {
        pathSelecao.reset();
        if (orientacao == Orientacao.CIMA)
            pathSelecao.addRect(new RectF(pontoUm.X - 1.2f * tamanhoMinimo, pontoDois.Y - 0.2f * tamanhoMinimo, pontoUm.X + 1.2f * tamanhoMinimo, pontoUm.Y + 0.2f * tamanhoMinimo), Path.Direction.CCW);
        else if (orientacao == Orientacao.BAIXO)
            pathSelecao.addRect(new RectF(pontoUm.X - 1.2f * tamanhoMinimo, pontoUm.Y - 0.2f * tamanhoMinimo, pontoUm.X + 1.2f * tamanhoMinimo, pontoDois.Y + 0.2f * tamanhoMinimo), Path.Direction.CCW);
        else if (orientacao == Orientacao.ESQUERDA)
            pathSelecao.addRect(new RectF(pontoUm.X - 0.2f * tamanhoMinimo, pontoUm.Y - 1.2f * tamanhoMinimo, pontoDois.X + 0.2f * tamanhoMinimo, pontoUm.Y + 1.2f * tamanhoMinimo), Path.Direction.CCW);
        else if (orientacao == Orientacao.DIREITA)
            pathSelecao.addRect(new RectF(pontoDois.X - 0.2f * tamanhoMinimo, pontoUm.Y - 1.2f * tamanhoMinimo, pontoUm.X + 0.2f * tamanhoMinimo, pontoUm.Y + 1.2f * tamanhoMinimo), Path.Direction.CCW);
    }

    public int selecionado() {
        return ultimoSelecionado;
    }
    //endregion

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(rectFundo, paintFundo);
        if(statusGrade)
            canvas.drawPath(pathGrade, paintGrade);
        for (int i = 0; i < pathTrilhas.size(); i++) {
            Path path = this.pathTrilhas.get(i);
            if (path != null)
                canvas.drawPath(path, paintDesenho);
        }

        for (Item item: itemList) {
            if (item.path != null) { //TODO: REVISAR ESSA LOGICA
                if (coresAjustaveis && item.numeroItem == itemcolorPrincipal)
                    paintDesenho.setColor(colorPrincipal);
                else if (coresAjustaveis && item.numeroItem == itemcolorSecundario)
                    paintDesenho.setColor(colorSecundario);
                else if (coresAjustaveis && item.numeroItem == itemcolorTerciario)
                    paintDesenho.setColor(colorTerciario);
                else
                    paintDesenho.setColor(colorNormal);
                canvas.drawPath(item.path, paintDesenho);
            }
        }

        if ((ultimoSelecionado > 0) && (pathSelecao != null)) {
            canvas.drawPath(pathSelecao, paintSelecao);
        }

        if(animacao) {
            canvas.drawPath(pathAnimacao, paintAnimacao);
        }
        super.onDraw(canvas);
    }
    @Override
    protected void onSizeChanged(int larguraTotal, int alturaTotal, int oldw, int oldh) {
        pathGrade.reset();
        pathTrilhas.clear();
        ultimoSelecionado = -1;
        itemList.clear();

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
        rectFundo = new RectF(0, 0, larguraTotal, alturaTotal);
        ultimoSelecionado = interfaceCircuito.circuitoPronto();
        for (Item item : itemList) {
            if (item.numeroItem == ultimoSelecionado) {
                DesenhaSelecionado(item.pontoUm, item.pontoDois, desenhar.verificarOrientacao(item.pontoUm, item.pontoDois));
            }
        }
        super.onSizeChanged(larguraTotal, alturaTotal, oldw, oldh);
    }

    //region getters e setters
    public void setStrokeWidth(int width) {
        paintDesenho.setStrokeWidth(width);
    }
    public void setColor(int color) {
        colorNormal = color;
    }
    public void setDivisao(int divisao) {
        this.divisao = divisao;
    }

    public void setCoresAjustaveis(boolean coresAjustaveis) {
        this.coresAjustaveis = coresAjustaveis;
    }
    public void setColorPrincipal(int color, int itemCor) {
        colorPrincipal = color;
        itemcolorPrincipal = itemCor;
    }
    public void setColorSecundario(int color, int itemCor) {
        colorSecundario = color;
        itemcolorSecundario = itemCor;
    }
    public void setColorTerciario(int color, int itemCor) {
        colorTerciario = color;
        itemcolorTerciario = itemCor;
    }

    public void setColorAnimacao(int color) {
        paintAnimacao.setColor(color);
    }
    public void setRaioGrade(int raioGrade) {
        this.raioGrade = raioGrade;
    }
    public void setStatusGrade(boolean statusGrade) {
        this.statusGrade = statusGrade;
    }

    public void setAnimacao(boolean animacao) {
        this.animacao = animacao;
    }
    public void startAnimacao() {
    }
    public void stopAnimacao() {
    }
    //endregion
}