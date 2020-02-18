package sorocaba.peteca.com.simuladorcircuito.graficosgerador;

import android.graphics.Color;
import android.graphics.Paint;

public class SerieBarras {
    public static final int tamanhoPontos = 15;
    public int[] valor_x;
    public double[] valor_y;
    private int numeroPontos;
    private ConfigPaint paintNormal, paintSelecionado;
    private class ConfigPaint {
        int StrokeWidth = 2;
        Paint.Style Style = Paint.Style.FILL_AND_STROKE;
        int colorFill = android.graphics.Color.YELLOW;
        int colorStroke = Color.BLACK;

    }
    public SerieBarras() {
        valor_x = new int[tamanhoPontos];
        valor_y = new double[tamanhoPontos];
        numeroPontos = 0;
        paintNormal = new ConfigPaint();
        paintSelecionado = new ConfigPaint();
    }

    public boolean addPonto(int x, double y) {
        if (numeroPontos < tamanhoPontos) {
            valor_x[numeroPontos] = x;
            valor_y[numeroPontos] = y;
            numeroPontos++;
            return true;
        } else {
            return false;
        }
    }
    public void reiniciar() {
        numeroPontos = 0;
    }
    public void limpar() {
        for (numeroPontos = 0; numeroPontos < tamanhoPontos; numeroPontos++){
            valor_x[numeroPontos] = 0;
            valor_y[numeroPontos] = 0;
        }
        reiniciar();
    }
    public int tamanho() {
        return numeroPontos;
    }

    public double valorMaximo() {
        double valorMaximo = valor_y[0];
        for (int ponto = 1; ponto < valor_y.length; ponto++) {
            if (valor_y[ponto] > valorMaximo)
                valorMaximo = valor_y[ponto];
        }
        return valorMaximo;
    }
    public double valorMinimo() {
        double valorMinimo = valor_y[0];
        for (int ponto = 1; ponto < valor_y.length; ponto++) {
            if (valor_y[ponto] < valorMinimo)
                valorMinimo = valor_y[ponto];
        }
        return valorMinimo;
    }

    public boolean valoresPositivos() {
        for (int ponto = 0; ponto < valor_y.length; ponto++) {
            if (valor_y[ponto] < 0)
                return false;
        }
        return true;
    }
    public boolean valoresNegativos() {
        for (int ponto = 0; ponto < valor_y.length; ponto++) {
            if (valor_y[ponto] > 0)
                return false;
        }
        return true;
    }

    public int getStrokeWidth() {
        return paintNormal.StrokeWidth;
    }
    public int getStrokeWidthSelecionado() {
        return paintSelecionado.StrokeWidth;
    }
    public void setStrokeWidth(int strokeWidth) {
        paintNormal.StrokeWidth = strokeWidth;
    }
    public void setStrokeWidthSelecionado(int strokeWidth) {
        paintSelecionado.StrokeWidth = strokeWidth;
    }

    public Paint.Style getStyle() {
        return paintNormal.Style;
    }
    public void setStyle(Paint.Style style) {
        paintNormal.Style = style;
        paintSelecionado.Style = style;
    }

    public int getColor() {
        return paintNormal.colorFill;
    }
    public int getColorStroke() {
        return paintNormal.colorStroke;
    }
    public int getColorSelecionado() {
        return paintSelecionado.colorFill;
    }
    public int getColorSelecionadoStroke() {
        return paintSelecionado.colorStroke;
    }
    public void setColor(int color) {
        paintNormal.colorFill = color;
    }
    public void setColorStroke(int color) {
        paintNormal.colorStroke = color;
    }
    public void setColorSelecionado(int color) {
        paintSelecionado.colorFill = color;
    }
    public void setColorSelecionadoStroke(int color) {
        paintSelecionado.colorStroke = color;
    }
}
