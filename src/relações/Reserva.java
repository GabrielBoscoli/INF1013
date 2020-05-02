package relações;

import java.util.Calendar;

import dao.Hidden;
import livro.Exemplar;
import livro.LivroCadastrado;

public class Reserva {
	private Calendar dataRealizacao;
	private Calendar dataPrevista;
	private boolean atendido;
	
	//atributos adicionados
	private boolean cancelado;
	private boolean livroDisponivel;
	@Hidden
	private LivroCadastrado livroReservado;
	@Hidden
	private Cliente cliente;
	
	public LivroCadastrado getLivroReservado() {
		return livroReservado;
	}
	public void setLivroReservado(LivroCadastrado livroReservado) {
		this.livroReservado = livroReservado;
	}
	
	//a propria criacao da reserva seria a funcao reservarLivro()
	public Reserva(Cliente cliente, LivroCadastrado livro) {
		setDataRealizacao(Calendar.getInstance());
		dataPrevista = Calendar.getInstance(); //calcular previsao
		atendido = false;
		cancelado = false;
		livroReservado = livro;
		this.cliente = cliente;
		
		if(reservarLivro()) {
			livro.reservar(this);
		}
		//reservarLivro();
	}
	
	private boolean reservarLivro() {
		int disponivel = livroReservado.getQuantidadeDisponivel();
		int alugado = livroReservado.getQuantidadeAlugado();
		int total = livroReservado.getQuantidadeTotal();
		int reservado = livroReservado.getQuantidadeReserva();
		if(disponivel > 0) {
			livroReservado.subQuantidadeDisponivel();
			livroDisponivel = true;
			livroReservado.addQuantidadeReserva();
			return true;
		}
		if(reservado - (total - alugado) < alugado) {
			livroDisponivel = false;
			livroReservado.addQuantidadeReserva();
			return true;
		}
		return false;
	}
	
	public boolean concluirReserva(Exemplar exemplar) {
		int reservado = livroReservado.getQuantidadeReserva();
		if(reservado <= 0) {
			return false;
		}
		livroReservado.subQuantidadeReserva();
		livroReservado.addQuantidadeDisponivel();
		cliente.alugarLivro(exemplar);
		print(livroReservado);
		atendido = true;
		return true;
	}
	
	public boolean cancelarReserva() {
		int disponivel = livroReservado.getQuantidadeDisponivel();
		int reservado = livroReservado.getQuantidadeReserva();
		int total = livroReservado.getQuantidadeTotal();
		int alugado = livroReservado.getQuantidadeAlugado();
		if(reservado <= 0) {
			return false;
		}
		livroReservado.subQuantidadeReserva();
		if(disponivel > 0 || reservado - (total - alugado) == 0) {
			livroReservado.addQuantidadeDisponivel();
		}
		print(livroReservado);
		cancelado = true;
		return true;
	}
	
	public void anteciparReserva() {
		livroDisponivel = true;
		//manda email avisando cliente
		enviarEmail();
	}
	
	//métodos adicionados
	public boolean reservaAtiva() {
		if(atendido == false && cancelado == false) {
			return true;
		}
		return false;
	}
	
	public boolean livroEstaDisponivel() {
		return livroDisponivel;
	}
	
	public Calendar getDataPrevista() {
		return dataPrevista;
	}
	
//	@SuppressWarnings("unchecked")
//	private JSONObject getJSONObject() {
//		JSONObject reserva = new JSONObject();
//		reserva.put("data-realizacao", this.dataRealizacao);
//		reserva.put("data-prevista", this.dataPrevista);
//		reserva.put("atendido", this.atendido);
//		reserva.put("cancelado", this.cancelado);
//		return reserva;
//	}
	
	private void enviarEmail() {
		
	}
	
	private void print(LivroCadastrado livro) {
//		System.out.println("disponivel: " + livro.getQuantidadeDisponivel());
//		System.out.println("alugado: " + livro.getQuantidadeAlugado());
//		System.out.println("total: " + livro.getQuantidadeTotal());
//		System.out.println("reservado: " + livro.getQuantidadeReserva());
	}

	public Calendar getDataRealizacao() {
		return dataRealizacao;
	}

	public void setDataRealizacao(Calendar dataRealizacao) {
		this.dataRealizacao = dataRealizacao;
	}
	
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

}
