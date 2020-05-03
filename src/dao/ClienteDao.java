package dao;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
//import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
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

import livro.Exemplar;
import livro.LivroCadastrado;
import livro.LivroSugerido;
import relações.Aluguel;

import relações.Cliente;
import relações.Reserva;

public class ClienteDao implements Dao<Cliente>{
	
	private class HiddenAnnotationExclusionStrategy implements ExclusionStrategy 
	{
	    public boolean shouldSkipClass(Class<?> classe) {
	        return classe.getAnnotation(Hidden.class) != null;
	    }
	 
	    public boolean shouldSkipField(FieldAttributes f) {
	        return f.getAnnotation(Hidden.class) != null && f.getDeclaredClass().equals(Cliente.class);
	    }
	}
	
	private class LivroCadastradoSerializer implements JsonSerializer<LivroCadastrado> {
		@Override
		public JsonElement serialize(LivroCadastrado object, Type arg1, JsonSerializationContext arg2) {
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("nome", object.getNome());
			jsonObject.addProperty("autor", object.getAutor());
			jsonObject.addProperty("editora", object.getEditora());
			return jsonObject;
		}
	}
	
	private class ExemplarSerializer implements JsonSerializer<Exemplar> {
		@Override
		public JsonElement serialize(Exemplar object, Type arg1, JsonSerializationContext arg2) {
			JsonObject jsonObject = new JsonObject();
			JsonObject livro = new LivroCadastradoSerializer().serialize(object.getLivro(), null, null).getAsJsonObject();
			jsonObject.add("livro", livro);
			jsonObject.addProperty("id", object.getId());
			return jsonObject;
		}
	}
	
	private class ClienteDeserializer implements JsonDeserializer<Cliente> {

		@Override
		public Cliente deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context)
				throws JsonParseException {
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			Cliente cliente = new Cliente(jsonObject.get("cpf").getAsLong(), jsonObject.get("nome").getAsString(),
					jsonObject.get("endereco").getAsString(), jsonObject.get("email").getAsString(),jsonObject.get("telefone").getAsInt());
			
			Set<Reserva> reservas = new HashSet<Reserva>();
			jsonObject.get("reservas").getAsJsonArray().forEach(reserva -> {
				reservas.add(context.deserialize(reserva, Reserva.class));
			});
			Set<Aluguel> alugueis = new HashSet<Aluguel>();
			jsonObject.get("alugueis").getAsJsonArray().forEach(aluguel -> {
				alugueis.add(context.deserialize(aluguel, Aluguel.class));
			});
			Set<LivroSugerido> livrosSugeridos = new HashSet<LivroSugerido>();
			jsonObject.get("livrosSugeridos").getAsJsonArray().forEach(livroSugerido -> {
				livrosSugeridos.add(context.deserialize(livroSugerido, LivroSugerido.class));
			});
			cliente.setReservas(reservas);
			cliente.setLivrosSugeridos(livrosSugeridos);
			cliente.setAlugueis(alugueis);
			
			reservas.forEach(reserva -> {
				reserva.setCliente(cliente);
			});
			//to confiando que da certo. sei q para reserva funciona.
			alugueis.forEach(aluguel -> {
				aluguel.setCliente(cliente);
			});

			return cliente;
		}
		
	}
	
	private ArrayList<Cliente> clientes;
	
	ClienteDao() throws IOException {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Cliente.class, new ClienteDeserializer());
		Gson gson = gsonBuilder.create();
		Reader reader = Files.newBufferedReader(Paths.get("C:\\Users\\Gabriel Boscoli\\Documents\\INF1013\\cliente-db.json"));
		Type clienteListType = new TypeToken<ArrayList<Cliente>>(){}.getType();
		clientes = gson.fromJson(reader, clienteListType);
		if(clientes == null) {
			clientes = new ArrayList<Cliente>();
		} 
		reader.close();
	}

	@Override
	public Optional<Cliente> get(String[] params) {
		Long cpf = Long.valueOf(params[0]);
		for(int i = 0; i < clientes.size(); i++) {
			Cliente cliente = clientes.get(i);
			if(cliente.getCpf() == cpf) {
				return Optional.of(cliente);
			}
		}
		return Optional.empty();
	}

	@Override
	public ArrayList<Cliente> getAll() {
		return clientes;
	}

	@Override
	public void save(Cliente t) {
		clientes.add(t);
		update();
	}

	@Override
	public void update() {
		GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
		gsonBuilder.registerTypeAdapter(LivroCadastrado.class, new LivroCadastradoSerializer());
		gsonBuilder.registerTypeAdapter(Exemplar.class, new ExemplarSerializer());
		gsonBuilder.addSerializationExclusionStrategy(new HiddenAnnotationExclusionStrategy());
		Gson gson = gsonBuilder.create();
		try {
			Writer writer = Files.newBufferedWriter(Paths.get("C:\\Users\\Gabriel Boscoli\\Documents\\INF1013\\cliente-db.json"));
			gson.toJson(clientes, writer);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(Cliente t) {
		clientes.remove(t);
		update();
	}

}
