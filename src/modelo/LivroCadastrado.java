package modelo;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import dao.Hidden;

public class LivroCadastrado extends Livro {
	//atributos adicionados
	protected int quantidadeTotal;
	protected int quantidadeAlugado;
	protected int quantidadeReserva;
	protected int quantidadeDisponivel;
	
	@Hidden
	private Set<Reserva> reservas;
	private Set<Exemplar> exemplares;
	
	public LivroCadastrado(String nome, String autor, String genero, String editora) {
		super(nome, autor, genero, editora);
		exemplares = new HashSet<Exemplar>();
		reservas = new HashSet<Reserva>();
		quantidadeTotal = exemplares.size();
		quantidadeDisponivel = quantidadeTotal;
		quantidadeAlugado = 0;
		quantidadeReserva = 0;
	}
	
	public LivroCadastrado(String nome, String autor, String genero, String editora, int quantidadeTotal,
			int quantidadeAlugado, int quantidadeReserva, int quantidadeDisponivel, Set<Reserva> reservas, Set<Exemplar> exemplares) {
		super(nome, autor, genero, editora);
		this.quantidadeAlugado = quantidadeAlugado;
		this.quantidadeDisponivel = quantidadeDisponivel;
		this.quantidadeReserva = quantidadeReserva;
		this.quantidadeTotal = quantidadeTotal;
		this.reservas = reservas;
		this.exemplares = exemplares;
	}
	
	public int addExemplar(Exemplar exemplar) {
		this.exemplares.add(exemplar);
		quantidadeTotal++;
		quantidadeDisponivel++;
		return quantidadeTotal;
	}
	
	public int addExemplares(Set<Exemplar> exemplares) {
		this.exemplares.addAll(exemplares);
		quantidadeTotal += exemplares.size();
		quantidadeDisponivel = quantidadeTotal;
		return quantidadeTotal;
	}
	
	public Exemplar getExemplar(int id) {
		Iterator<Exemplar> i = exemplares.iterator();
		Exemplar exemplar;
		while(i.hasNext()) {
			exemplar = i.next();
			if(exemplar.getId() == id) {
				return exemplar;
			}
		}
		return null;
	}
	
	public Set<Exemplar> getExemplares() {
		return exemplares;
	}
	
	public void reservar(Reserva reserva) {
		this.reservas.add(reserva);
	}
	
	public Set<Reserva> getReservas() {
		return reservas;
	}
	
	public void exibirDetalhes() {
		super.exibirDetalhes();
		System.out.println("Quantidade de livros no total: " + this.quantidadeTotal);
		System.out.println("Quantidade de livros alugados: " + this.quantidadeAlugado);
		System.out.println("Quantidade de livros em reserva: " + this.quantidadeReserva);
		System.out.println("Quantidade de livros disponíveis: " + this.quantidadeDisponivel);
	}
	
	public Reserva getReservaComPrevisaoMaisPerto() {
		Calendar data = null;
		Reserva reservaRetornada = null;
		Reserva reservaAux;
		Set<Reserva> aux = new HashSet<>();
		reservas.forEach(reserva -> {
			if(reserva.reservaAtiva() && !reserva.livroEstaDisponivel()) {
				aux.add(reserva);
			}
		});
		Iterator<Reserva> i = aux.iterator();
		if(i.hasNext()) {
			reservaRetornada = i.next();
			data = reservaRetornada.getDataPrevista();
		}
		while(i.hasNext()) {
			reservaAux = i.next();
			if(reservaAux.getDataPrevista().before(data)) {
				reservaRetornada = i.next();
				data = reservaRetornada.getDataPrevista();
			}
		}
		return reservaRetornada;
	}

	public int getQuantidadeTotal() {
		return quantidadeTotal;
	}

	public void setQuantidadeTotal(int quantidadeTotal) {
		this.quantidadeTotal = quantidadeTotal;
	}
	
	public void addQuantidadeTotal() {
		this.quantidadeTotal++;
	}
	
	public void subQuantidadeTotal() {
		this.quantidadeTotal--;
	}

	public int getQuantidadeAlugado() {
		return quantidadeAlugado;
	}

	public void setQuantidadeAlugado(int quantidadeAlugado) {
		this.quantidadeAlugado = quantidadeAlugado;
	}
	
	public void addQuantidadeAlugado() {
		this.quantidadeAlugado++;
	}
	
	public void subQuantidadeAlugado() {
		this.quantidadeAlugado--;
	}

	public int getQuantidadeDisponivel() {
		return quantidadeDisponivel;
	}

	public void setQuantidadeDisponivel(int quantidadeDisponivel) {
		this.quantidadeDisponivel = quantidadeDisponivel;
	}
	
	public void addQuantidadeDisponivel() {
		this.quantidadeDisponivel++;
	}
	
	public void subQuantidadeDisponivel() {
		this.quantidadeDisponivel--;
	}

	public int getQuantidadeReserva() {
		return quantidadeReserva;
	}

	public void setQuantidadeReserva(int quantidadeReserva) {
		this.quantidadeReserva = quantidadeReserva;
	}
	
	public void addQuantidadeReserva() {
		this.quantidadeReserva++;
	}
	
	public void subQuantidadeReserva() {
		this.quantidadeReserva--;
	}
}
