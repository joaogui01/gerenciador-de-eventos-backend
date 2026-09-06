package Projeto.Gerenciador_Eventos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

import tools.jackson.databind.ObjectMapper;

import Projeto.Gerenciador_Eventos.dto.DadosAlterarSenha;
import Projeto.Gerenciador_Eventos.dto.DadosAtualizarEvento;
import Projeto.Gerenciador_Eventos.dto.DadosAtualizarUsuario;
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
			.andExpect(status().isCreated())
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
			.andExpect(status().isCreated())
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
			.andExpect(status().isCreated())
			.andReturn();

		DadosDetalharTicket detalhes = objectMapper.readValue(resultado.getResponse().getContentAsString(), DadosDetalharTicket.class);
		idTicket = detalhes.idTicket();
		codigoHashTicket = detalhes.codigoHashTicket();

		assertNotNull(codigoHashTicket);
	}

	/*
	 * Atenção ao fixture: "organizador" é o PRIMEIRO usuário cadastrado, e o
	 * UsuarioService promove esse primeiro usuário a ADMIN (bootstrap). Ou seja, ele
	 * não é um organizador comum — é administrador, e ADMIN enxerga qualquer QR code
	 * por design. Quem não pode ver o ticket alheio é o usuário comum (teste seguinte).
	 */
	@Test
	@Order(12)
	void adminConsegueVerQrCodeDeQualquerTicket() throws Exception {
		mockMvc.perform(get("/ticket/detalhar/" + idTicket + "/qrcode")
				.header("Authorization", "Bearer " + tokenOrganizador))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.IMAGE_PNG));
	}

	@Test
	@Order(12)
	void donoDoTicketConsegueVerOProprioQrCode() throws Exception {
		mockMvc.perform(get("/ticket/detalhar/" + idTicket + "/qrcode")
				.header("Authorization", "Bearer " + tokenParticipante))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.IMAGE_PNG));
	}

	@Test
	@Order(12)
	void usuarioComumNaoConsegueVerQrCodeDeTicketAlheio() throws Exception {
		DadosCadastroUsuario dados = new DadosCadastroUsuario("Intruso", "intruso", "senha123", "44444444444", null);
		mockMvc.perform(post("/usuario/cadastrar")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dados)))
			.andExpect(status().isOk());

		String tokenIntruso = login("intruso", "senha123");

		mockMvc.perform(get("/ticket/detalhar/" + idTicket + "/qrcode")
				.header("Authorization", "Bearer " + tokenIntruso))
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


	/*
	 * A partir daqui: testes de contrato com o frontend. O app web depende destes
	 * endpoints e destes NOMES DE CAMPO exatos, então uma renomeação silenciosa em
	 * qualquer DTO quebra o app — melhor quebrar aqui.
	 */

	@Test
	@Order(17)
	void meuPerfilDevolveOsCamposQueOFrontendUsa() throws Exception {
		mockMvc.perform(get("/usuario/meuperfil")
				.header("Authorization", "Bearer " + tokenParticipante))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.idUsuario").exists())
			.andExpect(jsonPath("$.nome").value("Participante"))
			.andExpect(jsonPath("$.login").value("participante"))
			.andExpect(jsonPath("$.cpf").value("22222222222"))
			.andExpect(jsonPath("$.perfil").value("USER"));
	}

	@Test
	@Order(18)
	void listarEventosTrazIdOrganizadorParaDerivarMeusEventos() throws Exception {
		mockMvc.perform(get("/evento/listar")
				.header("Authorization", "Bearer " + tokenParticipante))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].idEvento").exists())
			.andExpect(jsonPath("$[0].nomeEvento").exists())
			.andExpect(jsonPath("$[0].localEvento").exists())
			.andExpect(jsonPath("$[0].dataEvento").exists())
			.andExpect(jsonPath("$[0].vagasTotaisEvento").exists())
			.andExpect(jsonPath("$[0].vagasDisponiveisEvento").exists())
			.andExpect(jsonPath("$[0].statusGeral").exists())
			.andExpect(jsonPath("$[0].idOrganizador").exists())
			.andExpect(jsonPath("$[0].nomeOrganizador").value("Organizador"));
	}

	@Test
	@Order(19)
	void filtroDeInscricaoPorParticipanteFunciona() throws Exception {
		Usuario participante = (Usuario) usuarioRepository.findByLogin("participante");

		mockMvc.perform(get("/inscricao/listar/filtro")
				.param("idParticipante", String.valueOf(participante.getIdUsuario()))
				.header("Authorization", "Bearer " + tokenParticipante))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].idInscricao").value(idInscricao.intValue()))
			.andExpect(jsonPath("$[0].idEvento").value(idEvento.intValue()))
			.andExpect(jsonPath("$[0].idParticipante").value(participante.getIdUsuario().intValue()))
			.andExpect(jsonPath("$[0].statusGeral").exists());
	}

	@Test
	@Order(20)
	void filtroDeTicketPorInscricaoFunciona() throws Exception {
		mockMvc.perform(get("/ticket/listar/filtro")
				.param("idInscricao", String.valueOf(idInscricao))
				.header("Authorization", "Bearer " + tokenParticipante))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].idTicket").value(idTicket.intValue()))
			.andExpect(jsonPath("$[0].idInscricao").value(idInscricao.intValue()))
			.andExpect(jsonPath("$[0].codigoHashTicket").value(codigoHashTicket));
	}

	@Test
	@Order(21)
	void notificacaoChegaProOrganizadorQuandoAlguemSeInscreve() throws Exception {
		mockMvc.perform(get("/notificacao/listar")
				.header("Authorization", "Bearer " + tokenOrganizador))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].idNotificacao").exists())
			.andExpect(jsonPath("$[0].titulo").value("Novo Integrante No Seu Evento!"))
			.andExpect(jsonPath("$[0].mensagem").exists())
			.andExpect(jsonPath("$[0].lida").value(false))
			.andExpect(jsonPath("$[0].dataHora").exists());
	}

	/*
	 * Um token corrompido/expirado tem que responder 403, nunca 500: é assim que o
	 * frontend sabe que a sessão acabou (descarta o token salvo e volta pro login)
	 * em vez de mostrar "erro no servidor".
	 */
	@Test
	@Order(22)
	void tokenInvalidoRetorna403ENao500() throws Exception {
		mockMvc.perform(get("/usuario/meuperfil")
				.header("Authorization", "Bearer token.completamente.invalido"))
			.andExpect(status().isForbidden());
	}

	@Test
	@Order(23)
	void cadastrarEventoSemDataRetorna400ENao500() throws Exception {
		DadosCadastroEvento dados = new DadosCadastroEvento(
				"Evento sem data", "Descricao", null, "Algum lugar", 10, null, null);

		mockMvc.perform(post("/evento/cadastrar")
				.header("Authorization", "Bearer " + tokenOrganizador)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dados)))
			.andExpect(status().isBadRequest());
	}

	/*
	 * vagasDisponiveisEvento não é mais obrigatório no cadastro: o servidor sempre
	 * inicia vagasDisponiveis = vagasTotais, então o frontend manda só vagasTotais.
	 */
	@Test
	@Order(24)
	void cadastrarEventoSemVagasDisponiveisDerivaDeVagasTotais() throws Exception {
		DadosCadastroEvento dados = new DadosCadastroEvento(
				"Evento sem vagas disponiveis", "Descricao", LocalDate.now().plusDays(10),
				"Algum lugar", 7, null, null);

		mockMvc.perform(post("/evento/cadastrar")
				.header("Authorization", "Bearer " + tokenOrganizador)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dados)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.vagasTotaisEvento").value(7))
			.andExpect(jsonPath("$.vagasDisponiveisEvento").value(7))
			.andExpect(jsonPath("$.precoEvento").doesNotExist());
	}

	@Test
	@Order(25)
	void usuarioComumNaoConsegueInativarTicketAlheio() throws Exception {
		String tokenIntruso = login("intruso", "senha123");

		mockMvc.perform(delete("/ticket/inativar/" + idTicket)
				.header("Authorization", "Bearer " + tokenIntruso))
			.andExpect(status().isForbidden());
	}

	/*
	 * Repare que estes dois testes conferem o efeito PERSISTIDO (relendo o perfil / fazendo
	 * login com a senha nova), não só o corpo da resposta. O corpo sozinho não provava nada:
	 * o service alterava uma entidade desanexada e devolvia 200 com os dados novos sem ter
	 * gravado nada no banco.
	 */
	@Test
	@Order(26)
	void atualizarPerfilAlteraNomeETelefoneDeVerdade() throws Exception {
		DadosAtualizarUsuario dados = new DadosAtualizarUsuario("Participante Renomeado", null, "11977770000");

		mockMvc.perform(put("/usuario/atualizar")
				.header("Authorization", "Bearer " + tokenParticipante)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dados)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.nome").value("Participante Renomeado"))
			.andExpect(jsonPath("$.telefone").value("11977770000"))
			.andExpect(jsonPath("$.login").value("participante"));

		mockMvc.perform(get("/usuario/meuperfil")
				.header("Authorization", "Bearer " + tokenParticipante))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.nome").value("Participante Renomeado"))
			.andExpect(jsonPath("$.telefone").value("11977770000"));
	}

	@Test
	@Order(27)
	void alterarSenhaTrocaASenhaDeVerdade() throws Exception {
		DadosAlterarSenha comSenhaErrada = new DadosAlterarSenha("naoEhAMinhaSenha", "novaSenha456");
		mockMvc.perform(put("/usuario/alterar-senha")
				.header("Authorization", "Bearer " + tokenParticipante)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(comSenhaErrada)))
			.andExpect(status().isBadRequest());

		DadosAlterarSenha dados = new DadosAlterarSenha("senha123", "novaSenha456");
		mockMvc.perform(put("/usuario/alterar-senha")
				.header("Authorization", "Bearer " + tokenParticipante)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dados)))
			.andExpect(status().isOk());

		// a senha antiga não vale mais...
		mockMvc.perform(post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new DadosAutenticacao("participante", "senha123"))))
			.andExpect(status().isBadRequest());

		// ...e a nova vale.
		assertNotNull(login("participante", "novaSenha456"));
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
