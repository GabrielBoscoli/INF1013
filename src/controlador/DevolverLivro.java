package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import dao.FachadaDao;
import modelo.Aluguel;
import modelo.Cliente;
import modelo.Funcion�rio;
import modelo.LivroCadastrado;

public class DevolverLivro {
	public static boolean devolver(Funcion�rio funcionario, ArrayList<Cliente> clientes, ArrayList<LivroCadastrado> livros,
			Scanner leitor) throws IOException {
		Aluguel aluguel = null;
		boolean devolucaoBemSucedida = false;
		
		//buscar aluguel ativo por nome do livro ou cpf do cliente
		System.out.println("Digite o nome do livro alugado ou o cpf do cliente que alugou");
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
			indexStr = leitor.next();
			try {
				index = Integer.parseInt(indexStr);
				inputValido = true;
			} catch(NumberFormatException e) {
				System.out.println("Digite um valor v�lido.");
			}
		}
		
		aluguel = alugueis.get(index);
		
		// confirmar devolu��o
		System.out.println("Confirmar devolu��o? Digite 's' para confirmar e 'n' para cancelar.");
		String confirmar;
		inputValido = false;
		
		while(!inputValido) {
			confirmar = leitor.next();
			try {
				if(confirmar.equals("s")) {
					if(aluguel.devolverLivro()) {
						FachadaDao.getInstance().update();		
						devolucaoBemSucedida = true;
					} else {
						System.out.println("N�o foi poss�vel devolver livro.");
					}
				} else if(confirmar.equals("n")) { // cancelar processo.
					System.out.println("Devolu��o cancelada.");
				} else {
					throw new Exception();
				}
				inputValido = true;
			} catch(Exception e) {
				System.out.println("Digite um valor v�lido.");
			}
		}

		return devolucaoBemSucedida;
	}
}
