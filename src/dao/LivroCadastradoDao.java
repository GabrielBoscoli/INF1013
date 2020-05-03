package dao;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import livro.Exemplar;
import livro.LivroCadastrado;
import relações.Cliente;
import relações.Reserva;

//UMA IDEIA A SE PENSAR: TIRAR A RESERVA E O ALUGUEL DA SERIALIZAÇÃO E PEGAR ELES NA FACHADA.
public class LivroCadastradoDao implements Dao<LivroCadastrado> {static int i = 0;
	
	private class HiddenAnnotationExclusionStrategy implements ExclusionStrategy 
	{
	    public boolean shouldSkipClass(Class<?> classe) {
	        return classe.getAnnotation(Hidden.class) != null;
	    }
	 
	    public boolean shouldSkipField(FieldAttributes f) {
	        return f.getAnnotation(Hidden.class) != null;
	    }
	}
	
	private class ClienteSerializer implements JsonSerializer<Cliente> {
		@Override
		public JsonElement serialize(Cliente object, Type arg1, JsonSerializationContext arg2) {
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("cpf", object.getCpf());
			return jsonObject;
		}
	}
	
	private class LivroCadastradoDeserializer implements JsonDeserializer<LivroCadastrado> {
		@Override
		public LivroCadastrado deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context)
				throws JsonParseException {
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			
			JsonArray jsonArrayExemplares = jsonObject.get("exemplares").getAsJsonArray();
			Set<Exemplar> exemplares = new HashSet<Exemplar>();
			jsonArrayExemplares.forEach(jsonExemplares -> {
				exemplares.add(context.deserialize(jsonExemplares, Exemplar.class));
			});
			
			LivroCadastrado livro = new LivroCadastrado(jsonObject.get("nome").getAsString(),
					jsonObject.get("autor").getAsString(),
					jsonObject.get("genero").getAsString(),
					jsonObject.get("editora").getAsString(),
					jsonObject.get("quantidadeTotal").getAsInt(),
					jsonObject.get("quantidadeAlugado").getAsInt(),
					jsonObject.get("quantidadeReserva").getAsInt(),
					jsonObject.get("quantidadeDisponivel").getAsInt(),
					new HashSet<Reserva>(),
					exemplares);
			
			exemplares.forEach(exemplar -> {
				exemplar.setLivro(livro);
			});
			return livro;
		}
	}
	
	private ArrayList<LivroCadastrado> livros;
	
	public LivroCadastradoDao() throws IOException {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(LivroCadastrado.class, new LivroCadastradoDeserializer());
		Gson gson = gsonBuilder.create();
		Reader reader = Files.newBufferedReader(Paths.get("C:\\Users\\Gabriel Boscoli\\Documents\\INF1013\\livro-cadastrado-db.json"));
		Type livroListType = new TypeToken<ArrayList<LivroCadastrado>>(){}.getType();
		livros = gson.fromJson(reader, livroListType);
		if(livros == null) {
			livros = new ArrayList<LivroCadastrado>();
		}
		reader.close();
	}

	@Override
	public Optional<LivroCadastrado> get(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<LivroCadastrado> getAll() {
		return livros;
	}

	@Override
	public void save(LivroCadastrado t) {
		livros.add(t);
		update();
	}

	@Override
	public void update() {
		GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
		gsonBuilder.registerTypeAdapter(Cliente.class, new ClienteSerializer());
		gsonBuilder.addSerializationExclusionStrategy(new HiddenAnnotationExclusionStrategy());
		Gson gson = gsonBuilder.create();
		try {
			Writer writer = Files.newBufferedWriter(Paths.get("C:\\Users\\Gabriel Boscoli\\Documents\\INF1013\\livro-cadastrado-db.json"));
			gson.toJson(livros, writer);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(LivroCadastrado t) {
		livros.remove(t);
		update();
	}

}
