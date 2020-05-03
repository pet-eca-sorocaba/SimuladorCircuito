package sorocaba.peteca.com.simuladorcircuito.circuitogerador;

import android.graphics.Path;
import android.graphics.RectF;

class Desenho {
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
    //endregion

    //region Desenhos FALTA ARRUMAR
    private Path diodo(Ponto pontoUm, Ponto pontoDois, Orientacao orientacao) {
        Path path = new Path();
        return path;
    }

    private Path tiristor(Ponto pontoUm, Ponto pontoDois, Orientacao orientacao) {
        Path path = new Path();
        return path;
    }

    private Path cargaR(Ponto pontoUm, Ponto pontoDois, Orientacao orientacao) {
        Path path = new Path();
        return path;
    }

    private Path cargaRL(Ponto pontoUm, Ponto pontoDois, Orientacao orientacao) {
        Path path = new Path();
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

//    private void Diodo(Ponto ponto1, Ponto ponto2, Paint paint, int numeroItem) {
//        Path path = new Path();
//        // Diodo Vertical (Mesma coluna - Mesmo valor em X)
//        if (ponto1.getX() == ponto2.getX()) {
//            int distancia = (ponto2.getY() - ponto1.getY()) > 0? (ponto2.getY() - ponto1.getY()): (ponto1.getY() - ponto2.getY());
//            path.moveTo(ponto2.getX(), ponto2.getY());
//            path.lineTo(ponto1.getX()+0.4f*distancia, ponto1.getY());
//            path.lineTo(ponto1.getX()-0.4f*distancia, ponto1.getY());
//            path.lineTo(ponto2.getX(), ponto2.getY());
//            path.lineTo(ponto2.getX()+0.4f*distancia, ponto2.getY());
//            path.lineTo(ponto2.getX()-0.4f*distancia, ponto2.getY());
//        }
//        // Diodo Horizontal (Mesma linha - Mesmo valor em y)
//        else if (ponto1.getY() == ponto2.getY()) {
//            int distancia = (ponto2.getX() - ponto1.getX()) > 0? (ponto2.getX() - ponto1.getX()): (ponto1.getX() - ponto2.getX());
//            path.moveTo(ponto2.getX(), ponto2.getY());
//            path.lineTo(ponto1.getX(), ponto1.getY()+0.4f*distancia);
//            path.lineTo(ponto1.getX(), ponto1.getY()-0.4f*distancia);
//            path.lineTo(ponto2.getX(), ponto2.getY());
//            path.lineTo(ponto2.getX(), ponto2.getY()+0.4f*distancia);
//            path.lineTo(ponto2.getX(), ponto2.getY()-0.4f*distancia);
//        }
//
//        pathLists.add(path);
//        paintLists.add(paint);
//        Item item = new Item(numeroItem, new Ponto((ponto1.getX()+ponto2.getX())/2, (ponto1.getY()+ponto2.getY())/2));
//        itemList.add(item);
//    } // ACHO QUE DÁ PARA MELHORAR - SEGUNDA
//    private void Tiristor(Ponto ponto1, Ponto ponto2, Paint paint, int numeroItem) {
//        Path path = new Path();
//        // Tiristor Vertical (Mesma coluna - Mesmo valor em X)
//        if (ponto1.getX() == ponto2.getX()) {
//            int distancia = (ponto2.getY() - ponto1.getY()) > 0? (ponto2.getY() - ponto1.getY()): (ponto1.getY() - ponto2.getY());
//            path.moveTo(ponto2.getX(), ponto2.getY());
//            path.lineTo(ponto1.getX()+0.4f*distancia, ponto1.getY());
//            path.lineTo(ponto1.getX()-0.4f*distancia, ponto1.getY());
//            path.lineTo(ponto2.getX(), ponto2.getY());
//            path.lineTo(ponto2.getX()+0.5f*distancia, ponto2.getY());
//            path.lineTo(ponto2.getX()-0.5f*distancia, ponto2.getY());
//            path.lineTo(ponto2.getX(), ponto2.getY());
//            path.lineTo(ponto2.getX()+0.4f*distancia, ponto2.getY()+0.4f*distancia);
//            path.lineTo(ponto2.getX()+0.7f*distancia, ponto2.getY()+0.4f*distancia);
//        }
//
//        // Tiristor Horizontal (Mesma linha - Mesmo valor em y)
//        else if (ponto1.getY() == ponto2.getY()) {
//            int distancia = (ponto2.getX() - ponto1.getX()) > 0? (ponto2.getX() - ponto1.getX()): (ponto1.getX() - ponto2.getX());
//            path.moveTo(ponto2.getX(), ponto2.getY());
//            path.lineTo(ponto1.getX(), ponto1.getY()+0.4f*distancia);
//            path.lineTo(ponto1.getX(), ponto1.getY()-0.4f*distancia);
//            path.lineTo(ponto2.getX(), ponto2.getY());
//            path.lineTo(ponto2.getX(), ponto2.getY()+0.5f*distancia);
//            path.lineTo(ponto2.getX(), ponto2.getY()-0.5f*distancia);
//            path.lineTo(ponto2.getX(), ponto2.getY());
//            path.lineTo(ponto2.getX()+0.4f*distancia, ponto2.getY()-0.4f*distancia);
//            path.lineTo(ponto2.getX()+0.4f*distancia, ponto2.getY()-0.7f*distancia);
//        }
//        pathLists.add(path);
//        paintLists.add(paint);
//        Item item = new Item(numeroItem, new Ponto((ponto1.getX()+ponto2.getX())/2, (ponto1.getY()+ponto2.getY())/2));
//        itemList.add(item);
//    } // PRECISA ARRUMAR E DÁ PARA MELHORAR - SEGUNDA
//    private void CargaR(Ponto ponto1, Ponto ponto2, Paint paint, int numeroItem) {
//        Path path = new Path();
//        if (ponto1.getX() == ponto2.getX()) { // Resistor Vertical - mesma coluna
//            int distancia = (ponto2.getY() - ponto1.getY()) > 0? (ponto2.getY() - ponto1.getY()): (ponto1.getY() - ponto2.getY());
//            path.moveTo(ponto1.getX(), ponto1.getY());
//            path.lineTo(ponto1.getX()+ 0.2f*distancia, ponto1.getY()+0.1f*distancia);
//            path.lineTo(ponto1.getX()- 0.2f*distancia, ponto1.getY()+0.26f*distancia);
//            path.lineTo(ponto1.getX()+ 0.2f*distancia, ponto1.getY()+0.42f*distancia);
//            path.lineTo(ponto1.getX()- 0.2f*distancia, ponto1.getY()+0.58f*distancia);
//            path.lineTo(ponto1.getX()+ 0.2f*distancia, ponto1.getY()+0.74f*distancia);
//            path.lineTo(ponto1.getX()- 0.2f*distancia, ponto1.getY()+0.90f*distancia);
//            path.lineTo(ponto2.getX(), ponto1.getY() + 1.0F*distancia);
//        } else if (ponto1.getY() == ponto2.getY()) { // Resistor Horizontal - mesma linha
//            int distancia = (ponto2.getX() - ponto1.getX()) > 0? (ponto2.getX() - ponto1.getX()): (ponto1.getX() - ponto2.getX());
//            path.moveTo(ponto1.getX(), ponto1.getY());
//            path.lineTo(ponto1.getX()+ 0.1f*distancia, ponto1.getY()-0.2f*distancia);
//            path.lineTo(ponto1.getX()+ 0.26f*distancia, ponto1.getY()+0.2f*distancia);
//            path.lineTo(ponto1.getX()+ 0.42f*distancia, ponto1.getY()-0.2f*distancia);
//            path.lineTo(ponto1.getX()+ 0.58f*distancia, ponto1.getY()+0.2f*distancia);
//            path.lineTo(ponto1.getX()+ 0.74f*distancia, ponto1.getY()-0.2f*distancia);
//            path.lineTo(ponto1.getX()+ 0.90f*distancia, ponto1.getY()+0.2f*distancia);
//            path.lineTo(ponto1.getX()+ 1.0f*distancia, ponto2.getY());
//        }
//        pathLists.add(path);
//        paintLists.add(paint);
//        Item item = new Item(numeroItem, new Ponto((ponto1.getX()+ponto2.getX())/2, (ponto1.getY()+ponto2.getY())/2));
//        itemList.add(item);
//    } // PRECISA ARRUMAR - SEGUNDA
//    private void CargaRL(Ponto ponto1, Ponto ponto2, Paint paint, int numeroItem) {
//        Path path = new Path();
//        if (ponto1.getX() == ponto2.getX()) { // Resistor e Indutor Vertical - mesma coluna
//            int distancia = ((ponto2.getY() - ponto1.getY()) > 0? (ponto2.getY() - ponto1.getY()): (ponto1.getY() - ponto2.getY()));
//            distancia = (distancia/2);
//            path.moveTo(ponto1.getX(), ponto1.getY());
//            path.lineTo(ponto1.getX()+ 0.2f*distancia, ponto1.getY()+0.1f*distancia);
//            path.lineTo(ponto1.getX()- 0.2f*distancia, ponto1.getY()+0.26f*distancia);
//            path.lineTo(ponto1.getX()+ 0.2f*distancia, ponto1.getY()+0.42f*distancia);
//            path.lineTo(ponto1.getX()- 0.2f*distancia, ponto1.getY()+0.58f*distancia);
//            path.lineTo(ponto1.getX()+ 0.2f*distancia, ponto1.getY()+0.74f*distancia);
//            path.lineTo(ponto1.getX()- 0.2f*distancia, ponto1.getY()+0.90f*distancia);
//            path.lineTo(ponto1.getX(), ponto1.getY() + 1.0F*distancia);
//            int meioY = ponto1.getY() + distancia;
//            path.moveTo(ponto1.getX(), meioY);
//            path.lineTo(ponto1.getX(), meioY + 0.1f * distancia);
//            RectF rectF1 = new RectF(ponto1.getX() - 0.3f * distancia, meioY + 0.1f * distancia,
//                    ponto1.getX() + 0.3f * distancia, meioY + 0.40f * distancia);
//            path.addArc(rectF1, -90, -230);
//            RectF rectF2 = new RectF(ponto1.getX() - 0.3f * distancia, meioY + 0.30f * distancia,
//                    ponto1.getX() + 0.3f * distancia, meioY + 0.6f * distancia);
//            path.addArc(rectF2, -40, -280);
//            RectF rectF3 = new RectF(ponto1.getX() - 0.3f * distancia, meioY + 0.5f * distancia,
//                    ponto1.getX() + 0.3f * distancia, meioY + 0.8f * distancia);
//            path.addArc(rectF3, -40, -280);
//            RectF rectF4 = new RectF(ponto1.getX() - 0.3f * distancia, meioY + 0.7f * distancia,
//                    ponto1.getX() + 0.3f * distancia, ponto2.getY());
//            path.addArc(rectF4, -40, -230);
//
//        } else if (ponto1.getY() == ponto2.getY()) { // Resistor e Indutor Horizontal - mesma linha
//            int distancia = (ponto2.getX() - ponto1.getX()) > 0? (ponto2.getX() - ponto1.getX()): (ponto1.getX() - ponto2.getX());
//            distancia = distancia/2;
//            path.moveTo(ponto1.getX(), ponto1.getY());
//            path.lineTo(ponto1.getX()+ 0.1f*distancia, ponto1.getY()-0.2f*distancia);
//            path.lineTo(ponto1.getX()+ 0.26f*distancia, ponto1.getY()+0.2f*distancia);
//            path.lineTo(ponto1.getX()+ 0.42f*distancia, ponto1.getY()-0.2f*distancia);
//            path.lineTo(ponto1.getX()+ 0.58f*distancia, ponto1.getY()+0.2f*distancia);
//            path.lineTo(ponto1.getX()+ 0.74f*distancia, ponto1.getY()-0.2f*distancia);
//            path.lineTo(ponto1.getX()+ 0.90f*distancia, ponto1.getY()+0.2f*distancia);
//            path.lineTo(ponto1.getX()+ 1.0f*distancia, ponto2.getY());
//
//            int meioX = ponto1.getX() + distancia;
//            path.moveTo(meioX, ponto1.getY());
//            path.lineTo(meioX + 0.1f * distancia, ponto1.getY());
//            RectF rectF1 = new RectF(meioX + 0.1f * distancia, ponto1.getY() - 0.3f * distancia,
//                    meioX + 0.40f * distancia, ponto1.getY() + 0.3f * distancia) ;
//            path.addArc(rectF1, -180, -230);
//            RectF rectF2 = new RectF(meioX + 0.30f * distancia, ponto1.getY() - 0.3f * distancia,
//                    meioX + 0.6f * distancia, ponto1.getY() + 0.3f * distancia);
//            path.addArc(rectF2, -130, -280);
//            RectF rectF3 = new RectF(meioX + 0.5f * distancia,ponto1.getY() - 0.3f * distancia,
//                    meioX + 0.8f * distancia,ponto1.getY() + 0.3f * distancia);
//            path.addArc(rectF3, -130, -280);
//            RectF rectF4 = new RectF(meioX + 0.7f * distancia, ponto1.getY() - 0.3f * distancia,
//                    ponto2.getX(), ponto2.getY() + 0.3f * distancia);
//            path.addArc(rectF4, -130, -230);
//        }
//        pathLists.add(path);
//        paintLists.add(paint);
//        Item item = new Item(numeroItem, new Ponto((ponto1.getX()+ponto2.getX())/2, (ponto1.getY()+ponto2.getY())/2));
//        itemList.add(item);
//    } // PRECISA ARRUMAR - SEGUNDA
//    private void CargaRLE(Ponto ponto1, Ponto ponto2, Paint paint, int numeroItem) {
//        Path path = new Path();
//        if (ponto1.getX() == ponto2.getX()) { // Resistor Vertical - mesma coluna
//            float distancia = ((ponto2.getY() - ponto1.getY()) > 0? (ponto2.getY() - ponto1.getY()): (ponto1.getY() - ponto2.getY()));
//            distancia = (distancia/2.5f);
//            path.moveTo(ponto1.getX(), ponto1.getY());
//            path.lineTo(ponto1.getX()+ 0.2f*distancia, ponto1.getY()+0.1f*distancia);
//            path.lineTo(ponto1.getX()- 0.2f*distancia, ponto1.getY()+0.26f*distancia);
//            path.lineTo(ponto1.getX()+ 0.2f*distancia, ponto1.getY()+0.42f*distancia);
//            path.lineTo(ponto1.getX()- 0.2f*distancia, ponto1.getY()+0.58f*distancia);
//            path.lineTo(ponto1.getX()+ 0.2f*distancia, ponto1.getY()+0.74f*distancia);
//            path.lineTo(ponto1.getX()- 0.2f*distancia, ponto1.getY()+0.90f*distancia);
//            path.lineTo(ponto1.getX(), ponto1.getY() + 1.0F*distancia);
//            float meioY = ponto1.getY() + distancia;
//            path.moveTo(ponto1.getX(), meioY);
//            path.lineTo(ponto1.getX(), meioY + 0.1f * distancia);
//            RectF rectF1 = new RectF(ponto1.getX() - 0.3f * distancia, meioY + 0.1f * distancia,
//                    ponto1.getX() + 0.3f * distancia, meioY + 0.40f * distancia);
//            path.addArc(rectF1, -90, -230);
//            RectF rectF2 = new RectF(ponto1.getX() - 0.3f * distancia, meioY + 0.30f * distancia,
//                    ponto1.getX() + 0.3f * distancia, meioY + 0.6f * distancia);
//            path.addArc(rectF2, -40, -280);
//            RectF rectF3 = new RectF(ponto1.getX() - 0.3f * distancia, meioY + 0.5f * distancia,
//                    ponto1.getX() + 0.3f * distancia, meioY + 0.8f * distancia);
//            path.addArc(rectF3, -40, -280);
//            RectF rectF4 = new RectF(ponto1.getX() - 0.3f * distancia, meioY + 0.7f * distancia,
//                    ponto1.getX() + 0.3f * distancia, ponto1.getY() + 2.0f * distancia);
//            path.addArc(rectF4, -40, -230);
//
//            float tercoY = ponto1.getY() + 2.0f * distancia;
//            path.lineTo(ponto1.getX(), tercoY + 0.2f * distancia);
//            path.moveTo(ponto1.getX(), tercoY + 0.2f * distancia);
//            path.lineTo(ponto1.getX() + 0.3f * distancia, tercoY + 0.2f * distancia);
//            path.lineTo(ponto1.getX() - 0.3f * distancia, tercoY + 0.2f * distancia);
//
//            path.moveTo(ponto1.getX(), tercoY + 0.3f * distancia);
//            path.lineTo(ponto1.getX() + 0.15f * distancia, tercoY + 0.3f * distancia);
//            path.lineTo(ponto1.getX() - 0.15f * distancia, tercoY + 0.3f * distancia);
//            path.moveTo(ponto1.getX(), tercoY + 0.3f * distancia);
//            path.lineTo(ponto2.getX(), ponto2.getY());
//
//        } else if (ponto1.getY() == ponto2.getY()) { // Diodo Horizontal - mesma linha
//            float distancia = (ponto2.getX() - ponto1.getX()) > 0? (ponto2.getX() - ponto1.getX()): (ponto1.getX() - ponto2.getX());
//            distancia = distancia/2.5f;
//            path.moveTo(ponto1.getX(), ponto1.getY());
//            path.lineTo(ponto1.getX()+ 0.1f*distancia, ponto1.getY()-0.2f*distancia);
//            path.lineTo(ponto1.getX()+ 0.26f*distancia, ponto1.getY()+0.2f*distancia);
//            path.lineTo(ponto1.getX()+ 0.42f*distancia, ponto1.getY()-0.2f*distancia);
//            path.lineTo(ponto1.getX()+ 0.58f*distancia, ponto1.getY()+0.2f*distancia);
//            path.lineTo(ponto1.getX()+ 0.74f*distancia, ponto1.getY()-0.2f*distancia);
//            path.lineTo(ponto1.getX()+ 0.90f*distancia, ponto1.getY()+0.2f*distancia);
//            path.lineTo(ponto1.getX()+ 1.0f*distancia, ponto2.getY());
//
//            float meioX = ponto1.getX() + distancia;
//            path.moveTo(meioX, ponto1.getY());
//            path.lineTo(meioX + 0.1f * distancia, ponto1.getY());
//            RectF rectF1 = new RectF(meioX + 0.1f * distancia, ponto1.getY() - 0.3f * distancia,
//                    meioX + 0.40f * distancia, ponto1.getY() + 0.3f * distancia) ;
//            path.addArc(rectF1, -180, -230);
//            RectF rectF2 = new RectF(meioX + 0.30f * distancia, ponto1.getY() - 0.3f * distancia,
//                    meioX + 0.6f * distancia, ponto1.getY() + 0.3f * distancia);
//            path.addArc(rectF2, -130, -280);
//            RectF rectF3 = new RectF(meioX + 0.5f * distancia,ponto1.getY() - 0.3f * distancia,
//                    meioX + 0.8f * distancia,ponto1.getY() + 0.3f * distancia);
//            path.addArc(rectF3, -130, -280);
//            RectF rectF4 = new RectF(meioX + 0.7f * distancia, ponto1.getY() - 0.3f * distancia,
//                    ponto1.getX() + 2.0f * distancia, ponto2.getY() + 0.3f * distancia);
//            path.addArc(rectF4, -130, -230);
//
//            float tercoX = ponto1.getX() + 2.0f * distancia;
//            path.lineTo(tercoX + 0.2f * distancia, ponto1.getY());
//            path.moveTo(tercoX + 0.2f * distancia, ponto1.getY());
//            path.lineTo(tercoX + 0.2f * distancia, ponto1.getY() + 0.3f * distancia);
//            path.lineTo(tercoX + 0.2f * distancia, ponto1.getY() - 0.3f * distancia);
//
//            path.moveTo(tercoX + 0.3f * distancia, ponto1.getY());
//            path.lineTo(tercoX + 0.3f * distancia, ponto1.getY() + 0.15f * distancia);
//            path.lineTo(tercoX + 0.3f * distancia, ponto1.getY() - 0.15f * distancia);
//            path.moveTo(tercoX + 0.3f * distancia, ponto1.getY());
//            path.lineTo(ponto2.getX(), ponto2.getY());
//        }
//        pathLists.add(path);
//        paintLists.add(paint);
//        Item item = new Item(numeroItem, new Ponto((ponto1.getX()+ponto2.getX())/2, (ponto1.getY()+ponto2.getY())/2));
//        itemList.add(item);
//    } // PRECISA ARRUMAR E MUITO - SEGUNDA
//    private void CargaRE(Ponto ponto1, Ponto ponto2, Paint paint, int numeroItem) {
//        Path path = new Path();
//        if (ponto1.getX() == ponto2.getX()) { // Resistor e Bateria Vertical - mesma coluna
//            int distancia = ((ponto2.getY() - ponto1.getY()) > 0? (ponto2.getY() - ponto1.getY()): (ponto1.getY() - ponto2.getY()));
//            distancia = (distancia/2);
//            path.moveTo(ponto1.getX(), ponto1.getY());
//            path.lineTo(ponto1.getX()+ 0.2f*distancia, ponto1.getY()+0.1f*distancia);
//            path.lineTo(ponto1.getX()- 0.2f*distancia, ponto1.getY()+0.26f*distancia);
//            path.lineTo(ponto1.getX()+ 0.2f*distancia, ponto1.getY()+0.42f*distancia);
//            path.lineTo(ponto1.getX()- 0.2f*distancia, ponto1.getY()+0.58f*distancia);
//            path.lineTo(ponto1.getX()+ 0.2f*distancia, ponto1.getY()+0.74f*distancia);
//            path.lineTo(ponto1.getX()- 0.2f*distancia, ponto1.getY()+0.90f*distancia);
//            path.lineTo(ponto1.getX(), ponto1.getY() + 1.0F*distancia);
//            int meioY = ponto1.getY() + distancia;
//            path.lineTo(ponto1.getX(), meioY + 0.35f * distancia);
//            path.moveTo(ponto1.getX(), meioY + 0.35f * distancia);
//            path.lineTo(ponto1.getX() + 0.3f * distancia, meioY + 0.35f * distancia);
//            path.lineTo(ponto1.getX() - 0.3f * distancia, meioY + 0.35f * distancia);
//
//            path.moveTo(ponto1.getX(), meioY + 0.5f * distancia);
//            path.lineTo(ponto1.getX() + 0.15f * distancia, meioY + 0.5f * distancia);
//            path.lineTo(ponto1.getX() - 0.15f * distancia, meioY + 0.5f * distancia);
//            path.moveTo(ponto1.getX(), meioY + 0.5f * distancia);
//            path.lineTo(ponto2.getX(), ponto2.getY());
//
//        } else if (ponto1.getY() == ponto2.getY()) { // Resistor e Bateria Horizontal - mesma linha
//            int distancia = (ponto2.getX() - ponto1.getX()) > 0? (ponto2.getX() - ponto1.getX()): (ponto1.getX() - ponto2.getX());
//            distancia = distancia/2;
//            path.moveTo(ponto1.getX(), ponto1.getY());
//            path.lineTo(ponto1.getX()+ 0.1f*distancia, ponto1.getY()-0.2f*distancia);
//            path.lineTo(ponto1.getX()+ 0.26f*distancia, ponto1.getY()+0.2f*distancia);
//            path.lineTo(ponto1.getX()+ 0.42f*distancia, ponto1.getY()-0.2f*distancia);
//            path.lineTo(ponto1.getX()+ 0.58f*distancia, ponto1.getY()+0.2f*distancia);
//            path.lineTo(ponto1.getX()+ 0.74f*distancia, ponto1.getY()-0.2f*distancia);
//            path.lineTo(ponto1.getX()+ 0.90f*distancia, ponto1.getY()+0.2f*distancia);
//            path.lineTo(ponto1.getX()+ 1.0f*distancia, ponto2.getY());
//
//            int meioX = ponto1.getX() + distancia;
//            path.lineTo(meioX + 0.3f * distancia, ponto1.getY());
//            path.moveTo(meioX + 0.3f * distancia, ponto1.getY());
//            path.lineTo(meioX + 0.3f * distancia, ponto1.getY() + 0.5f * distancia);
//            path.lineTo(meioX + 0.3f * distancia, ponto1.getY() - 0.3f * distancia);
//
//            path.moveTo(meioX + 0.5f * distancia, ponto1.getY());
//            path.lineTo(meioX + 0.5f * distancia, ponto1.getY() + 0.15f * distancia);
//            path.lineTo(meioX + 0.5f * distancia, ponto1.getY() - 0.15f * distancia);
//            path.moveTo(meioX + 0.5f * distancia, ponto1.getY());
//            path.lineTo(ponto2.getX(), ponto2.getY());
//        }
//        pathLists.add(path);
//        paintLists.add(paint);
//        Item item = new Item(numeroItem, new Ponto((ponto1.getX()+ponto2.getX())/2, (ponto1.getY()+ponto2.getY())/2));
//        itemList.add(item);
//    } // PRECISA ARRUMAR E MUITO - SEGUNDA
//    private void Transformador(Ponto ponto1, Ponto ponto2, Ponto ponto3, Ponto ponto4, Paint paint) {
//        Path path = new Path();
//
//        if ((ponto1.getX() == ponto2.getX()) && (ponto3.getX() == ponto4.getX())) { // Vertical
//            int distanciaEsq = (ponto2.getY() - ponto1.getY())/4;
//            RectF rectF1 = new RectF(ponto1.getX() - 0.75f * distanciaEsq, ponto1.getY(),
//                    ponto1.getX() + 0.75f * distanciaEsq, ponto1.getY() + distanciaEsq);
//            path.addArc(rectF1, -90, 180);
//
//            RectF rectF2 = new RectF(ponto1.getX() - 0.75f * distanciaEsq, ponto1.getY() + distanciaEsq,
//                    ponto1.getX() + 0.75f * distanciaEsq, ponto1.getY() + 2*distanciaEsq);
//            path.addArc(rectF2, -90, 180);
//
//            RectF rectF3 = new RectF(ponto1.getX() - 0.75f * distanciaEsq, ponto1.getY() + 2*distanciaEsq,
//                    ponto1.getX() + 0.75f * distanciaEsq, ponto1.getY() + 3* distanciaEsq);
//            path.addArc(rectF3, -90, 180);
//
//            RectF rectF4 = new RectF(ponto1.getX() - 0.75f * distanciaEsq, ponto1.getY() + 3*distanciaEsq,
//                    ponto1.getX() + 0.75f * distanciaEsq, ponto1.getY() + 4*distanciaEsq);
//            path.addArc(rectF4, -90, 180);
//
//            path.moveTo((ponto1.getX() + ponto3.getX())/2, ponto1.getY());
//            path.lineTo((ponto2.getX() + ponto4.getX())/2, ponto2.getY());
//
//            int distanciaDir = (ponto4.getY() - ponto3.getY())/4;
//            RectF rectF1d = new RectF(ponto3.getX() - 0.75f * distanciaDir, ponto3.getY(),
//                    ponto3.getX() + 0.75f * distanciaDir, ponto3.getY() + distanciaDir);
//            path.addArc(rectF1d, -90, -180);
//
//            RectF rectF2d = new RectF(ponto3.getX() - 0.75f * distanciaDir, ponto3.getY() + 1*distanciaDir,
//                    ponto3.getX() + 0.75f * distanciaDir, ponto3.getY() + 2*distanciaDir);
//            path.addArc(rectF2d, -90, -180);
//
//            RectF rectF3d = new RectF(ponto3.getX() - 0.75f * distanciaDir, ponto3.getY() + 2*distanciaDir,
//                    ponto3.getX() + 0.75f * distanciaDir, ponto3.getY() + 3* distanciaDir);
//            path.addArc(rectF3d, -90, -180);
//
//            RectF rectF4d = new RectF(ponto3.getX() - 0.75f * distanciaDir, ponto3.getY() + 3*distanciaDir,
//                    ponto3.getX() + 0.75f * distanciaDir, ponto3.getY() + 4*distanciaDir);
//            path.addArc(rectF4d, -90, -180);
//
//        }
//        pathLists.add(path);
//        paintLists.add(paint);
//    } // ACHO QUE DÁ PARA MELHORAR - SEGUNDA
//    private void Terra(Ponto ponto1, Ponto ponto2, Paint paint) {
//        Path path = new Path();
//        if (ponto1.getX() == ponto2.getX()) { // Fonte Vertical - mesma coluna
//            int distancia = (ponto1.getY() - ponto2.getY())/4;
//            path.moveTo(ponto1.getX(),ponto1.getY());
//            path.lineTo(ponto1.getX() , ponto1.getY()- 1.5f* distancia);
//
//            path.moveTo(ponto1.getX(), ponto1.getY() - 1.5f* distancia);
//            path.lineTo(ponto1.getX() + 2.5f * distancia, ponto1.getY() - 1.5f* distancia);
//            path.lineTo(ponto1.getX() - 2.5f * distancia, ponto1.getY() - 1.5f* distancia);
//
//            path.moveTo(ponto1.getX(), ponto1.getY() - 2.5f*distancia);
//            path.lineTo(ponto1.getX() + 1.5f * distancia, ponto1.getY() - 2.5f*distancia);
//            path.lineTo(ponto1.getX() - 1.5f * distancia, ponto1.getY() - 2.5f*distancia);
//
//            path.moveTo(ponto1.getX(), ponto1.getY() - 3.5f*distancia);
//            path.lineTo(ponto1.getX() + 1.0f * distancia, ponto1.getY() - 3.5f*distancia);
//            path.lineTo(ponto1.getX() - 1.0f * distancia, ponto1.getY() - 3.5f*distancia);
//
//        } else if (ponto1.getY() == ponto2.getY()) { // Fonte Horizontal - mesma linha
//            int distancia = (ponto1.getX() - ponto2.getX())/4;
//            path.moveTo(ponto1.getX(),ponto1.getY());
//            path.lineTo(ponto1.getX() - 1.5f* distancia , ponto1.getY());
//
//            path.moveTo(ponto1.getX() - 1.5f* distancia, ponto1.getY() );
//            path.lineTo(ponto1.getX() - 1.5f* distancia, ponto1.getY() + 2.5f * distancia);
//            path.lineTo(ponto1.getX() - 1.5f* distancia, ponto1.getY() - 2.5f * distancia);
//
//            path.moveTo(ponto1.getX() - 2.5f* distancia, ponto1.getY() );
//            path.lineTo(ponto1.getX() - 2.5f* distancia, ponto1.getY() + 1.5f * distancia);
//            path.lineTo(ponto1.getX() - 2.5f* distancia, ponto1.getY() - 1.5f * distancia);
//
//            path.moveTo(ponto1.getX() - 3.5f* distancia, ponto1.getY() );
//            path.lineTo(ponto1.getX() - 3.5f* distancia, ponto1.getY() + 1.0f * distancia);
//            path.lineTo(ponto1.getX() - 3.5f* distancia, ponto1.getY() - 1.0f * distancia);
//        }
//        pathLists.add(path);
//        paintLists.add(paint);
//    } // PRECISA ARRUMAR E MUITO - SEGUNDA

