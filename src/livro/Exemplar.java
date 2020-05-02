package livro;

import dao.Hidden;
import relações.Aluguel;

public class Exemplar {
	//atributo adicionado
	@Hidden
	private LivroCadastrado livro;
	private int id;
	private Aluguel aluguel;
	
	public Exemplar(int id, LivroCadastrado livro) {
		this.setId(id);
		this.setLivro(livro);
		aluguel = null;
	}
	
	@Override
	public boolean equals(Object o) {
		if(!o.getClass().equals(Exemplar.class)) {
			return false;
		}
		Exemplar exemplar = (Exemplar) o;
		if(livro.equals(exemplar.getLivro()) && id == exemplar.getId()) {
			return true;
		}
		return false;
	}
	
	public void exemplarAlugado(Aluguel aluguel) {
		this.aluguel = aluguel;
	}
	
	public void exemplarDevolvido() {
		aluguel = null;
	}
	
	public Aluguel getAluguel() {
		return aluguel;
	}

	public LivroCadastrado getLivro() {
		return livro;
	}

	public void setLivro(LivroCadastrado livro) {
		this.livro = livro;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
