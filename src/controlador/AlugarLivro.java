package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import dao.FachadaDao;
import modelo.Aluguel;
import modelo.Cliente;
import modelo.Exemplar;
import modelo.Funcionário;
import modelo.LivroCadastrado;

public class AlugarLivro {
	public static Aluguel alugar(Funcionário funcionario, ArrayList<Cliente> clientes, ArrayList<LivroCadastrado> livros,
			Scanner leitor) throws IOException {
		Cliente clienteAluguel = null;
		boolean novoCliente = false;
		Aluguel aluguel = null;
		
		//inserir cpf do cliente
		System.out.println("Digite o cpf do cliente:");
		String cpfStr = "";
		long cpf = 0;
		boolean inputValido = false;
		
		while(!inputValido) {
			cpfStr = leitor.next();
			try {
				cpf = Long.parseLong(cpfStr);
				inputValido = true;
			} catch (NumberFormatException e) {
				System.out.println("Digite um valor válido.");
			}			
		}
		
		String nome;
		String email;
		String endereco;
		int telefone;
		
		for(Cliente cliente: clientes) {
			if(cliente.getCpf() == cpf) {
				clienteAluguel = cliente;
				break;
			}
		}
		
		//se não tiver cliente cadastrado com tal cpf, inserir demais campos.
		if(clienteAluguel == null) {
			novoCliente = true;
			System.out.println("Digite o nome do cliente: ");
			nome = leitor.next();
			System.out.println("Digite o email do cliente: ");
			email = leitor.next();
			System.out.println("Digite o endereco do cliente: ");
			endereco = leitor.next();
			
			System.out.println("Digite o telefone do cliente: ");
			String telefoneStr;
			telefone = 0;
			inputValido = false;
			while(!inputValido) {
				telefoneStr = leitor.next();
				try {
					telefone = Integer.parseInt(telefoneStr);					
				} catch(NumberFormatException e) {
					System.out.println("Digite um telefone válido.");
				}
			}
			
			clienteAluguel = new Cliente(cpf, nome, endereco, email, telefone);
		} else { //caso contrário, só exibir as propriedades do cliente cadastrado
			System.out.println("Nome do cliente: " + clienteAluguel.getNome());
			System.out.println("Email do cliente: " + clienteAluguel.getEmail());
			System.out.println("Endereco do cliente: " + clienteAluguel.getEndereco());
			System.out.println("Telefone do cliente: " + clienteAluguel.getTelefone());
		}
		
		// buscar o livro que será alugado
		LivroCadastrado livroAlugado = BuscarLivro.buscar(funcionario, livros, leitor);
		
		// inserir exemplar do livro que será alugado
		System.out.println("Digite o id do exemplar alugado do livro " + livroAlugado.getNome() + ": ");
		String idStr;
		int id;
		inputValido = false;
		Exemplar exemplar = null;
		
		while(!inputValido) {
			idStr = leitor.next();
			try {
				id = Integer.parseInt(idStr);
				exemplar = livroAlugado.getExemplar(id);
				if(exemplar == null) {
					throw new Exception();
				}
				inputValido = true;
			} catch (NumberFormatException e) {
				System.out.println("Digite um id válido.");
			} catch (Exception e) {
				System.out.println("O livro alugado não possui exemplar com esse id.");
				System.out.println("Digite um id válido.");
			}
		}
		
		// confirmar aluguel
		System.out.println("Confirmar aluguel? Digite 's' para confirmar e 'n' para cancelar.");
		String confirmar;
		inputValido = false;
		
		while(!inputValido) {
			confirmar = leitor.next();
			try {
				if(confirmar.equals("s")) {
					aluguel = clienteAluguel.alugarLivro(exemplar);
					if(aluguel != null) {
						if(novoCliente) { // se cliente for novo, salvar ele no banco
							FachadaDao.getInstance().saveCliente(clienteAluguel);
						} else { // se cliente já foi cadastrado, apenas atualizar suas infos
							FachadaDao.getInstance().update();
						}						
					} else {
						System.out.println("Não foi possível realizar aluguel.");
					}
				} else if(confirmar.equals("n")) { // cancelar processo.
					System.out.println("Aluguel cancelado.");
				} else {
					throw new Exception();
				}
				inputValido = true;
			} catch(Exception e) {
				System.out.println("Digite um valor válido.");
			}
		}
		
		return aluguel;
	}
}
