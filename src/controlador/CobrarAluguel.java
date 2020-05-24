package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import dao.FachadaDao;
import modelo.Aluguel;
import modelo.Cliente;
import modelo.Funcion�rio;

public class CobrarAluguel {
	public static boolean cobrar(Funcion�rio funcionario, ArrayList<Cliente> clientes, Scanner leitor) throws IOException {
		boolean cobran�aBemSucedida = false;
		Aluguel aluguelCobrado;
		ArrayList<Aluguel> alugueisAtrasados = new ArrayList<Aluguel>();
		
		System.out.println("Exibindo alugueis em andamento atrasados: ");
		for(Cliente cliente : clientes) {
			cliente.getAluguel().forEach(aluguel -> {
				if(aluguel.isAtrasado()) {
					alugueisAtrasados.add(aluguel);
				}
			});
		}
		
		System.out.println("Selecione o aluguel desejado.");
		
		int i = 0;
		for(Aluguel aluguel : alugueisAtrasados) {
			System.out.println(i + ". " + aluguel);
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
		
		aluguelCobrado = alugueisAtrasados.get(index);
		
		System.out.println("Como deseja fazer a cobran�a? Digite 0 para email e 1 para telefone");
		String modoStr;
		int modo = 0;
		inputValido = false;
		
		while(!inputValido) {
			modoStr = leitor.next();
			try {
				if(modoStr.equals("0") || modoStr.equals("1")) {
					modo = Integer.parseInt(modoStr);
				} else {
					throw new Exception();
				}
				inputValido = true;
			} catch(Exception e) {
				System.out.println("Digite um valor v�lido.");
			}
		}
		
		// confirmar cobranca
		System.out.println("Confirmar cobran�a? Digite 's' para confirmar e 'n' para cancelar.");
		String confirmar;
		inputValido = false;
		
		while(!inputValido) {
			confirmar = leitor.next();
			try {
				if(confirmar.equals("s")) {
					aluguelCobrado.cobrarAluguelAtrasado(modo);
					FachadaDao.getInstance().update();
					cobran�aBemSucedida = true;
				} else if(confirmar.equals("n")) { // cancelar processo.
					System.out.println("Cobran�a cancelada.");
				} else {
					throw new Exception();
				}
				inputValido = true;
			} catch(Exception e) {
				System.out.println("Digite um valor v�lido.");
			}
		}

		return cobran�aBemSucedida;
	}
}
