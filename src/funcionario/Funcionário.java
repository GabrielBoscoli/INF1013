package funcionario;

import java.util.HashSet;
import java.util.Set;

import livro.LivroCadastrado;

public abstract class Funcionário {
	private int id;
	private String nome;
	
	//atributos adicionados
	private String senha;
	protected boolean autenticado = false;
	
	public Funcionário(String nome, int id, String senha) {
		this.id = id;
		this.nome = nome;
		this.senha = senha;
	}
	
	public boolean logar(int id, String senha) {
		if(senha == this.senha && id == this.id) {
			autenticado = true;
		}
		return autenticado;
	}
	
	public void deslogar() {
		autenticado = false;
	}
	
	public Set<LivroCadastrado> buscarLivro(Set<LivroCadastrado> livros, String nomeOuAutor) {
		if(!autenticado) {
			return null;
		}
		Set<LivroCadastrado> livrosRetorno = new HashSet<LivroCadastrado>();
		livros.forEach((livro) -> {
			if(livro.getNome().contains(nomeOuAutor)) {
				livrosRetorno.add(livro);
			} else if(livro.getAutor().contains(nomeOuAutor)) {
				livrosRetorno.add(livro);
			}
		});
		return livrosRetorno;
	}
	
	//duvida
	//criar metodos em funcionario que chamam os metodos do cliente, servindo com uma fachada?
}
