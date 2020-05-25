package controlador;

import java.io.IOException;
import java.util.Scanner;

import dao.FachadaDao;
import modelo.Funcion�rio;

public class DeslogarDoSistema {
	public static boolean deslogar(Funcion�rio funcionario, Scanner leitor) throws IOException {
		
		System.out.println("Tem certeza que deseja sair? (s/n)");
		String resposta = leitor.nextLine().toLowerCase();
		
		//qualquer input que n�o seja 's' vai cancelar a��o
		if(resposta.equals("s")) {
			System.out.println("Saindo do sistema...");
			funcionario.deslogar();
			return true;
		}
		
		FachadaDao.getInstance().update();
		
		return false;
	}
}
