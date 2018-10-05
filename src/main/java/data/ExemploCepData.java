package data;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.aventstack.extentreports.Status;

import cucumber.api.DataTable;
import maps.ExemploCepEnum;
import utils.FrameworkUtils;
import utils.RESTUtils;
import utils.ReportUtils;

public class ExemploCepData extends FrameworkUtils<Object>{

	RESTUtils restUtils = new RESTUtils();
	Logger log = LogManager.getLogger(this.getClass());
	ReportUtils report = new ReportUtils();
	
	public JSONObject gerarMassaData(DataTable credenciais) throws JSONException {
		JSONObject retorno = new JSONObject();
		List<Map<String,String>> data = credenciais.asMaps(String.class,String.class);
		retorno.put(ExemploCepEnum.CEP.toString(), data.get(0).get("cep"));
		retorno.put(ExemploCepEnum.LOGRADOURO.toString(), data.get(0).get("logradouro"));
		retorno.put(ExemploCepEnum.COMPLEMENTO.toString(), data.get(0).get("complemento"));
		retorno.put(ExemploCepEnum.BAIRRO.toString(), data.get(0).get("bairro"));
		retorno.put(ExemploCepEnum.LOCALIDADE.toString(), data.get(0).get("localidade"));
		retorno.put(ExemploCepEnum.UF.toString(), data.get(0).get("uf"));
		retorno.put(ExemploCepEnum.UNIDADE.toString(), data.get(0).get("unidade"));
		retorno.put(ExemploCepEnum.IBGE.toString(), data.get(0).get("ibge"));
		retorno.put(ExemploCepEnum.GIA.toString(), data.get(0).get("gia"));
		report.logMensagem(Status.INFO, "Massa de dados Gerada:" + retorno);
		return retorno;
	}
	
}