package controlador;

import java.util.ArrayList;
import java.util.Scanner;

import modelo.Funcionário;

public class LogarNoSistema {
	public static Funcionário logar(ArrayList<Funcionário> funcionarios) {
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
		
		for(Funcionário i : funcionarios) {
			if(i.logar(id, senha)) {
				return i;
			}
		}
		
		return null;
	}
}
