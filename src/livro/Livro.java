package livro;

public abstract class Livro {
	protected String nome;
	protected String autor;
	protected String genero;
	protected String editora;
	
	public Livro(String nome, String autor, String genero, String editora) {
		this.nome = nome;
		this.autor = autor;
		this.genero = genero;
		this.editora = editora;
	}

	public void exibirDetalhes() {
		System.out.println("Nome: " + this.nome);
		System.out.println("Autor: " + this.autor);
		System.out.println("Genero: " + this.genero);
		System.out.println("Editora: " + this.editora);
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Livro)) {
			return false;
		}
		Livro livro = (Livro) o;
		if(nome.equals(livro.getNome()) && autor.equals(livro.getAutor()) && editora.equals(livro.getEditora())) {
			return true;
		}
		return false;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}
	
	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}
	
	public String getEditora() {
		return editora;
	}

	public void setEditora(String editora) {
		this.editora = editora;
	}
}
