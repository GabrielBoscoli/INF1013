package funcionario;

import java.util.Iterator;
import java.util.Set;

import livro.LivroSugerido;
import relações.Cliente;

public class Gerente extends Funcionário {
	public Gerente(String nome, int id, String senha) {
		super(nome, id, senha);
	}
	
	public int atenderRequerimento(Set<Cliente> clientes, LivroSugerido livroAtendido) {
		int requerimentosAtendidos = 0;
		if(!this.autenticado) {
			return requerimentosAtendidos;
		}
		Iterator<Cliente> itr = clientes.iterator();
		Cliente cliente;
		while(itr.hasNext()) {
			cliente = itr.next();
			if(cliente.atenderRequerimento(livroAtendido)) {
				requerimentosAtendidos++;
			}
		}
		return requerimentosAtendidos;
	}
}
