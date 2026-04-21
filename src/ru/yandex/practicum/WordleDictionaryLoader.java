package ru.yandex.practicum;

import java.io.*;
import java.nio.charset.StandardCharsets;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {
    private String nameFile;

    public WordleDictionaryLoader(String nameFile) {
        this.nameFile = nameFile;
    }

    public WordleDictionary readFile(WordleDictionary wordleDictionary, PrintWriter pw) throws FileNotFoundException,
            IOException {
        pw.println("Program log:");
        pw.println("Попытка чтения файла " + nameFile + " со словарем");
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(nameFile,
                StandardCharsets.UTF_8))) {
            while (bufferedReader.ready()) {
                wordleDictionary.getWords().addLast(bufferedReader.readLine());
            }
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(e.getMessage());
        } catch (IOException e) {
            pw.println(e.getMessage());
        }
        pw.println("Успешное чтение файла " + nameFile + "\n");
        return wordleDictionary;
    }
}
