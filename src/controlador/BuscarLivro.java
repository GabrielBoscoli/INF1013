package controlador;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import modelo.Funcionário;
import modelo.LivroCadastrado;

public class BuscarLivro {
	public static LivroCadastrado buscar(Funcionário funcionario, ArrayList<LivroCadastrado> livros, Scanner leitor) {
		System.out.println("Digite o nome ou autor do livro:");
		String nomeOuAutor = leitor.nextLine().toLowerCase();
		
		Set<LivroCadastrado> setLivros = new HashSet<LivroCadastrado>(livros);
		setLivros = funcionario.buscarLivro(setLivros, nomeOuAutor);
		ArrayList<LivroCadastrado> arrayLivrosRetornados = new ArrayList<>();

		setLivros.forEach(livro -> {
			arrayLivrosRetornados.add(livro);
		});
		
		int i = 0;
		for(LivroCadastrado livro : arrayLivrosRetornados) {
			System.out.println(i + ": " + livro.getNome() + ", de " + livro.getAutor());
			i++;
		}
		
		if(arrayLivrosRetornados.isEmpty()) {
			System.out.println("Busca não retornou resultados.");
			return null;
		}
		
		System.out.println("Selecione o livro desejado");
		String livroIndexStr;
		int livroIndex = 0;
		boolean valido = false;
		
		while(!valido) {
			livroIndexStr = leitor.nextLine().toLowerCase();
			try {
				livroIndex = Integer.parseInt(livroIndexStr);
				valido = true;
			} catch (NumberFormatException e) {
				System.out.println("Digite um valor válido");
			}			
		}
		
		
		LivroCadastrado livro = arrayLivrosRetornados.get(livroIndex);
		livro.exibirDetalhes();
		return livro;
	}
}
