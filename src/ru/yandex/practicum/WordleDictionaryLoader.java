package ru.yandex.practicum;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class WordleDictionaryLoader {
    private String nameFile;

    public WordleDictionaryLoader(String nameFile) {
        this.nameFile = nameFile;
    }

    public WordleDictionary readFile(WordleDictionary wordleDictionary, PrintWriter pw)  {
        pw.println("Program log:");
        pw.println("Попытка чтения файла " + nameFile + " со словарем");
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(nameFile,
                StandardCharsets.UTF_8))) {
            while (bufferedReader.ready()) {
                wordleDictionary.getWords().addLast(bufferedReader.readLine());
            }
        } catch (IOException e) {
            pw.println(e.getMessage());
            throw new RuntimeException("Ошибка чтения файла");
        }

        pw.println("Успешное чтение файла " + nameFile + "\n");
        return wordleDictionary;
    }
}
