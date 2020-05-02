package dao;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import funcionario.Funcion�rio;

public class FuncionarioDao implements Dao<Funcion�rio> {
	
	private ArrayList<Funcion�rio> funcionarios;
	
	public FuncionarioDao() throws IOException {
		Gson gson = new Gson();
		Reader reader = Files.newBufferedReader(Paths.get("C:\\Users\\Gabriel Boscoli\\Documents\\INF1013\\funcionario-db.json"));
		Type funcionarioListType = new TypeToken<ArrayList<Funcion�rio>>(){}.getType();
		//da erro quando tem conteudo no arquivo.
		funcionarios = gson.fromJson(reader, funcionarioListType);
		if(funcionarios == null) {
			funcionarios = new ArrayList<Funcion�rio>();
		}
		reader.close();
	}

	@Override
	public Optional<Funcion�rio> get(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Funcion�rio> getAll() {
		return funcionarios;
	}

	@Override
	public void save(Funcion�rio t) {
		funcionarios.add(t);
		update();
	}

	@Override
	public void update() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try {
			Writer writer = Files.newBufferedWriter(Paths.get("C:\\Users\\Gabriel Boscoli\\Documents\\INF1013\\funcionario-db.json"));
			gson.toJson(funcionarios, writer);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(Funcion�rio t) {
		funcionarios.remove(t);
		update();
	}
	
}
