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

    private int divisoesAltura = 10, divisoesLargura, lado;
    private List<Ponto> matrizPontos = new ArrayList<>();
    private List<Path> pathLists  = new ArrayList<>();
    private List<Paint> paintLists = new ArrayList<>();
    private List<Item> itemList = new ArrayList<>();
    private int ultimoSelecionado = -1;
    private Path pathSelecao;
    private Paint paintSelecao;
    private Paint paintFundo;

    public Circuito(Context context) {
        super(context);
        init();
    }
    public Circuito(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init() {
        pathSelecao = new Path();
        paintSelecao = new Paint();
        paintSelecao.setStrokeWidth(4);
        paintSelecao.setStyle(Paint.Style.STROKE);
        paintSelecao.setColor(Color.DKGRAY);
        paintFundo = new Paint();
        paintFundo.setStyle(Paint.Style.FILL);
        paintFundo.setColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(new Rect(0, 0, getWidth(), getHeight()), paintFundo);

        for (int i = 0; i < pathLists.size(); i++) {
            Path path = this.pathLists.get(i);
            Paint paint = this.paintLists.get(i);
            if (path != null) {
                canvas.drawPath(path, paint);
            }
        }
        if((ultimoSelecionado > 0) && (pathSelecao !=null)) {
            canvas.drawPath(pathSelecao, paintSelecao);
        }
    }

    public void ToqueNaTela(MotionEvent event) {
        for (int elemento = 0; elemento < itemList.size(); elemento++) {
            if ((event.getX() >= itemList.get(elemento).ponto.getX() - lado) && (event.getX() < itemList.get(elemento).ponto.getX() + lado) &&
                    (event.getY() > itemList.get(elemento).ponto.getY() - lado) && (event.getY() < itemList.get(elemento).ponto.getY() + lado)) {
                DesenhaSelecionado(itemList.get(elemento).ponto);
                ultimoSelecionado = itemList.get(elemento).numeroItem;
            }
        }
        invalidate();
    }

    private void DesenhaSelecionado(Ponto ponto) {
        pathSelecao.reset();
        RectF rectF = new RectF(ponto.getX() - lado, ponto.getY() - lado, ponto.getX() + lado, ponto.getY() + lado);
        pathSelecao.addRect(rectF, Path.Direction.CCW);
    }

    public void atualizar() {
        invalidate();
    }
    public int selecionado() {
        return ultimoSelecionado;
    }

    public Ponto Ponto(int linha, int coluna) {
        return matrizPontos.get((linha - 1) * (divisoesLargura + 1) + (coluna - 1));
    }
    public int iniciar (int numeroLinhas) {
        this.divisoesAltura = (numeroLinhas - 1);
        lado = (int) ((getHeight() * 0.98)/ divisoesAltura);
        divisoesLargura = getWidth()/lado;
        int borda = (int) ((getWidth() - (divisoesLargura * lado))/2.0);

        for (int linha = 0; linha < (divisoesAltura + 1); linha++) {
            for (int coluna = 0; coluna < (divisoesLargura + 1); coluna++) {
                matrizPontos.add(new Ponto((borda + coluna * lado), (int) (0.01 * getWidth() + linha * lado)));
            }
        }
        return (divisoesLargura + 1);
    }
    public void grade (int raio) {
        Path path = new Path();
        for (int i = 0; i < (divisoesAltura + 1) * (divisoesLargura + 1); i++) {
            Ponto ponto = matrizPontos.get(i);
            path.addCircle(ponto.getX(), ponto.getY(), raio, Path.Direction.CCW);
        }
        pathLists.add(path);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paintLists.add(paint);
    }

    public void add(String componente, Ponto ponto1, Ponto ponto2, Paint paint, int numeroItem) {
        switch (componente) {
            case "Fonte":
                Fonte(ponto1, ponto2, paint, numeroItem);
                break;
            case "Diodo":
                Diodo(ponto1, ponto2, paint, numeroItem);
                break;
            case "Tiristor":
                Tiristor(ponto1, ponto2, paint, numeroItem);
                break;
            case "CargaR":
                CargaR(ponto1, ponto2, paint, numeroItem);
                break;
            case "CargaRL":
                CargaRL(ponto1, ponto2, paint, numeroItem);
                break;
            case "CargaRLE":
                CargaRLE(ponto1, ponto2, paint, numeroItem);
                break;
            case "CargaRE":
                CargaRE(ponto1, ponto2, paint, numeroItem);
                break;
        }
    }
    public void add(String componente, Ponto ponto1, Ponto ponto2, Paint paint) {
        switch (componente) {
            case "Trilha":
                Trilha(ponto1, ponto2, paint);
                break;
            case "Terra":
                Terra(ponto1, ponto2, paint);
                break;
        }
    }
    public void add(String componente, Ponto ponto1, Ponto ponto2, Ponto ponto3, Ponto ponto4, Paint paint) {
        switch (componente) {
            case "Transformador":
                Transformador(ponto1, ponto2, ponto3, ponto4, paint);
                break;
        }
    }

    private void Fonte (Ponto ponto1, Ponto ponto2, Paint paint, int numeroItem) {
        Path path = new Path();
        // Fonte Vertical (Mesma coluna - Mesmo valor em X)
        if (ponto1.getX() == ponto2.getX()) {
            if (ponto1.getY() > ponto2.getY()) {
                int troca = ponto2.getY();
                ponto2.setY(ponto1.getY());
                ponto1.setY(troca);
            }
            int centroX = ponto1.getX(); // Como os dois valores são iguais, o valor é igual um deles.
            int centroY = (ponto1.getY()+ponto2.getY())/2;
            int raio = ((ponto2.getY() - ponto1.getY()) / 2) > 0 ? (ponto2.getY() - ponto1.getY())/2:(ponto2.getY() + ponto1.getY())/2;
            path.addCircle(centroX, centroY, raio, Path.Direction.CCW);
            RectF rectF = new RectF(-0.7f * raio + centroX, -0.5f * raio + centroY, centroX, +0.5f * raio + centroY);
            RectF rectF2 = new RectF(centroX, -0.5f * raio + centroY, +0.7f * raio + centroX, +0.5f * raio + centroY);
            path.addArc(rectF, 180, 180);
            path.addArc(rectF2, 180, -180);
        }
        // Fonte Horizontal (Mesma linha - Mesmo valor em y)
        else if (ponto1.getY() == ponto2.getY()) {
            if (ponto1.getX() > ponto2.getX()) {
                int troca = ponto2.getX();
                ponto2.setX(ponto1.getX());
                ponto1.setX(troca);
            }
            int centroX = (ponto1.getX()+ponto2.getX())/2;
            int centroY = ponto1.getY(); // Como os dois valores são iguais, o valor é igual um deles.
            int raio = ((ponto2.getX() - ponto1.getX())/2) > 0 ?((ponto2.getX() - ponto1.getX())/2):((ponto2.getX() + ponto1.getX())/2);
            path.addCircle(centroX, centroY, raio, Path.Direction.CCW);
            RectF rectF = new RectF(-0.5f * raio + centroX, centroY, 0.5f * raio + centroX, +0.7f * raio + centroY);
            RectF rectF2 = new RectF(-0.5f * raio + centroX, -0.7f * raio+centroY, 0.5f * raio + centroX, centroY);
            path.addArc(rectF, 270, -180);
            path.addArc(rectF2, 270, 180);
        }
        pathLists.add(path);
        paintLists.add(paint);
        Item item = new Item(numeroItem, new Ponto((ponto1.getX()+ponto2.getX())/2, (ponto1.getY()+ponto2.getY())/2));
        itemList.add(item);
    }
    private void Trilha(Ponto ponto1, Ponto ponto2, Paint paint) {
        Path path = new Path();
        //Trilha Vertical (Mesma coluna - Mesmo valor em X)
        if (ponto1.getX() == ponto2.getX()) {
            path.moveTo(ponto1.getX(), ponto1.getY());
            path.lineTo(ponto2.getX(), ponto2.getY());
        }
        // Trilha Horizontal (Mesma linha - Mesmo valor em y)
        else if (ponto1.getY() == ponto2.getY()) {
            path.moveTo(ponto1.getX(), ponto1.getY());
            path.lineTo(ponto2.getX(), ponto2.getY());
        }
        pathLists.add(path);
        paintLists.add(paint);
    }
    private void Diodo(Ponto ponto1, Ponto ponto2, Paint paint, int numeroItem) {
        Path path = new Path();
        // Diodo Vertical (Mesma coluna - Mesmo valor em X)
        if (ponto1.getX() == ponto2.getX()) {
            int distancia = (ponto2.getY() - ponto1.getY()) > 0? (ponto2.getY() - ponto1.getY()): (ponto1.getY() - ponto2.getY());
            path.moveTo(ponto2.getX(), ponto2.getY());
            path.lineTo(ponto1.getX()+0.4f*distancia, ponto1.getY());
            path.lineTo(ponto1.getX()-0.4f*distancia, ponto1.getY());
            path.lineTo(ponto2.getX(), ponto2.getY());
            path.lineTo(ponto2.getX()+0.4f*distancia, ponto2.getY());
            path.lineTo(ponto2.getX()-0.4f*distancia, ponto2.getY());
        }
        // Diodo Horizontal (Mesma linha - Mesmo valor em y)
        else if (ponto1.getY() == ponto2.getY()) {
            int distancia = (ponto2.getX() - ponto1.getX()) > 0? (ponto2.getX() - ponto1.getX()): (ponto1.getX() - ponto2.getX());
            path.moveTo(ponto2.getX(), ponto2.getY());
            path.lineTo(ponto1.getX(), ponto1.getY()+0.4f*distancia);
            path.lineTo(ponto1.getX(), ponto1.getY()-0.4f*distancia);
            path.lineTo(ponto2.getX(), ponto2.getY());
            path.lineTo(ponto2.getX(), ponto2.getY()+0.4f*distancia);
            path.lineTo(ponto2.getX(), ponto2.getY()-0.4f*distancia);
        }

        pathLists.add(path);
        paintLists.add(paint);
        Item item = new Item(numeroItem, new Ponto((ponto1.getX()+ponto2.getX())/2, (ponto1.getY()+ponto2.getY())/2));
        itemList.add(item);
    } // ACHO QUE DÁ PARA MELHORAR - SEGUNDA
    private void Tiristor(Ponto ponto1, Ponto ponto2, Paint paint, int numeroItem) {
        Path path = new Path();
        // Tiristor Vertical (Mesma coluna - Mesmo valor em X)
        if (ponto1.getX() == ponto2.getX()) {
            int distancia = (ponto2.getY() - ponto1.getY()) > 0? (ponto2.getY() - ponto1.getY()): (ponto1.getY() - ponto2.getY());
            path.moveTo(ponto2.getX(), ponto2.getY());
            path.lineTo(ponto1.getX()+0.4f*distancia, ponto1.getY());
            path.lineTo(ponto1.getX()-0.4f*distancia, ponto1.getY());
            path.lineTo(ponto2.getX(), ponto2.getY());
            path.lineTo(ponto2.getX()+0.5f*distancia, ponto2.getY());
            path.lineTo(ponto2.getX()-0.5f*distancia, ponto2.getY());
            path.lineTo(ponto2.getX(), ponto2.getY());
            path.lineTo(ponto2.getX()+0.4f*distancia, ponto2.getY()+0.4f*distancia);
            path.lineTo(ponto2.getX()+0.7f*distancia, ponto2.getY()+0.4f*distancia);
        }

        // Tiristor Horizontal (Mesma linha - Mesmo valor em y)
        else if (ponto1.getY() == ponto2.getY()) {
            int distancia = (ponto2.getX() - ponto1.getX()) > 0? (ponto2.getX() - ponto1.getX()): (ponto1.getX() - ponto2.getX());
            path.moveTo(ponto2.getX(), ponto2.getY());
            path.lineTo(ponto1.getX(), ponto1.getY()+0.4f*distancia);
            path.lineTo(ponto1.getX(), ponto1.getY()-0.4f*distancia);
            path.lineTo(ponto2.getX(), ponto2.getY());
            path.lineTo(ponto2.getX(), ponto2.getY()+0.5f*distancia);
            path.lineTo(ponto2.getX(), ponto2.getY()-0.5f*distancia);
            path.lineTo(ponto2.getX(), ponto2.getY());
            path.lineTo(ponto2.getX()+0.4f*distancia, ponto2.getY()-0.4f*distancia);
            path.lineTo(ponto2.getX()+0.4f*distancia, ponto2.getY()-0.7f*distancia);
        }
        pathLists.add(path);
        paintLists.add(paint);
        Item item = new Item(numeroItem, new Ponto((ponto1.getX()+ponto2.getX())/2, (ponto1.getY()+ponto2.getY())/2));
        itemList.add(item);
    } // PRECISA ARRUMAR E DÁ PARA MELHORAR - SEGUNDA
    private void CargaR(Ponto ponto1, Ponto ponto2, Paint paint, int numeroItem) {
        Path path = new Path();
        if (ponto1.getX() == ponto2.getX()) { // Resistor Vertical - mesma coluna
            int distancia = (ponto2.getY() - ponto1.getY()) > 0? (ponto2.getY() - ponto1.getY()): (ponto1.getY() - ponto2.getY());
            path.moveTo(ponto1.getX(), ponto1.getY());
            path.lineTo(ponto1.getX()+ 0.2f*distancia, ponto1.getY()+0.1f*distancia);
            path.lineTo(ponto1.getX()- 0.2f*distancia, ponto1.getY()+0.26f*distancia);
            path.lineTo(ponto1.getX()+ 0.2f*distancia, ponto1.getY()+0.42f*distancia);
            path.lineTo(ponto1.getX()- 0.2f*distancia, ponto1.getY()+0.58f*distancia);
            path.lineTo(ponto1.getX()+ 0.2f*distancia, ponto1.getY()+0.74f*distancia);
            path.lineTo(ponto1.getX()- 0.2f*distancia, ponto1.getY()+0.90f*distancia);
            path.lineTo(ponto2.getX(), ponto1.getY() + 1.0F*distancia);
        } else if (ponto1.getY() == ponto2.getY()) { // Resistor Horizontal - mesma linha
            int distancia = (ponto2.getX() - ponto1.getX()) > 0? (ponto2.getX() - ponto1.getX()): (ponto1.getX() - ponto2.getX());
            path.moveTo(ponto1.getX(), ponto1.getY());
            path.lineTo(ponto1.getX()+ 0.1f*distancia, ponto1.getY()-0.2f*distancia);
            path.lineTo(ponto1.getX()+ 0.26f*distancia, ponto1.getY()+0.2f*distancia);
            path.lineTo(ponto1.getX()+ 0.42f*distancia, ponto1.getY()-0.2f*distancia);
            path.lineTo(ponto1.getX()+ 0.58f*distancia, ponto1.getY()+0.2f*distancia);
            path.lineTo(ponto1.getX()+ 0.74f*distancia, ponto1.getY()-0.2f*distancia);
            path.lineTo(ponto1.getX()+ 0.90f*distancia, ponto1.getY()+0.2f*distancia);
            path.lineTo(ponto1.getX()+ 1.0f*distancia, ponto2.getY());
        }
        pathLists.add(path);
        paintLists.add(paint);
        Item item = new Item(numeroItem, new Ponto((ponto1.getX()+ponto2.getX())/2, (ponto1.getY()+ponto2.getY())/2));
        itemList.add(item);
    } // PRECISA ARRUMAR - SEGUNDA
    private void CargaRL(Ponto ponto1, Ponto ponto2, Paint paint, int numeroItem) {
        Path path = new Path();
        if (ponto1.getX() == ponto2.getX()) { // Resistor e Indutor Vertical - mesma coluna
            int distancia = ((ponto2.getY() - ponto1.getY()) > 0? (ponto2.getY() - ponto1.getY()): (ponto1.getY() - ponto2.getY()));
            distancia = (distancia/2);
            path.moveTo(ponto1.getX(), ponto1.getY());
            path.lineTo(ponto1.getX()+ 0.2f*distancia, ponto1.getY()+0.1f*distancia);
            path.lineTo(ponto1.getX()- 0.2f*distancia, ponto1.getY()+0.26f*distancia);
            path.lineTo(ponto1.getX()+ 0.2f*distancia, ponto1.getY()+0.42f*distancia);
            path.lineTo(ponto1.getX()- 0.2f*distancia, ponto1.getY()+0.58f*distancia);
            path.lineTo(ponto1.getX()+ 0.2f*distancia, ponto1.getY()+0.74f*distancia);
            path.lineTo(ponto1.getX()- 0.2f*distancia, ponto1.getY()+0.90f*distancia);
            path.lineTo(ponto1.getX(), ponto1.getY() + 1.0F*distancia);
            int meioY = ponto1.getY() + distancia;
            path.moveTo(ponto1.getX(), meioY);
            path.lineTo(ponto1.getX(), meioY + 0.1f * distancia);
            RectF rectF1 = new RectF(ponto1.getX() - 0.3f * distancia, meioY + 0.1f * distancia,
                    ponto1.getX() + 0.3f * distancia, meioY + 0.40f * distancia);
            path.addArc(rectF1, -90, -230);
            RectF rectF2 = new RectF(ponto1.getX() - 0.3f * distancia, meioY + 0.30f * distancia,
                    ponto1.getX() + 0.3f * distancia, meioY + 0.6f * distancia);
            path.addArc(rectF2, -40, -280);
            RectF rectF3 = new RectF(ponto1.getX() - 0.3f * distancia, meioY + 0.5f * distancia,
                    ponto1.getX() + 0.3f * distancia, meioY + 0.8f * distancia);
            path.addArc(rectF3, -40, -280);
            RectF rectF4 = new RectF(ponto1.getX() - 0.3f * distancia, meioY + 0.7f * distancia,
                    ponto1.getX() + 0.3f * distancia, ponto2.getY());
            path.addArc(rectF4, -40, -230);

        } else if (ponto1.getY() == ponto2.getY()) { // Resistor e Indutor Horizontal - mesma linha
            int distancia = (ponto2.getX() - ponto1.getX()) > 0? (ponto2.getX() - ponto1.getX()): (ponto1.getX() - ponto2.getX());
            distancia = distancia/2;
            path.moveTo(ponto1.getX(), ponto1.getY());
            path.lineTo(ponto1.getX()+ 0.1f*distancia, ponto1.getY()-0.2f*distancia);
            path.lineTo(ponto1.getX()+ 0.26f*distancia, ponto1.getY()+0.2f*distancia);
            path.lineTo(ponto1.getX()+ 0.42f*distancia, ponto1.getY()-0.2f*distancia);
            path.lineTo(ponto1.getX()+ 0.58f*distancia, ponto1.getY()+0.2f*distancia);
            path.lineTo(ponto1.getX()+ 0.74f*distancia, ponto1.getY()-0.2f*distancia);
            path.lineTo(ponto1.getX()+ 0.90f*distancia, ponto1.getY()+0.2f*distancia);
            path.lineTo(ponto1.getX()+ 1.0f*distancia, ponto2.getY());

            int meioX = ponto1.getX() + distancia;
            path.moveTo(meioX, ponto1.getY());
            path.lineTo(meioX + 0.1f * distancia, ponto1.getY());
            RectF rectF1 = new RectF(meioX + 0.1f * distancia, ponto1.getY() - 0.3f * distancia,
                    meioX + 0.40f * distancia, ponto1.getY() + 0.3f * distancia) ;
            path.addArc(rectF1, -180, -230);
            RectF rectF2 = new RectF(meioX + 0.30f * distancia, ponto1.getY() - 0.3f * distancia,
                    meioX + 0.6f * distancia, ponto1.getY() + 0.3f * distancia);
            path.addArc(rectF2, -130, -280);
            RectF rectF3 = new RectF(meioX + 0.5f * distancia,ponto1.getY() - 0.3f * distancia,
                    meioX + 0.8f * distancia,ponto1.getY() + 0.3f * distancia);
            path.addArc(rectF3, -130, -280);
            RectF rectF4 = new RectF(meioX + 0.7f * distancia, ponto1.getY() - 0.3f * distancia,
                    ponto2.getX(), ponto2.getY() + 0.3f * distancia);
            path.addArc(rectF4, -130, -230);
        }
        pathLists.add(path);
        paintLists.add(paint);
        Item item = new Item(numeroItem, new Ponto((ponto1.getX()+ponto2.getX())/2, (ponto1.getY()+ponto2.getY())/2));
        itemList.add(item);
    } // PRECISA ARRUMAR - SEGUNDA
    private void CargaRLE(Ponto ponto1, Ponto ponto2, Paint paint, int numeroItem) {
        Path path = new Path();
        if (ponto1.getX() == ponto2.getX()) { // Resistor Vertical - mesma coluna
            float distancia = ((ponto2.getY() - ponto1.getY()) > 0? (ponto2.getY() - ponto1.getY()): (ponto1.getY() - ponto2.getY()));
            distancia = (distancia/2.5f);
            path.moveTo(ponto1.getX(), ponto1.getY());
            path.lineTo(ponto1.getX()+ 0.2f*distancia, ponto1.getY()+0.1f*distancia);
            path.lineTo(ponto1.getX()- 0.2f*distancia, ponto1.getY()+0.26f*distancia);
            path.lineTo(ponto1.getX()+ 0.2f*distancia, ponto1.getY()+0.42f*distancia);
            path.lineTo(ponto1.getX()- 0.2f*distancia, ponto1.getY()+0.58f*distancia);
            path.lineTo(ponto1.getX()+ 0.2f*distancia, ponto1.getY()+0.74f*distancia);
            path.lineTo(ponto1.getX()- 0.2f*distancia, ponto1.getY()+0.90f*distancia);
            path.lineTo(ponto1.getX(), ponto1.getY() + 1.0F*distancia);
            float meioY = ponto1.getY() + distancia;
            path.moveTo(ponto1.getX(), meioY);
            path.lineTo(ponto1.getX(), meioY + 0.1f * distancia);
            RectF rectF1 = new RectF(ponto1.getX() - 0.3f * distancia, meioY + 0.1f * distancia,
                    ponto1.getX() + 0.3f * distancia, meioY + 0.40f * distancia);
            path.addArc(rectF1, -90, -230);
            RectF rectF2 = new RectF(ponto1.getX() - 0.3f * distancia, meioY + 0.30f * distancia,
                    ponto1.getX() + 0.3f * distancia, meioY + 0.6f * distancia);
            path.addArc(rectF2, -40, -280);
            RectF rectF3 = new RectF(ponto1.getX() - 0.3f * distancia, meioY + 0.5f * distancia,
                    ponto1.getX() + 0.3f * distancia, meioY + 0.8f * distancia);
            path.addArc(rectF3, -40, -280);
            RectF rectF4 = new RectF(ponto1.getX() - 0.3f * distancia, meioY + 0.7f * distancia,
                    ponto1.getX() + 0.3f * distancia, ponto1.getY() + 2.0f * distancia);
            path.addArc(rectF4, -40, -230);

            float tercoY = ponto1.getY() + 2.0f * distancia;
            path.lineTo(ponto1.getX(), tercoY + 0.2f * distancia);
            path.moveTo(ponto1.getX(), tercoY + 0.2f * distancia);
            path.lineTo(ponto1.getX() + 0.3f * distancia, tercoY + 0.2f * distancia);
            path.lineTo(ponto1.getX() - 0.3f * distancia, tercoY + 0.2f * distancia);

            path.moveTo(ponto1.getX(), tercoY + 0.3f * distancia);
            path.lineTo(ponto1.getX() + 0.15f * distancia, tercoY + 0.3f * distancia);
            path.lineTo(ponto1.getX() - 0.15f * distancia, tercoY + 0.3f * distancia);
            path.moveTo(ponto1.getX(), tercoY + 0.3f * distancia);
            path.lineTo(ponto2.getX(), ponto2.getY());

        } else if (ponto1.getY() == ponto2.getY()) { // Diodo Horizontal - mesma linha
            float distancia = (ponto2.getX() - ponto1.getX()) > 0? (ponto2.getX() - ponto1.getX()): (ponto1.getX() - ponto2.getX());
            distancia = distancia/2.5f;
            path.moveTo(ponto1.getX(), ponto1.getY());
            path.lineTo(ponto1.getX()+ 0.1f*distancia, ponto1.getY()-0.2f*distancia);
            path.lineTo(ponto1.getX()+ 0.26f*distancia, ponto1.getY()+0.2f*distancia);
            path.lineTo(ponto1.getX()+ 0.42f*distancia, ponto1.getY()-0.2f*distancia);
            path.lineTo(ponto1.getX()+ 0.58f*distancia, ponto1.getY()+0.2f*distancia);
            path.lineTo(ponto1.getX()+ 0.74f*distancia, ponto1.getY()-0.2f*distancia);
            path.lineTo(ponto1.getX()+ 0.90f*distancia, ponto1.getY()+0.2f*distancia);
            path.lineTo(ponto1.getX()+ 1.0f*distancia, ponto2.getY());

            float meioX = ponto1.getX() + distancia;
            path.moveTo(meioX, ponto1.getY());
            path.lineTo(meioX + 0.1f * distancia, ponto1.getY());
            RectF rectF1 = new RectF(meioX + 0.1f * distancia, ponto1.getY() - 0.3f * distancia,
                    meioX + 0.40f * distancia, ponto1.getY() + 0.3f * distancia) ;
            path.addArc(rectF1, -180, -230);
            RectF rectF2 = new RectF(meioX + 0.30f * distancia, ponto1.getY() - 0.3f * distancia,
                    meioX + 0.6f * distancia, ponto1.getY() + 0.3f * distancia);
            path.addArc(rectF2, -130, -280);
            RectF rectF3 = new RectF(meioX + 0.5f * distancia,ponto1.getY() - 0.3f * distancia,
                    meioX + 0.8f * distancia,ponto1.getY() + 0.3f * distancia);
            path.addArc(rectF3, -130, -280);
            RectF rectF4 = new RectF(meioX + 0.7f * distancia, ponto1.getY() - 0.3f * distancia,
                    ponto1.getX() + 2.0f * distancia, ponto2.getY() + 0.3f * distancia);
            path.addArc(rectF4, -130, -230);

            float tercoX = ponto1.getX() + 2.0f * distancia;
            path.lineTo(tercoX + 0.2f * distancia, ponto1.getY());
            path.moveTo(tercoX + 0.2f * distancia, ponto1.getY());
            path.lineTo(tercoX + 0.2f * distancia, ponto1.getY() + 0.3f * distancia);
            path.lineTo(tercoX + 0.2f * distancia, ponto1.getY() - 0.3f * distancia);

            path.moveTo(tercoX + 0.3f * distancia, ponto1.getY());
            path.lineTo(tercoX + 0.3f * distancia, ponto1.getY() + 0.15f * distancia);
            path.lineTo(tercoX + 0.3f * distancia, ponto1.getY() - 0.15f * distancia);
            path.moveTo(tercoX + 0.3f * distancia, ponto1.getY());
            path.lineTo(ponto2.getX(), ponto2.getY());
        }
        pathLists.add(path);
        paintLists.add(paint);
        Item item = new Item(numeroItem, new Ponto((ponto1.getX()+ponto2.getX())/2, (ponto1.getY()+ponto2.getY())/2));
        itemList.add(item);
    } // PRECISA ARRUMAR E MUITO - SEGUNDA
    private void CargaRE(Ponto ponto1, Ponto ponto2, Paint paint, int numeroItem) {
        Path path = new Path();
        if (ponto1.getX() == ponto2.getX()) { // Resistor e Bateria Vertical - mesma coluna
            int distancia = ((ponto2.getY() - ponto1.getY()) > 0? (ponto2.getY() - ponto1.getY()): (ponto1.getY() - ponto2.getY()));
            distancia = (distancia/2);
            path.moveTo(ponto1.getX(), ponto1.getY());
            path.lineTo(ponto1.getX()+ 0.2f*distancia, ponto1.getY()+0.1f*distancia);
            path.lineTo(ponto1.getX()- 0.2f*distancia, ponto1.getY()+0.26f*distancia);
            path.lineTo(ponto1.getX()+ 0.2f*distancia, ponto1.getY()+0.42f*distancia);
            path.lineTo(ponto1.getX()- 0.2f*distancia, ponto1.getY()+0.58f*distancia);
            path.lineTo(ponto1.getX()+ 0.2f*distancia, ponto1.getY()+0.74f*distancia);
            path.lineTo(ponto1.getX()- 0.2f*distancia, ponto1.getY()+0.90f*distancia);
            path.lineTo(ponto1.getX(), ponto1.getY() + 1.0F*distancia);
            int meioY = ponto1.getY() + distancia;
            path.lineTo(ponto1.getX(), meioY + 0.35f * distancia);
            path.moveTo(ponto1.getX(), meioY + 0.35f * distancia);
            path.lineTo(ponto1.getX() + 0.3f * distancia, meioY + 0.35f * distancia);
            path.lineTo(ponto1.getX() - 0.3f * distancia, meioY + 0.35f * distancia);

            path.moveTo(ponto1.getX(), meioY + 0.5f * distancia);
            path.lineTo(ponto1.getX() + 0.15f * distancia, meioY + 0.5f * distancia);
            path.lineTo(ponto1.getX() - 0.15f * distancia, meioY + 0.5f * distancia);
            path.moveTo(ponto1.getX(), meioY + 0.5f * distancia);
            path.lineTo(ponto2.getX(), ponto2.getY());

        } else if (ponto1.getY() == ponto2.getY()) { // Resistor e Bateria Horizontal - mesma linha
            int distancia = (ponto2.getX() - ponto1.getX()) > 0? (ponto2.getX() - ponto1.getX()): (ponto1.getX() - ponto2.getX());
            distancia = distancia/2;
            path.moveTo(ponto1.getX(), ponto1.getY());
            path.lineTo(ponto1.getX()+ 0.1f*distancia, ponto1.getY()-0.2f*distancia);
            path.lineTo(ponto1.getX()+ 0.26f*distancia, ponto1.getY()+0.2f*distancia);
            path.lineTo(ponto1.getX()+ 0.42f*distancia, ponto1.getY()-0.2f*distancia);
            path.lineTo(ponto1.getX()+ 0.58f*distancia, ponto1.getY()+0.2f*distancia);
            path.lineTo(ponto1.getX()+ 0.74f*distancia, ponto1.getY()-0.2f*distancia);
            path.lineTo(ponto1.getX()+ 0.90f*distancia, ponto1.getY()+0.2f*distancia);
            path.lineTo(ponto1.getX()+ 1.0f*distancia, ponto2.getY());

            int meioX = ponto1.getX() + distancia;
            path.lineTo(meioX + 0.3f * distancia, ponto1.getY());
            path.moveTo(meioX + 0.3f * distancia, ponto1.getY());
            path.lineTo(meioX + 0.3f * distancia, ponto1.getY() + 0.5f * distancia);
            path.lineTo(meioX + 0.3f * distancia, ponto1.getY() - 0.3f * distancia);

            path.moveTo(meioX + 0.5f * distancia, ponto1.getY());
            path.lineTo(meioX + 0.5f * distancia, ponto1.getY() + 0.15f * distancia);
            path.lineTo(meioX + 0.5f * distancia, ponto1.getY() - 0.15f * distancia);
            path.moveTo(meioX + 0.5f * distancia, ponto1.getY());
            path.lineTo(ponto2.getX(), ponto2.getY());
        }
        pathLists.add(path);
        paintLists.add(paint);
        Item item = new Item(numeroItem, new Ponto((ponto1.getX()+ponto2.getX())/2, (ponto1.getY()+ponto2.getY())/2));
        itemList.add(item);
    } // PRECISA ARRUMAR E MUITO - SEGUNDA
    private void Transformador(Ponto ponto1, Ponto ponto2, Ponto ponto3, Ponto ponto4, Paint paint) {
        Path path = new Path();

        if ((ponto1.getX() == ponto2.getX()) && (ponto3.getX() == ponto4.getX())) { // Vertical
            int distanciaEsq = (ponto2.getY() - ponto1.getY())/4;
            RectF rectF1 = new RectF(ponto1.getX() - 0.75f * distanciaEsq, ponto1.getY(),
                    ponto1.getX() + 0.75f * distanciaEsq, ponto1.getY() + distanciaEsq);
            path.addArc(rectF1, -90, 180);

            RectF rectF2 = new RectF(ponto1.getX() - 0.75f * distanciaEsq, ponto1.getY() + distanciaEsq,
                    ponto1.getX() + 0.75f * distanciaEsq, ponto1.getY() + 2*distanciaEsq);
            path.addArc(rectF2, -90, 180);

            RectF rectF3 = new RectF(ponto1.getX() - 0.75f * distanciaEsq, ponto1.getY() + 2*distanciaEsq,
                    ponto1.getX() + 0.75f * distanciaEsq, ponto1.getY() + 3* distanciaEsq);
            path.addArc(rectF3, -90, 180);

            RectF rectF4 = new RectF(ponto1.getX() - 0.75f * distanciaEsq, ponto1.getY() + 3*distanciaEsq,
                    ponto1.getX() + 0.75f * distanciaEsq, ponto1.getY() + 4*distanciaEsq);
            path.addArc(rectF4, -90, 180);

            path.moveTo((ponto1.getX() + ponto3.getX())/2, ponto1.getY());
            path.lineTo((ponto2.getX() + ponto4.getX())/2, ponto2.getY());

            int distanciaDir = (ponto4.getY() - ponto3.getY())/4;
            RectF rectF1d = new RectF(ponto3.getX() - 0.75f * distanciaDir, ponto3.getY(),
                    ponto3.getX() + 0.75f * distanciaDir, ponto3.getY() + distanciaDir);
            path.addArc(rectF1d, -90, -180);

            RectF rectF2d = new RectF(ponto3.getX() - 0.75f * distanciaDir, ponto3.getY() + 1*distanciaDir,
                    ponto3.getX() + 0.75f * distanciaDir, ponto3.getY() + 2*distanciaDir);
            path.addArc(rectF2d, -90, -180);

            RectF rectF3d = new RectF(ponto3.getX() - 0.75f * distanciaDir, ponto3.getY() + 2*distanciaDir,
                    ponto3.getX() + 0.75f * distanciaDir, ponto3.getY() + 3* distanciaDir);
            path.addArc(rectF3d, -90, -180);

            RectF rectF4d = new RectF(ponto3.getX() - 0.75f * distanciaDir, ponto3.getY() + 3*distanciaDir,
                    ponto3.getX() + 0.75f * distanciaDir, ponto3.getY() + 4*distanciaDir);
            path.addArc(rectF4d, -90, -180);

        }
        pathLists.add(path);
        paintLists.add(paint);
    } // ACHO QUE DÁ PARA MELHORAR - SEGUNDA
    private void Terra(Ponto ponto1, Ponto ponto2, Paint paint) {
        Path path = new Path();
        if (ponto1.getX() == ponto2.getX()) { // Fonte Vertical - mesma coluna
            int distancia = (ponto1.getY() - ponto2.getY())/4;
            path.moveTo(ponto1.getX(),ponto1.getY());
            path.lineTo(ponto1.getX() , ponto1.getY()- 1.5f* distancia);

            path.moveTo(ponto1.getX(), ponto1.getY() - 1.5f* distancia);
            path.lineTo(ponto1.getX() + 2.5f * distancia, ponto1.getY() - 1.5f* distancia);
            path.lineTo(ponto1.getX() - 2.5f * distancia, ponto1.getY() - 1.5f* distancia);

            path.moveTo(ponto1.getX(), ponto1.getY() - 2.5f*distancia);
            path.lineTo(ponto1.getX() + 1.5f * distancia, ponto1.getY() - 2.5f*distancia);
            path.lineTo(ponto1.getX() - 1.5f * distancia, ponto1.getY() - 2.5f*distancia);

            path.moveTo(ponto1.getX(), ponto1.getY() - 3.5f*distancia);
            path.lineTo(ponto1.getX() + 1.0f * distancia, ponto1.getY() - 3.5f*distancia);
            path.lineTo(ponto1.getX() - 1.0f * distancia, ponto1.getY() - 3.5f*distancia);

        } else if (ponto1.getY() == ponto2.getY()) { // Fonte Horizontal - mesma linha
            int distancia = (ponto1.getX() - ponto2.getX())/4;
            path.moveTo(ponto1.getX(),ponto1.getY());
            path.lineTo(ponto1.getX() - 1.5f* distancia , ponto1.getY());

            path.moveTo(ponto1.getX() - 1.5f* distancia, ponto1.getY() );
            path.lineTo(ponto1.getX() - 1.5f* distancia, ponto1.getY() + 2.5f * distancia);
            path.lineTo(ponto1.getX() - 1.5f* distancia, ponto1.getY() - 2.5f * distancia);

            path.moveTo(ponto1.getX() - 2.5f* distancia, ponto1.getY() );
            path.lineTo(ponto1.getX() - 2.5f* distancia, ponto1.getY() + 1.5f * distancia);
            path.lineTo(ponto1.getX() - 2.5f* distancia, ponto1.getY() - 1.5f * distancia);

            path.moveTo(ponto1.getX() - 3.5f* distancia, ponto1.getY() );
            path.lineTo(ponto1.getX() - 3.5f* distancia, ponto1.getY() + 1.0f * distancia);
            path.lineTo(ponto1.getX() - 3.5f* distancia, ponto1.getY() - 1.0f * distancia);
        }
        pathLists.add(path);
        paintLists.add(paint);
    } // PRECISA ARRUMAR E MUITO - SEGUNDA
}