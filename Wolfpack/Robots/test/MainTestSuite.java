import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class MainTestSuite extends TestCase {
    
    public MainTestSuite(String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite("NewTestSuite");
        return suite;
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
