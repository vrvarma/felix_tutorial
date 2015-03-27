package examples.scrambler;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class TestFlexJson implements org.osgi.framework.BundleActivator {
    private static final Logger LOGGER = LogManager.getLogger(TestFlexJson.class);
    private String testStr;
    private static JSONSerializer serializer;
    private static JSONDeserializer<TestFlexJson> deserializer;

    static {
	serializer = new JSONSerializer();
	// /serializer.exclude("*.class");
	deserializer = new JSONDeserializer<TestFlexJson>();
	deserializer.use("values", TestFlexJson.class);
    }

    public TestFlexJson() {
	setTestStr("testStr");
    }

    @Override
    public void start(org.osgi.framework.BundleContext context)
	    throws Exception {
	try {
	    System.out.println(serializer.serialize(new TestFlexJson()));
	    LOGGER.debug(serializer.serialize(new TestFlexJson()));
	} catch (Exception e) {
	    System.out.println("Exception deserializing: " + e.getMessage());
	    LOGGER.error("Exception deserializing: " + e.getMessage());
	}
    }

    @Override
    public void stop(org.osgi.framework.BundleContext context) throws Exception {
    }

    public String getTestStr() {
	return testStr;
    }

    public void setTestStr(String testStr) {
	this.testStr = testStr;
    }
}