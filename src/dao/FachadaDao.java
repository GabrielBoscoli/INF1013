package dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import modelo.Cliente;
import modelo.Exemplar;
import modelo.Funcionário;
import modelo.LivroCadastrado;

public class FachadaDao {
	private static FachadaDao instance = null;
	
	private ClienteDao clienteDao;
	private LivroCadastradoDao livroDao;
	private FuncionarioDao funcionarioDao;
	
	private ArrayList<Cliente> clientes;
	private ArrayList<LivroCadastrado> livros;
	private ArrayList<Funcionário> funcionarios;
	
	//acho que vou tirar esses sets pq da mt trabalho manter eles nos updates e saves
	
//	private ArrayList<Reserva> reservas;
//	private ArrayList<Aluguel> alugueis;
//	private ArrayList<Exemplar> exemplares;
	
	public static FachadaDao getInstance() throws IOException {
		if(instance == null)
			instance = new FachadaDao();
		return instance;
	}
	
	private FachadaDao() throws IOException {
		//reservas = new ArrayList<Reserva>();
		//alugueis = new ArrayList<Aluguel>();
		//exemplares = new ArrayList<Exemplar>();
		
		clienteDao = new ClienteDao();
		livroDao = new LivroCadastradoDao();
		funcionarioDao = new FuncionarioDao();
		
		clientes = clienteDao.getAll();
		livros = livroDao.getAll();
		funcionarios = funcionarioDao.getAll();
		if(clientes == null) {
			clientes = new ArrayList<Cliente>();
		}
		if(livros == null) {
			livros = new ArrayList<LivroCadastrado>();
		}
		if(funcionarios == null) {
			funcionarios = new ArrayList<Funcionário>();
		}
		
		//consertando as referencias
		
		for(int j = 0; j < clientes.size(); j++) {
			ArrayList<LivroCadastrado> copyLivros = new ArrayList<>(livros);
			clientes.get(j).getReservas().forEach(reserva -> {
				//precisa construir um equals na classe livro. um livro seria igual ao outro se possuirem mesmo nome, autor e editora.
				//tem que verificar se o objeto da copia é de fato o objeto do arraylist livros.
				LivroCadastrado livro = copyLivros.get(copyLivros.indexOf(reserva.getLivroReservado()));
				reserva.setLivroReservado(livro);
				livro.reservar(reserva);// isso funcionará quando o livroCadastradoDao retornar livros com reservas vazias.
				//reservas.add(reserva);
			});
			ArrayList<LivroCadastrado> anotherCopyLivros = new ArrayList<>(livros);
			clientes.get(j).getAluguel().forEach(aluguel -> {
				Exemplar exemplar = aluguel.getLivroAlugado();
				LivroCadastrado livro = anotherCopyLivros.get(anotherCopyLivros.indexOf(exemplar.getLivro()));//precisa fazer equals no exemplar!!!
				if(aluguel.aluguelAtivo()) {
					livro.getExemplar(exemplar.getId()).exemplarAlugado(aluguel);//examplar do livro agora possui aluguel
				}
				aluguel.setLivroAlugado(livro.getExemplar(exemplar.getId()));//exemplar do aluguel agora é o mesmo do livro.
				//exemplares.add(aluguel.getLivroAlugado());
				//alugueis.add(aluguel);
			});
		}
	}
	
	public ArrayList<Cliente> getAllClientes() {
		return clientes;
	}
	
	public ArrayList<LivroCadastrado> getAllLivros() {
		return livros;
	}
	
	public ArrayList<Funcionário> getAllFuncionarios() {
		return funcionarios;
	}
	
	public Optional<Cliente> getCliente(long cpf) {
		String[] params = new String[1];
		params[0] = String.valueOf(cpf);
		return clienteDao.get(params);
	}
	
	public Optional<LivroCadastrado> getLivro(String nome) {
		String[] params = new String[1];
		params[0] = nome;
		return livroDao.get(params);
	}
	
	public void saveCliente(Cliente t) {
		if(!clientes.contains(t)) {
			clienteDao.save(t);
		}
//		t.getReservas().forEach(reserva -> {
//			if(!reservas.contains(reserva)) {
//				reservas.add(reserva);
//			}
//		});
//		t.getAluguel().forEach(aluguel -> {
//			if(!alugueis.contains(aluguel)) {
//				alugueis.add(aluguel);
//				exemplares.add(aluguel.getLivroAlugado());//faz sentido add exempalr?
//			}
//		});
	}
	
	public void saveLivro(LivroCadastrado t) {
		if(!livros.contains(t)) {
			livroDao.save(t);			
		}
//		t.getReservas().forEach(reserva -> {
//			if(!reservas.contains(reserva)) {
//				reservas.add(reserva);
//			}
//		});
//		t.getExemplares().forEach(exemplar -> {
//			if(!exemplares.contains(exemplar)) {
//				exemplares.add(exemplar);
//				alugueis.add(exemplar.getAluguel());//faz sentido add aluguel?
//			}
//		});
	}
	
	public void saveFuncionario(Funcionário t) {
		if(!funcionarios.contains(t)) {
			funcionarioDao.save(t);
		}
	}
	
	public void update() {
		clienteDao.update();
		livroDao.update();
		funcionarioDao.update();
	}
	
	public void deleteCliente(Cliente t) {
		if(clientes.contains(t)) {
			clienteDao.delete(t);
		}
//		t.getReservas().forEach(reserva -> {
//			if(reservas.contains(reserva)) {
//				reservas.remove(reserva);
//			}
//		});
//		t.getAluguel().forEach(aluguel -> {
//			if(alugueis.contains(aluguel)) {
//				alugueis.remove(aluguel);
//			}
//		});
	}
	
	public void deleteLivro(LivroCadastrado t) {
		if(livros.contains(t)) {
			livroDao.delete(t);
		}
//		t.getReservas().forEach(reserva -> {
//			if(reservas.contains(reserva)) {
//				reservas.remove(reserva);
//			}
//		});
//		t.getExemplares().forEach(exemplar -> {
//			if(exemplares.contains(exemplar)) {
//				exemplares.remove(exemplar);
//				alugueis.remove(exemplar.getAluguel());
//			}
//		});
	}
	
	public void deleteFuncionario(Funcionário t) {
		if(funcionarios.contains(t)) {
			funcionarioDao.delete(t);
		}
	}
	
}
