package sorocaba.peteca.com.simuladorcircuito.circuitogerador;

import android.graphics.Path;
import android.graphics.RectF;

public class Desenho {
    public static final float distDiodo = 0.15f;
    public static final float tamanhoDiodo = 0.35f;
    public static final float distTiristor = 0.15f;
    public static final float tamanhoTiristor = 0.35f;
    public static final float distResistor = 0f;
    public static final float tamanhoResistor = 0.23f;
    public static final float distRL = 0.0f;
    public static final float tamanhoRL = 0.23f;
    private int tamanhoMinimo;

    enum Orientacao {
        CIMA,
        BAIXO,
        ESQUERDA,
        DIREITA,
        ERRO
    }

    public Desenho(int tamanhoMinimo) {
        this.tamanhoMinimo = tamanhoMinimo;
    }

    public Path Trilha(Ponto pontoUm, Ponto pontoDois) {
        Path path = new Path();
        path.moveTo(pontoUm.X * tamanhoMinimo, pontoUm.Y * tamanhoMinimo);
        path.lineTo(pontoDois.X * tamanhoMinimo, pontoDois.Y * tamanhoMinimo);
        return path;
    }

    public Path Trilha(Ponto pontoUm, Ponto pontoDois, Ponto pontoTres) {
        Path path = new Path();
        path.moveTo(pontoUm.X * tamanhoMinimo, pontoUm.Y * tamanhoMinimo);
        path.lineTo(pontoDois.X * tamanhoMinimo, pontoDois.Y * tamanhoMinimo);
        path.lineTo(pontoTres.X * tamanhoMinimo, pontoTres.Y * tamanhoMinimo);
        return path;
    }

    public Path componente(Ponto pontoUm, Ponto pontoDois, int componente) {
        Orientacao orientacao = verificarOrientacao(pontoUm, pontoDois);
        if (orientacao != Orientacao.ERRO) {
            pontoUm.X *= tamanhoMinimo;
            pontoUm.Y *= tamanhoMinimo;
            pontoDois.X *= tamanhoMinimo;
            pontoDois.Y *= tamanhoMinimo;
            switch (componente) {
                case 1:
                    return fonte(pontoUm, pontoDois, orientacao);
                case 2:
                    return diodo(pontoUm, pontoDois, orientacao);
                case 3:
                    return tiristor(pontoUm, pontoDois, orientacao);
                case 4:
                    return cargaR(pontoUm, pontoDois, orientacao);
                case 5:
                    return cargaRL(pontoUm, pontoDois, orientacao);
                case 6:
                    return cargaRLE(pontoUm, pontoDois, orientacao);
                case 7:
                    return cargaRE(pontoUm, pontoDois, orientacao);
                case 8:
                    return terra(pontoUm, pontoDois, orientacao);
                case 9:
                    return trasfP1(pontoUm, pontoDois, orientacao);
                case 10:
                    return transfP2(pontoUm, pontoDois, orientacao);
            }
        }
        return null;
    }

    private Orientacao verificarOrientacao(Ponto pontoUm, Ponto pontoDois) {
        int X = (pontoUm.X - pontoDois.X), Y = (pontoUm.Y - pontoDois.Y);
        if ((X == 0) && (Y != 0)) {
            return (Y > 0) ? Orientacao.CIMA : Orientacao.BAIXO;
        } else if ((Y == 0) && (X != 0)) {
            return (X > 0) ? Orientacao.DIREITA : Orientacao.ESQUERDA;
        }
        return Orientacao.ERRO;
    }

    //region Desenhos OK
    private Path fonte(Ponto pontoUm, Ponto pontoDois, Orientacao orientacao) {
        Path path = new Path();
        if (orientacao == Orientacao.CIMA || orientacao == Orientacao.BAIXO) {
            int centroX = pontoUm.X, centroY = (pontoUm.Y + pontoDois.Y) / 2, raio = Math.abs((pontoDois.Y - pontoUm.Y) / 2);
            path.addCircle(centroX, centroY, raio, Path.Direction.CCW);
            path.addArc(new RectF(-0.7f * raio + centroX, -0.5f * raio + centroY, centroX, +0.5f * raio + centroY), 180, 180);
            path.addArc(new RectF(centroX, -0.5f * raio + centroY, +0.7f * raio + centroX, +0.5f * raio + centroY), 180, -180);
        } else if (orientacao == Orientacao.ESQUERDA || orientacao == Orientacao.DIREITA) {
            int centroX = (pontoUm.X + pontoDois.X) / 2, centroY = pontoUm.Y, raio = Math.abs((pontoDois.X - pontoUm.X) / 2);
            path.addCircle(centroX, centroY, raio, Path.Direction.CCW);
            path.addArc(new RectF(-0.5f * raio + centroX, centroY, 0.5f * raio + centroX, +0.7f * raio + centroY), 270, -180);
            path.addArc(new RectF(-0.5f * raio + centroX, -0.7f * raio + centroY, 0.5f * raio + centroX, centroY), 270, 180);
        }
        return path;
    }

    private Path diodo(Ponto pontoUm, Ponto pontoDois, Orientacao orientacao) {
        Path path = new Path();
        if (orientacao == Orientacao.CIMA) {
            int distancia = Math.abs(pontoDois.Y - pontoUm.Y);
            path.moveTo(pontoDois.X, pontoDois.Y);
            path.lineTo(pontoDois.X, pontoDois.Y + distDiodo * distancia);
            path.lineTo(pontoUm.X + tamanhoDiodo * distancia, pontoUm.Y - distDiodo * distancia);
            path.lineTo(pontoUm.X - tamanhoDiodo * distancia, pontoUm.Y - distDiodo * distancia);
            path.lineTo(pontoDois.X, pontoDois.Y + distDiodo * distancia);
            path.lineTo(pontoUm.X + tamanhoDiodo * distancia, pontoDois.Y + distDiodo * distancia);
            path.lineTo(pontoUm.X - tamanhoDiodo * distancia, pontoDois.Y + distDiodo * distancia);
            path.moveTo(pontoUm.X, pontoUm.Y - distDiodo * distancia);
            path.lineTo(pontoUm.X, pontoUm.Y);

        } else if (orientacao == Orientacao.BAIXO) {
            int distancia = Math.abs(pontoDois.Y - pontoUm.Y);
            path.moveTo(pontoDois.X, pontoDois.Y);
            path.lineTo(pontoDois.X, pontoDois.Y - distDiodo * distancia);
            path.lineTo(pontoUm.X - tamanhoDiodo * distancia, pontoUm.Y + distDiodo * distancia);
            path.lineTo(pontoUm.X + tamanhoDiodo * distancia, pontoUm.Y + distDiodo * distancia);
            path.lineTo(pontoDois.X, pontoDois.Y - distDiodo * distancia);
            path.lineTo(pontoUm.X - tamanhoDiodo * distancia, pontoDois.Y - distDiodo * distancia);
            path.lineTo(pontoUm.X + tamanhoDiodo * distancia, pontoDois.Y - distDiodo * distancia);
            path.moveTo(pontoUm.X, pontoUm.Y + distDiodo * distancia);
            path.lineTo(pontoUm.X, pontoUm.Y);
        } else if (orientacao == Orientacao.ESQUERDA) {
            int distancia = Math.abs(pontoDois.X - pontoUm.X);
            path.moveTo(pontoDois.X, pontoDois.Y);
            path.lineTo(pontoDois.X - distDiodo * distancia, pontoDois.Y);
            path.lineTo(pontoUm.X + distDiodo * distancia, pontoUm.Y + tamanhoDiodo * distancia);
            path.lineTo(pontoUm.X + distDiodo * distancia, pontoUm.Y - tamanhoDiodo * distancia);
            path.lineTo(pontoDois.X - distDiodo * distancia, pontoDois.Y);
            path.lineTo(pontoDois.X - distDiodo * distancia, pontoDois.Y + tamanhoDiodo * distancia);
            path.lineTo(pontoDois.X - distDiodo * distancia, pontoDois.Y - tamanhoDiodo * distancia);
            path.moveTo(pontoUm.X + distDiodo * distancia, pontoUm.Y);
            path.lineTo(pontoUm.X, pontoUm.Y);
        } else if (orientacao == Orientacao.DIREITA) {
            int distancia = Math.abs(pontoDois.X - pontoUm.X);
            path.moveTo(pontoDois.X, pontoDois.Y);
            path.lineTo(pontoDois.X + distDiodo * distancia, pontoDois.Y);
            path.lineTo(pontoUm.X - distDiodo * distancia, pontoUm.Y - tamanhoDiodo * distancia);
            path.lineTo(pontoUm.X - distDiodo * distancia, pontoUm.Y + tamanhoDiodo * distancia);
            path.lineTo(pontoDois.X + distDiodo * distancia, pontoDois.Y);
            path.lineTo(pontoDois.X + distDiodo * distancia, pontoDois.Y - tamanhoDiodo * distancia);
            path.lineTo(pontoDois.X + distDiodo * distancia, pontoDois.Y + tamanhoDiodo * distancia);
            path.moveTo(pontoUm.X - distDiodo * distancia, pontoUm.Y);
            path.lineTo(pontoUm.X, pontoUm.Y);
        }
        return path;
    }

    private Path tiristor(Ponto pontoUm, Ponto pontoDois, Orientacao orientacao) {
        Path path = new Path();
        if (orientacao == Orientacao.CIMA) {
            int distancia = Math.abs(pontoDois.Y - pontoUm.Y);
            path.moveTo(pontoDois.X, pontoDois.Y);
            path.lineTo(pontoDois.X, pontoDois.Y + distTiristor * distancia);
            path.lineTo(pontoUm.X + tamanhoTiristor * distancia, pontoUm.Y - distTiristor * distancia);
            path.lineTo(pontoUm.X - tamanhoTiristor * distancia, pontoUm.Y - distTiristor * distancia);
            path.lineTo(pontoDois.X, pontoDois.Y + distTiristor * distancia);
            path.lineTo(pontoDois.X - tamanhoTiristor / 2 * distancia, pontoDois.Y);
            path.lineTo(pontoDois.X - (tamanhoTiristor + distTiristor) * distancia, pontoDois.Y);
            path.moveTo(pontoDois.X + tamanhoTiristor * distancia, pontoDois.Y + distTiristor * distancia);
            path.lineTo(pontoDois.X - tamanhoTiristor * distancia, pontoDois.Y + distTiristor * distancia);
            path.moveTo(pontoUm.X, pontoUm.Y - distTiristor * distancia);
            path.lineTo(pontoUm.X, pontoUm.Y);
        } else if (orientacao == Orientacao.BAIXO) {
            int distancia = Math.abs(pontoDois.Y - pontoUm.Y);
            path.moveTo(pontoDois.X, pontoDois.Y);
            path.lineTo(pontoDois.X, pontoDois.Y - distTiristor * distancia);
            path.lineTo(pontoUm.X - tamanhoTiristor * distancia, pontoUm.Y + distTiristor * distancia);
            path.lineTo(pontoUm.X + tamanhoTiristor * distancia, pontoUm.Y + distTiristor * distancia);
            path.lineTo(pontoDois.X, pontoDois.Y - distTiristor * distancia);
            path.lineTo(pontoDois.X + tamanhoTiristor / 2 * distancia, pontoDois.Y);
            path.lineTo(pontoDois.X + (tamanhoTiristor + distTiristor) * distancia, pontoDois.Y);
            path.moveTo(pontoDois.X - tamanhoTiristor * distancia, pontoDois.Y - distTiristor * distancia);
            path.lineTo(pontoDois.X + tamanhoTiristor * distancia, pontoDois.Y - distTiristor * distancia);
            path.moveTo(pontoUm.X, pontoUm.Y + distTiristor * distancia);
            path.lineTo(pontoUm.X, pontoUm.Y);
        } else if (orientacao == Orientacao.ESQUERDA) {
            int distancia = Math.abs(pontoDois.X - pontoUm.X);
            path.moveTo(pontoDois.X, pontoDois.Y);
            path.lineTo(pontoDois.X - distTiristor * distancia, pontoDois.Y);
            path.lineTo(pontoUm.X + distTiristor * distancia, pontoUm.Y - tamanhoTiristor * distancia);
            path.lineTo(pontoUm.X + distTiristor * distancia, pontoUm.Y + tamanhoTiristor * distancia);
            path.lineTo(pontoDois.X - distTiristor * distancia, pontoDois.Y);
            path.lineTo(pontoDois.X, pontoDois.Y - tamanhoTiristor / 2 * distancia);
            path.lineTo(pontoDois.X, pontoDois.Y - (tamanhoTiristor + distTiristor) * distancia);
            path.moveTo(pontoDois.X - distTiristor * distancia, pontoDois.Y - tamanhoTiristor * distancia);
            path.lineTo(pontoDois.X - distTiristor * distancia, pontoDois.Y + tamanhoTiristor * distancia);
            path.moveTo(pontoUm.X + distTiristor * distancia, pontoUm.Y);
            path.lineTo(pontoUm.X, pontoUm.Y);
        } else if (orientacao == Orientacao.DIREITA) {
            int distancia = Math.abs(pontoDois.X - pontoUm.X);
            path.moveTo(pontoDois.X, pontoDois.Y);
            path.lineTo(pontoDois.X + distTiristor * distancia, pontoDois.Y);
            path.lineTo(pontoUm.X - distTiristor * distancia, pontoUm.Y + tamanhoTiristor * distancia);
            path.lineTo(pontoUm.X - distTiristor * distancia, pontoUm.Y - tamanhoTiristor * distancia);
            path.lineTo(pontoDois.X + distTiristor * distancia, pontoDois.Y);
            path.lineTo(pontoDois.X, pontoDois.Y + tamanhoTiristor / 2 * distancia);
            path.lineTo(pontoDois.X, pontoDois.Y + (tamanhoTiristor + distTiristor) * distancia);
            path.moveTo(pontoDois.X + distTiristor * distancia, pontoDois.Y + tamanhoTiristor * distancia);
            path.lineTo(pontoDois.X + distTiristor * distancia, pontoDois.Y - tamanhoTiristor * distancia);
            path.moveTo(pontoUm.X - distTiristor * distancia, pontoUm.Y);
            path.lineTo(pontoUm.X, pontoUm.Y);
        }
        return path;
    }

    private Path cargaR(Ponto pontoUm, Ponto pontoDois, Orientacao orientacao) {
        Path path = new Path();
        if (orientacao == Orientacao.CIMA) {
            int distancia = Math.abs(pontoDois.Y - pontoUm.Y);
            float passo = (1 - 2 * distResistor) / 12;
            path.moveTo(pontoUm.X, pontoUm.Y);
            path.lineTo(pontoUm.X, pontoUm.Y - distResistor * distancia);
            path.lineTo(pontoUm.X - tamanhoResistor * distancia, pontoUm.Y - (distResistor + 1 * passo) * distancia);
            path.lineTo(pontoUm.X + tamanhoResistor * distancia, pontoUm.Y - (distResistor + 3 * passo) * distancia);
            path.lineTo(pontoUm.X - tamanhoResistor * distancia, pontoUm.Y - (distResistor + 5 * passo) * distancia);
            path.lineTo(pontoUm.X + tamanhoResistor * distancia, pontoUm.Y - (distResistor + 7 * passo) * distancia);
            path.lineTo(pontoUm.X - tamanhoResistor * distancia, pontoUm.Y - (distResistor + 9 * passo) * distancia);
            path.lineTo(pontoUm.X + tamanhoResistor * distancia, pontoUm.Y - (distResistor + 11 * passo) * distancia);
            path.lineTo(pontoDois.X, pontoDois.Y + distResistor * distancia);
            path.lineTo(pontoDois.X, pontoDois.Y);
        } else if (orientacao == Orientacao.BAIXO) {
            int distancia = Math.abs(pontoDois.Y - pontoUm.Y);
            float passo = (1 - 2 * distResistor) / 12;
            path.moveTo(pontoUm.X, pontoUm.Y);
            path.lineTo(pontoUm.X, pontoUm.Y + distResistor * distancia);
            path.lineTo(pontoUm.X + tamanhoResistor * distancia, pontoUm.Y + (distResistor + 1 * passo) * distancia);
            path.lineTo(pontoUm.X - tamanhoResistor * distancia, pontoUm.Y + (distResistor + 3 * passo) * distancia);
            path.lineTo(pontoUm.X + tamanhoResistor * distancia, pontoUm.Y + (distResistor + 5 * passo) * distancia);
            path.lineTo(pontoUm.X - tamanhoResistor * distancia, pontoUm.Y + (distResistor + 7 * passo) * distancia);
            path.lineTo(pontoUm.X + tamanhoResistor * distancia, pontoUm.Y + (distResistor + 9 * passo) * distancia);
            path.lineTo(pontoUm.X - tamanhoResistor * distancia, pontoUm.Y + (distResistor + 11 * passo) * distancia);
            path.lineTo(pontoDois.X, pontoDois.Y - distResistor * distancia);
            path.lineTo(pontoDois.X, pontoDois.Y);
        } else if (orientacao == Orientacao.ESQUERDA) {
            int distancia = Math.abs(pontoDois.X - pontoUm.X);
            float passo = (1 - 2 * distResistor) / 12;
            path.moveTo(pontoUm.X, pontoUm.Y);
            path.lineTo(pontoUm.X + distResistor * distancia, pontoUm.Y);
            path.lineTo(pontoUm.X + (distResistor + 1 * passo) * distancia, pontoUm.Y - tamanhoResistor * distancia);
            path.lineTo(pontoUm.X + (distResistor + 3 * passo) * distancia, pontoUm.Y + tamanhoResistor * distancia);
            path.lineTo(pontoUm.X + (distResistor + 5 * passo) * distancia, pontoUm.Y - tamanhoResistor * distancia);
            path.lineTo(pontoUm.X + (distResistor + 7 * passo) * distancia, pontoUm.Y + tamanhoResistor * distancia);
            path.lineTo(pontoUm.X + (distResistor + 9 * passo) * distancia, pontoUm.Y - tamanhoResistor * distancia);
            path.lineTo(pontoUm.X + (distResistor + 11 * passo) * distancia, pontoUm.Y + tamanhoResistor * distancia);
            path.lineTo(pontoDois.X - distResistor * distancia, pontoDois.Y);
            path.lineTo(pontoDois.X, pontoDois.Y);
        } else if (orientacao == Orientacao.DIREITA) {
            int distancia = Math.abs(pontoDois.X - pontoUm.X);
            float passo = (1 - 2 * distResistor) / 12;
            path.moveTo(pontoUm.X, pontoUm.Y);
            path.lineTo(pontoUm.X - distResistor * distancia, pontoUm.Y);
            path.lineTo(pontoUm.X - (distResistor + 1 * passo) * distancia, pontoUm.Y + tamanhoResistor * distancia);
            path.lineTo(pontoUm.X - (distResistor + 3 * passo) * distancia, pontoUm.Y - tamanhoResistor * distancia);
            path.lineTo(pontoUm.X - (distResistor + 5 * passo) * distancia, pontoUm.Y + tamanhoResistor * distancia);
            path.lineTo(pontoUm.X - (distResistor + 7 * passo) * distancia, pontoUm.Y - tamanhoResistor * distancia);
            path.lineTo(pontoUm.X - (distResistor + 9 * passo) * distancia, pontoUm.Y + tamanhoResistor * distancia);
            path.lineTo(pontoUm.X - (distResistor + 11 * passo) * distancia, pontoUm.Y - tamanhoResistor * distancia);
            path.lineTo(pontoDois.X + distResistor * distancia, pontoDois.Y);
            path.lineTo(pontoDois.X, pontoDois.Y);
        }
        return path;
    }
    //endregion

    //    if (orientacao == Orientacao.CIMA) {
//
//    } else if (orientacao == Orientacao.BAIXO) {
//
//    } else if (orientacao == Orientacao.ESQUERDA) {
//
//    } else if (orientacao == Orientacao.DIREITA) {
//
//    }
    //region Desenhos FALTA ARRUMAR

    private Path cargaRL(Ponto pontoUm, Ponto pontoDois, Orientacao orientacao) {
        Path path = new Path();
        if (orientacao == Orientacao.CIMA) {
            int distancia = Math.abs(pontoDois.Y - pontoUm.Y)/2;
            float passo = (1 - 2 * distResistor) / 12;
            path.moveTo(pontoUm.X, pontoUm.Y);
            path.lineTo(pontoUm.X, pontoUm.Y - distResistor * distancia);
            path.lineTo(pontoUm.X - tamanhoResistor * distancia, pontoUm.Y - (distResistor + 1 * passo) * distancia);
            path.lineTo(pontoUm.X + tamanhoResistor * distancia, pontoUm.Y - (distResistor + 3 * passo) * distancia);
            path.lineTo(pontoUm.X - tamanhoResistor * distancia, pontoUm.Y - (distResistor + 5 * passo) * distancia);
            path.lineTo(pontoUm.X + tamanhoResistor * distancia, pontoUm.Y - (distResistor + 7 * passo) * distancia);
            path.lineTo(pontoUm.X - tamanhoResistor * distancia, pontoUm.Y - (distResistor + 9 * passo) * distancia);
            path.lineTo(pontoUm.X + tamanhoResistor * distancia, pontoUm.Y - (distResistor + 11 * passo) * distancia);
            path.lineTo(pontoDois.X, pontoDois.Y + distResistor * distancia);
            path.lineTo(pontoDois.X, pontoDois.Y);
        } else if (orientacao == Orientacao.BAIXO) {
            int distancia = Math.abs(pontoDois.Y - pontoUm.Y)/2;
            float passo = (1 - 2 * distResistor) / 12;
            path.moveTo(pontoUm.X, pontoUm.Y);
            path.lineTo(pontoUm.X, pontoUm.Y + distResistor * distancia);
            path.lineTo(pontoUm.X + tamanhoResistor * distancia, pontoUm.Y + (distResistor + 1 * passo) * distancia);
            path.lineTo(pontoUm.X - tamanhoResistor * distancia, pontoUm.Y + (distResistor + 3 * passo) * distancia);
            path.lineTo(pontoUm.X + tamanhoResistor * distancia, pontoUm.Y + (distResistor + 5 * passo) * distancia);
            path.lineTo(pontoUm.X - tamanhoResistor * distancia, pontoUm.Y + (distResistor + 7 * passo) * distancia);
            path.lineTo(pontoUm.X + tamanhoResistor * distancia, pontoUm.Y + (distResistor + 9 * passo) * distancia);
            path.lineTo(pontoUm.X - tamanhoResistor * distancia, pontoUm.Y + (distResistor + 11 * passo) * distancia);
            path.lineTo(pontoUm.X, pontoUm.Y + distancia);
            int meioY = pontoUm.Y + distancia; //TODO: NAO GOSTEI, ACHO QUE TEM QUE COLOCAR O distResistor antes do indutor (em volta do resistor
            // e não nas pontas, pq dai aperta o indutor e parece que fica ligado diretao
            RectF rectF1 = new RectF(pontoUm.X - tamanhoRL* 1.5f * distancia, meioY,
                    pontoUm.X + tamanhoRL * 1.5f * distancia, meioY + 4 * passo * distancia);
            path.addArc(rectF1, 90, -180);
            RectF rectF2 = new RectF(pontoUm.X - tamanhoRL* 1.5f * distancia, meioY + 4 * passo * distancia,
                    pontoUm.X + tamanhoRL * 1.5f * distancia, meioY + 8 * passo * distancia);
            path.addArc(rectF2, 90, -180);
            RectF rectF3 = new RectF(pontoUm.X - tamanhoRL* 1.5f * distancia, meioY +8 * passo * distancia,
                    pontoUm.X + tamanhoRL * 1.5f * distancia, meioY + 12 * passo * distancia);
            path.addArc(rectF3, 90, -180);
//            RectF rectF4 = new RectF(pontoUm.X - tamanhoRL* 1.5f * distancia, meioY + 9 * passo * distancia,
//                    pontoUm.X + tamanhoRL * 1.5f * distancia, meioY + 12 * passo * distancia);
//            path.addArc(rectF4, 90, -180);
            path.moveTo(pontoDois.X - distRL * distancia, pontoDois.Y);
            path.lineTo(pontoDois.X, pontoDois.Y);

        } else if (orientacao == Orientacao.ESQUERDA) {
            int distancia = Math.abs(pontoDois.X - pontoUm.X)/2;
            float passo = (1 - distRL) / 12;
            path.moveTo(pontoUm.X, pontoUm.Y);
            path.lineTo(pontoUm.X + distRL * distancia, pontoUm.Y);
            path.lineTo(pontoUm.X + (distRL + 1 * passo) * distancia, pontoUm.Y - tamanhoRL * distancia);
            path.lineTo(pontoUm.X + (distRL + 3 * passo) * distancia, pontoUm.Y + tamanhoRL * distancia);
            path.lineTo(pontoUm.X + (distRL + 5 * passo) * distancia, pontoUm.Y - tamanhoRL * distancia);
            path.lineTo(pontoUm.X + (distRL + 7 * passo) * distancia, pontoUm.Y + tamanhoRL * distancia);
            path.lineTo(pontoUm.X + (distRL + 9 * passo) * distancia, pontoUm.Y - tamanhoRL * distancia);
            path.lineTo(pontoUm.X + (distRL + 11 * passo) * distancia, pontoUm.Y + tamanhoRL * distancia);
            path.lineTo(pontoUm.X + (distRL + 12 * passo) * distancia, pontoUm.Y);
            path.lineTo(pontoUm.X + distancia, pontoUm.Y);
            int meioX = pontoUm.X + distancia;
            RectF rectF1 = new RectF(meioX, pontoUm.Y - tamanhoRL* 1.2f * distancia,
                    meioX + 3 * passo * distancia, pontoUm.Y + tamanhoRL * 1.2f * distancia) ;
            path.addArc(rectF1, 0, -180);
            RectF rectF2 = new RectF(meioX + 3 * passo* distancia, pontoUm.Y - tamanhoRL * 1.2f* distancia,
                    meioX + 6 * passo * distancia, pontoUm.Y + tamanhoRL * 1.2f* distancia);
            path.addArc(rectF2, 0, -180);
            RectF rectF3 = new RectF(meioX + 6 * passo* distancia, pontoUm.Y - tamanhoRL * 1.2f* distancia,
                    meioX + 9 * passo * distancia, pontoUm.Y + tamanhoRL * 1.2f* distancia);
            path.addArc(rectF3, 0, -180);
            RectF rectF4 = new RectF(meioX + 9 * passo* distancia, pontoUm.Y - tamanhoRL * 1.2f* distancia,
                    meioX + 12 * passo * distancia, pontoUm.Y + tamanhoRL * 1.2f* distancia);
            path.addArc(rectF4, 0, -180);
            path.moveTo(pontoDois.X - distRL * distancia, pontoDois.Y);
            path.lineTo(pontoDois.X, pontoDois.Y);
        } else if (orientacao == Orientacao.DIREITA) {
            int distancia = Math.abs(pontoDois.X - pontoUm.X)/2;
            float passo = (1 - distRL) / 12;
            path.moveTo(pontoUm.X, pontoUm.Y);
            path.lineTo(pontoUm.X - distRL * distancia, pontoUm.Y);
            path.lineTo(pontoUm.X - (distRL + 1 * passo) * distancia, pontoUm.Y + tamanhoRL * distancia);
            path.lineTo(pontoUm.X - (distRL + 3 * passo) * distancia, pontoUm.Y - tamanhoRL * distancia);
            path.lineTo(pontoUm.X - (distRL + 5 * passo) * distancia, pontoUm.Y + tamanhoRL * distancia);
            path.lineTo(pontoUm.X - (distRL + 7 * passo) * distancia, pontoUm.Y - tamanhoRL * distancia);
            path.lineTo(pontoUm.X - (distRL + 9 * passo) * distancia, pontoUm.Y + tamanhoRL * distancia);
            path.lineTo(pontoUm.X - (distRL + 11 * passo) * distancia, pontoUm.Y - tamanhoRL * distancia);
            path.lineTo(pontoUm.X - (distRL + 12 * passo) * distancia, pontoUm.Y);
            path.lineTo(pontoUm.X - distancia, pontoUm.Y);
            int meioX = pontoUm.X - distancia;
            RectF rectF1 = new RectF(meioX - 3 * passo * distancia, pontoUm.Y - tamanhoRL* 1.2f * distancia,
                    meioX, pontoUm.Y + tamanhoRL * 1.2f * distancia) ;
            path.addArc(rectF1, 0, 180);
            RectF rectF2 = new RectF(meioX - 6 * passo* distancia, pontoUm.Y - tamanhoRL * 1.2f* distancia,
                    meioX - 3 * passo * distancia, pontoUm.Y + tamanhoRL * 1.2f* distancia);
            path.addArc(rectF2, 0, 180);
            RectF rectF3 = new RectF(meioX - 9 * passo* distancia, pontoUm.Y - tamanhoRL * 1.2f* distancia,
                    meioX - 6 * passo * distancia, pontoUm.Y + tamanhoRL * 1.2f* distancia);
            path.addArc(rectF3, 0, 180);
            RectF rectF4 = new RectF(meioX - 12 * passo* distancia, pontoUm.Y - tamanhoRL * 1.2f* distancia,
                    meioX - 9 * passo * distancia, pontoUm.Y + tamanhoRL * 1.2f* distancia);
            path.addArc(rectF4, 0, 180);
            path.moveTo(pontoDois.X - distRL * distancia, pontoDois.Y);
            path.lineTo(pontoDois.X, pontoDois.Y);
        }
        return path;
    }

    private Path cargaRLE(Ponto pontoUm, Ponto pontoDois, Orientacao orientacao) {
        Path path = new Path();
        return path;
    }

    private Path cargaRE(Ponto pontoUm, Ponto pontoDois, Orientacao orientacao) {
        Path path = new Path();
        return path;
    }

    private Path terra(Ponto pontoUm, Ponto pontoDois, Orientacao orientacao) {
        Path path = new Path();
        return path;
    }

    private Path trasfP1(Ponto pontoUm, Ponto pontoDois, Orientacao orientacao) {
        Path path = new Path();
        return path;
    }

    private Path transfP2(Ponto pontoUm, Ponto pontoDois, Orientacao orientacao) {
        Path path = new Path();
        return path;
    }
    //endregion
}

//    private void CargaRL(Ponto pontoUm, Ponto pontoDois, Paint paint, int numeroItem) {
//        Path path = new Path();
//        if (pontoUm.X == pontoDois.X) { // Resistor e Indutor Vertical - mesma coluna
//            int distancia = ((pontoDois.Y - pontoUm.Y) > 0? (pontoDois.Y - pontoUm.Y): (pontoUm.Y - pontoDois.Y));
//            distancia = (distancia/2);
//            path.moveTo(pontoUm.X, pontoUm.Y);
//            path.lineTo(pontoUm.X+ 0.2f*distancia, pontoUm.Y+0.1f*distancia);
//            path.lineTo(pontoUm.X- 0.2f*distancia, pontoUm.Y+0.26f*distancia);
//            path.lineTo(pontoUm.X+ 0.2f*distancia, pontoUm.Y+0.42f*distancia);
//            path.lineTo(pontoUm.X- 0.2f*distancia, pontoUm.Y+0.58f*distancia);
//            path.lineTo(pontoUm.X+ 0.2f*distancia, pontoUm.Y+0.74f*distancia);
//            path.lineTo(pontoUm.X- 0.2f*distancia, pontoUm.Y+0.90f*distancia);
//            path.lineTo(pontoUm.X, pontoUm.Y + 1.0F*distancia);
//            int meioY = pontoUm.Y + distancia;
//            path.moveTo(pontoUm.X, meioY);
//            path.lineTo(pontoUm.X, meioY + 0.1f * distancia);
//            RectF rectF1 = new RectF(pontoUm.X - 0.3f * distancia, meioY + 0.1f * distancia,
//                    pontoUm.X + 0.3f * distancia, meioY + 0.40f * distancia);
//            path.addArc(rectF1, -90, -230);
//            RectF rectF2 = new RectF(pontoUm.X - 0.3f * distancia, meioY + 0.30f * distancia,
//                    pontoUm.X + 0.3f * distancia, meioY + 0.6f * distancia);
//            path.addArc(rectF2, -40, -280);
//            RectF rectF3 = new RectF(pontoUm.X - 0.3f * distancia, meioY + 0.5f * distancia,
//                    pontoUm.X + 0.3f * distancia, meioY + 0.8f * distancia);
//            path.addArc(rectF3, -40, -280);
//            RectF rectF4 = new RectF(pontoUm.X - 0.3f * distancia, meioY + 0.7f * distancia,
//                    pontoUm.X + 0.3f * distancia, pontoDois.Y);
//            path.addArc(rectF4, -40, -230);
//
//        } else if (pontoUm.Y == pontoDois.Y) { // Resistor e Indutor Horizontal - mesma linha
//            int distancia = (pontoDois.X - pontoUm.X) > 0? (pontoDois.X - pontoUm.X): (pontoUm.X - pontoDois.X);
//            distancia = distancia/2;
//            path.moveTo(pontoUm.X, pontoUm.Y);
//            path.lineTo(pontoUm.X+ 0.1f*distancia, pontoUm.Y-0.2f*distancia);
//            path.lineTo(pontoUm.X+ 0.26f*distancia, pontoUm.Y+0.2f*distancia);
//            path.lineTo(pontoUm.X+ 0.42f*distancia, pontoUm.Y-0.2f*distancia);
//            path.lineTo(pontoUm.X+ 0.58f*distancia, pontoUm.Y+0.2f*distancia);
//            path.lineTo(pontoUm.X+ 0.74f*distancia, pontoUm.Y-0.2f*distancia);
//            path.lineTo(pontoUm.X+ 0.90f*distancia, pontoUm.Y+0.2f*distancia);
//            path.lineTo(pontoUm.X+ 1.0f*distancia, pontoDois.Y);
//
//            int meioX = pontoUm.X + distancia;
//            path.moveTo(meioX, pontoUm.Y);
//            path.lineTo(meioX + 0.1f * distancia, pontoUm.Y);
//            RectF rectF1 = new RectF(meioX + 0.1f * distancia, pontoUm.Y - 0.3f * distancia,
//                    meioX + 0.40f * distancia, pontoUm.Y + 0.3f * distancia) ;
//            path.addArc(rectF1, -180, -230);
//            RectF rectF2 = new RectF(meioX + 0.30f * distancia, pontoUm.Y - 0.3f * distancia,
//                    meioX + 0.6f * distancia, pontoUm.Y + 0.3f * distancia);
//            path.addArc(rectF2, -130, -280);
//            RectF rectF3 = new RectF(meioX + 0.5f * distancia,pontoUm.Y - 0.3f * distancia,
//                    meioX + 0.8f * distancia,pontoUm.Y + 0.3f * distancia);
//            path.addArc(rectF3, -130, -280);
//            RectF rectF4 = new RectF(meioX + 0.7f * distancia, pontoUm.Y - 0.3f * distancia,
//                    pontoDois.X, pontoDois.Y + 0.3f * distancia);
//            path.addArc(rectF4, -130, -230);
//        }
//        pathLists.add(path);
//        paintLists.add(paint);
//        Item item = new Item(numeroItem, new Ponto((pontoUm.X+pontoDois.X)/2, (pontoUm.Y+pontoDois.Y)/2));
//        itemList.add(item);
//    } // PRECISA ARRUMAR - SEGUNDA
//    private void CargaRLE(Ponto pontoUm, Ponto pontoDois, Paint paint, int numeroItem) {
//        Path path = new Path();
//        if (pontoUm.X == pontoDois.X) { // Resistor Vertical - mesma coluna
//            float distancia = ((pontoDois.Y - pontoUm.Y) > 0? (pontoDois.Y - pontoUm.Y): (pontoUm.Y - pontoDois.Y));
//            distancia = (distancia/2.5f);
//            path.moveTo(pontoUm.X, pontoUm.Y);
//            path.lineTo(pontoUm.X+ 0.2f*distancia, pontoUm.Y+0.1f*distancia);
//            path.lineTo(pontoUm.X- 0.2f*distancia, pontoUm.Y+0.26f*distancia);
//            path.lineTo(pontoUm.X+ 0.2f*distancia, pontoUm.Y+0.42f*distancia);
//            path.lineTo(pontoUm.X- 0.2f*distancia, pontoUm.Y+0.58f*distancia);
//            path.lineTo(pontoUm.X+ 0.2f*distancia, pontoUm.Y+0.74f*distancia);
//            path.lineTo(pontoUm.X- 0.2f*distancia, pontoUm.Y+0.90f*distancia);
//            path.lineTo(pontoUm.X, pontoUm.Y + 1.0F*distancia);
//            float meioY = pontoUm.Y + distancia;
//            path.moveTo(pontoUm.X, meioY);
//            path.lineTo(pontoUm.X, meioY + 0.1f * distancia);
//            RectF rectF1 = new RectF(pontoUm.X - 0.3f * distancia, meioY + 0.1f * distancia,
//                    pontoUm.X + 0.3f * distancia, meioY + 0.40f * distancia);
//            path.addArc(rectF1, -90, -230);
//            RectF rectF2 = new RectF(pontoUm.X - 0.3f * distancia, meioY + 0.30f * distancia,
//                    pontoUm.X + 0.3f * distancia, meioY + 0.6f * distancia);
//            path.addArc(rectF2, -40, -280);
//            RectF rectF3 = new RectF(pontoUm.X - 0.3f * distancia, meioY + 0.5f * distancia,
//                    pontoUm.X + 0.3f * distancia, meioY + 0.8f * distancia);
//            path.addArc(rectF3, -40, -280);
//            RectF rectF4 = new RectF(pontoUm.X - 0.3f * distancia, meioY + 0.7f * distancia,
//                    pontoUm.X + 0.3f * distancia, pontoUm.Y + 2.0f * distancia);
//            path.addArc(rectF4, -40, -230);
//
//            float tercoY = pontoUm.Y + 2.0f * distancia;
//            path.lineTo(pontoUm.X, tercoY + 0.2f * distancia);
//            path.moveTo(pontoUm.X, tercoY + 0.2f * distancia);
//            path.lineTo(pontoUm.X + 0.3f * distancia, tercoY + 0.2f * distancia);
//            path.lineTo(pontoUm.X - 0.3f * distancia, tercoY + 0.2f * distancia);
//
//            path.moveTo(pontoUm.X, tercoY + 0.3f * distancia);
//            path.lineTo(pontoUm.X + 0.15f * distancia, tercoY + 0.3f * distancia);
//            path.lineTo(pontoUm.X - 0.15f * distancia, tercoY + 0.3f * distancia);
//            path.moveTo(pontoUm.X, tercoY + 0.3f * distancia);
//            path.lineTo(pontoDois.X, pontoDois.Y);
//
//        } else if (pontoUm.Y == pontoDois.Y) { // Diodo Horizontal - mesma linha
//            float distancia = (pontoDois.X - pontoUm.X) > 0? (pontoDois.X - pontoUm.X): (pontoUm.X - pontoDois.X);
//            distancia = distancia/2.5f;
//            path.moveTo(pontoUm.X, pontoUm.Y);
//            path.lineTo(pontoUm.X+ 0.1f*distancia, pontoUm.Y-0.2f*distancia);
//            path.lineTo(pontoUm.X+ 0.26f*distancia, pontoUm.Y+0.2f*distancia);
//            path.lineTo(pontoUm.X+ 0.42f*distancia, pontoUm.Y-0.2f*distancia);
//            path.lineTo(pontoUm.X+ 0.58f*distancia, pontoUm.Y+0.2f*distancia);
//            path.lineTo(pontoUm.X+ 0.74f*distancia, pontoUm.Y-0.2f*distancia);
//            path.lineTo(pontoUm.X+ 0.90f*distancia, pontoUm.Y+0.2f*distancia);
//            path.lineTo(pontoUm.X+ 1.0f*distancia, pontoDois.Y);
//
//            float meioX = pontoUm.X + distancia;
//            path.moveTo(meioX, pontoUm.Y);
//            path.lineTo(meioX + 0.1f * distancia, pontoUm.Y);
//            RectF rectF1 = new RectF(meioX + 0.1f * distancia, pontoUm.Y - 0.3f * distancia,
//                    meioX + 0.40f * distancia, pontoUm.Y + 0.3f * distancia) ;
//            path.addArc(rectF1, -180, -230);
//            RectF rectF2 = new RectF(meioX + 0.30f * distancia, pontoUm.Y - 0.3f * distancia,
//                    meioX + 0.6f * distancia, pontoUm.Y + 0.3f * distancia);
//            path.addArc(rectF2, -130, -280);
//            RectF rectF3 = new RectF(meioX + 0.5f * distancia,pontoUm.Y - 0.3f * distancia,
//                    meioX + 0.8f * distancia,pontoUm.Y + 0.3f * distancia);
//            path.addArc(rectF3, -130, -280);
//            RectF rectF4 = new RectF(meioX + 0.7f * distancia, pontoUm.Y - 0.3f * distancia,
//                    pontoUm.X + 2.0f * distancia, pontoDois.Y + 0.3f * distancia);
//            path.addArc(rectF4, -130, -230);
//
//            float tercoX = pontoUm.X + 2.0f * distancia;
//            path.lineTo(tercoX + 0.2f * distancia, pontoUm.Y);
//            path.moveTo(tercoX + 0.2f * distancia, pontoUm.Y);
//            path.lineTo(tercoX + 0.2f * distancia, pontoUm.Y + 0.3f * distancia);
//            path.lineTo(tercoX + 0.2f * distancia, pontoUm.Y - 0.3f * distancia);
//
//            path.moveTo(tercoX + 0.3f * distancia, pontoUm.Y);
//            path.lineTo(tercoX + 0.3f * distancia, pontoUm.Y + 0.15f * distancia);
//            path.lineTo(tercoX + 0.3f * distancia, pontoUm.Y - 0.15f * distancia);
//            path.moveTo(tercoX + 0.3f * distancia, pontoUm.Y);
//            path.lineTo(pontoDois.X, pontoDois.Y);
//        }
//        pathLists.add(path);
//        paintLists.add(paint);
//        Item item = new Item(numeroItem, new Ponto((pontoUm.X+pontoDois.X)/2, (pontoUm.Y+pontoDois.Y)/2));
//        itemList.add(item);
//    } // PRECISA ARRUMAR E MUITO - SEGUNDA
//    private void CargaRE(Ponto pontoUm, Ponto pontoDois, Paint paint, int numeroItem) {
//        Path path = new Path();
//        if (pontoUm.X == pontoDois.X) { // Resistor e Bateria Vertical - mesma coluna
//            int distancia = ((pontoDois.Y - pontoUm.Y) > 0? (pontoDois.Y - pontoUm.Y): (pontoUm.Y - pontoDois.Y));
//            distancia = (distancia/2);
//            path.moveTo(pontoUm.X, pontoUm.Y);
//            path.lineTo(pontoUm.X+ 0.2f*distancia, pontoUm.Y+0.1f*distancia);
//            path.lineTo(pontoUm.X- 0.2f*distancia, pontoUm.Y+0.26f*distancia);
//            path.lineTo(pontoUm.X+ 0.2f*distancia, pontoUm.Y+0.42f*distancia);
//            path.lineTo(pontoUm.X- 0.2f*distancia, pontoUm.Y+0.58f*distancia);
//            path.lineTo(pontoUm.X+ 0.2f*distancia, pontoUm.Y+0.74f*distancia);
//            path.lineTo(pontoUm.X- 0.2f*distancia, pontoUm.Y+0.90f*distancia);
//            path.lineTo(pontoUm.X, pontoUm.Y + 1.0F*distancia);
//            int meioY = pontoUm.Y + distancia;
//            path.lineTo(pontoUm.X, meioY + 0.35f * distancia);
//            path.moveTo(pontoUm.X, meioY + 0.35f * distancia);
//            path.lineTo(pontoUm.X + 0.3f * distancia, meioY + 0.35f * distancia);
//            path.lineTo(pontoUm.X - 0.3f * distancia, meioY + 0.35f * distancia);
//
//            path.moveTo(pontoUm.X, meioY + 0.5f * distancia);
//            path.lineTo(pontoUm.X + 0.15f * distancia, meioY + 0.5f * distancia);
//            path.lineTo(pontoUm.X - 0.15f * distancia, meioY + 0.5f * distancia);
//            path.moveTo(pontoUm.X, meioY + 0.5f * distancia);
//            path.lineTo(pontoDois.X, pontoDois.Y);
//
//        } else if (pontoUm.Y == pontoDois.Y) { // Resistor e Bateria Horizontal - mesma linha
//            int distancia = (pontoDois.X - pontoUm.X) > 0? (pontoDois.X - pontoUm.X): (pontoUm.X - pontoDois.X);
//            distancia = distancia/2;
//            path.moveTo(pontoUm.X, pontoUm.Y);
//            path.lineTo(pontoUm.X+ 0.1f*distancia, pontoUm.Y-0.2f*distancia);
//            path.lineTo(pontoUm.X+ 0.26f*distancia, pontoUm.Y+0.2f*distancia);
//            path.lineTo(pontoUm.X+ 0.42f*distancia, pontoUm.Y-0.2f*distancia);
//            path.lineTo(pontoUm.X+ 0.58f*distancia, pontoUm.Y+0.2f*distancia);
//            path.lineTo(pontoUm.X+ 0.74f*distancia, pontoUm.Y-0.2f*distancia);
//            path.lineTo(pontoUm.X+ 0.90f*distancia, pontoUm.Y+0.2f*distancia);
//            path.lineTo(pontoUm.X+ 1.0f*distancia, pontoDois.Y);
//
//            int meioX = pontoUm.X + distancia;
//            path.lineTo(meioX + 0.3f * distancia, pontoUm.Y);
//            path.moveTo(meioX + 0.3f * distancia, pontoUm.Y);
//            path.lineTo(meioX + 0.3f * distancia, pontoUm.Y + 0.5f * distancia);
//            path.lineTo(meioX + 0.3f * distancia, pontoUm.Y - 0.3f * distancia);
//
//            path.moveTo(meioX + 0.5f * distancia, pontoUm.Y);
//            path.lineTo(meioX + 0.5f * distancia, pontoUm.Y + 0.15f * distancia);
//            path.lineTo(meioX + 0.5f * distancia, pontoUm.Y - 0.15f * distancia);
//            path.moveTo(meioX + 0.5f * distancia, pontoUm.Y);
//            path.lineTo(pontoDois.X, pontoDois.Y);
//        }
//        pathLists.add(path);
//        paintLists.add(paint);
//        Item item = new Item(numeroItem, new Ponto((pontoUm.X+pontoDois.X)/2, (pontoUm.Y+pontoDois.Y)/2));
//        itemList.add(item);
//    } // PRECISA ARRUMAR E MUITO - SEGUNDA
//    private void Transformador(Ponto pontoUm, Ponto pontoDois, Ponto ponto3, Ponto ponto4, Paint paint) {
//        Path path = new Path();
//
//        if ((pontoUm.X == pontoDois.X) && (ponto3.X == ponto4.X)) { // Vertical
//            int distanciaEsq = (pontoDois.Y - pontoUm.Y)/4;
//            RectF rectF1 = new RectF(pontoUm.X - 0.75f * distanciaEsq, pontoUm.Y,
//                    pontoUm.X + 0.75f * distanciaEsq, pontoUm.Y + distanciaEsq);
//            path.addArc(rectF1, -90, 180);
//
//            RectF rectF2 = new RectF(pontoUm.X - 0.75f * distanciaEsq, pontoUm.Y + distanciaEsq,
//                    pontoUm.X + 0.75f * distanciaEsq, pontoUm.Y + 2*distanciaEsq);
//            path.addArc(rectF2, -90, 180);
//
//            RectF rectF3 = new RectF(pontoUm.X - 0.75f * distanciaEsq, pontoUm.Y + 2*distanciaEsq,
//                    pontoUm.X + 0.75f * distanciaEsq, pontoUm.Y + 3* distanciaEsq);
//            path.addArc(rectF3, -90, 180);
//
//            RectF rectF4 = new RectF(pontoUm.X - 0.75f * distanciaEsq, pontoUm.Y + 3*distanciaEsq,
//                    pontoUm.X + 0.75f * distanciaEsq, pontoUm.Y + 4*distanciaEsq);
//            path.addArc(rectF4, -90, 180);
//
//            path.moveTo((pontoUm.X + ponto3.X)/2, pontoUm.Y);
//            path.lineTo((pontoDois.X + ponto4.X)/2, pontoDois.Y);
//
//            int distanciaDir = (ponto4.Y - ponto3.Y)/4;
//            RectF rectF1d = new RectF(ponto3.X - 0.75f * distanciaDir, ponto3.Y,
//                    ponto3.X + 0.75f * distanciaDir, ponto3.Y + distanciaDir);
//            path.addArc(rectF1d, -90, -180);
//
//            RectF rectF2d = new RectF(ponto3.X - 0.75f * distanciaDir, ponto3.Y + 1*distanciaDir,
//                    ponto3.X + 0.75f * distanciaDir, ponto3.Y + 2*distanciaDir);
//            path.addArc(rectF2d, -90, -180);
//
//            RectF rectF3d = new RectF(ponto3.X - 0.75f * distanciaDir, ponto3.Y + 2*distanciaDir,
//                    ponto3.X + 0.75f * distanciaDir, ponto3.Y + 3* distanciaDir);
//            path.addArc(rectF3d, -90, -180);
//
//            RectF rectF4d = new RectF(ponto3.X - 0.75f * distanciaDir, ponto3.Y + 3*distanciaDir,
//                    ponto3.X + 0.75f * distanciaDir, ponto3.Y + 4*distanciaDir);
//            path.addArc(rectF4d, -90, -180);
//
//        }
//        pathLists.add(path);
//        paintLists.add(paint);
//    } // ACHO QUE DÁ PARA MELHORAR - SEGUNDA
//    private void Terra(Ponto pontoUm, Ponto pontoDois, Paint paint) {
//        Path path = new Path();
//        if (pontoUm.X == pontoDois.X) { // Fonte Vertical - mesma coluna
//            int distancia = (pontoUm.Y - pontoDois.Y)/4;
//            path.moveTo(pontoUm.X,pontoUm.Y);
//            path.lineTo(pontoUm.X , pontoUm.Y- 1.5f* distancia);
//
//            path.moveTo(pontoUm.X, pontoUm.Y - 1.5f* distancia);
//            path.lineTo(pontoUm.X + 2.5f * distancia, pontoUm.Y - 1.5f* distancia);
//            path.lineTo(pontoUm.X - 2.5f * distancia, pontoUm.Y - 1.5f* distancia);
//
//            path.moveTo(pontoUm.X, pontoUm.Y - 2.5f*distancia);
//            path.lineTo(pontoUm.X + 1.5f * distancia, pontoUm.Y - 2.5f*distancia);
//            path.lineTo(pontoUm.X - 1.5f * distancia, pontoUm.Y - 2.5f*distancia);
//
//            path.moveTo(pontoUm.X, pontoUm.Y - 3.5f*distancia);
//            path.lineTo(pontoUm.X + 1.0f * distancia, pontoUm.Y - 3.5f*distancia);
//            path.lineTo(pontoUm.X - 1.0f * distancia, pontoUm.Y - 3.5f*distancia);
//
//        } else if (pontoUm.Y == pontoDois.Y) { // Fonte Horizontal - mesma linha
//            int distancia = (pontoUm.X - pontoDois.X)/4;
//            path.moveTo(pontoUm.X,pontoUm.Y);
//            path.lineTo(pontoUm.X - 1.5f* distancia , pontoUm.Y);
//
//            path.moveTo(pontoUm.X - 1.5f* distancia, pontoUm.Y );
//            path.lineTo(pontoUm.X - 1.5f* distancia, pontoUm.Y + 2.5f * distancia);
//            path.lineTo(pontoUm.X - 1.5f* distancia, pontoUm.Y - 2.5f * distancia);
//
//            path.moveTo(pontoUm.X - 2.5f* distancia, pontoUm.Y );
//            path.lineTo(pontoUm.X - 2.5f* distancia, pontoUm.Y + 1.5f * distancia);
//            path.lineTo(pontoUm.X - 2.5f* distancia, pontoUm.Y - 1.5f * distancia);
//
//            path.moveTo(pontoUm.X - 3.5f* distancia, pontoUm.Y );
//            path.lineTo(pontoUm.X - 3.5f* distancia, pontoUm.Y + 1.0f * distancia);
//            path.lineTo(pontoUm.X - 3.5f* distancia, pontoUm.Y - 1.0f * distancia);
//        }
//        pathLists.add(path);
//        paintLists.add(paint);
//    } // PRECISA ARRUMAR E MUITO - SEGUNDA

