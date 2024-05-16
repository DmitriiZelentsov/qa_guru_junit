import data.Language;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

@DisplayName("Тесты проверки сайта Самарского университета")
public class SelenideWebTest extends TestBase {

    @EnumSource(Language.class)
    @ParameterizedTest(name = "Для локали {0}")
    @DisplayName("Проверка блока Новости")
    void selenideSiteShouldDisplayCorrectText(Language language) {
        open("/");
        $$(".top-bar__text").get(2).click();
        $$(".top-bar__dropdown-item").find(text(language.name())).click();

        $(".news").shouldHave(text(language.description));
    }

    static Stream<Arguments> selenideSiteShouldDisplayCorrectButtons() {
        return Stream.of(
                Arguments.of(
                        Language.EN,
                        List.of("News & events", "Education", "Research", "About Us", "Information & service", "Student's life")
                ),
                Arguments.of(
                        Language.RU,
                        List.of("Абитуриенту", "Студенту", "Выпускнику", "Сотруднику", "Партнеру")
                )
        );
    }

    @MethodSource("selenideSiteShouldDisplayCorrectButtons")
    @ParameterizedTest(name = "Для локали {0}")
    @DisplayName("Проверка блока Навигации")
    void selenideSiteShouldDisplayCorrectButtons(Language language, List<String> expectedButtons) {
        open("/");
        $$(".top-bar__text").get(2).click();
        $$(".top-bar__dropdown-item").find(text(language.name())).click();

        $$(".navigation-secondary a").filter(visible)
                .shouldHave(texts(expectedButtons));
    }

    @CsvSource(value = {
            "Преподаватели , Сотрудники и преподаватели - Самарский университет",
            "Кафедры , Кафедры - Самарский университет"
    })
    @ParameterizedTest(name = "Для поискового запроса '{0}' в первой карточке должн быть результат: {1}")
    @DisplayName("Проверка поиска")
    void selenideSiteShouldSearch(String searchQuery, String expectedLink) {
        open("/");
        $$(".search a").get(2).click();
        $$(".search__inp").get(2).setValue(searchQuery).pressEnter();

        $(".gs-title a")
                .shouldHave(text(expectedLink));
    }

    @CsvFileSource(resources = "searchResult.csv")
    @ParameterizedTest(name = "Для поискового запроса '{0}' в первой карточке должн быть результат: {1}")
    @DisplayName("Проверка поиска из csv файла")
    void selenideSiteShouldSearchCsv(String searchQuery, String expectedLink) {
        open("/");
        $$(".search a").get(2).click();
        $$(".search__inp").get(2).setValue(searchQuery).pressEnter();

        $(".gs-title a")
                .shouldHave(text(expectedLink));
    }
}
