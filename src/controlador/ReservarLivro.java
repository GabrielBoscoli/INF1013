package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import dao.FachadaDao;
import modelo.Cliente;
import modelo.Funcionário;
import modelo.LivroCadastrado;
import modelo.Reserva;

public class ReservarLivro {
	public static Reserva reservar(Funcionário funcionario, ArrayList<Cliente> clientes, ArrayList<LivroCadastrado> livros,
			Scanner leitor) throws IOException {
		Cliente clienteReserva = null;
		boolean novoCliente = false;
		Reserva reserva = null;
		
		//inserir cpf do cliente
		System.out.println("Digite o cpf do cliente:");
		String cpfStr = "";
		long cpf = 0;
		boolean inputValido = false;
		
		while(!inputValido) {
			cpfStr = leitor.nextLine().toLowerCase();
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
			nome = leitor.nextLine().toLowerCase();
			System.out.println("Digite o email do cliente: ");
			email = leitor.nextLine().toLowerCase();
			System.out.println("Digite o endereco do cliente: ");
			endereco = leitor.nextLine().toLowerCase();
			
			System.out.println("Digite o telefone do cliente: ");
			String telefoneStr;
			telefone = 0;
			inputValido = false;
			while(!inputValido) {
				telefoneStr = leitor.nextLine().toLowerCase();
				try {
					telefone = Integer.parseInt(telefoneStr);
					inputValido = true;
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
		
		// buscar o livro que será reservado
		LivroCadastrado livroReservado = BuscarLivro.buscar(funcionario, livros, leitor);
		
		// confirmar reserva
		System.out.println("Confirmar reserva? Digite 's' para confirmar e 'n' para cancelar.");
		String confirmar;
		inputValido = false;
		
		while(!inputValido) {
			confirmar = leitor.nextLine().toLowerCase();
			try {
				if(confirmar.equals("s")) {
					reserva = clienteReserva.reservarLivro(livroReservado);
					if(reserva != null) {
						if(novoCliente) { // se cliente for novo, salvar ele no banco
							FachadaDao.getInstance().saveCliente(clienteReserva);
						}
						FachadaDao.getInstance().update();
					} else {
						System.out.println("Não foi possível realizar reserva.");
					}
				} else if(confirmar.equals("n")) { // cancelar processo.
					System.out.println("Reserva cancelado.");
				} else {
					throw new Exception();
				}
				inputValido = true;
			} catch(Exception e) {
				System.out.println("Digite um valor válido.");
			}
		}

		return reserva;
	}
}
