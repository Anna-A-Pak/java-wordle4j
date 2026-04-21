package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public class WordleDictionary {

    private List<String> words;

    public WordleDictionary(List<String> words) {
        this.words = words;
    }

    public void normaliseDictionary(int lengthWord, PrintWriter pw) throws DictionaryIsEmpty {
        pw.println("Program log:");
        pw.println("Попытка нормализовать словарь");

        if (words.isEmpty()) {
            throw new DictionaryIsEmpty("Словарь пустой!");
        }
        for (int i = 0; i < words.size(); i++) {
            String word = words.get(i);
            if (word.trim().length() == lengthWord && !word.isBlank()) {
                word = word.replace("ё", "е");
                word = word.toLowerCase();
            } else {
                words.remove(i);
                i--;
            }
        }
        pw.println("Словарь нормализован" + "\n");
    }

    public String checkInputWord(String inputWord, int lengthWord, PrintWriter pw) throws InputWordIsBlank,
            IncorrectLength, InputWordNotRu,WordNotFoundInDictionary, DictionaryIsEmpty {
        pw.println("Game log:");
        pw.println("Проверка введенного слова на общую корректность");
        boolean wordInDictionary = false;

        if (inputWord.isBlank()) {
            throw new InputWordIsBlank("Неверный ввод. Слово должно быть без пробелов");
        } else {
            inputWord = inputWord.trim().toLowerCase().replace("ё", "е");
        }
        if (inputWord.length() != lengthWord) {
            throw new IncorrectLength("Неверный ввод. Слово должно быть из " + lengthWord + " букв");
        }
        if (!inputWord.matches("[а-я]+")) {
            throw new InputWordNotRu("Неверный ввод. Для ввода слова необходимо использовать кириллицу");
        }

        for (String word : words) {
            if (word.equals(inputWord)) {
                wordInDictionary = true;
                break;
            }
        }
        if (!wordInDictionary) {
            throw new WordNotFoundInDictionary("Введенное слово не найдено в словаре");
        }

        pw.println("Проверка слова прошла успешно" + "\n");
        pw.println("Введено слово: " + inputWord);
        return inputWord;
    }

    public String compareToAnswer(String inputWord, String answer, int lengthWord, PrintWriter pw,
                                   Map<Integer, Map<String, Character>> letters) {
        StringBuilder sb = new StringBuilder(lengthWord);
        pw.println("Выполняется сравнение введенного слов");
        if (answer.equals(inputWord)) {
            pw.println("Сравнение слов выполнено. Слово отгадано");
            return inputWord;
        }
        for (int i = 0; i < inputWord.length(); i++) {
            Map<String, Character> letter = new HashMap<>();
            for (int j = 0; j < answer.length(); j++) {
                if (inputWord.charAt(i) == answer.charAt(j)) {
                    if (i == j) {
                        sb.replace(i, i + 1,"+");
                        letter.put("+", inputWord.charAt(i));
                        break;
                    } else {
                        sb.replace(i, i + 1,"^");
                        letter.put("^", inputWord.charAt(i));
                        break;
                    }
                } else if (j + 1 == answer.length()) {
                    sb.replace(i, i + 1, "-");
                    letter.put("-", inputWord.charAt(i));
                }
            }
            letters.put(i, letter);
        }
        pw.println("Сравнение слов выполнено. Слово не отгадано. Графическая подсказка: " + sb.toString());
        return sb.toString();
    }

    public void removeWordsWithLetterInWrongPosition(int i, char ch, List<String> dictionary) {
        dictionary.removeIf(word -> (word.charAt(i) == ch));
        dictionary.removeIf(word -> (!word.contains(String.valueOf(ch))));
    }

    public void removeWordsWithLettersNotInAnswer(char ch, List<String> dictionary) {
        String letter = String.valueOf(ch);
        dictionary.removeIf(word -> (word.contains(letter)));
    }

    public void removeWordsWithoutLetterAtPosition(int i, char ch, List<String> dictionary) {
        dictionary.removeIf(word -> (word.charAt(i) != ch));
    }

    public List<String> getWords() {
        return words;
    }
}
