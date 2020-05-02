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
