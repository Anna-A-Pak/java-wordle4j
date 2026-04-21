package ru.yandex.practicum;


import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public class Wordle {
    public static final String WORDS_FILE = "words_ru.txt";
    public static final int NUMBER_OF_ATTEMPTS = 6;
    public static final int LENGTH_WORD = 5;

    public static void main(String[] args) {
        Random random = new Random();
        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
        WordleDictionaryLoader wordleDictionaryLoader;
        WordleDictionary wordleDictionary = new WordleDictionary(new LinkedList<>());
        WordleGame wordleGame;

        try (PrintWriter printWriter = new PrintWriter(new FileWriter("log.txt", StandardCharsets.UTF_8))) {
            try {
                wordleDictionaryLoader = new WordleDictionaryLoader(WORDS_FILE);
                wordleDictionary = wordleDictionaryLoader.readFile(wordleDictionary, printWriter);
                wordleDictionary.normaliseDictionary(LENGTH_WORD, printWriter);
                int value = random.nextInt(wordleDictionary.getWords().size());
                String answer = wordleDictionary.getWords().get(value);
                //answer = "герой";
                wordleGame = new WordleGame(answer, NUMBER_OF_ATTEMPTS, wordleDictionary);
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
                        inputWord = wordleGame.playGame(inputWord, LENGTH_WORD, printWriter, random);
                        printWriter.println("Количество оставшихся попыток: " + wordleGame.getSteps() + "\n");
                        if (answer.equals(inputWord)) {
                            continuePlay = false;
                            System.out.println("Победа!");
                        } else if (wordleGame.getSteps() == 0) {
                            continuePlay = false;
                            System.out.println(answer);
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
