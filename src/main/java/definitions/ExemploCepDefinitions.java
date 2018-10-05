package definitions;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.aventstack.extentreports.Status;

import maps.ExemploCepEnum;
import utils.FrameworkUtils;
import utils.RESTUtils;
import utils.ReportUtils;

public class ExemploCepDefinitions extends FrameworkUtils<Object>{

	RESTUtils restUtils = new RESTUtils();
	Logger log = LogManager.getLogger(this.getClass());
	ReportUtils report = new ReportUtils();

	
	public boolean validaExistenciaCamposCep(JSONObject jsonObject) throws JSONException, IOException {
		String[] camposApi = ExemploCepEnum.getNames(ExemploCepEnum.class);
		log.info("valida se todos os campos existem");
		report.logMensagem(Status.INFO, "valida se todos os campos existem");
		return validaExistenciaCamposApi(null, jsonObject, camposApi, true);	
	}
	
	@SuppressWarnings("unused")
	public Boolean validaIntegridadeCamposCep(JSONObject jsonObject) throws JSONException, IOException {
		Boolean retorno = null;
		
		String cep = jsonObject.getString(ExemploCepEnum.CEP.toString());
		String logradouro = jsonObject.getString(ExemploCepEnum.LOGRADOURO.toString());
		String complemento = jsonObject.getString(ExemploCepEnum.COMPLEMENTO.toString());
		String bairro = jsonObject.getString(ExemploCepEnum.BAIRRO.toString());
		String localidade = jsonObject.getString(ExemploCepEnum.LOCALIDADE.toString());
		String uf = jsonObject.getString(ExemploCepEnum.UF.toString());
		String unidade = jsonObject.getString(ExemploCepEnum.UNIDADE.toString());
		String ibge = jsonObject.getString(ExemploCepEnum.IBGE.toString());
		String gia = jsonObject.getString(ExemploCepEnum.GIA.toString());

		report.logMensagem(Status.INFO, "valida se os campos sensiveis estao vazios");
		retorno = validaIntegridadeCampos("validar_obrigatorio", ExemploCepEnum.CEP.toString(), cep, null, null, null, true);
		retorno = (retorno == false)? false : validaIntegridadeCampos("validar_obrigatorio", ExemploCepEnum.LOGRADOURO.toString(), logradouro, null, null, null, true);
		retorno = (retorno == false)? false : validaIntegridadeCampos("validar_obrigatorio", ExemploCepEnum.COMPLEMENTO.toString(), logradouro, null, null, null, true);
		retorno = (retorno == false)? false : validaIntegridadeCampos("validar_obrigatorio", ExemploCepEnum.BAIRRO.toString(), logradouro, null, null, null, true);
		retorno = (retorno == false)? false : validaIntegridadeCampos("validar_obrigatorio", ExemploCepEnum.LOCALIDADE.toString(), logradouro, null, null, null, true);
		retorno = (retorno == false)? false : validaIntegridadeCampos("validar_obrigatorio", ExemploCepEnum.UF.toString(), logradouro, null, null, null, true);
		retorno = (retorno == false)? false : validaIntegridadeCampos("validar_obrigatorio", ExemploCepEnum.UNIDADE.toString(), logradouro, null, null, null, true);
		retorno = (retorno == false)? false : validaIntegridadeCampos("validar_obrigatorio", ExemploCepEnum.IBGE.toString(), logradouro, null, null, null, true);
		retorno = (retorno == false)? false : validaIntegridadeCampos("validar_obrigatorio", ExemploCepEnum.GIA.toString(), logradouro, null, null, null, true);

		return retorno;	
	}

}
