package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import dao.FachadaDao;
import modelo.Cliente;
import modelo.Funcion�rio;
import modelo.LivroCadastrado;
import modelo.Reserva;

public class CancelarReserva {
	public static boolean cancelar(Funcion�rio funcionario, ArrayList<Cliente> clientes, ArrayList<LivroCadastrado> livros,
			Scanner leitor) throws IOException {
		Reserva reserva = null;
		boolean reservaCancelada = false;
		
		//buscar reserva ativa por nome do livro ou cpf do cliente
		System.out.println("Digite o nome do livro reservado ou o cpf do cliente que reservou");
		String nomeOuCpf = leitor.nextLine().toLowerCase();
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
		
		if(reservas.isEmpty()) {
			System.out.println("Busca n�o retornou resultados.");
			return false;
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
			indexStr = leitor.nextLine().toLowerCase();
			try {
				index = Integer.parseInt(indexStr);
				inputValido = true;
			} catch(NumberFormatException e) {
				System.out.println("Digite um valor v�lido.");
			}
		}
		
		reserva = reservas.get(index);
		
		// confirmar cancelamento
		System.out.println("Confirmar cancelamento? Digite 's' para confirmar e 'n' para cancelar.");
		String confirmar;
		inputValido = false;
		
		while(!inputValido) {
			confirmar = leitor.nextLine().toLowerCase();
			try {
				if(confirmar.equals("s")) {
					if(reserva.cancelarReserva()) {
						FachadaDao.getInstance().update();		
						reservaCancelada = true;
					} else {
						System.out.println("N�o foi poss�vel cancelar reserva.");
					}
				} else if(confirmar.equals("n")) { // cancelar processo.
					System.out.println("Cancelamento cancelada.");
				} else {
					throw new Exception();
				}
				inputValido = true;
			} catch(Exception e) {
				System.out.println("Digite um valor v�lido.");
			}
		}

		return reservaCancelada;
	}
}
