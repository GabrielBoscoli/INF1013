package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import dao.FachadaDao;
import modelo.Cliente;
import modelo.Gerente;
import modelo.LivroSugerido;

public class AtenderRequerimento {
	public static boolean atender(Gerente gerente, ArrayList<Cliente> clientes, Scanner leitor) throws IOException {
		boolean atendimentoBemSucedido = false;
		
		System.out.println("Digite o nome do livro atendido: ");
		String nomeLivro = leitor.nextLine().toLowerCase();
		System.out.println("Digite o autor do livro atendido: ");
		String autorLivro = leitor.nextLine().toLowerCase();
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
				break;
			}
		}
		
		// se o livro atendido nao foi sugerido
		if(livroSugerido == null) {
			System.out.println("Não há pedidos de aquisição para esse livro.");
			return false;
		}
		
		// confirmar atendimento
		System.out.println("Confirmar atendimento? Digite 's' para confirmar e 'n' para cancelar.");
		String confirmar;
		boolean inputValido = false;
		
		while(!inputValido) {
			confirmar = leitor.nextLine().toLowerCase();
			try {
				if(confirmar.equals("s")) {
					Set<Cliente> setClientes = new HashSet<Cliente>(clientes);
					int qntAtendido = gerente.atenderRequerimento(setClientes, livroSugerido);
					System.out.println("Fora atendidos " + qntAtendido + " requerimentos.");
					FachadaDao.getInstance().update();
					atendimentoBemSucedido = true;
				} else if(confirmar.equals("n")) { // cancelar processo.
					System.out.println("Atendimento cancelado.");
				} else {
					throw new Exception();
				}
				inputValido = true;
			} catch(Exception e) {
				System.out.println("Digite um valor válido.");
			}
		}

		return atendimentoBemSucedido;
	}
}
