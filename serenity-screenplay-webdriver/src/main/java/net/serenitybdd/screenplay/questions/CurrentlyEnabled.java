package net.serenitybdd.screenplay.questions;

import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.core.pages.WebElementState;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;
import static net.serenitybdd.screenplay.questions.LabelledQuestion.answer;
import static net.serenitybdd.screenplay.questions.LabelledQuestion.answerEach;

public class CurrentlyEnabled {

    public static Question<Boolean> of(Target target) {
        return answer(target.getName() + " is curently enabled for", actor -> matches(target.resolveAllFor(actor)));
    }

    public static Question<Boolean> of(By byLocator) {
        return answer(byLocator + " is curently enabled for", actor -> matches(BrowseTheWeb.as(actor).findAll(byLocator)));
    }

    public static Question<Boolean> of(String locator) {
        return answer(locator + " is curently enabled for", actor -> matches(BrowseTheWeb.as(actor).findAll(locator)));
    }

    public static Question<List<Boolean>> ofEach(Target target) {
        return answerEach(target.getName() + " is curently enabled for",
                actor -> target.resolveAllFor(actor)
                    .stream()
                    .map(element -> matches(singletonList(element)))
                    .collect(Collectors.toList()));
    }

    public static Question<List<Boolean>> ofEach(By byLocator) {
        return answerEach(byLocator + " is curently enabled for",
                actor -> BrowseTheWeb.as(actor).findAll(byLocator)
                .stream()
                .map(element -> matches(singletonList(element)))
                .collect(Collectors.toList()));
    }

    public static Question<List<Boolean>> ofEach(String locator) {
        return answerEach(locator + " is curently enabled for",
                actor -> BrowseTheWeb.as(actor).findAll(locator)
                .stream()
                .map(element -> matches(singletonList(element)))
                .collect(Collectors.toList()));
    }

    private static boolean matches(List<WebElementFacade> elements) {
        return elements.stream()
                .findFirst()
                .map(WebElementState::isCurrentlyEnabled)
                .orElse(false);
    }

}
