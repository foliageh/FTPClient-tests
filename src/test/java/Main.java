import org.testng.TestNG;
import org.testng.collections.Lists;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        TestNG testNG = new TestNG();
        List<String> suites = Lists.newArrayList();
        suites.add("src/test/resources/testng.xml");
        testNG.setTestSuites(suites);
        testNG.run();
    }
}
