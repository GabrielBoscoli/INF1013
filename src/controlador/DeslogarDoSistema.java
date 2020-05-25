package controlador;

import java.io.IOException;
import java.util.Scanner;

import dao.FachadaDao;
import modelo.Funcionário;

public class DeslogarDoSistema {
	public static boolean deslogar(Funcionário funcionario, Scanner leitor) throws IOException {
		
		System.out.println("Tem certeza que deseja sair? (s/n)");
		String resposta = leitor.nextLine().toLowerCase();
		
		//qualquer input que não seja 's' vai cancelar ação
		if(resposta.equals("s")) {
			System.out.println("Saindo do sistema...");
			funcionario.deslogar();
			return true;
		}
		
		FachadaDao.getInstance().update();
		
		return false;
	}
}
