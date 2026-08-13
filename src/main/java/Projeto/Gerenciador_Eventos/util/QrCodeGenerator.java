package Projeto.Gerenciador_Eventos.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

/*
 * Classe utilitária simples para transformar um texto (o código do ticket) em uma
 * imagem PNG de QR code. Usa a biblioteca ZXing (com.google.zxing).
 */
public class QrCodeGenerator {

	private static final int TAMANHO_PADRAO = 300;

	private QrCodeGenerator() {

	}

	public static byte[] gerarPng(String conteudo) {
		try {
			QRCodeWriter writer = new QRCodeWriter();
			BitMatrix matrix = writer.encode(conteudo, BarcodeFormat.QR_CODE, TAMANHO_PADRAO, TAMANHO_PADRAO);

			ByteArrayOutputStream saida = new ByteArrayOutputStream();
			MatrixToImageWriter.writeToStream(matrix, "PNG", saida);

			return saida.toByteArray();
		} catch (WriterException | IOException exception) {
			throw new RuntimeException("Erro ao gerar o QR code do ticket.", exception);
		}
	}

}
