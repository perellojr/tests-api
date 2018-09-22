package maps;

import java.util.Arrays;

/**
 * Enum Exemplo json cep
 */
public enum ExemploCepEnum {
	
	CEP("cep"),
	LOGRADOURO("logradouro"),
	COMPLEMENTO("complemento"),
	BAIRRO("bairro"),
	LOCALIDADE("localidade"),
	UF("uf"),
	UNIDADE("unidade"),
	IBGE("ibge"),
	GIA("gia");
	
	private String fields;
	
	private ExemploCepEnum(String fields) {
		this.fields = fields;
	}
	
	public static String[] getNames(Class<? extends Enum<?>> e) {
	    return Arrays.toString(e.getEnumConstants()).replaceAll("^.|.$", "").split(", ");
	}

	@Override
	public String toString() {
		return this.fields;
	}
}