package steps;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Assert;

import com.aventstack.extentreports.Status;

import cucumber.api.DataTable;
import cucumber.api.java.pt.Dado;
import cucumber.api.java.pt.E;
import cucumber.api.java.pt.Entao;
import cucumber.api.java.pt.Quando;
import data.ExemploCepData;
import definitions.ExemploCepDefinitions;
import utils.FrameworkUtils;
import utils.RESTUtils;
import utils.ReportUtils;

@SuppressWarnings("rawtypes")
public class ExemploCepSteps {
	ExemploCepDefinitions exemploCepDefinitions = new ExemploCepDefinitions();
	ExemploCepData exemploCepData = new ExemploCepData();
	FrameworkUtils frameworkUtils = new FrameworkUtils();
	RESTUtils restUtils = new RESTUtils();
	Logger log = LogManager.getLogger(this.getClass());
	ReportUtils report = new ReportUtils();
	String url = null;
	String cep = null;
	
	@Dado("^que acesse a api cep$")
	public void acessaApiSolicitada(DataTable credenciais) throws Throwable {
		report.logMensagem(Status.INFO, "Dado que acesse a api cep");
		List<Map<String,String>> data = credenciais.asMaps(String.class,String.class);
		cep = data.get(0).get("cep");
		url = frameworkUtils.loadFromPropertiesFile("application.properties", data.get(0).get("api")) + cep + "/json/";
		Assert.assertTrue("A api [" + data.get(0).get("api") + "] não pode responder a requisicao realizada, verifique!", restUtils.sendValidaGet(url, true));		
	}
	
	@E("^que gere a massa de dados para api cep")
	public void geraMassaApiCep(DataTable credenciais) throws Throwable {
		report.logMensagem(Status.INFO, "E que gere a massa de dados para api cep");
		exemploCepData.gerarMassaData(credenciais);
		
	}

	@Quando("^valido a existencia dos campos da api cep$")
	public void validoExistenciaCamposHierarquiaComercial() throws Throwable {
		report.logMensagem(Status.INFO, "Quando valido a existencia dos campos da api cep");
		Assert.assertTrue("A validacao da existencia dos campos falhou, por favor verifique", exemploCepDefinitions.validaExistenciaCamposCep(new JSONObject(restUtils.sendGet(url, false))));
	}
	
	@Entao("^valido a integridade dos dados dos campos da api cep$")
	public void validoDadosCamposApiHierarquiaComercial() throws Throwable {
		report.logMensagem(Status.INFO, "Entao valido a integridade dos dados dos campos da api cep");
		Assert.assertTrue("A validacao da integridade dos campos falhou, por favor verifique", exemploCepDefinitions.validaIntegridadeCamposCep(new JSONObject(restUtils.sendGet(url, false))));
	}
}
