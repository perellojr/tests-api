package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import cucumber.api.Scenario;

public class ReportUtils{

	public static ExtentHtmlReporter htmlReporter;
	public static ExtentReports extentReport;
	public static ExtentTest extentTest;
	public static String diretorioReport;
	Logger log = LogManager.getLogger(this.getClass());

	public static void criarReport(Scenario cenario) {
		if (extentReport == null) {
			extentReport = new ExtentReports();
			String dir = System.getProperty("user.dir");
			FrameworkUtils.criarDiretorio(dir + "\\report");
			setDiretorioReport("./report/");
			FrameworkUtils.criarDiretorio(diretorioReport);
			htmlReporter = new ExtentHtmlReporter(diretorioReport + "\\report.html");
			extentReport.attachReporter(htmlReporter);
		}
		extentTest = extentReport.createTest(cenario.getName());
	}

	public void atualizaReport(Scenario cenario) {
		if (cenario.isFailed()) {
			extentTest.log(Status.ERROR, "Erro encontrado durante a execucao.");
		} else {
			extentTest.log(Status.PASS, "Cenario executado com sucesso.");
		}
		extentReport.flush();
	}

	public static ExtentTest getExtentTest() {
		return extentTest;
	}

	public static void setDiretorioReport(String diretorio) {
		diretorioReport = diretorio;
	}

	public static String getDiretorioReport() {
		return diretorioReport;
	}

	public void logMensagem(Status status, String mensagem) {
        log.info(mensagem);
        extentTest.log(status, mensagem);
        extentReport.flush();
    }

    public void logMensagem(Status status, Markup m) {
        extentTest.log(status, m);
        extentReport.flush();
    }
	
	
}
