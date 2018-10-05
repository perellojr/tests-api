package setup;

import java.net.MalformedURLException;
import java.sql.SQLException;

import com.aventstack.extentreports.Status;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import utils.ReportUtils;

public class TestRule {
	
	ReportUtils report = new ReportUtils();

	public String nomeCenario;

	@Before
	public void beforeCenario(Scenario cenario) throws SQLException, MalformedURLException {
		ReportUtils.criarReport(cenario);
		nomeCenario = cenario.getName();
		report.logMensagem(Status.INFO, "Executando Cenario de Teste: " + nomeCenario);
	}

	@After
	public void afterCenario(Scenario cenario) {
		report.logMensagem(Status.INFO, "Finalizando Teste Automatizado");
		report.atualizaReport(cenario);
	}
}
