package Base;

import org.testng.annotations.BeforeSuite;

import utils.EnvironmentDetails;
import utils.TestDataUtils;

public class BaseTest {
	 @BeforeSuite
	    public void beforeSuite() {
	        EnvironmentDetails.loadProperties();
	        TestDataUtils.loadProperties();
	    }

}
