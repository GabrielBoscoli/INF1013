package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import dao.FachadaDao;
import modelo.Aluguel;
import modelo.Cliente;
import modelo.Funcionário;
import modelo.LivroCadastrado;

public class CancelarAluguel {
	public static boolean cancelar(Funcionário funcionario, ArrayList<Cliente> clientes, ArrayList<LivroCadastrado> livros,
			Scanner leitor) throws IOException {
		Aluguel aluguel = null;
		boolean aluguelCancelado = false;
		
		//buscar reserva ativa por nome do livro ou cpf do cliente
		System.out.println("Digite o nome do livro alugado ou o cpf do cliente que alugou");
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
		ArrayList<Aluguel> alugueis = new ArrayList<>();
		for(Cliente cliente : clientes) {
			cliente.getAluguel().forEach(alug -> {
				if(alug.aluguelAtivo()) {
					if(cliente.getCpf() == finalCpf || alug.getLivroAlugado().getLivro().getNome().equals(finalNome)) {
						alugueis.add(alug);
					}
				}
			});
		}
		
		if(alugueis.isEmpty()) {
			System.out.println("Busca não retornou resultados.");
			return false;
		}
		
		System.out.println("Selecione o aluguel desejado.");
		
		int i = 0;
		for(Aluguel alug : alugueis) {
			System.out.println(i + ". " + alug);
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
				System.out.println("Digite um valor válido.");
			}
		}
		
		aluguel = alugueis.get(index);
		
		// confirmar cancelamento
		System.out.println("Confirmar cancelamento? Digite 's' para confirmar e 'n' para cancelar.");
		String confirmar;
		inputValido = false;
		
		while(!inputValido) {
			confirmar = leitor.nextLine().toLowerCase();
			try {
				if(confirmar.equals("s")) {
					if(aluguel.cancelarAluguel()) {
						FachadaDao.getInstance().update();		
						aluguelCancelado = true;
					} else {
						System.out.println("Não foi possível cancelar aluguel.");
					}
				} else if(confirmar.equals("n")) { // cancelar processo.
					System.out.println("Cancelamento cancelado.");
				} else {
					throw new Exception();
				}
				inputValido = true;
			} catch(Exception e) {
				System.out.println("Digite um valor válido.");
			}
		}

		return aluguelCancelado;
	}
}
