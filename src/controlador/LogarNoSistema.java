package controlador;

import java.util.ArrayList;
import java.util.Scanner;

import modelo.Funcion�rio;

public class LogarNoSistema {
	public static Funcion�rio logar(ArrayList<Funcion�rio> funcionarios) {
		Scanner leitor = new Scanner(System.in);
		
		System.out.println("Digite id de login:");
		String idStr = leitor.next();
		
		System.out.println("Digite a senha:");
		String senha = leitor.next();
		leitor.close();
		
		int id = -1;
		try {
			id = Integer.parseInt(idStr);
		} catch (NumberFormatException e) {
			return null;
		}
		
		for(Funcion�rio i : funcionarios) {
			if(i.logar(id, senha)) {
				return i;
			}
		}
		
		return null;
	}
}
