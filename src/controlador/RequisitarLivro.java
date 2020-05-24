package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import dao.FachadaDao;
import modelo.Cliente;
import modelo.Funcionário;
import modelo.LivroSugerido;

public class RequisitarLivro {
	public static boolean requisitar(Funcionário funcionario, ArrayList<Cliente> clientes) throws IOException {
		Scanner leitor = new Scanner(System.in);
		Cliente clienteReserva = null;
		boolean novoCliente = false;
		boolean requerimentoBemSucedido = false;
		
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
				clienteReserva = cliente;
				break;
			}
		}
		
		//se não tiver cliente cadastrado com tal cpf, inserir demais campos.
		if(clienteReserva == null) {
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
			
			clienteReserva = new Cliente(cpf, nome, endereco, email, telefone);
		} else { //caso contrário, só exibir as propriedades do cliente cadastrado
			System.out.println("Nome do cliente: " + clienteReserva.getNome());
			System.out.println("Email do cliente: " + clienteReserva.getEmail());
			System.out.println("Endereco do cliente: " + clienteReserva.getEndereco());
			System.out.println("Telefone do cliente: " + clienteReserva.getTelefone());
		}
		
		System.out.println("Digite o nome do livro requisitado: ");
		String nomeLivro = leitor.next();
		System.out.println("Digite o autor do livro atendido: ");
		String autorLivro = leitor.next();
		LivroSugerido livroSugerido = null;
		
		// verifica se livro ja foi sugerido
		for(Cliente cliente : clientes) {
			LivroSugerido[] livros = (LivroSugerido[]) cliente.getLivrosSugeridos().toArray();
			for(LivroSugerido livro : livros) {
				if(livro.getNome().equals(nomeLivro) && livro.getAutor().equals(autorLivro)) {
					livroSugerido = livro;
					break;
				}
			}
			if(livroSugerido != null) {
				System.out.println("Autor do livro: " + livroSugerido.getAutor());
				break;
			}
		}
		
		if(livroSugerido == null) {
			livroSugerido = new LivroSugerido(nomeLivro, autorLivro);
		}
		
		// confirmar requisição
		System.out.println("Confirmar requerimento? Digite 's' para confirmar e 'n' para cancelar.");
		String confirmar;
		inputValido = false;
		
		while(!inputValido) {
			confirmar = leitor.next();
			try {
				if(confirmar.equals("s")) {
					if(clienteReserva.requisitarLivro(livroSugerido)) {
						if(novoCliente) { // se cliente for novo, salvar ele no banco
							FachadaDao.getInstance().saveCliente(clienteReserva);
						} else { // se cliente já foi cadastrado, apenas atualizar suas infos
							FachadaDao.getInstance().update();
						}
						requerimentoBemSucedido = true;
					} else {
						System.out.println("Não foi possível realizar requerimento.");
					}
				} else if(confirmar.equals("n")) { // cancelar processo.
					System.out.println("Requerimento cancelado.");
				} else {
					throw new Exception();
				}
				inputValido = true;
			} catch(Exception e) {
				System.out.println("Digite um valor válido.");
			}
		}

		leitor.close();
		return requerimentoBemSucedido;
	}
}
