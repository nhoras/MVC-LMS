package ru.mts.teta.validator;

import org.junit.jupiter.api.Test;
import ru.mts.teta.annotation.TitleLanguage;

import static org.junit.jupiter.api.Assertions.*;

class TitleCaseCheckerTest {
    private static final TitleCaseChecker titleCaseChecker = new TitleCaseChecker();

    private static final String[] RIGHT_RUS_TITLES = {
            "Заголовок", "Два слова", "Несколько слов подряд", "Кавычки в \"середине\" предложения",
            "\"Кавычки\" в начале предложения", "Кавычки в конце \"предложения\"", "Апостроф в конце заголовка'",
            "П", "П п п", "Жанна д'арк"
    };

    private static final String[] WRONG_RUS_TITLES = {
            "", null, " ", "   ","маленькая буква", "Запя,тая", "Всего одна \"кавычка", "Кавычка в сере\"дине слова",
            "С Большой Буквы Каждое Слово", "123", "'Апостроф снаружи слова", "Запрещенка\nне\tпройдет\rсовсем",
            "Два  пробела между словами", " пробелы в начале и конце ", "   пробелы   ",
            "Рyssкие букvы", "Двоеточие не на :месте", "Больше,: пунктуации", "Пунктуация такая:, пунктуация",
            "Запятая в конце заголовка,", "Двоеточие в конце заголовка:", "Пробел ", " Пробел"
    };

    private static final String[] RIGHT_ENG_TITLES = {
            "Title", "Title Title", "Title or Title", "And the Title", "The Title, the Title",
            "Quotes \"In the Midle\" Of Title", "\"The Quotes\" In the Beginning",
            "Quotes In \"the End\"", "Apostrophe'", "Zhanna D'ark", "P P P", "P", "And"
    };

    private static final String[] WRONG_ENG_TITLES = {
            "", null, " ", "lower case", "Com,ma", "Only One \"Quote", "Qu\"ote",
            "the Title", "123", "'Apostrophe", "Forbidden\nSpaces\tTabs\rLines",
            "Two  Spaces", " Additional Spaces ", "   Spaces   ",
            "London Is a Capitaл Oф Great Британ", "Colons :Crazy", "More,: Punctuation", "Punctuation Gonna:, Mad",
            "Comma In the End Of Sentence,", "Colons In the End:", "Space ", " Space"
    };

    @Test
    public void rightRussianTitle() {
        titleCaseChecker.setLang(TitleLanguage.RU);
        for(String string:RIGHT_RUS_TITLES) {
            assertTrue(titleCaseChecker.isValid(string, null), string);
        }
    }

    @Test
    public void wrongRussianTitle() {
        titleCaseChecker.setLang(TitleLanguage.RU);
        for(String string:WRONG_RUS_TITLES) {
            assertFalse(titleCaseChecker.isValid(string, null), string);
        }
        assertFalse(titleCaseChecker.isValid("English Title", null), "English Title");
    }

    @Test
    public void rightEnglishTitle() {
        titleCaseChecker.setLang(TitleLanguage.EN);
        for(String string:RIGHT_ENG_TITLES) {
            assertTrue(titleCaseChecker.isValid(string, null), string);
        }
    }

    @Test
    public void wrongEnglishTitle() {
        titleCaseChecker.setLang(TitleLanguage.EN);
        for(String string:WRONG_ENG_TITLES) {
            assertFalse(titleCaseChecker.isValid(string, null), string);
        }
        assertFalse(titleCaseChecker.isValid("Русский заголовок", null), "Русский заголовок");
    }

    @Test
    public void rightAnyTitle() {
        titleCaseChecker.setLang(TitleLanguage.ANY);
        for(String string:RIGHT_RUS_TITLES) {
            assertTrue(titleCaseChecker.isValid(string, null), string);
        }
        for(String string:RIGHT_ENG_TITLES) {
            assertTrue(titleCaseChecker.isValid(string, null), string);
        }
    }

    @Test
    public void wrongAnyTitle() {
        titleCaseChecker.setLang(TitleLanguage.ANY);
        for(String string:WRONG_RUS_TITLES) {
            assertFalse(titleCaseChecker.isValid(string, null), string);
        }
        for(String string:WRONG_ENG_TITLES) {
            assertFalse(titleCaseChecker.isValid(string, null), string);
        }
    }
}