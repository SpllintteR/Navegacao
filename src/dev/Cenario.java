package dev;

import java.util.ArrayList;
import java.util.List;

public class Cenario {
	private Ponto inicio;
	private Ponto fim;
	private List<Ponto> pontosInvalidos;
	private int linhas;
	private int colunas;
	
	public Cenario() {
		pontosInvalidos = new ArrayList<Ponto>();
	}
	
	public Ponto getInicio() {
		return inicio;
	}
	public void setInicio(Ponto inicio) {
		this.inicio = inicio;
	}
	public Ponto getFim() {
		return fim;
	}
	public void setFim(Ponto fim) {
		this.fim = fim;
	}
	public List<Ponto> getPontosInvalidos() {
		return pontosInvalidos;
	}
	public void setPontosInvalidos(List<Ponto> pontosInvalidos) {
		this.pontosInvalidos = pontosInvalidos;
	}
	public void addPontoInvalido(Ponto ponto) {
		this.pontosInvalidos.add(ponto);
	}
	
	public int getLinhas() {
		return linhas;
	}
	public void setLinhas(int linhas) {
		this.linhas = linhas;
	}
	public int getColunas() {
		return colunas;
	}
	public void setColunas(int colunas) {
		this.colunas = colunas;
	}
}
