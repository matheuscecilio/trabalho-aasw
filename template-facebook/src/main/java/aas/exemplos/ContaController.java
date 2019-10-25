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

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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
		
		String stringUrl = String.format(
				"https://www.facebook.com/v3.3/dialog/oauth?client_id={0}&redirect_uri={1}&state={2}", 
				CLIENT_ID, "localhost:8080/conta/cadastro/" + registros.size() + 1, "ABCD");
		
				
		throw new UnsupportedOperationException("Endpoint01 não implementado");
		
		// Endpoint 01:
		// Verifica se existe alguma Conta com o id igual ao id recebido como parâmetro
		//
		// Se o identificador foi encontrado
		//     Retorna um JSON contendo os dados da conta;
		//     O status da resposta é um http 200;
		// 
		// Caso o identificador não seja encontrado
		//     O status da resposta é um http redirect 307;
		//     Adiciona na resposta um cabeçalho "Location", cujo valor é a página de login do facebook com os devidos parâmetros:
		//         cliente_id: o client_id da aplicação;
		//         redirect_uri: indica que o facebook deverá redirecionar a aplicação para o Endpoint 02, utilizando o id de entrada;
		//         state: uma string utilizada para garantir do remetente da mensagem.
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
