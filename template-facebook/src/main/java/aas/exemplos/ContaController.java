package aas.exemplos;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@RestController
public class ContaController {
	
	private static final String CLIENT_ID = "422169628682963";
	private static final String CLIENT_SECRET = "7ddb5d7f1e6b6938a772b28a768ea12e";
	private static final String ACCESS_TOKEN = "422169628682963|1VV0U8_WmKUptyE0DQFybaUPDDU";
	private static final String URL_BASE_REDIRECT = "http://localhost:8080/conta/";
	
	private List<Conta> registros = new ArrayList<Conta>();
	
	@GetMapping("/conta/{id}")
	public ResponseEntity<Conta> recuperaConta(@PathVariable("id") Long id) {
		Conta conta = null;
		for(Conta c : registros) {
			if(c.getId() == id) {
				conta = c;
			}
		}
		
		if(conta != null) {
			return new ResponseEntity<>(conta, HttpStatus.OK);
		}
		
		String facebook_login_url = "https://www.facebook.com/v3.3/dialog/oauth?client_id=%s&redirect_uri=%s&state=%s"; 
		int new_id = registros.size() + 1;
		String state = "security_text";
		String url_to_redirect = URL_BASE_REDIRECT + "cadastro/" + new_id; 
		String facebook_login_url_formated = String.format(facebook_login_url, CLIENT_ID, url_to_redirect, state);
				
		HttpHeaders headers = new HttpHeaders();
	    headers.add("Location", facebook_login_url_formated);
				
		return new ResponseEntity<>(null, headers, HttpStatus.TEMPORARY_REDIRECT);
	}
	
	@GetMapping("/conta/cadastro/{id}")
	public ResponseEntity<Conta> cadastrarConta(@PathVariable("id") Long id, @PathParam("code") String code, @PathParam("state") String state) throws URISyntaxException {
		
		String url_busca_token_acesso = "https://graph.facebook.com/v3.3/oauth/access_token?client_id={CLIENT_ID}&redirect_uri={REDIRECT_URI}&state={STATE}&client_secret={SECRET}&code={CODE}";
		url_busca_token_acesso = url_busca_token_acesso.replace("{CLIENT_ID}", CLIENT_ID);
		url_busca_token_acesso = url_busca_token_acesso.replace("{REDIRECT_URI}", URL_BASE_REDIRECT + "cadastro/" + id);
		url_busca_token_acesso = url_busca_token_acesso.replace("{STATE}", state);
		url_busca_token_acesso = url_busca_token_acesso.replace("{SECRET}", CLIENT_SECRET);
		url_busca_token_acesso = url_busca_token_acesso.replace("{CODE}", code);
		
		RestTemplate restTemplate = new RestTemplate();    
	     
		String resp = restTemplate.getForObject(url_busca_token_acesso, String.class);
		
	    JsonObject jsonObject = new JsonParser().parse(resp).getAsJsonObject();
	    String input_token = jsonObject.get("access_token").getAsString();

	    String url_busca_data_user_id = "https://graph.facebook.com/debug_token?input_token={INPUT_TOKEN}&access_token={ACCESS_TOKEN}";
	    url_busca_data_user_id = url_busca_data_user_id.replace("{INPUT_TOKEN}", input_token);
		url_busca_data_user_id = url_busca_data_user_id.replace("{ACCESS_TOKEN}", ACCESS_TOKEN);
		
		resp = restTemplate.getForObject(url_busca_data_user_id, String.class);
		
	    jsonObject = new JsonParser().parse(resp).getAsJsonObject();
	    String user_id = jsonObject.get("data").getAsJsonObject().get("user_id").getAsString();
	    
	    String url_busca_usuario = "https://graph.facebook.com/{ID_USUARIO_FACEBOOK}/?access_token={INPUT_TOKEN}";
	    url_busca_usuario = url_busca_usuario.replace("{ID_USUARIO_FACEBOOK}", user_id);
	    url_busca_usuario = url_busca_usuario.replace("{INPUT_TOKEN}", input_token);
	    
	    resp = restTemplate.getForObject(url_busca_usuario, String.class);
	    
	    jsonObject = new JsonParser().parse(resp).getAsJsonObject();
	    String user_facebook_id = jsonObject.get("id").getAsString();
	    String user_facebook_name = jsonObject.get("name").getAsString();
	    
	    Conta conta = null;
		for(Conta c : registros) {
			if(c.getFacebookId().equals(user_facebook_id)) {
				conta = c;
				id = conta.getId();
			}
		}
	    
		if(conta == null) {
			conta = new Conta();
		    conta.setId(id);
		    conta.setFacebookId(user_facebook_id);
		    conta.setNome(user_facebook_name);
		    registros.add(conta);
		}

	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Location", URL_BASE_REDIRECT + id);
				
		return new ResponseEntity<>(null, headers, HttpStatus.TEMPORARY_REDIRECT);
	}
}
