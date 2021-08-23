package ru.mts.teta.validator;

import ru.mts.teta.annotation.*;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TitleCaseChecker implements ConstraintValidator<TitleCase, String> {

    private static Matcher matcher;
    private static final Pattern mainPattern = Pattern.compile("\"?[A-Z][a-z]*'?[a-z]*[,:\"]?");
    private static final Pattern conjPattern = Pattern.compile("\"?[a-z]*[,:\"]?");
    private static final Pattern upperCasePattern = Pattern.compile("\"?[А-ЯЁ][а-яё]*'?[а-яё]*[,:\"]?");
    private static final Pattern lowerCasePattern = Pattern.compile("\"?[а-яё]+'?[а-яё]*[,:\"]?");
    private static final String[] conjs = {"a", "but", "for", "or", "not", "the", "an", "and"};
    private TitleLanguage lang;

    public void setLang(TitleLanguage lang) {
        this.lang = lang;
    }

    @Override
    public void initialize(TitleCase constraintAnnotation) {
        lang = constraintAnnotation.lang();
    }

    @Override
    public boolean isValid(String title, ConstraintValidatorContext constraintValidatorContext) {
        if (lang == TitleLanguage.RU) {
            return checkRusTitle(title);
        }
        if (lang == TitleLanguage.EN) {
            return checkEngTitle(title);
        }
        if (lang == TitleLanguage.ANY) {
            return (checkAnyTitle(title));
        }
        return false;
    }

    private boolean commonChecks(String title) {
        if (title == null || title.isBlank()) {
            return false;
        }
        // check spaces in the begin and in the end of string
        if (!title.equals(title.trim())) {
            return false;
        }
        // check all spaces are single
        if (title.contains("  ")) {
            return false;
        }
        // check an even number of quotation marks
        if (title.chars().filter(c -> c == '"').count() % 2 == 1) {
            return false;
        }
        // check comma or colons in the end of title
        if (title.charAt(title.length() - 1) == ',' || title.charAt(title.length() - 1) == ':') {
            return false;
        }
        return true;
    }

    private boolean checkRusTitle(String title) {

        if (!commonChecks(title)) {
            return false;
        }
        // check each word:
        // - only Russian letters
        // - double quote only in the begin or/and it the end of word
        // - apostrophe only in the middle or in the end of word
        // - comma or colons only in the end of word
        String[] words = title.split(" ");
        // -- check first word with capital letter
        matcher = upperCasePattern.matcher(words[0]);
        if (!matcher.matches()) {
            return false;
        }
        // check other words are lowercase
        for (int i = 1; i < words.length; i++) {
            matcher = lowerCasePattern.matcher(words[i]);
            if (!matcher.matches()) {
                return false;
            }
        }
        return true;
    }

    private boolean checkEngTitle(String title) {

        if (!commonChecks(title)) {
            return false;
        }
        // check each word:
        // - only English letters
        // - double quote only in the begin or/and it the end of word
        // - apostrophe only in the middle or in the end of word
        // - comma or colons only in the end of word
        String[] words = title.split(" ");
        // -- check first word with capital letter
        matcher = mainPattern.matcher(words[0]);
        if (!matcher.matches()) {
            return false;
        }
        // -- check last word with capital letter
        if (words.length > 1) {
            matcher = mainPattern.matcher(words[words.length - 1]);
            if (!matcher.matches()) {
                return false;
            }
        }
        // -- if conjunctions isn't first or last word, it must be lower case
        for (int i = 1; i < words.length - 1; i++) {
            if (isConjunctions(words[i])) {
                matcher = conjPattern.matcher(words[i]);
            } else {
                matcher = mainPattern.matcher(words[i]);
            }
            if (!matcher.matches()) {
                return false;
            }
        }
        return true;
    }

    private boolean isConjunctions(String word) {
        StringBuilder stringBuilder = new StringBuilder(word);

        if (stringBuilder.charAt(0) == '"') {
            stringBuilder.deleteCharAt(0);
        }
        int end = stringBuilder.length() - 1;
        if (end > 0 && (stringBuilder.charAt(end) == '"' ||
                stringBuilder.charAt(end) == ',' || stringBuilder.charAt(end) == ':')) {
            stringBuilder.deleteCharAt(end);
        }
        for (String conj:conjs){
            if (stringBuilder.toString().equalsIgnoreCase(conj)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkAnyTitle(String title) {
        return checkEngTitle(title) || checkRusTitle(title);
    }
}
