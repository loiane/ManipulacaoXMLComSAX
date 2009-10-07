package com.loiane.reader;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.loiane.agenda.Contato;

/**
 * Classe que faz o parser/leitura de um arquivo XML
 * 
 * @author Loiane Groner
 *
 */
public class SAXReader extends DefaultHandler{

	/** Buffer que guarda as informações quando um texto é encontrado */
	private StringBuffer valorAtual = new StringBuffer(50); 
	
	/** Lista que possui os contatos do arquivo XML */
	private ArrayList<Contato> contatos = new ArrayList<Contato>();
	
	public ArrayList<Contato> getContatos() {
		return contatos;
	}

	/** contato temporário, apenas para coletar as informações do XML */
	private Contato contatoTemp;
	
	//contantes que representam as tags do arquivo XML
	private final String NO_CONTATO = "contato";
	private final String NO_NOME = "nome";
	private final String NO_ENDERECO = "endereco";
	private final String NO_TELEFONE = "telefone";
	private final String NO_EMAIL = "email";
	private final String ATT_ID = "id";
	private final String ATT_GRAVADO = "gravado";
	

	/**
	 * Constutor que inicializa os objetos necessários para fazer o parse
	 * do arquivo contato.xml
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public void parse() throws ParserConfigurationException, SAXException, IOException {
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser parser = spf.newSAXParser(); 
		parser.parse("contato.xml", this );
	}

	/**
	 * Indica que o parser achou o início do documento XML. Este evento não
	 * lhe passa qualquer informação, apenas indica que o parser vai começar
	 * a escanear o arquivo XML.
	 */
	public void startDocument() {
		System.out.println("Iniciando a leitura do XML");
	}

	/**
	 * Indica que o parser achou e fim do documento XML.
	 */
	public void endDocument() {
		System.out.println("Acabou a leitura do XML");
	}

	/**
	 * Indica que o parser achou o início de uma tag (tag de abertura/início).
	 * Este evento fornece o nome do elemento, o nome e valor dos atributos
	 * deste elemento, e também pode fornecer as informações sobre o namespace.
	 */
	public void startElement(String uri, String localName, String tag, Attributes atributos){

		//cria um contato
		if (tag.equalsIgnoreCase(NO_CONTATO)){
			contatoTemp = new Contato();
		}

		//imprime o caminho da tag
		System.out.print("\n" + tag + ": ");
		
		//se o elemento possui atributos, imprime
		for (int i=0; i< atributos.getLength(); i++){
			System.out.print(" " + atributos.getQName(i) + "=" + atributos.getValue(i));
			if (atributos.getQName(i).equalsIgnoreCase(ATT_ID)){
				contatoTemp.setId(Integer.parseInt(atributos.getValue(i)));
			} else if (atributos.getQName(i).equalsIgnoreCase(ATT_GRAVADO)){
				contatoTemp.setGravado(atributos.getValue(i));
			}
		}

	}

	/** 
	 * Indica que o parser achou o fim de uma tag/elemento.
	 * Este evento fornece o nome do elemento, e também pode
	 * fornecer as informações sobre o namespace.
	 */
	public void endElement(String uri, String localName, String tag){

		//adiciona o contato na lista
		if (tag.equalsIgnoreCase(NO_CONTATO)){
			contatos.add(contatoTemp);
			System.out.println();
		} 
		//senão, seta os atributos do contato
		else if (tag.equalsIgnoreCase(NO_NOME)){
			contatoTemp.setNome(valorAtual.toString().trim());
		} else if (tag.equalsIgnoreCase(NO_ENDERECO)){
			contatoTemp.setEndereco(valorAtual.toString().trim());
		} else if (tag.equalsIgnoreCase(NO_TELEFONE)){
			contatoTemp.setTelefone(valorAtual.toString().trim());
		} else if (tag.equalsIgnoreCase(NO_EMAIL)){
			contatoTemp.setEmail(valorAtual.toString().trim());
		}
		
		//limpa o valor Atual
		valorAtual.delete(0, valorAtual.length()); 
	}
	
	/**
	 * Indica que o parser achou algum Texto (Informação).
	 */
	public void characters(char[] ch, int start, int length) {
		System.out.print(String.copyValueOf(ch,start,length).trim());
		
		valorAtual.append(ch,start,length);

	}
	
	/**
	 * Imprime os contatos contidos na lista
	 */
	public void imprimeContatos(){
		for (int i=0; i<contatos.size(); i++){
			System.out.println(contatos.get(i));
		}
	}
	
	public static void main(String[] args){
		try {
			SAXReader reader = new SAXReader();
			reader.parse();
			
			System.out.println("\n\n<<<<Monstrando as informações coletadas:>>>>");
			reader.imprimeContatos();
			
		} catch (ParserConfigurationException e) {
			System.out.println("O parser não foi configurado corretamente.");
			e.printStackTrace();
		} catch (SAXException e) {
			System.out.println("Problema ao fazer o parse do arquivo.");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("O arquivo não pode ser lido.");
			e.printStackTrace();
		}
	}
	


}
