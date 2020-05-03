package sorocaba.peteca.com.simuladorcircuito.circuitogerador;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class Circuito extends View {
    int divisao = 10, tamanhoMinimo = 0, segundaDivisao = 0, ultimoSelecionado = -1;
    boolean orientacaoVertical = false;

    private List<Ponto> matrizPontos = new ArrayList<>(); //TODO: VOU APAGAR DPS
    private List<Path> pathLists = new ArrayList<>();
    private List<Item> itemList = new ArrayList<>();
    Path pathGrade, pathSelecao;
    Paint paintFundo, paintGrade, paintSelecao;
    Desenho desenhar;

    // VARIAVEIS QUE O USUARIO PODE MUDAR
    int raioGrade = 2;
    private Paint paintDesenho;

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
        paintDesenho.setColor(Color.RED);
        paintDesenho.setStyle(Paint.Style.STROKE);
        paintDesenho.setStrokeWidth(5);

        pathGrade = new Path();
        pathSelecao = new Path();
    }

    //region TA OK

    public void grade() {
        pathGrade.reset();
        for (int i = 0; i < matrizPontos.size(); i++) {
            Ponto ponto = matrizPontos.get(i);
            pathGrade.addCircle(ponto.X, ponto.Y, raioGrade, Path.Direction.CCW);
        }
    }

    private boolean isValid(Ponto ponto) {
        return (orientacaoVertical) ? (ponto.X > 0 && ponto.X < segundaDivisao) && (ponto.Y > 0 && ponto.Y < divisao) : (ponto.X > 0 && ponto.X < divisao) && (ponto.Y > 0 && ponto.Y < segundaDivisao);
    }

    public void trilha(Ponto pontoUm, Ponto pontoDois) {
        if (isValid(pontoUm) && isValid(pontoDois)) {
            pathLists.add(desenhar.Trilha(pontoUm, pontoDois));
        }
    }

    public void trilha(Ponto pontoUm, Ponto pontoDois, Ponto pontoTres) {
        if (isValid(pontoUm) && isValid(pontoDois)) {
            pathLists.add(desenhar.Trilha(pontoUm, pontoDois, pontoTres));
        }
    }

    public void componente(Ponto pontoUm, Ponto pontoDois, int componente, int numeroItem) {
        if (isValid(pontoUm) && isValid(pontoDois)) {
            Path path = desenhar.componente(pontoUm, pontoDois, componente); // Ele atualiza os valores dos pontos
            if (path != null) {
                pathLists.add(path);
                itemList.add(new Item(numeroItem, new Ponto((pontoUm.X + pontoDois.X) / 2, (pontoUm.Y + pontoDois.Y) / 2)));
            }
        }
    }

    public String dimensoes() {
        return (orientacaoVertical ? ("X =" + (segundaDivisao - 1) + " Y=" + (divisao - 1)) : ("X =" + (divisao - 1) + " Y=" + (segundaDivisao - 1)));
    }
    //endregion

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(new Rect(0, 0, getWidth(), getHeight()), paintFundo);
        canvas.drawPath(pathGrade, paintGrade);

        for (int i = 0; i < pathLists.size(); i++) {
            Path path = this.pathLists.get(i);
            if (path != null) {
                canvas.drawPath(path, paintDesenho);
            }
        }

        if ((ultimoSelecionado > 0) && (pathSelecao != null)) {
            canvas.drawPath(pathSelecao, paintSelecao);
        }
        super.onDraw(canvas);
    }
    @Override
    protected void onSizeChanged(int larguraTotal, int alturaTotal, int oldw, int oldh) {
        matrizPontos.clear();
        if (larguraTotal > alturaTotal) { // retangulo deitado
            tamanhoMinimo = alturaTotal / divisao;
            orientacaoVertical = true;
            segundaDivisao = larguraTotal / tamanhoMinimo;

            for (int linha = 1; linha < divisao; linha++) {// A LOGICA É APAGAR ISSO DEPOIS
                for (int coluna = 1; coluna < segundaDivisao; coluna++) {
                    matrizPontos.add(new Ponto(coluna * tamanhoMinimo, linha * tamanhoMinimo));
                }
            }

        } else { // retangulo em pé
            tamanhoMinimo = larguraTotal / divisao;
            orientacaoVertical = false;
            segundaDivisao = alturaTotal / tamanhoMinimo;

            for (int linha = 1; linha < segundaDivisao; linha++) {
                for (int coluna = 1; coluna < divisao; coluna++) {
                    matrizPontos.add(new Ponto(coluna * tamanhoMinimo, linha * tamanhoMinimo));
                }
            }
        }

        grade();
        desenhar = new Desenho(tamanhoMinimo);
        //TODO: A LOGICA SERIA COLOCAR TUDO AQUI, QUANDO A CLASSE SABE O TAMANHO
        //TODO: ELE VAI PUXAR TODA A CONFIGURAÇÃO DE COMPONENTES PARA PINTAR E ARMAZENAR NO PATH
        //TODO: DAI O RESTO DA CLASSE FICA PARA MANDAR MENSAGENS E CONTROLES BLABLABLABLABLA

        pathLists.clear();
        ultimoSelecionado = interfaceCircuito.circuitoPronto();
        for (Item item: itemList) {
            if (item.numeroItem == ultimoSelecionado) {
                DesenhaSelecionado(item.ponto);
            }
        }
        super.onSizeChanged(larguraTotal, alturaTotal, oldw, oldh);
    }

    //region Tratamento do Toque
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
        RectF rectF = new RectF(ponto.X - tamanhoMinimo, ponto.Y - tamanhoMinimo, ponto.X + tamanhoMinimo, ponto.Y + tamanhoMinimo);
        pathSelecao.addRect(rectF, Path.Direction.CCW);
    }
    public int selecionado() {
        return ultimoSelecionado;
    }
    //endregion
}

//public class Circuito extends View {
//    private int divisoesAltura = 10, divisoesLargura, lado;
//    private List<Ponto> matrizPontos = new ArrayList<>();
//    private List<Path> pathLists  = new ArrayList<>();
//    private List<Paint> paintLists = new ArrayList<>();
//    private List<Item> itemList = new ArrayList<>();
//    private int ultimoSelecionado = -1;
//    private Path pathSelecao;
//    private Paint paintSelecao, paintFundo;
//
//    private void init() {
//        pathSelecao = new Path();
//        paintSelecao = new Paint();
//        paintSelecao.setStrokeWidth(4);
//        paintSelecao.setStyle(Paint.Style.STROKE);
//        paintSelecao.setColor(Color.DKGRAY);
//        paintFundo = new Paint();
//        paintFundo.setStyle(Paint.Style.FILL);
//        paintFundo.setColor(Color.WHITE);
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        canvas.drawRect(new Rect(0, 0, getWidth(), getHeight()), paintFundo);
//
//        for (int i = 0; i < pathLists.size(); i++) {
//            Path path = this.pathLists.get(i);
//            Paint paint = this.paintLists.get(i);
//            if (path != null) {
//                canvas.drawPath(path, paint);
//            }
//        }
//        if((ultimoSelecionado > 0) && (pathSelecao !=null)) {
//            canvas.drawPath(pathSelecao, paintSelecao);
//        }
//    }
//    public void ToqueNaTela(MotionEvent event) {
//        for (int elemento = 0; elemento < itemList.size(); elemento++) {
//            if ((event.getX() >= itemList.get(elemento).ponto.getX() - lado) && (event.getX() < itemList.get(elemento).ponto.getX() + lado) &&
//                    (event.getY() > itemList.get(elemento).ponto.getY() - lado) && (event.getY() < itemList.get(elemento).ponto.getY() + lado)) {
//                DesenhaSelecionado(itemList.get(elemento).ponto);
//                ultimoSelecionado = itemList.get(elemento).numeroItem;
//            }
//        }
//        invalidate();
//    }
//    private void DesenhaSelecionado(Ponto ponto) {
//        pathSelecao.reset();
//        RectF rectF = new RectF(ponto.getX() - lado, ponto.getY() - lado, ponto.getX() + lado, ponto.getY() + lado);
//        pathSelecao.addRect(rectF, Path.Direction.CCW);
//    }
//    public int selecionado() {
//        return ultimoSelecionado;
//    }
//}