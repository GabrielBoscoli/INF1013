package relações;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import dao.Hidden;
import livro.Exemplar;
import livro.LivroCadastrado;
import livro.LivroSugerido;

public class Cliente {
	private long cpf;
	private String nome;
	private String endereco; //bairro, rua, numero predio/casa, apto
	private String email;
	private int telefone;
	
	//atributos adicionados
	private Set<Reserva> reservas;
	private Set<Aluguel> alugueis;
	private Set<LivroSugerido> livrosSugeridos;
	
	public Cliente(long cpf, String nome, String endereco, String email, int telefone) {
		this.cpf = cpf;
		this.nome = nome;
		this.endereco = endereco;
		this.email = email;
		this.telefone = telefone;
		reservas = new HashSet<Reserva>();
		alugueis = new HashSet<Aluguel>();
		livrosSugeridos = new HashSet<LivroSugerido>();
	}
	
	public long getCpf() {
		return cpf;
	}
	public void setCpf(int cpf) {
		this.cpf = cpf;
	}
	
	public Set<Reserva> getReservas() {
		return reservas;
	}
	public void setReservas(Set<Reserva> reservas) {
		this.reservas = reservas;
	}
	
	public Set<Aluguel> getAluguel() {
		return alugueis;
	}
	public void setAlugueis(Set<Aluguel> alugueis) {
		this.alugueis = alugueis;
	}
	
	public Set<LivroSugerido> getLivrosSugeridos() {
		return livrosSugeridos;
	}
	public void setLivrosSugeridos(Set<LivroSugerido> livrosSugeridos) {
		this.livrosSugeridos = livrosSugeridos;
	}
	
	public Reserva reservarLivro(LivroCadastrado livro) {
		int disponivel = livro.getQuantidadeDisponivel();
		int alugado = livro.getQuantidadeAlugado();
		int total = livro.getQuantidadeTotal();
		int reservado = livro.getQuantidadeReserva();
		print(livro);
		
		//não permitir nova reserva se o cliente já está com o livro
		Iterator<Reserva> itr = reservas.iterator();
		Reserva reserva;
		while(itr.hasNext()) {
			reserva = itr.next();
			if(reserva.getLivroReservado() == livro && reserva.reservaAtiva()) {
				return null;
			}
		}
		
		//não permitir reserva caso não exista exemplar do livro disponivel para alugar
		
		if(disponivel <= 0 && reservado - (total - alugado) >= alugado) {
			return null;
		}
		reserva = new Reserva(this, livro);
		print(livro);
		reservas.add(reserva);
		return reserva;
	}
	
	public Aluguel alugarLivro(Exemplar exemplar) {
		LivroCadastrado livro = exemplar.getLivro();
		int disponivel = livro.getQuantidadeDisponivel();
//		int alugado = livro.getQuantidadeAlugado();
//		int total = livro.getQuantidadeTotal();
//		int reservado = livro.getQuantidadeReserva();
		
		//não permitir alugar livro que já está alugado pelo mesmo cliente
		Iterator<Aluguel> itr = alugueis.iterator();
		Aluguel aluguel;
		while(itr.hasNext()) {
			aluguel = itr.next();
			if(aluguel.getLivroAlugado().getLivro() == livro && aluguel.aluguelAtivo()) {
				return null;
			}
		}
		
		//não permitir aluguel caso não exista exemplar do livro disponivel para alugar
		if(disponivel > 0) {
			aluguel = new Aluguel(this, exemplar);
			alugueis.add(aluguel);
			print(livro);
			return aluguel;
		}System.out.println(disponivel);
		return null;
	}
	
	//método movido de funcionário para cliente
	public boolean requisitarLivro(LivroSugerido livro) {
		if(livrosSugeridos.contains(livro)) {
			return false;
		}
		livrosSugeridos.add(livro);
		return true;
	}
	
	public boolean atenderRequerimento(LivroSugerido livroAtendido) {
		if(livrosSugeridos.contains(livroAtendido)) {
			livrosSugeridos.remove(livroAtendido);
			enviarEmailRequerimentoAtendido(email);
			return true;
		}
		//cliente nao requisitou o livro que foi atendido
		return false;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public int getTelefone() {
		return telefone;
	}
	
	public void setTelefone(int telefone) {
		this.telefone = telefone;
	}
	
	private boolean enviarEmailRequerimentoAtendido(String email) {
		return false;
	}
	
	private void print(LivroCadastrado livro) {
//		System.out.println("disponivel: " + livro.getQuantidadeDisponivel());
//		System.out.println("alugado: " + livro.getQuantidadeAlugado());
//		System.out.println("total: " + livro.getQuantidadeTotal());
//		System.out.println("reservado: " + livro.getQuantidadeReserva());
	}

}
