package net.serenitybdd.screenplay.actions.selectactions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.targets.Target;
import net.thucydides.core.annotations.Step;

import java.util.Arrays;

public class SelectByValueFromTarget implements Performable {
    private Target target;
    private String[] values;

    public SelectByValueFromTarget() {}

    public SelectByValueFromTarget(Target target, String... values) {
        this.target = target;
        this.values = values;
    }

    @Step("{0} selects #value in #target")
    public <T extends Actor> void performAs(T theUser) {
        for(String value : values) {
            target.resolveFor(theUser).selectByValue(value);
        }
    }


}
