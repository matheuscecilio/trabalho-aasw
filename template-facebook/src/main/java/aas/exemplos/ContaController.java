package aas.exemplos;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.server.PathParam;

import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContaController {
	
	private static final String CLIENT_ID = "422169628682963";
	private static final String CLIENT_SECRET = "7ddb5d7f1e6b6938a772b28a768ea12e";
	private static final String ACCESS_TOKEN = "422169628682963|1VV0U8_WmKUptyE0DQFybaUPDDU";
	
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
		String url_to_redirect ="http://localhost:8080/conta/cadastro/" + new_id; 
		String facebook_login_url_formated = String.format(facebook_login_url, CLIENT_ID, url_to_redirect, state);
				
		HttpHeaders headers = new HttpHeaders();
	    headers.add("Location", facebook_login_url_formated);
				
		return new ResponseEntity<>(null, headers, HttpStatus.TEMPORARY_REDIRECT);
	}
	
	@GetMapping("/conta/cadastro/{id}")
	public ResponseEntity<Conta> cadastrarConta(@PathVariable("id") Long id, @PathParam("code") String code, @PathParam("state") String state) {
		
		throw new UnsupportedOperationException("Endpoint02 não implementado");
		
		// Endpoint 02:
		// Realiza uma série de 3 chamadas para o servidor do facebook
		//     1) Envia o code para obter o token de acesso;
		//     2) Envia o token para ser auditado e obter id do usuário; 
		//     3) Obtem os dados da conta.
		// 
		// Salva os dados do usuário recebidos na terceira requisição
		//
		// Retorna um redirecionamento para o usuário:
		//     O status da resposta é um http redirect 307;
		//     Adiciona na resposta um cabeçalho "Location", cujo valor é o Endpoint 01 com o identificador do usuário  
	}
}
