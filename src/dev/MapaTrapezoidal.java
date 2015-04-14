package dev;

import java.util.ArrayList;
import java.util.List;

import lejos.nxt.Button;
import lejos.nxt.Motor;

public class MapaTrapezoidal {

	private int[][] pontos;
	private Ponto inicio;
	private Ponto fim;

	private static int VERDE = 1;
	private static int AMARELO = 2;
	private static int BRANCO = 0;
	private static int INICIO = 3;
	private static int OBSTACULO = -1;
	private static int AZUL = 4;

	private static int NORMAL = 165;
	private static int MAX = 330;

	private static int FRENTE = 410;

	public static void main(final String[] args) {
		MapaTrapezoidal mapa = new MapaTrapezoidal();
		// Cenario cenario = montarCenario1();
		Cenario cenario = montarCenario2();
		mapa.calcularPontosMediosEIntersecoes(cenario);

		for (int i = 0; i < mapa.pontos.length; i++) {
			String str = "";
			for (int j = 0; j < mapa.pontos[i].length; j++) {
				str += mapa.pontos[i][j] + " ";
			}
			System.out.println(str);
		}

		List<Passo> passos = new ArrayList<Passo>();
		mapa.encontrarPassos(passos, mapa.inicio, null);
		
		for(Passo p: passos){
			System.out.println(p);
		}
		
		Passo last = Passo.CIMA;
		boolean first = true;
		while (passos.size() > 0) {
			if (Button.ESCAPE.isDown()) {
				break;
			}
			Passo p = passos.get(0);
			System.out.println(p);
			if (p != last) {
				if (p == Passo.DIREITA) {
					if (last == Passo.CIMA) {
						giraParaDirieta();
					} else {
						if (last == Passo.BAIXO) {
							giraParaEsquerda();
						} else {
							inverter();
						}
					}
				} else {
					if (p == Passo.ESQUERDA) {
						if (last == Passo.CIMA) {
							giraParaEsquerda();
						} else {
							if (last == Passo.BAIXO) {
								giraParaDirieta();
							} else {
								inverter();
							}
						}
					} else {
						if (p == Passo.CIMA) {
							if (last == Passo.DIREITA) {
								giraParaEsquerda();
							} else {
								if (last == Passo.ESQUERDA) {
									giraParaDirieta();
								} else {
									inverter();
								}
							}
						} else {
							if (last == Passo.DIREITA) {
								giraParaDirieta();
							} else {
								if (last == Passo.ESQUERDA) {
									giraParaEsquerda();
								} else {
									inverter();
								}
							}
						}
					}
				}
			}
			if (first) {
				Motor.A.rotate(-FRENTE - 200, true);
				Motor.C.rotate(-FRENTE - 200);
				first = false;
			} else {
				Motor.A.rotate(-FRENTE, true);
				Motor.C.rotate(-FRENTE);
			}
			last = p;
			passos.remove(0);
		}
		giraParaDirieta();
	}

	private static Cenario montarCenario2() {
		Cenario cenario = new Cenario();
		cenario.setLinhas(7);
		cenario.setColunas(6);
		cenario.setInicio(new Ponto(3, 0));
		cenario.setFim(new Ponto(6, 5));
		cenario.addPontoInvalido(new Ponto(1, 2));
		cenario.addPontoInvalido(new Ponto(2, 3));
		cenario.addPontoInvalido(new Ponto(3, 4));
		cenario.addPontoInvalido(new Ponto(3, 1));
		cenario.addPontoInvalido(new Ponto(4, 2));
		cenario.addPontoInvalido(new Ponto(5, 4));
		cenario.addPontoInvalido(new Ponto(5, 2));
		cenario.addPontoInvalido(new Ponto(6, 3));
		return cenario;
	}

	private static Cenario montarCenario1() {
		Cenario cenario = new Cenario();
		cenario.setLinhas(7);
		cenario.setColunas(6);
		cenario.setInicio(new Ponto(6, 0));
		cenario.setFim(new Ponto(2, 5));
		cenario.addPontoInvalido(new Ponto(0, 1));
		cenario.addPontoInvalido(new Ponto(1, 3));
		cenario.addPontoInvalido(new Ponto(2, 4));
		cenario.addPontoInvalido(new Ponto(3, 2));
		cenario.addPontoInvalido(new Ponto(4, 4));
		cenario.addPontoInvalido(new Ponto(5, 0));
		cenario.addPontoInvalido(new Ponto(5, 2));
		cenario.addPontoInvalido(new Ponto(6, 3));
		cenario.addPontoInvalido(new Ponto(6, 5));
		return cenario;
	}

	private static void inverter() {
		Motor.A.rotate(-MAX, true);
		Motor.C.rotate(MAX);
	}

	private static void giraParaEsquerda() {
		Motor.A.rotate(NORMAL, true);
		Motor.C.rotate(-NORMAL);
	}

	private static void giraParaDirieta() {
		Motor.A.rotate(-NORMAL, true);
		Motor.C.rotate(NORMAL);
	}

	private void encontrarPassos(final List<Passo> passos, Ponto ponto,
			final Ponto ultimoPonto) {
		List<Passo> p = azulNaMesmaColuna(ponto);
		while (p != null) {
			passos.addAll(p);
		}
		Passo passo = getVerdeEmAlgumaDirecao(ponto);
		if (passo != null) {
			passos.add(passo);
		} else {
			passo = getAmareloEmAlgumaDirecao(ponto);
			if (passo != null) {
				passos.add(passo);
			} else {
				passo = getBrancoEmAlgumaDirecao(ponto);
				if (passo != null) {
					passos.add(passo);
				} else {
					marcaJafoi(ponto);
					ponto = ultimoPonto;
				}
			}
		}
	}

	private void marcaJafoi(final Ponto ponto) {
		pontos[ponto.getX()][ponto.getY()] = OBSTACULO;
	}

	private Passo emAlgumaDirecao(final Ponto ponto, final int cor) {
		if (ponto.getY() != (pontos[0].length - 1)) {
			if (pontos[ponto.getX()][ponto.getY() + 1] == cor) {
				return Passo.DIREITA;
			}
		}
		if (ponto.getY() > 0) {
			if (pontos[ponto.getX()][ponto.getY() - 1] == cor) {
				return Passo.ESQUERDA;
			}
		}
		if (ponto.getX() != (pontos.length - 1)) {
			if (pontos[ponto.getX() + 1][ponto.getY()] == cor) {
				return Passo.BAIXO;
			}
		}
		if (ponto.getX() > 0) {
			if (pontos[ponto.getX() - 1][ponto.getY()] == cor) {
				return Passo.CIMA;
			}
		}
		return null;
	}

	private Passo getBrancoEmAlgumaDirecao(final Ponto ponto) {
		return emAlgumaDirecao(ponto, BRANCO);
	}

	private Passo getAmareloEmAlgumaDirecao(final Ponto ponto) {
		return emAlgumaDirecao(ponto, AMARELO);
	}

	private Passo getVerdeEmAlgumaDirecao(final Ponto ponto) {
		return emAlgumaDirecao(ponto, VERDE);
	}

	private List<Passo> azulNaMesmaColuna(final Ponto ponto) {
		for (int i = ponto.getX(); i < pontos.length; i++) {
			if (pontos[i][ponto.getY()] == AZUL) {
				List<Passo> passos = new ArrayList<Passo>();
				for (int j = 0; j < (i - ponto.getX()); j++) {
					passos.add(Passo.BAIXO);
				}
				return passos;
			}
		}
		for (int i = ponto.getX(); i < 0; i--) {
			if (pontos[i][ponto.getY()] == AZUL) {
				List<Passo> passos = new ArrayList<Passo>();
				for (int j = 0; j < (i - ponto.getX()); j++) {
					passos.add(Passo.CIMA);
				}
				return passos;
			}
		}
		return null;
	}

	private void calcularPontosMediosEIntersecoes(final Cenario cenario) {
		pontos = new int[cenario.getLinhas()][cenario.getColunas()];
		for (Ponto p : cenario.getPontosInvalidos()) {
			pontos[p.getX()][p.getY()] = -1;
		}
		fim = cenario.getFim();
		inicio = cenario.getInicio();

		pontos[inicio.getX()][inicio.getY()] = INICIO;
		pontos[fim.getX()][fim.getY()] = AZUL;

		for (int i = 0; i < pontos[0].length; i++) {
			int count = 0;
			int linha = 0;
			for (int j = 0; j < pontos.length; j++) {
				if (pontos[j][i] == OBSTACULO) {
					if (count > 0) {
						if ((count % 2) == 0) {
							pontos[linha + (count / 2)][i] = VERDE;
						} else {
							pontos[linha + (int) ((count / 2) + 0.5)][i] = VERDE;
						}
					}
					linha += count;
					count = 0;
				} else {
					if ((j == pontos.length - 1) && (pontos[j][i] == BRANCO)) {
						count++;
						if ((count % 2) == 0) {
							pontos[linha + (count / 2)][i] = VERDE;
						} else {
							pontos[linha + (int) ((count / 2) + 0.5)][i] = VERDE;
						}
						linha += count;
						count = 0;
					} else {
						count++;
					}
				}
			}
		}
		
		for (int i = 0; i < pontos.length; i++) {
			String str = "";
			for (int j = 0; j < pontos[i].length; j++) {
				str += pontos[i][j] + " ";
			}
			System.out.println(str);
		}
		
		System.out.println("");

		for (int i = 0; i < pontos.length; i++) {
			for (int j = 0; j < pontos[i].length; j++) {
				if (((j < (pontos[0].length - 1)) && (pontos[i][j+1] == VERDE))
						|| ((j > 0) && (pontos[i][j-1] == VERDE))) {
					pontos[i][j] = AMARELO;
				}
			}
		}
	}
}
/*
0  -1 0  1  0 0 
0  0  1  -1 1 0 
1  0  0  0  1 4 
0  1  1  1  0 1 
0  0  1  0  1 0 
-1 0  -1 0  0 0 
3  0  0  -1 0 -1 
*/
/*
0  -1 2  1  2  0 
2  0  1  2  1  0 
1  2  2  1  2  2 
2  1  -1 2  0  1 
0  2  0  0  -1 2 
-1 0  -1 0  0  0 
3  0  0  -1 0  -1
*/
