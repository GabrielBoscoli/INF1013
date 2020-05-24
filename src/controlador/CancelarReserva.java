package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import dao.FachadaDao;
import modelo.Cliente;
import modelo.Funcionário;
import modelo.LivroCadastrado;
import modelo.Reserva;

public class CancelarReserva {
	public static boolean cancelar(Funcionário funcionario, ArrayList<Cliente> clientes, ArrayList<LivroCadastrado> livros) throws IOException {
		Scanner leitor = new Scanner(System.in);
		Reserva reserva = null;
		boolean reservaCancelada = false;
		
		//buscar reserva ativa por nome do livro ou cpf do cliente
		System.out.println("Digite o nome do livro reservado ou o cpf do cliente que reservou");
		String nomeOuCpf = leitor.next();
		String nome = null;
		int cpf = -1;
		try {
			cpf = Integer.parseInt(nomeOuCpf);
		} catch(NumberFormatException e) {
			nome = nomeOuCpf;
		}
		
		final int finalCpf = cpf;
		final String finalNome = nome;
		ArrayList<Reserva> reservas = new ArrayList<>();
		for(Cliente cliente : clientes) {
			cliente.getReservas().forEach(reserv -> {
				if(reserv.reservaAtiva()) {
					if(cliente.getCpf() == finalCpf || reserv.getLivroReservado().getNome().equals(finalNome)) {
						reservas.add(reserv);
					}
				}
			});
		}
		
		System.out.println("Selecione a reserva desejado.");
		
		int i = 0;
		for(Reserva reserv : reservas) {
			System.out.println(i + ". " + reserv);
			i++;
		}
		
		String indexStr;
		int index = 0;
		boolean inputValido = false;
		
		while(!inputValido) {
			indexStr = leitor.next();
			try {
				index = Integer.parseInt(indexStr);
				inputValido = true;
			} catch(NumberFormatException e) {
				System.out.println("Digite um valor válido.");
			}
		}
		
		reserva = reservas.get(index);
		
		// confirmar cancelamento
		System.out.println("Confirmar cancelamento? Digite 's' para confirmar e 'n' para cancelar.");
		String confirmar;
		inputValido = false;
		
		while(!inputValido) {
			confirmar = leitor.next();
			try {
				if(confirmar.equals("s")) {
					if(reserva.cancelarReserva()) {
						FachadaDao.getInstance().update();		
						reservaCancelada = true;
					} else {
						System.out.println("Não foi possível cancelar reserva.");
					}
				} else if(confirmar.equals("n")) { // cancelar processo.
					System.out.println("Cancelamento cancelada.");
				} else {
					throw new Exception();
				}
				inputValido = true;
			} catch(Exception e) {
				System.out.println("Digite um valor válido.");
			}
		}

		leitor.close();
		return reservaCancelada;
	}
}
