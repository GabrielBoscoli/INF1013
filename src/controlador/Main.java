package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import dao.FachadaDao;
import modelo.Aluguel;
import modelo.Bibliotecário;
import modelo.Cliente;
import modelo.Exemplar;
import modelo.Funcionário;
import modelo.Gerente;
import modelo.LivroCadastrado;
import modelo.LivroSugerido;
import modelo.Reserva;

public class Main {
	
	private static int geraId() {
		Random r = new Random( System.currentTimeMillis() );
	    return 10000 + r.nextInt(90000);
	}

	public static void main(String[] args) throws IOException {
		Scanner leitor = new Scanner(System.in);
		String inputString;
		int input = -1;
		
		//inicializacao dos dao
		FachadaDao fachadaDao = FachadaDao.getInstance();
		
		Set<Cliente> clientes = new HashSet<Cliente>();
		
		ArrayList<Cliente> arrayClientes = fachadaDao.getAllClientes();
		ArrayList<LivroCadastrado> arrayLivros = fachadaDao.getAllLivros();
		ArrayList<Funcionário> arrayFuncionarios = fachadaDao.getAllFuncionarios();
		
		boolean encerrarPrograma = false;
		
		while(!encerrarPrograma) {
			Funcionário funcionario = LogarNoSistema.logar(arrayFuncionarios, leitor);
			while(funcionario != null && funcionario.estaLogado()) {
				System.out.println("Para buscar livro: 0");
				System.out.println("Para alugar livro: 1");
				System.out.println("Para devolver livro: 2");
				System.out.println("Para cancelar aluguel: 3");
				System.out.println("Para cobrar aluguel atrasado: 4");
				System.out.println("Para reservar livro: 5");
				System.out.println("Para concluir reserva: 6");
				System.out.println("Para cancelar reserva: 7");
				System.out.println("Para requisitar livro: 8");
				if(funcionario instanceof Gerente) {
					System.out.println("Para atender requerimento: 9");
				}
				
				inputString = leitor.nextLine().toLowerCase();
				try {
					input = Integer.parseInt(inputString);
				} catch (NumberFormatException e) {
					System.out.println("Digite um valor válido.");
				}
				
				if(input >= 0 && input <= 9) {
					switch (input) {
						case 0:
							BuscarLivro.buscar(funcionario, arrayLivros, leitor);
							break;
						case 1:
							AlugarLivro.alugar(funcionario, arrayClientes, arrayLivros, leitor);
							break;
						case 2:
							DevolverLivro.devolver(funcionario, arrayClientes, arrayLivros, leitor);
							break;
						case 3:
							CancelarAluguel.cancelar(funcionario, arrayClientes, arrayLivros, leitor);
							break;
						case 4:
							CobrarAluguel.cobrar(funcionario, arrayClientes, leitor);
							break;
						case 5:
							ReservarLivro.reservar(funcionario, arrayClientes, arrayLivros, leitor);
							break;
						case 6:
							ConcluirReserva.concluir(funcionario, arrayClientes, arrayLivros, leitor);
							break;
						case 7:
							CancelarReserva.cancelar(funcionario, arrayClientes, arrayLivros, leitor);
							break;
						case 8:
							RequisitarLivro.requisitar(funcionario, arrayClientes, leitor);
							break;
						case 9:
							if(funcionario instanceof Gerente) {
								AtenderRequerimento.atender((Gerente) funcionario, arrayClientes, leitor);
							}
							break;
						case 10:
							break;
					}
				}
			}
		}
		
//		if(arrayClientes.size() > 0 && arrayLivros.size() > 0) {
//			
//			System.out.println("Verifica se as referencia estao certas");
//			Cliente magui = fachadaDao.getCliente(11111111111l).get();
//			LivroCadastrado livroHP2 = fachadaDao.getLivro("Harry Potter 2").get();
//			magui.getAluguel().forEach(aluguel -> {
//				if(aluguel.aluguelAtivo()) {
//					if(aluguel == livroHP2.getExemplar(97).getAluguel()) {
//						System.out.println("Referencias estão corretas");
//					} else {
//						System.out.println("Referencia está incorreta " + aluguel.getLivroAlugado().getId());
//					}
//				}
//			});
//			
//			fachadaDao.update();
//			System.out.println("\nPressione enter para continuar.");
//			leitor.nextLine().toLowerCase();
//
//			System.out.println("cancela todos os alugueis do magui");
//			magui.getAluguel().forEach(aluguel -> {
//				if(aluguel.cancelarAluguel()) {
//					System.out.println(aluguel.getLivroAlugado().getLivro() + "cancelou");
//				} else {
//					System.out.println(aluguel.getLivroAlugado().getLivro() + "não cancelou");
//				}
//			});
//			
//			fachadaDao.update();
//			System.out.println("\nPressione enter para continuar.");
//			leitor.nextLine().toLowerCase();
//			
//			System.out.println("magui aluga Harry Potter 1");
//			LivroCadastrado livro = fachadaDao.getLivro("Harry Potter 1").get();
//			magui.alugarLivro(livro.getExemplar(99));
//			
//			fachadaDao.update();
//			System.out.println("\nPressione enter para continuar.");
//			leitor.nextLine().toLowerCase();
//			
//		} else {
//			//criando primeiro cliente
//			Cliente magui = new Cliente(11111111111l, "Magui", "Gavea, Marques, 98, 301", "magui@email.com", 999999999);
//			clientes.add(magui);
//			fachadaDao.saveCliente(magui);
//			
//			//criando segundo cliente
//			Cliente singelo = new Cliente(22222222222l, "Singelo", "Leblon, Arqueto, 51, 102", "singelo@email.com", 999999998);
//			clientes.add(singelo);
//			fachadaDao.saveCliente(singelo);
//			
//			//criando terceiro cliente
//			Cliente gargamel = new Cliente(33333333333l, "Gargamel", "Gavea, Arauto, 62", "gargamel@email.com", 999999988);
//			clientes.add(gargamel);
//			fachadaDao.saveCliente(gargamel);
//			
//			Set<LivroCadastrado> livrosCadastrados = new HashSet<LivroCadastrado>();
//			
//			//criando primeiro livro
//			LivroCadastrado hp1 = new LivroCadastrado("Harry Potter 1", "J.K Rolling", "Fantasia", "Harry Editora");
//			livrosCadastrados.add(hp1);
//			Set<Exemplar> exemplaresHP1 = new HashSet<Exemplar>();
//			Exemplar e1 = new Exemplar('a', hp1);
//			exemplaresHP1.add(e1);
//			Exemplar e2 = new Exemplar('b', hp1);
//			exemplaresHP1.add(e2);
//			Exemplar e3 = new Exemplar('c', hp1);
//			exemplaresHP1.add(e3);
//			hp1.addExemplares(exemplaresHP1);
//			fachadaDao.saveLivro(hp1);
//			
//			//criando segundo livro
//			LivroCadastrado hp2 = new LivroCadastrado("Harry Potter 2", "J.K Rolling", "Fantasia", "Harry Editora");
//			livrosCadastrados.add(hp2);
//			Set<Exemplar> exemplaresHP2 = new HashSet<Exemplar>();
//			Exemplar eHP2 = new Exemplar('a', hp2);
//			exemplaresHP2.add(eHP2);
//			e2 = new Exemplar('b', hp2);
//			exemplaresHP2.add(e2);
//			e3 = new Exemplar('c', hp2);
//			exemplaresHP2.add(e3);
//			hp2.addExemplares(exemplaresHP2);
//			fachadaDao.saveLivro(hp2);
//			
//			//criando primeiro livro
//			LivroCadastrado nemTeConto = new LivroCadastrado("Nem te conto", "Spielberg Harry", "Biografia", "Editora Spius");
//			livrosCadastrados.add(nemTeConto);
//			Exemplar eNemTeConto = new Exemplar('a', nemTeConto);
//			nemTeConto.addExemplar(eNemTeConto);
//			fachadaDao.saveLivro(nemTeConto);
//			
//			//criando primeiro livro
//			LivroCadastrado voltaProTerreo = new LivroCadastrado("Volta pro térreo", "Baltazar", "Drama", "Editazar");
//			livrosCadastrados.add(voltaProTerreo);
//			Set<Exemplar> exemplaresVoltaProTerreo = new HashSet<Exemplar>();
//			e1 = new Exemplar('a', voltaProTerreo);
//			exemplaresVoltaProTerreo.add(e1);
//			e2 = new Exemplar('b', voltaProTerreo);
//			exemplaresVoltaProTerreo.add(e2);
//			voltaProTerreo.addExemplares(exemplaresVoltaProTerreo);
//			fachadaDao.saveLivro(voltaProTerreo);
//			
//			//criando primeiro livro sugerido
//			LivroSugerido livroSugerido1 = new LivroSugerido("Lixeiro","Cataclisma");
//			
//			//criando segundo livro sugerido
//			LivroSugerido livroSugerido2 = new LivroSugerido("Motorista","Poli Glota");
//			
//			//criando bibliotecário
//			int id = Main.geraId();
//			Bibliotecário bibliotecario = new Bibliotecário("Cornelius", id, "12345");
//			fachadaDao.saveFuncionario(bibliotecario);
//			
//			//criando gerente
//			int idGerente = Main.geraId();
//			Gerente gerente = new Gerente("Papa Aquatico", idGerente, "11111");
//			fachadaDao.saveFuncionario(gerente);
//			
//			System.out.println("Realizando login com senha/id inválido...");
//			if(!bibliotecario.logar(id, "12344") && !bibliotecario.logar(1, "12345")) {
//				System.out.println("Erro ao realizar login.");
//			} else {
//				System.out.println("Pelo menos um dos logins concluiram indevidamente.");
//			}
//			
//			System.out.println("\nPressione enter para continuar.");
//			leitor.nextLine().toLowerCase();
//			
//			System.out.println("Realizando login...");
//			if(bibliotecario.logar(id, "12345")) {
//				System.out.println("Login realizado com sucesso.");
//			} else {
//				System.out.println("Erro ao realizar login.");
//			}
//			
//			fachadaDao.update();
//			
//			System.out.println("\nPressione enter para continuar.");
//			leitor.nextLine().toLowerCase();
//			
//			System.out.println("Bibliotecário buscando por livros que combinam com 'Harry'...");
//			Set<LivroCadastrado> retornoBusca = bibliotecario.buscarLivro(livrosCadastrados, "Harry");
//			System.out.println("Resultado da busca:");
//			if(retornoBusca != null) {
//				retornoBusca.forEach((livro) -> {
//					livro.exibirDetalhes();
//				});			
//			}
//			
//			System.out.println("\nPressione enter para continuar.");
//			leitor.nextLine().toLowerCase();
//			
//			System.out.println("Bibliotecário buscando por livros que combinam com 'Spius'...");
//			retornoBusca = bibliotecario.buscarLivro(livrosCadastrados, "Spius");
//			System.out.println("Resultado da busca:");
//			if(retornoBusca.size() == 0) {
//				System.out.println("Busca não encontrou resultados, como esperado.");		
//			} else {
//				System.out.println("Busca encontrou resultados, mas não devia.");
//			}
//			
//			System.out.println("\nPressione enter para continuar.");
//			leitor.nextLine().toLowerCase();
//			
//			System.out.println("Magui reserva livro intitulado 'Nem te Conto'...");
//			Reserva reservaMagui = magui.reservarLivro(nemTeConto);
//			if(reservaMagui != null) {
//				System.out.println("Reserva realizada com sucesso.");
//			} else {
//				System.out.println("Erro ao realizar reserva.");
//			}
//			
//			fachadaDao.update();
//			
//			System.out.println("\nPressione enter para continuar.");
//			leitor.nextLine().toLowerCase();
//			
//			System.out.println("Singelo reserva livro cujo unico exemplar já está reservado...");
//			if(singelo.reservarLivro(nemTeConto) != null) {
//				System.out.println("Reserva foi concluída, mas não devia.");
//			} else {
//				System.out.println("Reserva não foi realizada, como esperado.");
//			}
//			
//			fachadaDao.update();
//			
//			System.out.println("\nPressione enter para continuar.");
//			leitor.nextLine().toLowerCase();
//			
//			System.out.println("Magui cancela sua reserva...");
//			if(reservaMagui.cancelarReserva()) {
//				System.out.println("Reserva cancelada com sucesso.");
//			} else {
//				System.out.println("Erro ao cancelar reserva.");
//			}
//			
//			fachadaDao.update();
//			
//			System.out.println("\nPressione enter para continuar.");
//			leitor.nextLine().toLowerCase();
//			
//			System.out.println("Singelo reserva livro intitulado 'Nem te Conto'...");
//			Reserva reservaSingelo = singelo.reservarLivro(nemTeConto);
//			if(reservaSingelo != null) {
//				System.out.println("Reserva realizada com sucesso.");
//			} else {
//				System.out.println("Erro ao realizar reserva.");
//			}
//			
//			fachadaDao.update();
//			
//			System.out.println("\nPressione enter para continuar.");
//			leitor.nextLine().toLowerCase();
//			
//			System.out.println("Singelo conclui sua reserva...");
//			if(reservaSingelo.concluirReserva(eNemTeConto)) {
//				System.out.println("Reserva concluida com sucesso.");
//			} else {
//				System.out.println("Erro ao concluir reserva.");
//			}
//			
//			fachadaDao.update();
//			
//			System.out.println("\nPressione enter para continuar.");
//			leitor.nextLine().toLowerCase();
//			
//			System.out.println("Gargamel reserva livro cujo unico exemplar está alugado...");
//			Reserva reservaGargamel = gargamel.reservarLivro(nemTeConto);
//			if(reservaGargamel != null) {
//				System.out.println("Reserva realizada com sucesso.");
//			} else {
//				System.out.println("Erro ao realizar reserva.");
//			}
//			
//			fachadaDao.update();
//			
//			System.out.println("\nPressione enter para continuar.");
//			leitor.nextLine().toLowerCase();
//			
//			System.out.println("Singelo devolve livro alugado...");
//			if(eNemTeConto.getAluguel().devolverLivro()) {
//				System.out.println("Livro devolvido com sucesso.");
//			} else {
//				System.out.println("Erro ao devolver livro.");
//			}
//			
//			fachadaDao.update();
//			
//			System.out.println("\nPressione enter para continuar.");
//			leitor.nextLine().toLowerCase();
//			
//			System.out.println("Magui reserva livro cujo unico exemplar já está reservado...");
//			if(magui.reservarLivro(nemTeConto) != null) {
//				System.out.println("Reserva foi concluída, mas não devia.");
//			} else {
//				System.out.println("Reserva não foi realizada, como esperado.");
//			}
//			
//			fachadaDao.update();
//			
//			System.out.println("\nPressione enter para continuar.");
//			leitor.nextLine().toLowerCase();
//			
//			System.out.println("Magui aluga livro intitulado 'Volta Pro Terreo'...");
//			Aluguel aluguelMagui = magui.alugarLivro(e1);
//			if(aluguelMagui != null) {
//				System.out.println("Aluguel realizado com sucesso.");
//			} else {
//				System.out.println("Erro ao realizar aluguel.");
//			}
//			
//			fachadaDao.update();
//			
//			System.out.println("\nPressione enter para continuar.");
//			leitor.nextLine().toLowerCase();
//			
//			System.out.println("Magui cancela aluguel...");
//			if(aluguelMagui.cancelarAluguel()) {
//				System.out.println("Aluguel cancelado com sucesso.");
//			} else {
//				System.out.println("Erro ao cancelar aluguel.");
//			}
//			
//			fachadaDao.update();
//			
//			System.out.println("\nPressione enter para continuar.");
//			leitor.nextLine().toLowerCase();
//			
//			System.out.println("Magui aluga livro intitulado 'Harry Potter 2'...");
//			aluguelMagui = magui.alugarLivro(eHP2);
//			if(aluguelMagui != null) {
//				System.out.println("Aluguel realizado com sucesso.");
//			} else {
//				System.out.println("Erro ao realizar aluguel.");
//			}
//			
//			fachadaDao.update();
//			
//			System.out.println("\nPressione enter para continuar.");
//			leitor.nextLine().toLowerCase();
//			
//			System.out.println("Aluguel de magui é cobrado...");
//			if(aluguelMagui.cobrarAluguelAtrasado(1)) {
//				System.out.println("Cobrança realizada com sucesso, sendo que deveria ter sido impedida.");
//			} else {
//				System.out.println("Não foi permitido realizar cobrança, visto que cliente não está com aluguel atrasado.");
//			}
//			
//			fachadaDao.update();
//			
//			System.out.println("\nPressione enter para continuar.");
//			leitor.nextLine().toLowerCase();
//			
//			System.out.println("Aluguel de magui é cobrado depois da data prevista de devolução...");
//			aluguelMagui.mudarDatas();
//			if(aluguelMagui.cobrarAluguelAtrasado(1)) {
//				System.out.println("Cobrança realizada com sucesso.");
//			} else {
//				System.out.println("Erro ao realizar cobrança.");
//			}
//			
//			fachadaDao.update();
//			
//			System.out.println("\nPressione enter para continuar.");
//			leitor.nextLine().toLowerCase();
//			
//			System.out.println("Clientes requisitam aquisição de livros...");
//			if(singelo.requisitarLivro(livroSugerido1) && singelo.requisitarLivro(livroSugerido2) && magui.requisitarLivro(livroSugerido1)) {
//				System.out.println("Requisições realizadas com sucesso.");
//			} else {
//				System.out.println("Erro ao realizar pelo menos uma das requisições.");
//			}
//			
//			fachadaDao.update();
//			
//			System.out.println("\nPressione enter para continuar.");
//			leitor.nextLine().toLowerCase();
//			
//			System.out.println("Gerente atende requerimentos...");
//			gerente.logar(idGerente, "11111");
//			//obs: o método conta com o pre requisito que o livro sugerido já foi cadastrado
//			if(gerente.atenderRequerimento(clientes, livroSugerido1) == 2) {
//				System.out.println("Requisições atendidas com sucesso.");
//			} else {
//				System.out.println("Pelo menos uma das requisições pelo livro não foram atendidas.");
//			}
//			
//			fachadaDao.update();
//			
//			System.out.println("\nPressione enter para continuar.");
//			leitor.nextLine().toLowerCase();
//			
//			System.out.println("Funcionário deslogam do sistema...");
//			gerente.deslogar();
//			bibliotecario.deslogar();
//			if(gerente.buscarLivro(livrosCadastrados, "Harry") == null && bibliotecario.buscarLivro(livrosCadastrados, "Harry") == null) {
//				System.out.println("Funcionário sairam do sistema, como esperado.");
//			} else {
//				System.out.println("Funcionário ainda estão no sistema, incorretamente.");
//			}
//			
//			fachadaDao.update();
//			
//			System.out.println("\nPressione enter para continuar.");
//			leitor.nextLine().toLowerCase();
//			
//			//clienteDao.delete(magui);
//			//clienteDao.delete(singelo);
//			//clienteDao.delete(gargamel);
//			
//			//funcionarioDao.delete(gerente);
//			//funcionarioDao.delete(bibliotecario);
//			
//			//livrocadastradoDao.delete(hp1);
//			//livrocadastradoDao.delete(hp2);
//			//livrocadastradoDao.delete(nemTeConto);
//			//livrocadastradoDao.delete(voltaProTerreo);
//		}
		
		
		leitor.close();
		//LivroCadastradoDao l = new LivroCadastradoDao();
	}

}
