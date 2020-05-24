package controlador;

import java.util.ArrayList;
import java.util.Scanner;

import modelo.Funcionário;

public class LogarNoSistema {
	public static Funcionário logar(ArrayList<Funcionário> funcionarios, Scanner leitor) {
		
		System.out.println("Digite id de login:");
		String idStr = leitor.next();
		
		System.out.println("Digite a senha:");
		String senha = leitor.next();
		
		int id = -1;
		try {
			id = Integer.parseInt(idStr);
		} catch (NumberFormatException e) {
			System.out.println("Id ou senha inválido. Tente novamente.");
			return null;
		}
		
		for(Funcionário funcionario : funcionarios) {
			if(funcionario.logar(id, senha)) {
				System.out.println("Bem vindo, " + funcionario.getNome() + ".");
				return funcionario;
			}
		}
		
		System.out.println("Id ou senha inválido. Tente novamente.");
		return null;
	}
}
