package controlador;

import java.util.ArrayList;
import java.util.Scanner;

import modelo.Funcion�rio;

public class LogarNoSistema {
	public static Funcion�rio logar(ArrayList<Funcion�rio> funcionarios, Scanner leitor) {
		
		System.out.println("Digite id de login:");
		String idStr = leitor.next();
		
		System.out.println("Digite a senha:");
		String senha = leitor.next();
		
		int id = -1;
		try {
			id = Integer.parseInt(idStr);
		} catch (NumberFormatException e) {
			System.out.println("Id ou senha inv�lido. Tente novamente.");
			return null;
		}
		
		for(Funcion�rio funcionario : funcionarios) {
			if(funcionario.logar(id, senha)) {
				System.out.println("Bem vindo, " + funcionario.getNome() + ".");
				return funcionario;
			}
		}
		
		System.out.println("Id ou senha inv�lido. Tente novamente.");
		return null;
	}
}
