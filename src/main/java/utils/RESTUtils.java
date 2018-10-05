package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;

public class RESTUtils {
	Logger log = LogManager.getLogger(this.getClass());
	ReportUtils report = new ReportUtils();

	public RESTUtils() {

	}
	
	public boolean sendPost(String requestUrl, String payload, String codUsuario, String cpfUsuario) throws IOException {
		HttpURLConnection connection = null;
		try {
			String cookie = ".=" + codUsuario + "|" + cpfUsuario;
			URL url = new URL(requestUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			if (!codUsuario.equals("") || !cpfUsuario.equals("")) {
				connection.setRequestProperty("Cookie", cookie);
			}
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
			writer.write(payload);
			writer.close();
			logarMensagem("POST", requestUrl, connection.getOutputStream().toString(), connection.getResponseCode());
			if (connection.getResponseCode() == 200 || connection.getResponseCode() == 204) {
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			log.error(e.toString());
			logarMensagemErro(connection);
			return false;
		}
	}

	public String sendPost(String requestUrl, String payload, String mobile_id) throws IOException {
		HttpURLConnection connection = null;
		StringBuffer jsonString;
		try {
			URL url = new URL(requestUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("x-mobile-id", mobile_id);
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
			writer.write(payload);
			writer.close();
			logarMensagem("POST", requestUrl, connection.getOutputStream().toString(), connection.getResponseCode());
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			jsonString = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				jsonString.append(line);
			}
			br.close();
			connection.disconnect();
		} catch (Exception e) {
			log.error(e.toString());
			logarMensagemErro(connection);
			return "";
//			throw new RuntimeException(e.getMessage());
		}
		return jsonString.toString();
	}

	public boolean sendPost(String requestUrl, String payload) throws IOException {
		HttpURLConnection connection = null;
		try {
			URL url = new URL(requestUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
			writer.write(payload);
			writer.close();
			logarMensagem("POST", requestUrl, connection.getOutputStream().toString(), connection.getResponseCode());
			if (connection.getResponseCode() == 200 || connection.getResponseCode() == 204) {
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			log.error(e.toString());
			logarMensagemErro(connection);
			return false;
		}
	}

	public boolean sendPost(String requestUrl) throws IOException {
		HttpURLConnection connection = null;
		try {
			URL url = new URL(requestUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Accept", "application/json");
			OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
			osw.flush();
			osw.close();
			logarMensagem("POST", requestUrl, connection.getOutputStream().toString(), connection.getResponseCode());
			if (connection.getResponseCode() == 200 || connection.getResponseCode() == 202 || connection.getResponseCode() == 204) {
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			log.error(e.toString());
			logarMensagemErro(connection);
			return false;
		}
	}

	public boolean retrySendPost(String requestUrl) throws IOException {
		boolean resultado;
		for (int retries = 0; retries < 5; retries++) {
			if (retries > 0) {
				log.info("Tentativa: " + retries + "/5");
			}
			resultado = sendPost(requestUrl);
			if (resultado) {
				return true;
			}
		}
		log.error("Todas as tentativas falharam");
		return false;
	}
	
	public boolean retrySendPost(String requestUrl, String payload) throws IOException {
		boolean resultado;
		for (int retries = 0; retries < 5; retries++) {
			if (retries > 0) {
				log.info("Tentativa: " + retries + "/5");
			}
			resultado = sendPost(requestUrl, payload);
			if (resultado) {
				return true;
			}
		}
		log.error("Todas as tentativas falharam");
		return false;
	}

	public boolean sendPut(String requestUrl, String data) throws IOException {
		HttpURLConnection connection = null;
		try {
			URL url = new URL(requestUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("PUT");
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Accept", "application/json");
			OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
			osw.write(data);
			osw.flush();
			osw.close();
			logarMensagem("PUT", requestUrl, connection.getOutputStream().toString(), connection.getResponseCode());
			if (connection.getResponseCode() == 200 || connection.getResponseCode() == 204) {
				return true;
			} else {
				log.error("Erro: " + connection.getErrorStream());
				log.error("Codigo do erro: " + connection.getResponseCode());
				logarMensagemErro(connection);
				return false;
			}
		} catch (IOException e) {
			log.error(e.toString());
			logarMensagemErro(connection);
			return false;
		}
	}
	
	public boolean sendPut(String requestUrl, String data, String cpfUsuario, String codUsuario) throws IOException {
		HttpURLConnection connection = null;
		try {
			String cookie = ".=" + codUsuario + "|" + cpfUsuario;
			URL url = new URL(requestUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("PUT");
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Accept", "application/json");
			if (!codUsuario.equals("") || !cpfUsuario.equals("")) {
				connection.setRequestProperty("Cookie", cookie);
			}
			OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
			osw.write(data);
			osw.flush();
			osw.close();
			logarMensagem("PUT", requestUrl, connection.getOutputStream().toString(), connection.getResponseCode());
			if (connection.getResponseCode() == 200 || connection.getResponseCode() == 204) {
				return true;
			} else {
				log.error("Erro: " + connection.getErrorStream());
				log.error("Codigo do erro: " + connection.getResponseCode());
				logarMensagemErro(connection);
				return false;
			}
		} catch (IOException e) {
			log.error(e.toString());
			logarMensagemErro(connection);
			return false;
		}
	}

	public boolean retrySendPut(String requestUrl, String data) throws IOException {
		boolean resultado;
		for (int retries = 0; retries < 5; retries++) {
			if (retries > 0) {
				log.info("Tentativa: " + retries + "/5");
			}
			resultado = sendPut(requestUrl, data);
			if (resultado) {
				return true;
			}
		}
		log.error("Todas as tentativas falharam");
		return false;
	}
	
	public boolean sendValidaGet(String requestUrl, Boolean logarMensagem) throws IOException {
		HttpURLConnection connection = null;
		String output = null;
		String saida = null;
		boolean retorno  = false;
		try {
			URL url = new URL(requestUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			connection.setRequestProperty("Accept", "application/json");
			BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
			while ((output = br.readLine()) != null) {
				saida += output;
			}
			if(logarMensagem)logarMensagem("GET", requestUrl, saida, connection.getResponseCode());
			connection.disconnect();
			
			if (connection.getResponseCode() == 200) {
				retorno = true;
			}
			
		} catch (Exception e) {
			log.error(e.toString());
			logarMensagemErro(connection);
			return retorno;
		}
		return retorno;
	}

	public String sendGet(String requestUrl, Boolean logarMensagem) throws IOException {
		HttpURLConnection connection = null;
		String output = "";
		String saida = "";
		try {
			URL url = new URL(requestUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			connection.setRequestProperty("Accept", "application/json");
			BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
			while ((output = br.readLine()) != null) {
				saida += output;
			}
			if(logarMensagem)logarMensagem("GET", requestUrl, saida, connection.getResponseCode());
			connection.disconnect();
			
		} catch (Exception e) {
			log.error(e.toString());
			logarMensagemErro(connection);
//			throw new RuntimeException(e.getMessage());
			return "";
		}
		return saida;
	}
	
	public boolean sendDelete(String requestUrl) throws IOException {
		HttpURLConnection connection = null;
		try {
			URL url = new URL(requestUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("DELETE");
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Accept", "application/json");
			OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
			osw.flush();
			osw.close();
			logarMensagem("POST", requestUrl, connection.getOutputStream().toString(), connection.getResponseCode());
			if (connection.getResponseCode() == 200 || connection.getResponseCode() == 202 || connection.getResponseCode() == 204) {
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			log.error(e.toString());
			logarMensagemErro(connection);
			return false;
		}
	}

	public void logarMensagem(String metodo, String url, String outputStream, int responseCode) {
        String mensagem = "";
        if (metodo.contains("GET")) {
            mensagem = "Retorno " + metodo + ": " + outputStream;
        } else if (outputStream.toString().contains("base64") || outputStream.toString().contains(".jpg")) {
            mensagem = "Enviando " + metodo + ": " + "doc base 64";
        } else {
            mensagem = "Enviando " + metodo + ": " + outputStream;
        }

        String[][] data = {
                { "Endereço: " + url },
                { mensagem },
                { "Codigo de Retorno " + metodo + ": " + responseCode }
                };
        Markup m = MarkupHelper.createTable(data);
        if (responseCode == 200 || responseCode == 202 || responseCode == 204) {
            report.logMensagem(Status.PASS, m);
            for(int i=0; i<data.length; i++) {
                log.info(data[i][0].toString());
            }
        } else {
            report.logMensagem(Status.ERROR, m);
            for(int i=0; i<data.length; i++) {
                log.error(data[i][0].toString());
            }
        }
        FrameworkUtils.sleeps(3);
    }
public void logarMensagem(String metodo, String url, String outputStream, String mobileId, int responseCode) {
        String mensagem = "";
        if (metodo.contains("GET")) {
            mensagem = "Retorno " + metodo + ": " + outputStream;
        } else if (outputStream.toString().contains("base64") || outputStream.toString().contains(".jpg")) {
            mensagem = "Enviando " + metodo + ": " + "doc base 64";
        } else {
            mensagem = "Enviando " + metodo + ": " + outputStream;
        }

        String[][] data = {
                { "Endereço: " + url },
                { mensagem },
                {"Mobile ID: " + mobileId },
                { "Codigo de Retorno " + metodo + ": " + responseCode }
                };
        Markup m = MarkupHelper.createTable(data);
        if (responseCode == 200 || responseCode == 202 || responseCode == 204) {
            report.logMensagem(Status.PASS, m);
            for(int i=0; i<data.length; i++) {
                log.info(data[i][0].toString());
            }
        } else {
            report.logMensagem(Status.ERROR, m);
            for(int i=0; i<data.length; i++) {
                log.error(data[i][0].toString());
            }
        }
        FrameworkUtils.sleeps(3);
    }
public void logarMensagemErro(HttpURLConnection connection) throws IOException {
        try {
            InputStream inputStream = connection.getErrorStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder response = new StringBuilder();
            String currentLine;
            while ((currentLine = in.readLine()) != null)
                response.append(currentLine);
            in.close();
            JSONObject json = new JSONObject(response.toString());
            
            String[][] data = {
                    { "Error Stream: " + connection.getErrorStream() },
                    { "Response menssage: " + connection.getResponseMessage() },
                    { "Codigo do erro: " + json.getString("codigo") },
                    { "Tipo do erro: " + json.getString("tipo") },
                    { "Motivo do erro: " + json.getString("motivo") },
                    { "Mensagem do erro: " + json.getString("mensagem")},
                    { "Detalhes do erro: " + json.getString("detalhes")}
                    };
            Markup m = MarkupHelper.createTable(data);
            report.logMensagem(Status.ERROR, m);
            for(int i=0; i<data.length; i++) {
                log.error(data[i][0].toString());
            }
        } catch (JSONException e) {
            report.logMensagem(Status.ERROR, e.getMessage());
            log.error(e.getMessage());
        }
    }
	
}
