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

import funcionario.Funcionário;

public class FuncionarioDao implements Dao<Funcionário> {
	
	private ArrayList<Funcionário> funcionarios;
	
	public FuncionarioDao() throws IOException {
		Gson gson = new Gson();
		Reader reader = Files.newBufferedReader(Paths.get("C:\\Users\\Gabriel Boscoli\\Documents\\INF1013\\funcionario-db.json"));
		Type funcionarioListType = new TypeToken<ArrayList<Funcionário>>(){}.getType();
		//da erro quando tem conteudo no arquivo.
		funcionarios = gson.fromJson(reader, funcionarioListType);
		if(funcionarios == null) {
			funcionarios = new ArrayList<Funcionário>();
		}
		reader.close();
	}

	@Override
	public Optional<Funcionário> get(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Funcionário> getAll() {
		return funcionarios;
	}

	@Override
	public void save(Funcionário t) {
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
	public void delete(Funcionário t) {
		funcionarios.remove(t);
		update();
	}
	
}
