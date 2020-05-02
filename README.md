# SimuladorCircuito

SimuladorCircuito simulador = findViewById(R.id.simulador);

simulador.addSerie(new Serie(valores, 250), new Serie(valoresY), 1);

simulador.addSerie(new Serie(valoresY, 250), 2);

simulador.setNomesEixoY("V", "A");

simulador.setNomesEixoX("ωt", "ωt");

simulador.setCursorConfig(Color.BLUE, 3);

simulador.setCursorStatus(true);

simulador.setPeriodos(1);

simulador.setEixosWidth(5);

simulador.setEixosHeigthMarcacoes(0.05f);

simulador.setEixosTextSize(1.5f);

simulador.setEixosSubTextSize(1.3f);

simulador.setBeta(true);

simulador.setGradeStatus(true);

simulador.setColorTensaoUm(Color.YELLOW);

simulador.setColorTensaoDois(Color.BLUE);

simulador.setColorTensaoTres(Color.BLACK);

simulador.setColorCorrente(Color.RED);

