package ru.yandex.practicum;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Random;

class WordleTest {

    private static WordleGame wordleGame;
    private static PrintWriter printWriter = new PrintWriter(System.out);
    private static Random fixedRandom = new Random(3);

    @BeforeAll
    static void beforeAll() throws FileNotFoundException, IOException, DictionaryIsEmpty {
        WordleDictionary wordleDictionary = new WordleDictionary(new LinkedList<>());
        WordleDictionaryLoader wordleDictionaryLoader = new WordleDictionaryLoader("words_ru.txt");
        wordleDictionaryLoader.readFile(wordleDictionary, printWriter);
        wordleDictionary.normaliseDictionary(5, printWriter);
        String answer = "герой";
        wordleGame = new WordleGame(answer, 5, wordleDictionary);
    }

    @Test
    public void testGetLineHelpForIncorrectWord() throws InputWordIsBlank,
            IncorrectLength, InputWordNotRu, WordNotFoundInDictionary, DictionaryIsEmpty  {

        Assertions.assertEquals("-^+^+", wordleGame.playGame("порей", 5, printWriter,
                fixedRandom));

        Assertions.assertEquals("--^^-", wordleGame.playGame("дверь", 5, printWriter,
                fixedRandom));

        Assertions.assertEquals("-^-^^", wordleGame.playGame("ковёр", 5, printWriter,
                fixedRandom));
    }

    @Test
    public void testGetWordHelp() throws InputWordIsBlank,
            IncorrectLength, InputWordNotRu, WordNotFoundInDictionary, DictionaryIsEmpty {
        wordleGame.playGame("дверь", 5, printWriter, fixedRandom);
        wordleGame.playGame("сарай", 5, printWriter, fixedRandom);
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
    public void testIncorrectLength() throws InputWordIsBlank,
            InputWordNotRu, WordNotFoundInDictionary, DictionaryIsEmpty {
        try {
            wordleGame.playGame("сок", 5, printWriter, fixedRandom);
        } catch (IncorrectLength e) {
            Assertions.assertEquals("Неверный ввод. Слово должно быть из " + 5 + " букв",
                    e.getMessage());
        }
    }

    @Test
    public void testInputWordIsBlank() throws IncorrectLength,
            InputWordNotRu, WordNotFoundInDictionary, DictionaryIsEmpty {
        try {
            wordleGame.playGame("    ", 5, printWriter, fixedRandom);
        } catch (InputWordIsBlank e) {
            Assertions.assertEquals("Неверный ввод. Слово должно быть без пробелов",
                    e.getMessage());
        }
    }

    @Test
    public void testInputWordNotRu() throws IncorrectLength,
            InputWordIsBlank, WordNotFoundInDictionary, DictionaryIsEmpty {
        try {
            wordleGame.playGame("fj15g", 5, printWriter, fixedRandom);
        } catch (InputWordNotRu e) {
            Assertions.assertEquals("Неверный ввод. Для ввода слова необходимо использовать кириллицу",
                    e.getMessage());
        }
    }

    @Test
    public void testWordNotFoundInDictionary() throws IncorrectLength,
            InputWordIsBlank, InputWordNotRu, DictionaryIsEmpty {
        try {
            wordleGame.playGame("сорай", 5, printWriter, fixedRandom);
        } catch (WordNotFoundInDictionary e) {
            Assertions.assertEquals("Введенное слово не найдено в словаре",
                    e.getMessage());
        }
    }
}
