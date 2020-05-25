package controlador;

import java.io.IOException;
import java.util.Scanner;

import dao.FachadaDao;
import modelo.Exemplar;
import modelo.LivroCadastrado;

public class CadastrarLivro {
	public static LivroCadastrado cadastrar(Scanner leitor) throws IOException {
		LivroCadastrado livro;
		
		System.out.print("Digite o nome do livro: ");
		String nome = leitor.nextLine().toLowerCase();
		if(nome.equals("cancelar")) {
			return null;
		}
		
		System.out.print("Digite o autor do livro: ");
		String autor = leitor.nextLine().toLowerCase();
		if(autor.equals("cancelar")) {
			return null;
		}
		
		System.out.print("Digite o genero do livro: ");
		String genero = leitor.nextLine().toLowerCase();
		if(genero.equals("cancelar")) {
			return null;
		}
		
		System.out.print("Digite a editora do livro: ");
		String editora = leitor.nextLine().toLowerCase();
		if(editora.equals("cancelar")) {
			return null;
		}
		
		System.out.print("Digite a quantidade de livros: ");
		String quantidadeString;
		int quantidade = -1;
		boolean inputValido = false;
		
		while(!inputValido) {
			quantidadeString = leitor.nextLine().toLowerCase();
			if(editora.equals("cancelar")) {
				return null;
			}
			try {
				quantidade = Integer.parseInt(quantidadeString);
				if(quantidade < 0) {
					throw new Exception();
				}
				inputValido = true;
			} catch (NumberFormatException e) {
				System.out.println("Por favor, digite um numero.");
			} catch (Exception e) {
				System.out.println("Por favor, digite um numero positivo.");
			}
		}
		
		int[] ids = new int[quantidade];
		for(int i = 1; i <= quantidade; i++) {
			System.out.print("Digite o id do Exemplar " + i + ": ");
			String idString;
			int id = -1;
			inputValido = false;
			
			while(!inputValido) {
				idString = leitor.nextLine();
				if(idString.equals("cancelar")) {
					return null;
				}
				try {
					id = Integer.parseInt(idString);
					if(id < 0) {
						throw new Exception();
					}
					inputValido = true;
					ids[i - 1] = id;
				} catch (NumberFormatException e) {
					System.out.println("Por favor, digite um numero.");
				} catch (Exception e) {
					System.out.println("Por favor, digite um numero positivo.");
				}
			}
		}
		
		livro = new LivroCadastrado(nome, autor, genero, editora);
		for(int i = 0; i < quantidade; i++) {
			livro.addExemplar(new Exemplar(ids[i], livro));
		}
		FachadaDao.getInstance().saveLivro(livro);
		
		System.out.println("Livro cadastrado com sucesso!");
		
		return livro;
	}
}
