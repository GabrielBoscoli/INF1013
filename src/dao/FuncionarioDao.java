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
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import modelo.Bibliotec�rio;
import modelo.Funcion�rio;
import modelo.Gerente;

public class FuncionarioDao implements Dao<Funcion�rio> {
	
	private class FuncionarioSerializer implements JsonSerializer<Funcion�rio> {
		@Override
		public JsonElement serialize(Funcion�rio object, Type type, JsonSerializationContext context) {
			JsonObject jsonObject = new JsonObject();
			if(object instanceof Gerente) {
				jsonObject.addProperty("tipo", "Gerente");
			} else {
				jsonObject.addProperty("tipo", "Bibliotecario");
			}
			jsonObject.addProperty("nome", object.getNome());
			jsonObject.addProperty("id", object.getId());
			jsonObject.addProperty("senha", object.getSenha());
			jsonObject.addProperty("autenticado", object.estaLogado());
			return jsonObject;
		}
	}
	
	private class FuncionarioDeserializer implements JsonDeserializer<Funcion�rio> {
		@Override
		public Funcion�rio deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context)
				throws JsonParseException {
			Funcion�rio funcionario = null;
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			String tipo = jsonObject.get("tipo").getAsString();
			if(tipo.equals("Gerente")) {
				funcionario = new Gerente(jsonObject.get("nome").getAsString(),
						jsonObject.get("id").getAsInt(),
						jsonObject.get("senha").getAsString());
			} else {
				funcionario = new Bibliotec�rio(jsonObject.get("nome").getAsString(),
						jsonObject.get("id").getAsInt(),
						jsonObject.get("senha").getAsString());
			}
			return funcionario;
		}
	}
	
	private ArrayList<Funcion�rio> funcionarios;
	
	FuncionarioDao() throws IOException {
		Gson gson = new GsonBuilder().registerTypeAdapter(Funcion�rio.class, new FuncionarioDeserializer()).create();
		Reader reader = Files.newBufferedReader(Paths.get("C:\\Users\\Gabriel Boscoli\\Documents\\INF1013\\funcionario-db.json"));
		Type funcionarioListType = new TypeToken<ArrayList<Funcion�rio>>(){}.getType();
		funcionarios = gson.fromJson(reader, funcionarioListType);
		if(funcionarios == null) {
			funcionarios = new ArrayList<Funcion�rio>();
		}
		reader.close();
	}

	@Override
	public Optional<Funcion�rio> get(String params[]) {
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
		GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
		gsonBuilder.registerTypeAdapter(Bibliotec�rio.class, new FuncionarioSerializer());
		gsonBuilder.registerTypeAdapter(Gerente.class, new FuncionarioSerializer());
		Gson gson = gsonBuilder.create();
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
