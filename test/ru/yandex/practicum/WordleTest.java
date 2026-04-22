package ru.yandex.practicum;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

class WordleTest {

    private static WordleGame wordleGame;
    private static PrintWriter printWriter = new PrintWriter(System.out);
    private static Random fixedRandom = new Random(3);

    @BeforeAll
    static void beforeAll() {
        WordleDictionary wordleDictionary = new WordleDictionary(new LinkedList<>());
        List<String> normalWords;
        WordleDictionaryLoader wordleDictionaryLoader = new WordleDictionaryLoader("words_ru.txt");
        wordleDictionaryLoader.readFile(wordleDictionary, printWriter);
        normalWords = wordleDictionary.normaliseDictionary(5, printWriter);
        WordleDictionary normalWordleDictionary = new WordleDictionary(normalWords);
        String answer = "герой";
        wordleGame = new WordleGame(answer, 5, normalWordleDictionary);
    }

    @Test
    public void testGetLineHelpForIncorrectWord() throws InputWordIsBlank, IncorrectLength, InputWordNotRu,
            WordNotFoundInDictionary  {

        Assertions.assertEquals("-^+^+", wordleGame.playGame("порей", 5, printWriter));

        Assertions.assertEquals("--^^-", wordleGame.playGame("дверь", 5, printWriter));

        Assertions.assertEquals("-^-^^", wordleGame.playGame("ковёр", 5, printWriter));
    }

    @Test
    public void testGetWordHelp() throws InputWordIsBlank, IncorrectLength, InputWordNotRu, WordNotFoundInDictionary {
        wordleGame.playGame("дверь", 5, printWriter);
        wordleGame.playGame("сарай", 5, printWriter);
        String wordHelp = wordleGame.playGameGetHint(fixedRandom, printWriter);

        Assertions.assertFalse(wordHelp.contains("д"));
        Assertions.assertFalse(wordHelp.contains("в"));
        Assertions.assertFalse(wordHelp.contains("c"));
        Assertions.assertFalse(wordHelp.contains("а"));
        Assertions.assertTrue(wordHelp.contains("е"));
        Assertions.assertTrue(wordHelp.contains("р"));
        Assertions.assertTrue(wordHelp.contains("й"));
        Assertions.assertEquals('р', wordHelp.charAt(2));
    }

    @Test
    public void testIncorrectLength() throws InputWordIsBlank, InputWordNotRu, WordNotFoundInDictionary {
        try {
            wordleGame.playGame("сок", 5, printWriter);
        } catch (IncorrectLength e) {
            Assertions.assertEquals("Неверный ввод. Слово должно быть из " + 5 + " букв",
                    e.getMessage());
        }
    }

    @Test
    public void testInputWordIsBlank() throws IncorrectLength, InputWordNotRu, WordNotFoundInDictionary {
        try {
            wordleGame.playGame("    ", 5, printWriter);
        } catch (InputWordIsBlank e) {
            Assertions.assertEquals("Неверный ввод. Слово должно быть без пробелов",
                    e.getMessage());
        }
    }

    @Test
    public void testInputWordNotRu() throws IncorrectLength, InputWordIsBlank, WordNotFoundInDictionary {
        try {
            wordleGame.playGame("fj15g", 5, printWriter);
        } catch (InputWordNotRu e) {
            Assertions.assertEquals("Неверный ввод. Для ввода слова необходимо использовать кириллицу",
                    e.getMessage());
        }
    }

    @Test
    public void testWordNotFoundInDictionary() throws IncorrectLength, InputWordIsBlank, InputWordNotRu {
        try {
            wordleGame.playGame("сорай", 5, printWriter);
        } catch (WordNotFoundInDictionary e) {
            Assertions.assertEquals("Введенное слово не найдено в словаре",
                    e.getMessage());
        }
    }
}
