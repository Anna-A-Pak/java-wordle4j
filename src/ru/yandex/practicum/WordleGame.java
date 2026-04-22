package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.*;

public class WordleGame {

    private String answer;
    private int steps;
    private WordleDictionary dictionary;
    private Map<Integer, Map<String, Character>> letters = new HashMap<>();
    private Map<Integer, Map<Integer, Map<String, Character>>> inputWords = new HashMap<>();
    private HashSet<String> helpWords = new HashSet<>();
    private List<String> dictionaryForHelp = new LinkedList<>();

    public WordleGame(String answer, int steps, WordleDictionary dictionary) {
        this.answer = answer;
        this.steps = steps;
        this.dictionary = dictionary;
    }

    public int getSteps() {
        return steps;
    }

    public String playGame(String inputWord, int lengthWord, PrintWriter pw) throws InputWordIsBlank, IncorrectLength,
            InputWordNotRu, WordNotFoundInDictionary {
        inputWord = dictionary.checkInputWord(inputWord, lengthWord, pw);
        steps--;
        return dictionary.compareToAnswer(inputWord, answer, lengthWord, pw, letters);
    }

    public String playGameGetHint(Random random, PrintWriter pw) {
        if (dictionaryForHelp.isEmpty()) {
            dictionaryForHelp.addAll(dictionary.getWords());
        }
        if (!letters.isEmpty()) {
            inputWords.put(steps, letters);
            for (Map.Entry<Integer, Map<Integer, Map<String, Character>>> entry : inputWords.entrySet()) {
                for (Map.Entry<Integer, Map<String, Character>> element : entry.getValue().entrySet()) {
                    for (Map.Entry<String, Character> letter : element.getValue().entrySet()) {
                        if (letter.getKey().equals("^")) {
                            dictionary.removeWordsWithLetterInWrongPosition(element.getKey(),
                                    letter.getValue(), dictionaryForHelp);
                        } else if (letter.getKey().equals("-")) {
                            dictionary.removeWordsWithLettersNotInAnswer(letter.getValue(),
                                    dictionaryForHelp);
                        } else if (letter.getKey().equals("+")) {
                            dictionary.removeWordsWithoutLetterAtPosition(element.getKey(),
                                    letter.getValue(), dictionaryForHelp);
                        }
                    }
                }
            }
        }
        return pickHint(random, dictionaryForHelp, pw);
    }

    private String pickHint(Random random, List<String> dictionaryForHelp, PrintWriter pw) {
        pw.println("Game log:");
        pw.println("Выполняется подбор подсказки");
        if (dictionaryForHelp.isEmpty()) {
            throw new RuntimeException("Подсказок больше нет");
        }
        int value = random.nextInt(dictionaryForHelp.size());
        String hintWord = dictionaryForHelp.get(value);
        while (helpWords.contains(hintWord)) {
            value = random.nextInt(dictionaryForHelp.size());
            hintWord = dictionaryForHelp.get(value);
        }
        helpWords.add(hintWord);
        pw.println("Подсказка: " + hintWord);
        return dictionaryForHelp.get(value);
    }
}
