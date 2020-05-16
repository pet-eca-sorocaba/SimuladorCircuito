package sorocaba.peteca.com.simuladorcircuito.circuitogerador;

import android.graphics.Path;
import android.graphics.RectF;

class Desenho {
    private static final float distDiodo = 0.15f;
    private static final float tamanhoDiodo = 0.35f;
    private static final float distTiristor = 0.15f;
    private static final float tamanhoTiristor = 0.35f;
    private static final float distResistor = 0f;
    private static final float tamanhoResistor = 0.23f;
    private static final float distRL = 0.05f;
    private static final float tamanhoRL = 0.23f;
    private static final float distRE = 0.05f;
    private static final float tamanhoBateria = 0.35f;
    private static final float distE = 0.40f;
    private static final float distTerra = 0.75f;
    private static final float distRLE = 0.1f;
    private int tamanhoMinimo;

    Desenho(int tamanhoMinimo) {
        this.tamanhoMinimo = tamanhoMinimo;
    }

    Path Trilha(Ponto pontoUm, Ponto pontoDois) {
        Path path = new Path();
        path.moveTo(pontoUm.X * tamanhoMinimo, pontoUm.Y * tamanhoMinimo);
        path.lineTo(pontoDois.X * tamanhoMinimo, pontoDois.Y * tamanhoMinimo);
        return path;
    }

    Path Trilha(Ponto pontoUm, Ponto pontoDois, Ponto pontoTres) {
        Path path = new Path();
        path.moveTo(pontoUm.X * tamanhoMinimo, pontoUm.Y * tamanhoMinimo);
        path.lineTo(pontoDois.X * tamanhoMinimo, pontoDois.Y * tamanhoMinimo);
        path.lineTo(pontoTres.X * tamanhoMinimo, pontoTres.Y * tamanhoMinimo);
        return path;
    }

    Path componente(Ponto pontoUm, Ponto pontoDois, int componente) {
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

    Orientacao verificarOrientacao(Ponto pontoUm, Ponto pontoDois) {
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

    private Path cargaRL(Ponto pontoUm, Ponto pontoDois, Orientacao orientacao) {
        Path path = new Path();
        if (orientacao == Orientacao.CIMA) {
            int distancia = Math.abs(pontoDois.Y - pontoUm.Y)/2;
            float passo = (1 - distRL) / 12;
            path.moveTo(pontoUm.X, pontoUm.Y);
            path.lineTo(pontoUm.X - tamanhoRL * distancia, pontoUm.Y - (1 * passo) * distancia);
            path.lineTo(pontoUm.X + tamanhoRL * distancia, pontoUm.Y - (3 * passo) * distancia);
            path.lineTo(pontoUm.X - tamanhoRL * distancia, pontoUm.Y - ( 5 * passo) * distancia);
            path.lineTo(pontoUm.X + tamanhoRL * distancia, pontoUm.Y - ( 7 * passo) * distancia);
            path.lineTo(pontoUm.X - tamanhoRL * distancia, pontoUm.Y - ( 9 * passo) * distancia);
            path.lineTo(pontoUm.X + tamanhoRL * distancia, pontoUm.Y - ( 11 * passo) * distancia);
            path.lineTo(pontoUm.X, pontoUm.Y - (12 * passo) * distancia);
            path.lineTo(pontoUm.X, pontoUm.Y - distancia);
            for (int i = 0; i<4; i++) {
                path.addArc(new RectF(pontoUm.X - tamanhoRL* 1.5f * distancia, pontoUm.Y - distancia - (i+1) * distancia/4f,
                        pontoUm.X + tamanhoRL * 1.5f * distancia, pontoUm.Y - distancia - i * distancia/4f), 90, +180);
            }
        } else if (orientacao == Orientacao.BAIXO) {
            int distancia = Math.abs(pontoDois.Y - pontoUm.Y)/2;
            float passo = (1 - distRL) / 12;
            path.moveTo(pontoUm.X, pontoUm.Y);
            path.lineTo(pontoUm.X + tamanhoRL * distancia, pontoUm.Y + (1 * passo) * distancia);
            path.lineTo(pontoUm.X - tamanhoRL * distancia, pontoUm.Y + (3 * passo) * distancia);
            path.lineTo(pontoUm.X + tamanhoRL * distancia, pontoUm.Y + ( 5 * passo) * distancia);
            path.lineTo(pontoUm.X - tamanhoRL * distancia, pontoUm.Y + ( 7 * passo) * distancia);
            path.lineTo(pontoUm.X + tamanhoRL * distancia, pontoUm.Y + ( 9 * passo) * distancia);
            path.lineTo(pontoUm.X - tamanhoRL * distancia, pontoUm.Y + ( 11 * passo) * distancia);
            path.lineTo(pontoUm.X, pontoUm.Y + (12 * passo) * distancia);
            path.lineTo(pontoUm.X, pontoUm.Y + distancia);
            for (int i = 0; i<4; i++) {
                path.addArc(new RectF(pontoUm.X - tamanhoRL* 1.5f * distancia, pontoUm.Y + distancia + i * distancia/4f,
                        pontoUm.X + tamanhoRL * 1.5f * distancia, pontoUm.Y + distancia + (i+1) * distancia/4f), 90, -180);
            }
        } else if (orientacao == Orientacao.ESQUERDA) {
            int distancia = Math.abs(pontoDois.X - pontoUm.X)/2;
            float passo = (1 - distRL) / 12;
            path.moveTo(pontoUm.X, pontoUm.Y);
            path.lineTo(pontoUm.X + (1 * passo) * distancia, pontoUm.Y - tamanhoRL * distancia);
            path.lineTo(pontoUm.X + (3 * passo) * distancia, pontoUm.Y + tamanhoRL * distancia);
            path.lineTo(pontoUm.X + (5 * passo) * distancia, pontoUm.Y - tamanhoRL * distancia);
            path.lineTo(pontoUm.X + (7 * passo) * distancia, pontoUm.Y + tamanhoRL * distancia);
            path.lineTo(pontoUm.X + (9 * passo) * distancia, pontoUm.Y - tamanhoRL * distancia);
            path.lineTo(pontoUm.X + (11 * passo) * distancia, pontoUm.Y + tamanhoRL * distancia);
            path.lineTo(pontoUm.X+ (12 * passo) * distancia, pontoUm.Y);
            path.lineTo(pontoUm.X + distancia, pontoUm.Y);
            for (int i = 0; i<4; i++) {
                path.addArc(new RectF(pontoUm.X + distancia + i * distancia/4f, pontoUm.Y - tamanhoRL* 1.5f * distancia,
                        pontoUm.X  + distancia + (i+1) * distancia/4f, pontoUm.Y + tamanhoRL * 1.5f * distancia), 180, 180);
            }
        } else if (orientacao == Orientacao.DIREITA) {
            int distancia = Math.abs(pontoDois.X - pontoUm.X)/2;
            float passo = (1 - distRL) / 12;
            path.moveTo(pontoUm.X, pontoUm.Y);
            path.lineTo(pontoUm.X - (1 * passo) * distancia, pontoUm.Y + tamanhoRL * distancia);
            path.lineTo(pontoUm.X - (3 * passo) * distancia, pontoUm.Y - tamanhoRL * distancia);
            path.lineTo(pontoUm.X - (5 * passo) * distancia, pontoUm.Y + tamanhoRL * distancia);
            path.lineTo(pontoUm.X - (7 * passo) * distancia, pontoUm.Y - tamanhoRL * distancia);
            path.lineTo(pontoUm.X - (9 * passo) * distancia, pontoUm.Y + tamanhoRL * distancia);
            path.lineTo(pontoUm.X - (11 * passo) * distancia, pontoUm.Y - tamanhoRL * distancia);
            path.lineTo(pontoUm.X - (12 * passo) * distancia, pontoUm.Y);
            path.lineTo(pontoUm.X - distancia, pontoUm.Y);
            for (int i = 0; i<4; i++) {
                path.addArc(new RectF(pontoUm.X  - distancia - (i+1) * distancia/4f, pontoUm.Y - tamanhoRL* 1.5f * distancia,
                        pontoUm.X - distancia - i * distancia/4f, pontoUm.Y + tamanhoRL * 1.5f * distancia), 180, -180);
            }
        }
        return path;
    }

    private Path cargaRLE(Ponto pontoUm, Ponto pontoDois, Orientacao orientacao) {
        Path path = new Path();
        if (orientacao == Orientacao.CIMA) {
            int distancia = Math.abs(pontoDois.Y - pontoUm.Y)/3;
            float passo = (1 - distRL) / 12;
            path.moveTo(pontoUm.X, pontoUm.Y);
            path.lineTo(pontoUm.X - tamanhoRL * distancia, pontoUm.Y - (1 * passo) * distancia);
            path.lineTo(pontoUm.X + tamanhoRL * distancia, pontoUm.Y - (3 * passo) * distancia);
            path.lineTo(pontoUm.X - tamanhoRL * distancia, pontoUm.Y - ( 5 * passo) * distancia);
            path.lineTo(pontoUm.X + tamanhoRL * distancia, pontoUm.Y - ( 7 * passo) * distancia);
            path.lineTo(pontoUm.X - tamanhoRL * distancia, pontoUm.Y - ( 9 * passo) * distancia);
            path.lineTo(pontoUm.X + tamanhoRL * distancia, pontoUm.Y - ( 11 * passo) * distancia);
            path.lineTo(pontoUm.X, pontoUm.Y - (12 * passo) * distancia);
            path.lineTo(pontoUm.X, pontoUm.Y - distancia);
            for (int i = 0; i<4; i++) {
                path.addArc(new RectF(pontoUm.X - tamanhoRL* 1.5f * distancia, pontoUm.Y - distancia - (i+1) * distancia/4.0f,
                        pontoUm.X + tamanhoRL * 1.5f * distancia, pontoUm.Y - distancia - i * distancia/4.0f), 90, +180);
            }
            path.moveTo(pontoUm.X, pontoUm.Y - 2 * distancia);
            path.lineTo(pontoUm.X , pontoUm.Y - (2 + distE) * distancia);
            path.lineTo(pontoUm.X  + tamanhoBateria * distancia, pontoUm.Y - (2 + distE) * distancia);
            path.lineTo(pontoUm.X  - tamanhoBateria * distancia, pontoUm.Y - (2 + distE) * distancia);
            path.moveTo(pontoUm.X, pontoUm.Y - (3 - distE) * distancia);
            path.lineTo(pontoUm.X  + tamanhoBateria/2 * distancia, pontoUm.Y - (3 - distE) * distancia);
            path.lineTo(pontoUm.X  - tamanhoBateria/2 * distancia, pontoUm.Y - (3 - distE) * distancia);
            path.moveTo(pontoUm.X , pontoUm.Y - (3 - distE) * distancia);
            path.lineTo(pontoDois.X, pontoDois.Y);
        } else if (orientacao == Orientacao.BAIXO) {
            int distancia = Math.abs(pontoDois.Y - pontoUm.Y)/3;
            float passo = (1 - distRLE) / 12;
            path.moveTo(pontoUm.X, pontoUm.Y);
            path.lineTo(pontoUm.X + tamanhoRL * distancia, pontoUm.Y + (1 * passo) * distancia);
            path.lineTo(pontoUm.X - tamanhoRL * distancia, pontoUm.Y + (3 * passo) * distancia);
            path.lineTo(pontoUm.X + tamanhoRL * distancia, pontoUm.Y + ( 5 * passo) * distancia);
            path.lineTo(pontoUm.X - tamanhoRL * distancia, pontoUm.Y + ( 7 * passo) * distancia);
            path.lineTo(pontoUm.X + tamanhoRL * distancia, pontoUm.Y + ( 9 * passo) * distancia);
            path.lineTo(pontoUm.X - tamanhoRL * distancia, pontoUm.Y + ( 11 * passo) * distancia);
            path.lineTo(pontoUm.X, pontoUm.Y + (12 * passo) * distancia);
            path.lineTo(pontoUm.X, pontoUm.Y + distancia);
            for (int i = 0; i<4; i++) {
                path.addArc(new RectF(pontoUm.X - tamanhoRL* 1.5f * distancia, pontoUm.Y + distancia + i * distancia/4f,
                        pontoUm.X + tamanhoRL * 1.5f * distancia, pontoUm.Y + distancia + (i+1) * distancia/4f), 90, -180);
            }
            path.moveTo(pontoUm.X, pontoUm.Y + 2 * distancia);
            path.lineTo(pontoUm.X , pontoUm.Y + (2 + distE) * distancia);
            path.lineTo(pontoUm.X  + tamanhoBateria * distancia, pontoUm.Y + (2 + distE) * distancia);
            path.lineTo(pontoUm.X  - tamanhoBateria * distancia, pontoUm.Y + (2 + distE) * distancia);
            path.moveTo(pontoUm.X, pontoUm.Y + (3 - distE) * distancia);
            path.lineTo(pontoUm.X  + tamanhoBateria/2 * distancia, pontoUm.Y + (3 - distE) * distancia);
            path.lineTo(pontoUm.X  - tamanhoBateria/2 * distancia, pontoUm.Y + (3 - distE) * distancia);
            path.moveTo(pontoUm.X , pontoUm.Y + (3 - distE) * distancia);
            path.lineTo(pontoDois.X, pontoDois.Y);

        } else if (orientacao == Orientacao.ESQUERDA) {
            int distancia = Math.abs(pontoDois.X - pontoUm.X)/3;
            float passo = (1 - distRL) / 12;
            path.moveTo(pontoUm.X, pontoUm.Y);
            path.lineTo(pontoUm.X + (1 * passo) * distancia, pontoUm.Y - tamanhoRL * distancia);
            path.lineTo(pontoUm.X + (3 * passo) * distancia, pontoUm.Y + tamanhoRL * distancia);
            path.lineTo(pontoUm.X + (5 * passo) * distancia, pontoUm.Y - tamanhoRL * distancia);
            path.lineTo(pontoUm.X + (7 * passo) * distancia, pontoUm.Y + tamanhoRL * distancia);
            path.lineTo(pontoUm.X + (9 * passo) * distancia, pontoUm.Y - tamanhoRL * distancia);
            path.lineTo(pontoUm.X + (11 * passo) * distancia, pontoUm.Y + tamanhoRL * distancia);
            path.lineTo(pontoUm.X+ (12 * passo) * distancia, pontoUm.Y);
            path.lineTo(pontoUm.X + distancia, pontoUm.Y);
            for (int i = 0; i<4; i++) {
                path.addArc(new RectF(pontoUm.X + distancia + i * distancia/4f, pontoUm.Y - tamanhoRL* 1.5f * distancia,
                        pontoUm.X  + distancia + (i+1) * distancia/4f, pontoUm.Y + tamanhoRL * 1.5f * distancia), 180, 180);
            }
            path.moveTo(pontoUm.X + 2 * distancia, pontoUm.Y);
            path.lineTo(pontoUm.X + (2 + distE) * distancia, pontoUm.Y);
            path.lineTo(pontoUm.X  + (2 + distE) * distancia, pontoUm.Y + tamanhoBateria * distancia);
            path.lineTo(pontoUm.X  + (2 + distE) * distancia, pontoUm.Y - tamanhoBateria * distancia);
            path.moveTo(pontoUm.X + (3 - distE) * distancia, pontoUm.Y);
            path.lineTo(pontoUm.X  + (3 - distE) * distancia, pontoUm.Y + tamanhoBateria/2 * distancia);
            path.lineTo(pontoUm.X  + (3 - distE) * distancia, pontoUm.Y - tamanhoBateria/2 * distancia);
            path.moveTo(pontoUm.X + (3 - distE) * distancia, pontoUm.Y);
            path.lineTo(pontoDois.X, pontoDois.Y);
        } else if (orientacao == Orientacao.DIREITA) {
            int distancia = Math.abs(pontoDois.X - pontoUm.X)/3;
            float passo = (1 - distRL) / 12;
            path.moveTo(pontoUm.X, pontoUm.Y);
            path.lineTo(pontoUm.X - (1 * passo) * distancia, pontoUm.Y + tamanhoRL * distancia);
            path.lineTo(pontoUm.X - (3 * passo) * distancia, pontoUm.Y - tamanhoRL * distancia);
            path.lineTo(pontoUm.X - (5 * passo) * distancia, pontoUm.Y + tamanhoRL * distancia);
            path.lineTo(pontoUm.X - (7 * passo) * distancia, pontoUm.Y - tamanhoRL * distancia);
            path.lineTo(pontoUm.X - (9 * passo) * distancia, pontoUm.Y + tamanhoRL * distancia);
            path.lineTo(pontoUm.X - (11 * passo) * distancia, pontoUm.Y - tamanhoRL * distancia);
            path.lineTo(pontoUm.X - (12 * passo) * distancia, pontoUm.Y);
            path.lineTo(pontoUm.X - distancia, pontoUm.Y);
            for (int i = 0; i<4; i++) {
                path.addArc(new RectF(pontoUm.X  - distancia - (i+1) * distancia/4f, pontoUm.Y - tamanhoRL* 1.5f * distancia,
                        pontoUm.X - distancia - i * distancia/4f, pontoUm.Y + tamanhoRL * 1.5f * distancia), 180, -180);
            }
            path.moveTo(pontoUm.X - 2 * distancia, pontoUm.Y);
            path.lineTo(pontoUm.X - (2 + distE) * distancia, pontoUm.Y);
            path.lineTo(pontoUm.X  - (2 + distE) * distancia, pontoDois.Y + tamanhoBateria * distancia);
            path.lineTo(pontoUm.X  - (2 + distE) * distancia, pontoDois.Y - tamanhoBateria * distancia);
            path.moveTo(pontoUm.X - (3 - distE) * distancia, pontoUm.Y);
            path.lineTo(pontoUm.X  - (3 - distE) * distancia, pontoDois.Y + tamanhoBateria/2 * distancia);
            path.lineTo(pontoUm.X  - (3 - distE) * distancia, pontoDois.Y - tamanhoBateria/2 * distancia);
            path.moveTo(pontoUm.X - (3 - distE) * distancia, pontoUm.Y);
            path.lineTo(pontoDois.X, pontoDois.Y);
        }
        return path;
    }

    private Path cargaRE(Ponto pontoUm, Ponto pontoDois, Orientacao orientacao) {
        Path path = new Path();
        if (orientacao == Orientacao.CIMA) {
            int distancia = Math.abs(pontoDois.Y - pontoUm.Y)/2;
            float passo = (1 - distRE) / 12;
            path.moveTo(pontoUm.X, pontoUm.Y);
            path.lineTo(pontoUm.X - tamanhoResistor * distancia, pontoUm.Y - (1 * passo) * distancia);
            path.lineTo(pontoUm.X + tamanhoResistor * distancia, pontoUm.Y - (3 * passo) * distancia);
            path.lineTo(pontoUm.X - tamanhoResistor * distancia, pontoUm.Y - ( 5 * passo) * distancia);
            path.lineTo(pontoUm.X + tamanhoResistor * distancia, pontoUm.Y - ( 7 * passo) * distancia);
            path.lineTo(pontoUm.X - tamanhoResistor * distancia, pontoUm.Y - ( 9 * passo) * distancia);
            path.lineTo(pontoUm.X + tamanhoResistor * distancia, pontoUm.Y - ( 11 * passo) * distancia);
            path.lineTo(pontoUm.X, pontoUm.Y - (12 * passo) * distancia);
            path.lineTo(pontoUm.X , pontoUm.Y - (1 + distE) * distancia);
            path.lineTo(pontoUm.X  + tamanhoBateria * distancia, pontoUm.Y - (1 + distE) * distancia);
            path.lineTo(pontoUm.X  - tamanhoBateria * distancia, pontoUm.Y - (1 + distE) * distancia);
            path.moveTo(pontoUm.X, pontoUm.Y - (2 - distE) * distancia);
            path.lineTo(pontoUm.X  + tamanhoBateria/2 * distancia, pontoUm.Y - (2 - distE) * distancia);
            path.lineTo(pontoUm.X  - tamanhoBateria/2 * distancia, pontoUm.Y - (2 - distE) * distancia);
            path.moveTo(pontoUm.X , pontoUm.Y - (2 - distE) * distancia);
            path.lineTo(pontoDois.X, pontoDois.Y);
        } else if (orientacao == Orientacao.BAIXO) {
            int distancia = Math.abs(pontoDois.Y - pontoUm.Y)/2;
            float passo = (1 - distRE) / 12;
            path.moveTo(pontoUm.X, pontoUm.Y);
            path.lineTo(pontoUm.X + tamanhoResistor * distancia, pontoUm.Y + (1 * passo) * distancia);
            path.lineTo(pontoUm.X - tamanhoResistor * distancia, pontoUm.Y + (3 * passo) * distancia);
            path.lineTo(pontoUm.X + tamanhoResistor * distancia, pontoUm.Y + ( 5 * passo) * distancia);
            path.lineTo(pontoUm.X - tamanhoResistor * distancia, pontoUm.Y + ( 7 * passo) * distancia);
            path.lineTo(pontoUm.X + tamanhoResistor * distancia, pontoUm.Y + ( 9 * passo) * distancia);
            path.lineTo(pontoUm.X - tamanhoResistor * distancia, pontoUm.Y + ( 11 * passo) * distancia);
            path.lineTo(pontoUm.X, pontoUm.Y + (12 * passo) * distancia);
            path.lineTo(pontoUm.X , pontoUm.Y + (1 + distE) * distancia);
            path.lineTo(pontoUm.X  + tamanhoBateria * distancia, pontoUm.Y + (1 + distE) * distancia);
            path.lineTo(pontoUm.X  - tamanhoBateria * distancia, pontoUm.Y + (1 + distE) * distancia);
            path.moveTo(pontoUm.X, pontoUm.Y + (2 - distE) * distancia);
            path.lineTo(pontoUm.X  + tamanhoBateria/2 * distancia, pontoUm.Y + (2 - distE) * distancia);
            path.lineTo(pontoUm.X  - tamanhoBateria/2 * distancia, pontoUm.Y + (2 - distE) * distancia);
            path.moveTo(pontoUm.X , pontoUm.Y + (2 - distE) * distancia);
            path.lineTo(pontoDois.X, pontoDois.Y);
        } else if (orientacao == Orientacao.ESQUERDA) {
            int distancia = Math.abs(pontoDois.X - pontoUm.X)/2;
            float passo = (1 - distRE) / 12;
            path.moveTo(pontoUm.X, pontoUm.Y);
            path.lineTo(pontoUm.X + (1 * passo) * distancia, pontoUm.Y - tamanhoResistor * distancia);
            path.lineTo(pontoUm.X + (3 * passo) * distancia, pontoUm.Y + tamanhoResistor * distancia);
            path.lineTo(pontoUm.X + (5 * passo) * distancia, pontoUm.Y - tamanhoResistor * distancia);
            path.lineTo(pontoUm.X + (7 * passo) * distancia, pontoUm.Y + tamanhoResistor * distancia);
            path.lineTo(pontoUm.X + (9 * passo) * distancia, pontoUm.Y - tamanhoResistor * distancia);
            path.lineTo(pontoUm.X + (11 * passo) * distancia, pontoUm.Y + tamanhoResistor * distancia);
            path.lineTo(pontoUm.X+ (12 * passo) * distancia, pontoUm.Y);
            path.lineTo(pontoUm.X + (1 + distE) * distancia, pontoUm.Y);
            path.lineTo(pontoUm.X  + (1 + distE) * distancia, pontoUm.Y + tamanhoBateria * distancia);
            path.lineTo(pontoUm.X  + (1 + distE) * distancia, pontoUm.Y - tamanhoBateria * distancia);
            path.moveTo(pontoUm.X + (2 - distE) * distancia, pontoUm.Y);
            path.lineTo(pontoUm.X  + (2 - distE) * distancia, pontoUm.Y + tamanhoBateria/2 * distancia);
            path.lineTo(pontoUm.X  + (2 - distE) * distancia, pontoUm.Y - tamanhoBateria/2 * distancia);
            path.moveTo(pontoUm.X + (2 - distE) * distancia, pontoUm.Y);
            path.lineTo(pontoDois.X, pontoDois.Y);
        } else if (orientacao == Orientacao.DIREITA) {
            int distancia = Math.abs(pontoDois.X - pontoUm.X)/2;
            float passo = (1 - distRE) / 12;
            path.moveTo(pontoUm.X, pontoUm.Y);
            path.lineTo(pontoUm.X - (1 * passo) * distancia, pontoUm.Y + tamanhoResistor * distancia);
            path.lineTo(pontoUm.X - (3 * passo) * distancia, pontoUm.Y - tamanhoResistor * distancia);
            path.lineTo(pontoUm.X - (5 * passo) * distancia, pontoUm.Y + tamanhoResistor * distancia);
            path.lineTo(pontoUm.X - (7 * passo) * distancia, pontoUm.Y - tamanhoResistor * distancia);
            path.lineTo(pontoUm.X - (9 * passo) * distancia, pontoUm.Y + tamanhoResistor * distancia);
            path.lineTo(pontoUm.X - (11 * passo) * distancia, pontoUm.Y - tamanhoResistor * distancia);
            path.lineTo(pontoUm.X - (12 * passo) * distancia, pontoUm.Y);

            path.lineTo(pontoUm.X - (1 + distE) * distancia, pontoUm.Y);
            path.lineTo(pontoUm.X  - (1 + distE) * distancia, pontoDois.Y + tamanhoBateria * distancia);
            path.lineTo(pontoUm.X  - (1 + distE) * distancia, pontoDois.Y - tamanhoBateria * distancia);

            path.moveTo(pontoUm.X - (2 - distE) * distancia, pontoUm.Y);
            path.lineTo(pontoUm.X  - (2 - distE) * distancia, pontoDois.Y + tamanhoBateria/2 * distancia);
            path.lineTo(pontoUm.X  - (2 - distE) * distancia, pontoDois.Y - tamanhoBateria/2 * distancia);
            path.moveTo(pontoUm.X - (2 - distE) * distancia, pontoUm.Y);
            path.lineTo(pontoDois.X, pontoDois.Y);
        }
        return path;
    }

    private Path terra(Ponto pontoUm, Ponto pontoDois, Orientacao orientacao) {
        Path path = new Path();
        if (orientacao == Orientacao.CIMA) {
            int distancia = Math.abs(pontoUm.Y - pontoDois.Y)/5;
            path.moveTo(pontoUm.X,pontoUm.Y);
            path.lineTo(pontoUm.X, pontoUm.Y - 2* distancia);
            path.lineTo(pontoUm.X + (0.5f + 2* distTerra) * distancia, pontoUm.Y - 2 * distancia);
            path.lineTo(pontoUm.X - (0.5f + 2* distTerra) * distancia, pontoUm.Y - 2 * distancia);
            path.moveTo(pontoUm.X, pontoUm.Y - 3*distancia);
            path.lineTo(pontoUm.X + (0.5f + distTerra) * distancia, pontoUm.Y - 3 *distancia);
            path.lineTo(pontoUm.X - (0.5f + distTerra) * distancia, pontoUm.Y - 3 *distancia);
            path.moveTo(pontoUm.X, pontoUm.Y - 4*distancia);
            path.lineTo(pontoUm.X + 0.5f * distancia, pontoUm.Y - 4 *distancia);
            path.lineTo(pontoUm.X - 0.5f * distancia, pontoUm.Y - 4 *distancia);
        } else if (orientacao == Orientacao.BAIXO) {
            int distancia = Math.abs(pontoUm.Y - pontoDois.Y)/5;
            path.moveTo(pontoUm.X,pontoUm.Y);
            path.lineTo(pontoUm.X, pontoUm.Y + 2* distancia);
            path.lineTo(pontoUm.X + (0.5f + 2* distTerra) * distancia, pontoUm.Y + 2 * distancia);
            path.lineTo(pontoUm.X - (0.5f + 2* distTerra) * distancia, pontoUm.Y + 2 * distancia);
            path.moveTo(pontoUm.X, pontoUm.Y + 3*distancia);
            path.lineTo(pontoUm.X + (0.5f + distTerra) * distancia, pontoUm.Y + 3 *distancia);
            path.lineTo(pontoUm.X - (0.5f + distTerra) * distancia, pontoUm.Y + 3 *distancia);
            path.moveTo(pontoUm.X, pontoUm.Y + 4*distancia);
            path.lineTo(pontoUm.X + 0.5f * distancia, pontoUm.Y + 4 *distancia);
            path.lineTo(pontoUm.X - 0.5f * distancia, pontoUm.Y + 4 *distancia);
        } else if (orientacao == Orientacao.ESQUERDA) {
            int distancia = Math.abs(pontoUm.X - pontoDois.X)/5;
            path.moveTo(pontoUm.X,pontoUm.Y);
            path.lineTo(pontoUm.X + 2 * distancia, pontoUm.Y);
            path.lineTo(pontoUm.X + 2* distancia, pontoUm.Y + (0.5f + 2* distTerra) * distancia);
            path.lineTo(pontoUm.X + 2* distancia, pontoUm.Y - (0.5f +2* distTerra) * distancia);

            path.moveTo(pontoUm.X + 3 * distancia, pontoUm.Y);
            path.lineTo(pontoUm.X + 3 * distancia, pontoUm.Y + (0.5f + distTerra) * distancia);
            path.lineTo(pontoUm.X + 3 * distancia, pontoUm.Y - (0.5f + distTerra) * distancia);
            path.moveTo(pontoUm.X + 4 * distancia, pontoUm.Y);
            path.lineTo(pontoUm.X + 4 * distancia, pontoUm.Y + 0.5f * distancia);
            path.lineTo(pontoUm.X + 4 * distancia, pontoUm.Y - 0.5f * distancia);
        } else if (orientacao == Orientacao.DIREITA) {
            int distancia = Math.abs(pontoUm.X - pontoDois.X)/5;
            path.moveTo(pontoUm.X,pontoUm.Y);
            path.lineTo(pontoUm.X - 2 * distancia, pontoUm.Y);
            path.lineTo(pontoUm.X - 2* distancia, pontoUm.Y + (0.5f + 2* distTerra) * distancia);
            path.lineTo(pontoUm.X - 2* distancia, pontoUm.Y - (0.5f +2* distTerra) * distancia);

            path.moveTo(pontoUm.X - 3 * distancia, pontoUm.Y);
            path.lineTo(pontoUm.X - 3 * distancia, pontoUm.Y + (0.5f + distTerra) * distancia);
            path.lineTo(pontoUm.X - 3 * distancia, pontoUm.Y - (0.5f + distTerra) * distancia);
            path.moveTo(pontoUm.X - 4 * distancia, pontoUm.Y);
            path.lineTo(pontoUm.X - 4 * distancia, pontoUm.Y + 0.5f * distancia);
            path.lineTo(pontoUm.X - 4 * distancia, pontoUm.Y - 0.5f * distancia);
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

