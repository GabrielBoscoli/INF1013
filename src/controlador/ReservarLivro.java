package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import dao.FachadaDao;
import modelo.Cliente;
import modelo.Funcion�rio;
import modelo.LivroCadastrado;
import modelo.Reserva;

public class ReservarLivro {
	public static Reserva reservar(Funcion�rio funcionario, ArrayList<Cliente> clientes, ArrayList<LivroCadastrado> livros) throws IOException {
		Scanner leitor = new Scanner(System.in);
		Cliente clienteReserva = null;
		boolean novoCliente = false;
		Reserva reserva = null;
		
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
				System.out.println("Digite um valor v�lido.");
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
		
		//se n�o tiver cliente cadastrado com tal cpf, inserir demais campos.
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
					System.out.println("Digite um telefone v�lido.");
				}
			}
			
			clienteReserva = new Cliente(cpf, nome, endereco, email, telefone);
		} else { //caso contr�rio, s� exibir as propriedades do cliente cadastrado
			System.out.println("Nome do cliente: " + clienteReserva.getNome());
			System.out.println("Email do cliente: " + clienteReserva.getEmail());
			System.out.println("Endereco do cliente: " + clienteReserva.getEndereco());
			System.out.println("Telefone do cliente: " + clienteReserva.getTelefone());
		}
		
		// buscar o livro que ser� reservado
		LivroCadastrado livroReservado = BuscarLivro.buscar(funcionario, livros);
		
		// confirmar reserva
		System.out.println("Confirmar reserva? Digite 's' para confirmar e 'n' para cancelar.");
		String confirmar;
		inputValido = false;
		
		while(!inputValido) {
			confirmar = leitor.next();
			try {
				if(confirmar.equals("s")) {
					reserva = clienteReserva.reservarLivro(livroReservado);
					if(reserva != null) {
						if(novoCliente) { // se cliente for novo, salvar ele no banco
							FachadaDao.getInstance().saveCliente(clienteReserva);
						} else { // se cliente j� foi cadastrado, apenas atualizar suas infos
							FachadaDao.getInstance().update();
						}
					} else {
						System.out.println("N�o foi poss�vel realizar reserva.");
					}
				} else if(confirmar.equals("n")) { // cancelar processo.
					System.out.println("Reserva cancelado.");
				} else {
					throw new Exception();
				}
				inputValido = true;
			} catch(Exception e) {
				System.out.println("Digite um valor v�lido.");
			}
		}

		leitor.close();
		return reserva;
	}
}
