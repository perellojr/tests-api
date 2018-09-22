package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.aventstack.extentreports.Status;

public class FrameworkUtils<CamposApi> {

	Logger log = LogManager.getLogger(this.getClass());
	ReportUtils report = new ReportUtils();
	String listaSelecionada = null;

	public FrameworkUtils() {
	}

	public static void sleeps(long segundos) {
		try {
			Thread.sleep(segundos * 1000);
		} catch (InterruptedException e) {

		}
	}

	public static boolean existeDir(String caminho) {
		File diretorio;
		diretorio = new File(caminho);
		if (diretorio.exists()) {
			return true;
		} else {
			return false;
		}
	}

	public static void esvaziaDiretorio(String caminho) {
		try {
			File folder = new File(caminho);
			if (folder.isDirectory()) {
				File[] sun = folder.listFiles();
				for (File toDelete : sun) {
					toDelete.delete();
				}
			}
		} catch (Exception e) {
		}
	}

	public static boolean isDiretorioVazio(String diretorio) {
		boolean dirVazio = false;
		File dir = new File(diretorio);
		String[] children = dir.list();
		if (children == null) {
			// Diretorio nao existe ou nao eh um diretorio
			dirVazio = true;
		} else {
			if (children.length > 0) {
				dirVazio = false;
			}
		}
		return dirVazio;
	}

	public static void criarDiretorio(String diretorioASerCriado) {
		try {
			File diretorio = new File(diretorioASerCriado);
			if (!diretorio.exists()) {
				diretorio.mkdirs();
			}
		} catch (Exception e) {
		}
	}

	public static void deletarArquivo(File arq) {
		if (arq.isDirectory()) {
			File[] arquivos = arq.listFiles();
			for (int i = 0; i < arquivos.length; i++) {
				deletarArquivo(arquivos[i]);
			}
		}
		arq.delete();
	}

	public String loadFromPropertiesFile(String propertieFileName, String propertLoad) {
		Properties prop = new Properties();
		InputStream input = null;
		String path = "src/main/resources/";
		String property = "";
		try {
			input = new FileInputStream(path + propertieFileName);
			prop.load(input);
			property = prop.getProperty(propertLoad);
		} catch (IOException ex) {
			System.out.println(ex.toString());
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					System.out.println(e.toString());
				}
			}
		}
		return property;
	}

	public boolean validaIntegridadeCampos(String tipoValidacao, String NomeCampo, String valorCampo, String valorCorreto, String formatoData, Integer linha, boolean exibeLog) {
		boolean retorno = false;
		String msgLinha = (linha == null) ? "" : "Linha (" + linha + "), ";
		String msgCampo = "campo '" + NomeCampo + "', com o valor '" + valorCampo + "', ";
		
		switch (retornaTipoValidacaoInteiro(tipoValidacao)) {
		// 1 - validar_obrigatorio
		case 1:
			if (valorCampo != null && !valorCampo.isEmpty()) {
				if (exibeLog)
					report.logMensagem(Status.PASS, msgLinha + msgCampo + "nao esta vazio, validado com sucesso");
				retorno = true;
			} else {
				report.logMensagem(Status.ERROR, msgLinha + msgCampo + "esta vazio");
				retorno = false;
			}
			break;

		// 2 - validar_igualdade
		case 2:
			if (valorCampo.equals(valorCorreto)) {
				if (exibeLog)
					report.logMensagem(Status.PASS, msgLinha + msgCampo + "esta correto");
				retorno = true;
			} else {
				report.logMensagem(Status.ERROR, msgLinha + msgCampo + "e diferente de '" + valorCorreto + "'");
				retorno = false;
			}
			break;

		// 3 - "validar_inteiro"
		case 3:
			if (valorCampo == null) {
				retorno = true;
			} else {
				if (valorCampo.matches("[0-9]*")) {
					if (exibeLog) {
						report.logMensagem(Status.PASS, msgLinha + msgCampo + " e um valor inteiro, validado com sucesso");
					}
					retorno = true;
				} else {
					report.logMensagem(Status.ERROR, msgLinha + msgCampo + " nao e um valor inteiro");
					retorno = false;
				}
			}
			break;

		// 4 - "validar_data"
		case 4:
			try {
				if (!(valorCampo  == null || valorCampo.isEmpty())) {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern((formatoData == null || formatoData.isEmpty()) ? "yyyy-MM-dd" : formatoData);
					@SuppressWarnings("unused")
					LocalDate localDate = LocalDate.parse(valorCampo, formatter);
					if (exibeLog) {
						report.logMensagem(Status.PASS, msgLinha + msgCampo + "e válido");
					}
				}
				retorno = true;
			} catch (DateTimeParseException e) {
				report.logMensagem(Status.ERROR,msgLinha + msgCampo + "e inválido");
				retorno = false;
			}
			break;

		// 5 - "validar_boolean"
		case 5:
			try {
				if (valorCampo == null || valorCampo.equalsIgnoreCase("true") || valorCampo.equalsIgnoreCase("false")) {
					if (exibeLog) {
						report.logMensagem(Status.PASS, msgLinha + msgCampo + " e um valor Boolean, validado com sucesso");
					}
					retorno = true;
				} else {
					report.logMensagem(Status.ERROR,msgLinha + msgCampo + " nao e um valor Boolean");
				}
			} catch (Exception e) {
				log.info(e.toString());
				retorno = false;
			}
			break;
		
		// 6 - "validar_decimal"
		case 6:
			if (valorCampo == null) {
				retorno = true;
			} else {
				if (valorCampo.matches("[0-9]{0,10}[.]{1,1}[0-9]{0,4}")) {
					if (exibeLog)
						report.logMensagem(Status.PASS, msgLinha + msgCampo + " e um valor decimal, validado com sucesso");
						retorno = true;
				} else {
					report.logMensagem(Status.ERROR, msgLinha + msgCampo + " nao e um valor decimal");
					retorno = false;
				}
			}
		break;	

		default:
		break;
		}

		return retorno;
	}
	
	public Integer retornaTipoValidacaoInteiro(String tipoValidacao){
		Integer  tipoValidacaoInt = null;
		
		if(tipoValidacao.equalsIgnoreCase("validar_obrigatorio")){
			tipoValidacaoInt = 1; 
		}else if (tipoValidacao.equalsIgnoreCase("validar_igualdade")) {
			tipoValidacaoInt = 2;
		}else if (tipoValidacao.equalsIgnoreCase("validar_inteiro")) {
			tipoValidacaoInt = 3;
		}else if (tipoValidacao.equalsIgnoreCase("validar_data")) {
			tipoValidacaoInt = 4;
		}else if (tipoValidacao.equalsIgnoreCase("validar_boolean")) {
			tipoValidacaoInt = 5;
		}else if (tipoValidacao.equalsIgnoreCase("validar_big_decimal")) {
			tipoValidacaoInt = 6;
		}			
		
		return tipoValidacaoInt;
		
	}

	public boolean validaExistenciaCamposApi(JSONArray jsonArray, JSONObject jsonObject, String[] camposApi, boolean imprimeQtdCamposEsperados)throws JSONException {
		Boolean retorno = false;
		JSONObject objetoJson = (jsonArray != null) ? new JSONObject(jsonArray.getString(0)) : jsonObject;
		Integer qtdCamposEsperados =  objetoJson.length();
		Integer qtdCamposJson =  camposApi.length;
		if(imprimeQtdCamposEsperados) {
			report.logMensagem(Status.INFO, (qtdCamposJson == qtdCamposEsperados) ? "Quantidade Campos Json: " + qtdCamposJson + " ---- Quantidade Campos Esperados: " + qtdCamposEsperados: "A Quantidade de campos existentes no json nao corresponde a quantidade esperada");
		}
		for (int i = 0; i < camposApi.length; i++) {
			retorno = (i == 0) ? objetoJson.has(camposApi[i].toString()) : (retorno == false) ? false : objetoJson.has(camposApi[i].toString());
			report.logMensagem((retorno == false)? Status.ERROR : Status.PASS, (retorno == false) ? "O campo '" + camposApi[i] + "' nao esta presente no json utilizado, verifique!" : "Campo '" + camposApi[i] + "' validado com sucesso");
		}
		return retorno;
	}
	
	public String gerarDataHoraAtual(){
		Date dataHoraAtual = new Date();
		String dataHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dataHoraAtual);
		return " " + dataHora;
	}
}