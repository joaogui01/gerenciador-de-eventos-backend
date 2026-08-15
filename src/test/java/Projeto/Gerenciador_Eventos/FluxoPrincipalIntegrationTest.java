package Projeto.Gerenciador_Eventos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import Projeto.Gerenciador_Eventos.dto.DadosAtualizarEvento;
import Projeto.Gerenciador_Eventos.dto.DadosCadastroEvento;
import Projeto.Gerenciador_Eventos.dto.DadosCadastroInscricao;
import Projeto.Gerenciador_Eventos.dto.DadosCadastroTicket;
import Projeto.Gerenciador_Eventos.dto.DadosCadastroUsuario;
import Projeto.Gerenciador_Eventos.dto.DadosAutenticacao;
import Projeto.Gerenciador_Eventos.dto.DadosDetalharEvento;
import Projeto.Gerenciador_Eventos.dto.DadosDetalharInscricao;
import Projeto.Gerenciador_Eventos.dto.DadosDetalharTicket;
import Projeto.Gerenciador_Eventos.dto.DadosEscanearTicket;
import Projeto.Gerenciador_Eventos.dto.DadosTokenJWT;
import Projeto.Gerenciador_Eventos.entity.Usuario;
import Projeto.Gerenciador_Eventos.entity.enums.Perfil;
import Projeto.Gerenciador_Eventos.repository.UsuarioRepository;

/*
 * Teste de integração do fluxo principal da aplicação: cadastro de usuários,
 * autenticação, criação de evento, inscrição, emissão de ticket e check-in via QR code.
 * Também cobre as regras de posse (quem pode fazer o quê) e as validações de negócio.
 *
 * É um "roteiro" (steps em ordem, um dependendo do resultado do anterior), por isso
 * os métodos são numerados com @Order e a classe usa uma instância só (PER_CLASS)
 * pra poder guardar tokens e ids entre os passos.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FluxoPrincipalIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UsuarioRepository usuarioRepository;

	private String tokenOrganizador;
	private String tokenParticipante;
	private Long idEvento;
	private Long idInscricao;
	private Long idTicket;
	private String codigoHashTicket;

	@Test
	@Order(1)
	void primeiroUsuarioCadastradoViraAdmin() throws Exception {
		DadosCadastroUsuario dados = new DadosCadastroUsuario("Organizador", "organizador", "senha123", "11111111111", "11999990000");

		mockMvc.perform(post("/usuario/cadastrar")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dados)))
			.andExpect(status().isOk());

		UserDetails usuario = usuarioRepository.findByLogin("organizador");
		assertEquals(Perfil.ADMIN, ((Usuario) usuario).getPerfil());
	}

	@Test
	@Order(2)
	void segundoUsuarioCadastradoEhUser() throws Exception {
		DadosCadastroUsuario dados = new DadosCadastroUsuario("Participante", "participante", "senha123", "22222222222", "11988880000");

		mockMvc.perform(post("/usuario/cadastrar")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dados)))
			.andExpect(status().isOk());

		UserDetails usuario = usuarioRepository.findByLogin("participante");
		assertEquals(Perfil.USER, ((Usuario) usuario).getPerfil());
	}

	@Test
	@Order(3)
	void naoDeixaCadastrarLoginDuplicado() throws Exception {
		DadosCadastroUsuario dados = new DadosCadastroUsuario("Outro Nome", "organizador", "outraSenha", "33333333333", null);

		mockMvc.perform(post("/usuario/cadastrar")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dados)))
			.andExpect(status().isBadRequest());
	}

	@Test
	@Order(4)
	void fazLoginDosDoisUsuarios() throws Exception {
		tokenOrganizador = login("organizador", "senha123");
		tokenParticipante = login("participante", "senha123");

		assertNotNull(tokenOrganizador);
		assertNotNull(tokenParticipante);
	}

	@Test
	@Order(5)
	void loginComSenhaErradaRetorna400() throws Exception {
		DadosAutenticacao dados = new DadosAutenticacao("organizador", "senhaErrada");

		mockMvc.perform(post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dados)))
			.andExpect(status().isBadRequest());
	}

	@Test
	@Order(6)
	void requisicaoSemTokenRetorna403() throws Exception {
		mockMvc.perform(get("/evento/listar"))
			.andExpect(status().isForbidden());
	}

	@Test
	@Order(7)
	void organizadorCadastraUmEventoComDuasVagas() throws Exception {
		DadosCadastroEvento dados = new DadosCadastroEvento(
				"Show de rock", "Uma noite de rock", LocalDate.now().plusDays(30),
				"Praça Central", 2, 2, new java.math.BigDecimal("50.00"));

		MvcResult resultado = mockMvc.perform(post("/evento/cadastrar")
				.header("Authorization", "Bearer " + tokenOrganizador)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dados)))
			.andExpect(status().isOk())
			.andReturn();

		DadosDetalharEvento detalhes = objectMapper.readValue(resultado.getResponse().getContentAsString(), DadosDetalharEvento.class);
		idEvento = detalhes.idEvento();
		assertEquals(2, detalhes.vagasDisponiveisEvento());
	}

	@Test
	@Order(8)
	void participanteNaoConsegueAtualizarEventoDeOutraPessoa() throws Exception {
		DadosAtualizarEvento dados = new DadosAtualizarEvento(idEvento, "Tentativa indevida", null, null, null, null, null, null);

		mockMvc.perform(put("/evento/atualizar")
				.header("Authorization", "Bearer " + tokenParticipante)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dados)))
			.andExpect(status().isForbidden());
	}

	@Test
	@Order(9)
	void participanteSeInscreveNoEvento() throws Exception {
		DadosCadastroInscricao dados = new DadosCadastroInscricao(idEvento, LocalDate.now());

		MvcResult resultado = mockMvc.perform(post("/inscricao/cadastrar")
				.header("Authorization", "Bearer " + tokenParticipante)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dados)))
			.andExpect(status().isOk())
			.andReturn();

		DadosDetalharInscricao detalhes = objectMapper.readValue(resultado.getResponse().getContentAsString(), DadosDetalharInscricao.class);
		idInscricao = detalhes.idInscricao();

		MvcResult eventoAtualizado = mockMvc.perform(get("/evento/detalhar/" + idEvento)
				.header("Authorization", "Bearer " + tokenParticipante))
			.andExpect(status().isOk())
			.andReturn();

		DadosDetalharEvento evento = objectMapper.readValue(eventoAtualizado.getResponse().getContentAsString(), DadosDetalharEvento.class);
		assertEquals(1, evento.vagasDisponiveisEvento());
	}

	@Test
	@Order(10)
	void naoDeixaSeInscreverDuasVezesNoMesmoEvento() throws Exception {
		DadosCadastroInscricao dados = new DadosCadastroInscricao(idEvento, LocalDate.now());

		mockMvc.perform(post("/inscricao/cadastrar")
				.header("Authorization", "Bearer " + tokenParticipante)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dados)))
			.andExpect(status().isBadRequest());
	}

	@Test
	@Order(11)
	void participanteEmiteOProprioTicket() throws Exception {
		DadosCadastroTicket dados = new DadosCadastroTicket(idInscricao);

		MvcResult resultado = mockMvc.perform(post("/ticket/cadastrar")
				.header("Authorization", "Bearer " + tokenParticipante)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dados)))
			.andExpect(status().isOk())
			.andReturn();

		DadosDetalharTicket detalhes = objectMapper.readValue(resultado.getResponse().getContentAsString(), DadosDetalharTicket.class);
		idTicket = detalhes.idTicket();
		codigoHashTicket = detalhes.codigoHashTicket();

		assertNotNull(codigoHashTicket);
	}

	@Test
	@Order(12)
	void organizadorNaoConsegueVerQrCodeDeTicketAlheio() throws Exception {
		mockMvc.perform(get("/ticket/detalhar/" + idTicket + "/qrcode")
				.header("Authorization", "Bearer " + tokenOrganizador))
			.andExpect(status().isForbidden());
	}

	@Test
	@Order(13)
	void participanteNaoConsegueEscanearOProprioTicket() throws Exception {
		DadosEscanearTicket dados = new DadosEscanearTicket(codigoHashTicket);

		mockMvc.perform(post("/checkin/escanear")
				.header("Authorization", "Bearer " + tokenParticipante)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dados)))
			.andExpect(status().isForbidden());
	}

	@Test
	@Order(14)
	void organizadorEscaneiaOTicketEConfirmaCheckIn() throws Exception {
		DadosEscanearTicket dados = new DadosEscanearTicket(codigoHashTicket);

		mockMvc.perform(post("/checkin/escanear")
				.header("Authorization", "Bearer " + tokenOrganizador)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dados)))
			.andExpect(status().isOk());
	}

	@Test
	@Order(15)
	void escanearOMesmoTicketDeNovoRetorna400() throws Exception {
		DadosEscanearTicket dados = new DadosEscanearTicket(codigoHashTicket);

		mockMvc.perform(post("/checkin/escanear")
				.header("Authorization", "Bearer " + tokenOrganizador)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dados)))
			.andExpect(status().isBadRequest());
	}

	@Test
	@Order(16)
	void organizadorConsegueCancelarInscricaoDeOutraPessoaNoProprioEvento() throws Exception {
		mockMvc.perform(delete("/inscricao/inativar/" + idInscricao)
				.header("Authorization", "Bearer " + tokenOrganizador))
			.andExpect(status().isOk());

		MvcResult eventoAtualizado = mockMvc.perform(get("/evento/detalhar/" + idEvento)
				.header("Authorization", "Bearer " + tokenOrganizador))
			.andExpect(status().isOk())
			.andReturn();

		DadosDetalharEvento evento = objectMapper.readValue(eventoAtualizado.getResponse().getContentAsString(), DadosDetalharEvento.class);
		assertEquals(2, evento.vagasDisponiveisEvento());
	}

	private String login(String login, String senha) throws Exception {
		DadosAutenticacao dados = new DadosAutenticacao(login, senha);

		MvcResult resultado = mockMvc.perform(post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dados)))
			.andExpect(status().isOk())
			.andReturn();

		DadosTokenJWT tokenDTO = objectMapper.readValue(resultado.getResponse().getContentAsString(), DadosTokenJWT.class);
		return tokenDTO.token();
	}

}
