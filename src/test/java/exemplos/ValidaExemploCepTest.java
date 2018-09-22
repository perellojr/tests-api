package exemplos;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import cucumber.api.testng.AbstractTestNGCucumberTests;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/exemplos/ValidaExemploCep.feature", glue = { "" })
public class ValidaExemploCepTest extends AbstractTestNGCucumberTests {

}
