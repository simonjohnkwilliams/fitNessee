package test.fitnesse.junit;

import org.junit.runner.RunWith;
import fitnesse.junit.FitNesseRunner;

@RunWith(FitNesseRunner.class)
//how can i find where this is looking for the files?
@FitNesseRunner.Suite("FrontPage.TestSuiteForBig.GenericMQFixture")
@FitNesseRunner.FitnesseDir(".")
@FitNesseRunner.OutputDir("./build/fitnesse-results")
public class InterfaceTest {
}
