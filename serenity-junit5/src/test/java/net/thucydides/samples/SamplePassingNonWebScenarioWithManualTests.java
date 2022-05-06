package net.thucydides.samples;

import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.thucydides.core.annotations.Manual;
import net.thucydides.core.model.TestResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SerenityJUnit5Extension.class)
public class SamplePassingNonWebScenarioWithManualTests {
    
    @Test
    @Manual
    public void a_manual_test() {}

    @Test
    @Manual(result = TestResult.FAILURE)
    public void a_failing_manual_test() {}
}