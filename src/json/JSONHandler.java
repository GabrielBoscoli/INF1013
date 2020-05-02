package json;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONHandler {
	private static String path = "C:\\Users\\Gabriel Boscoli\\Documents\\INF1013\\db.json";
	private static JSONObject rootJSONObject = null;
	
	public static JSONArray getJSONArray(String array) {
		JSONParser parser = new JSONParser();
		if(rootJSONObject == null) {
			try {
				JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(path));
				rootJSONObject = jsonObject;
			} catch(Exception e) {
				e.printStackTrace();
				return null;
			}			
		}
		return (JSONArray) rootJSONObject.get(array);
	}
	
	public static JSONObject getCliente(JSONArray array, String propriedade, String valor) {
		JSONObject jsonObject;
		for(Object i : array) {
			jsonObject = (JSONObject) i;
			if(String.valueOf(jsonObject.get(propriedade)).contains(valor)) {
				return jsonObject;
			}
		}
		return null;
	}

	public static ArrayList<Object> buscarLivros(String nomeOuAutor) {
		JSONArray array = (JSONArray) rootJSONObject.get("livro");
		@SuppressWarnings("unchecked")
		Iterator<JSONObject> iterator = array.iterator();
		ArrayList<Object> arrayLivros = new ArrayList<Object>();
		while(iterator.hasNext()) {
			JSONObject jsonObject = iterator.next();
			if(String.valueOf(jsonObject.get("nome")).contains(nomeOuAutor)) {
				arrayLivros.add(jsonObject);
			} else if(String.valueOf(jsonObject.get("autor")).contains(nomeOuAutor)) {
				arrayLivros.add(jsonObject);
			}
		}
		return arrayLivros;
	}
	
	@SuppressWarnings("unchecked")
	public static boolean addAluguel(JSONObject cliente, String nome, String autor) {
		JSONObject jsonObject = new JSONObject();
		if(cliente == null) {
			return false;
		}
		JSONArray jsonArray= (JSONArray) cliente.get("aluguel");
		if(!verificarExistenciaLivroNoArray(getJSONArray("livro"), nome, autor) || verificarExistenciaLivroNoArray(jsonArray, nome, autor)) {
			return false;
		}
		jsonObject.put("nome", nome);
		jsonObject.put("autor", autor);
		Calendar data = Calendar.getInstance();
		jsonObject.put("data", data.getTime());
		data.add(Calendar.DAY_OF_MONTH, 7);
		jsonObject.put("data-devolucao", data.getTime());
		jsonArray.add(jsonObject);
		if(!disponivelParaAlugado(pegaLivro(nome, autor))) {
			return false;
		}
		return (writeJSONFile());
	}
	
	@SuppressWarnings("unchecked")
	public static boolean devolverLivro(JSONObject cliente, String nome, String autor) {
		JSONObject jsonObject = new JSONObject();
		if(cliente == null) {System.out.println();
			return false;
		}
		JSONArray jsonArray = (JSONArray) cliente.get("aluguel");
		Iterator<JSONObject> iterator = jsonArray.iterator();
		while(iterator.hasNext()) {
			jsonObject = iterator.next();
			if(String.valueOf(jsonObject.get("nome")).equals(nome) && String.valueOf(jsonObject.get("autor")).equals(autor)) {
				jsonArray.remove(jsonObject);
				if(!alugadoParaDisponivel(pegaLivro(nome, autor))) {
					return false;
				}
				return writeJSONFile();
			}
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public static boolean cancelarAluguel(JSONObject cliente, String nome, String autor) {
		JSONObject jsonObject = new JSONObject();
		if(cliente == null) {
			return false;
		}
		JSONArray jsonArray = (JSONArray) cliente.get("aluguel");
		Iterator<JSONObject> iterator = jsonArray.iterator();
		while(iterator.hasNext()) {
			jsonObject = iterator.next();
			if(String.valueOf(jsonObject.get("nome")).equals(nome) && String.valueOf(jsonObject.get("autor")).equals(autor)) {
				jsonArray.remove(jsonObject);
				if(!alugadoParaDisponivel(pegaLivro(nome, autor))) {
					return false;
				}
				return writeJSONFile();
			}
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public static boolean addReserva(JSONObject cliente, String nome, String autor) {
		JSONObject jsonObject = new JSONObject();
		if(cliente == null) {
			return false;
		}
		JSONArray jsonArray= (JSONArray) cliente.get("reserva");
		if(!verificarExistenciaLivroNoArray(getJSONArray("livro"), nome, autor) || verificarExistenciaLivroNoArray(jsonArray, nome, autor)) {
			return false;
		}
		jsonObject.put("nome", nome);
		jsonObject.put("autor", autor);
		Calendar data = Calendar.getInstance();
		jsonObject.put("data", data.getTime());
		jsonObject.put("data-disponibilidade-prevista", data.getTime());
		jsonArray.add(jsonObject);
		if(!disponivelParaReservado(pegaLivro(nome, autor))) {
			return false;
		}
		return (writeJSONFile());
	}
	
	@SuppressWarnings("unchecked")
	public static boolean cancelarReserva(JSONObject cliente, String nome, String autor) {
		JSONObject jsonObject = new JSONObject();
		if(cliente == null) {
			return false;
		}
		JSONArray jsonArray = (JSONArray) cliente.get("reserva");
		Iterator<JSONObject> iterator = jsonArray.iterator();
		while(iterator.hasNext()) {
			jsonObject = iterator.next();
			if(String.valueOf(jsonObject.get("nome")).equals(nome) && String.valueOf(jsonObject.get("autor")).equals(autor)) {
				jsonArray.remove(jsonObject);
				if(!reservadoParaDisponivel(pegaLivro(nome, autor))) {
					return false;
				}
				return writeJSONFile();
			}
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public static boolean concluirReserva(JSONObject cliente, String nome, String autor) {
		JSONObject jsonObject = new JSONObject();
		if(cliente == null) {
			return false;
		}
		JSONArray jsonArray = (JSONArray) cliente.get("reserva");
		Iterator<JSONObject> iterator = jsonArray.iterator();
		while(iterator.hasNext()) {
			jsonObject = iterator.next();
			if(String.valueOf(jsonObject.get("nome")).equals(nome) && String.valueOf(jsonObject.get("autor")).equals(autor)) {
				jsonArray.remove(jsonObject);
				if(!reservadoParaAlugado(pegaLivro(nome, autor))) {
					return false;
				}
				return writeJSONFile();
			}
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public static boolean addRequerimento(JSONObject cliente, String nome, String autor) {
		JSONObject jsonObject = new JSONObject();
		if(cliente == null) {
			return false;
		}
		JSONArray jsonArray = (JSONArray) cliente.get("requerimento");
		if(verificarExistenciaLivroNoArray(getJSONArray("livro"), nome, autor) || verificarExistenciaLivroNoArray(jsonArray, nome, autor)) {
			return false;
		}
		jsonObject.put("nome", nome);
		jsonObject.put("autor", autor);
		jsonArray.add(jsonObject);
		return writeJSONFile();
	}
	
	private static boolean verificarExistenciaLivroNoArray(JSONArray array, String nome, String autor) {
		JSONObject jsonObject;
		for(Object o : array) {
			jsonObject = (JSONObject) o;
			if(String.valueOf(jsonObject.get("nome")).equals(nome) && String.valueOf(jsonObject.get("autor")).equals(autor)) {
				return true;
			}
		}
		return false;
	}
	
	private static boolean writeJSONFile() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String prettyJson;
		try (FileWriter file = new FileWriter(path)) {
			prettyJson = gson.toJson(rootJSONObject);
            file.write(prettyJson);
            file.flush();
 
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
		return true;
	}
	
	private static JSONObject pegaLivro(String nome, String autor) {
		JSONObject jsonObject;
		JSONArray array = (JSONArray) rootJSONObject.get("livro");
		for(Object o : array) {
			jsonObject = (JSONObject) o;
			if(String.valueOf(jsonObject.get("nome")).equals(nome) && String.valueOf(jsonObject.get("autor")).equals(autor)) {
				return jsonObject;
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private static boolean disponivelParaAlugado(JSONObject livro) {
		long disponivel = (long) livro.get("disponivel");
		if(disponivel <= 0) {
			return false;
		}
		long alugado = (long) livro.get("alugado");
		livro.put("alugado", ++alugado);
		livro.put("disponivel", --disponivel);
		return true;
	}
	
	@SuppressWarnings("unchecked")
	private static boolean disponivelParaReservado(JSONObject livro) {
		long disponivel = (long) livro.get("disponivel");
		long reservado = (long) livro.get("reservado");
		long total = (long) livro.get("total");
		if(disponivel > 0) {
			livro.put("disponivel", --disponivel);
			livro.put("reservado", ++reservado);
			return true;
		} else if(reservado < total) {
			livro.put("reservado", ++reservado);
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	private static boolean reservadoParaDisponivel(JSONObject livro) {
		long disponivel = (long) livro.get("disponivel");
		long reservado = (long) livro.get("reservado");
		if(reservado <= 0) {
			return false;
		}
		livro.put("reservado", --reservado);
		livro.put("disponivel", ++disponivel);	
		return true;
	}
	
	@SuppressWarnings("unchecked")
	private static boolean reservadoParaAlugado(JSONObject livro) {
		long reservado = (long) livro.get("reservado");
		long alugado = (long) livro.get("alugado");
		if(reservado <= 0) {
			return false;
		}
		livro.put("reservado", --reservado);
		livro.put("alugado", ++alugado);	
		return true;
	}
	
	@SuppressWarnings("unchecked")
	private static boolean alugadoParaDisponivel(JSONObject livro) {
		long alugado = (long) livro.get("alugado");
		long disponivel = (long) livro.get("disponivel");
		long reservado = (long) livro.get("reservado");
		if(alugado <= 0) {
			return false;
		}
		if(reservado > disponivel) {
			livro.put("alugado", --alugado);
		} else {
			livro.put("alugado", --alugado);
			livro.put("disponivel", ++disponivel);			
		}
		return true;
	}
}
