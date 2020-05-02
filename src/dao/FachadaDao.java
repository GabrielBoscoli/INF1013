package dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import livro.Exemplar;
import livro.LivroCadastrado;
import relações.Aluguel;
import relações.Cliente;
import relações.Reserva;

public class FachadaDao {
	private ClienteDao clienteDao;
	private LivroCadastradoDao livroDao;
	
	private ArrayList<Cliente> clientes;
	private ArrayList<LivroCadastrado> livros;
	private ArrayList<Reserva> reservas;
	private ArrayList<Aluguel> alugueis;
	private ArrayList<Exemplar> exemplares;
	
	public FachadaDao() throws IOException {
		reservas = new ArrayList<Reserva>();
		alugueis = new ArrayList<Aluguel>();
		exemplares = new ArrayList<Exemplar>();
		
		clienteDao = new ClienteDao();
		livroDao = new LivroCadastradoDao();
		
		clientes = clienteDao.getAll();
		livros = livroDao.getAll();
		if(clientes == null) {
			clientes = new ArrayList<Cliente>();
		}
		if(livros == null) {
			livros = new ArrayList<LivroCadastrado>();
		}
		
		//consertando as referencias
		
		for(int j = 0; j < clientes.size(); j++) {
			ArrayList<LivroCadastrado> copyLivros = new ArrayList<>(livros);
			clientes.get(j).getReservas().forEach(reserva -> {
				//precisa construir um equals na classe livro. um livro seria igual ao outro se possuirem mesmo nome, autor e editora.
				//tem que verificar se o objeto da copia é de fato o objeto do arraylist livros.
				LivroCadastrado livro = copyLivros.remove(copyLivros.indexOf(reserva.getLivroReservado()));
				reserva.setLivroReservado(livro);
				livro.reservar(reserva);// isso funcionará quando o livroCadastradoDao retornar livros com reservas vazias.
				reservas.add(reserva);
			});
			ArrayList<LivroCadastrado> anotherCopyLivros = new ArrayList<>(livros);
			clientes.get(j).getAluguel().forEach(aluguel -> {
				Exemplar exemplar = aluguel.getLivroAlugado();
				LivroCadastrado livro = anotherCopyLivros.remove(anotherCopyLivros.indexOf(exemplar.getLivro()));//precisa fazer equals no exemplar!!!
				if(exemplar.getAluguel().aluguelAtivo()) {
					livro.getExemplar(exemplar.getId()).exemplarAlugado(exemplar.getAluguel());//examplar do livro agora possui aluguel
				}
				aluguel.setLivroAlugado(livro.getExemplar(exemplar.getId()));//exemplar do aluguel agora é o mesmo do livro.
				exemplares.add(aluguel.getLivroAlugado());
				alugueis.add(aluguel);
			});
		}
	}
	
	public ArrayList<Cliente> getAllClientes() {
		return clientes;
	}
	
	public ArrayList<LivroCadastrado> getAllLivros() {
		return livros;
	}
	
}
