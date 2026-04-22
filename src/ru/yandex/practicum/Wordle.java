package ru.yandex.practicum;


import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Wordle {
    public static final String WORDS_FILE = "words_ru.txt";
    public static final int NUMBER_OF_ATTEMPTS = 6;
    public static final int LENGTH_WORD = 5;

    public static void main(String[] args) {
        Random random = new Random();
        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
        WordleDictionaryLoader wordleDictionaryLoader;
        WordleDictionary wordleDictionary = new WordleDictionary(new LinkedList<>());
        List<String> normalWords;
        WordleGame wordleGame;

        try (PrintWriter printWriter = new PrintWriter(new FileWriter("log.txt", StandardCharsets.UTF_8))) {
            try {
                wordleDictionaryLoader = new WordleDictionaryLoader(WORDS_FILE);
                wordleDictionary = wordleDictionaryLoader.readFile(wordleDictionary, printWriter);
                normalWords = wordleDictionary.normaliseDictionary(LENGTH_WORD, printWriter);
                WordleDictionary normalWordleDictionary = new WordleDictionary(normalWords);
                int value = random.nextInt(normalWordleDictionary.getWords().size());
                String answer = normalWordleDictionary.getWords().get(value);
                wordleGame = new WordleGame(answer, NUMBER_OF_ATTEMPTS, normalWordleDictionary);
                boolean continuePlay = true;
                printWriter.println("Game log:");
                printWriter.println("Начало игры");
                printWriter.println("Загаданное слово: " + answer);
                printWriter.println("Количество попыток: " + NUMBER_OF_ATTEMPTS);
                while (continuePlay) {
                    System.out.println("Введите слово: ");
                    String inputWord = scanner.nextLine();
                    try {
                        if (inputWord.isEmpty()) {
                            inputWord = wordleGame.playGameGetHint(random, printWriter);
                            System.out.println(inputWord);
                        }
                        inputWord = wordleGame.playGame(inputWord, LENGTH_WORD, printWriter);
                        printWriter.println("Количество оставшихся попыток: " + wordleGame.getSteps() + "\n");
                        if (answer.equals(inputWord)) {
                            continuePlay = false;
                            System.out.println("Победа!");
                        } else if (wordleGame.getSteps() == 0) {
                            continuePlay = false;
                            System.out.println("Проигрыш! Загаданное слово: " + answer);
                        } else {
                            System.out.println(inputWord + " осталось попыток: " + wordleGame.getSteps() +
                                    "/" + NUMBER_OF_ATTEMPTS);
                        }

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        printWriter.println(e.getMessage());
                        writeLog(e, printWriter);
                    }
                }
                printWriter.println("Игра завершена");
            } catch (Exception e) {
                System.out.println(e.getMessage());
                printWriter.println(e.getMessage());
                writeLog(e, printWriter);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Program log: FileNotFoundException");
        } catch (IOException e) {
            System.out.println("Program log: IOException");
        }
    }

    public static void writeLog(Exception e, PrintWriter pw) {
        for (StackTraceElement stack : e.getStackTrace()) {
            pw.println("Класс: " + stack.getClassName() + ", " +
                    "метод: " + stack.getMethodName() + ", " +
                    "имя файла: " + stack.getFileName() + ", " +
                    "строка кода: " + stack.getLineNumber());
        }
        pw.println("");
    }
}
