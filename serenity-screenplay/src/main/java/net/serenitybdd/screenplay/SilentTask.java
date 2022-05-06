package net.serenitybdd.screenplay;

import net.serenitybdd.core.steps.Instrumented;

import java.util.function.Consumer;

import static java.util.Arrays.asList;

/**
 * Perform a task or sequence of tasks without having it appear in the reports.
 */
public class SilentTask {

    static <T extends Performable> SilentPerformableFunction where(Consumer<Actor> performableOperation) {
        return Instrumented.instanceOf(SilentPerformableFunction.class).withProperties(performableOperation);
    }

    public static <T extends Performable> AnonymousTask where(T... steps) {
        return Instrumented.instanceOf(AnonymousTask.class)
                           .withProperties("Anonymous task",  asList(steps)).withNoReporting();
    }
}
