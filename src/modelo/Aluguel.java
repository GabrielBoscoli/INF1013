package modelo;

import java.util.Calendar;

import dao.Hidden;

public class Aluguel {
	private Calendar dataRealizacao;
	private Calendar dataDevolucaoPrevista;
	private Calendar dataDevolucao;
	
	//atributos adicionados
	private boolean cancelado = false;
	private boolean cobrado = false;
	@Hidden
	private Exemplar livroAlugado;
	@Hidden
	private Cliente cliente;
	
	//a propria criacao do aluguel seria a funcao alugarLivro()
	public Aluguel(Cliente cliente, Exemplar livro) {
		dataRealizacao = Calendar.getInstance();
		dataDevolucaoPrevista = Calendar.getInstance();
		dataDevolucaoPrevista.add(Calendar.DAY_OF_MONTH, 7); //o livro deve ser devolvido 7 dias após aluguel
		dataDevolucao = null;
		cancelado = false;
		livroAlugado = livro;
		this.cliente = cliente;
		
		alugarLivro();
	}
	
	private boolean alugarLivro() {
		LivroCadastrado livro = livroAlugado.getLivro();
		if(livro.getQuantidadeDisponivel() > 0) {
			livro.subQuantidadeDisponivel();
			livro.addQuantidadeAlugado();
			livroAlugado.exemplarAlugado(this);
			return true;
		}
		return false;
	}
	
	public boolean devolverLivro() {
		if(devolverOuCancelarAux()) {
			dataDevolucao = Calendar.getInstance();
			livroAlugado.exemplarDevolvido();
			LivroCadastrado livro = livroAlugado.getLivro();
			print(livro);
			return true;
		}
		return false;
	}
	
	public boolean cancelarAluguel() {
		if(devolverOuCancelarAux()) {
			cancelado = true;
			print(livroAlugado.getLivro());
			return true;
		}
		return false;
	}

	public Exemplar getLivroAlugado() {
		return livroAlugado;
	}

	public void setLivroAlugado(Exemplar livroAlugado) {
		this.livroAlugado = livroAlugado;
	}
	
	//métodos adicionados
	public boolean aluguelAtivo() {
		if(dataDevolucao == null && cancelado == false) {
			return true;
		}
		return false;
	}
	
	public boolean isAtrasado() {
		Calendar hoje = Calendar.getInstance();
		if(cancelado == true ||
				hoje.before(dataDevolucaoPrevista) ||
				(dataDevolucao != null && dataDevolucao.before(dataDevolucaoPrevista))) {
			return false; //ou ainda n chegou o dia previsto para devolver ou a devolucao ja foi feita ou cliente ja foi cobrado
		}
		return true;
	}
	
	private boolean devolverOuCancelarAux() {
		LivroCadastrado livro = livroAlugado.getLivro();
		int disponivel = livro.getQuantidadeDisponivel();
		int alugado = livro.getQuantidadeAlugado();
		int reservado = livro.getQuantidadeReserva();
		int total = livro.getQuantidadeTotal();
		if(alugado <= 0) {
			return false;
		}
		if(disponivel > 0) {
			livro.addQuantidadeDisponivel();
			livro.subQuantidadeAlugado();
		} else if(reservado - (total - alugado) <= alugado) {
			livro.subQuantidadeAlugado();
			//avisar pro cliente q alugou q o livro está disponível
			Reserva reserva = livro.getReservaComPrevisaoMaisPerto();
			if(reserva != null) {
				reserva.anteciparReserva();
			}
		} else {
			return false;
		}
		return true;
	}
	
	/**
	 * Cobra o aluguel, se ele estiver atrasado.
	 * @param modo - o modo que a cobrança será efetuada.
	 * Se modo = 1, cobrança será por email.
	 * Se modo !=, cobraça será por telefone.
	 * @return false, se o alguel não pode ser cobrado.
	 * true, se aluguel foi cobrado corretamente.
	 */
	public boolean cobrarAluguelAtrasado(int modo) {
		//se nao estiver atrasado, não permitir cobrança
		if(!this.isAtrasado() || cobrado == true) {
			return false;
		}
		if(modo == 1) {
			return cobrarPorEmail(cliente.getEmail());			
		}
		return cobrarPorTelefone(cliente.getTelefone());
	}
	
	private boolean cobrarPorEmail(String email) {
		if(enviarEmail(email)) {
			cobrado = true;
		}
		return cobrado;
	}
	
	private boolean cobrarPorTelefone(int telefone) {
		if(efetuarLigacao(telefone)) {
			cobrado = true;
		}
		return false;
	}
	
	private boolean enviarEmail(String email) {
		return true;
	}
	
	private boolean efetuarLigacao(int telefone) {
		return false;
	}
	
	//metodo inserido somente para demostracao
	public void mudarDatas() {
		dataRealizacao.add(Calendar.DAY_OF_MONTH, -14);
		dataDevolucaoPrevista.add(Calendar.DAY_OF_MONTH, -7);
	}
	
	private void print(LivroCadastrado livro) {
//		System.out.println("disponivel: " + livro.getQuantidadeDisponivel());
//		System.out.println("alugado: " + livro.getQuantidadeAlugado());
//		System.out.println("total: " + livro.getQuantidadeTotal());
//		System.out.println("reservado: " + livro.getQuantidadeReserva());
	}
	
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
}
