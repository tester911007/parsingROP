package parsing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Parsing {
    static String[] allParams = new String[57];

    public static void main(String[] args) {
        printPath();
    }

    private static void printPath() {
        try (Stream<Path> paths = Files.walk(Paths.get("C:\\Работа\\Export\\Записка расчётная\\Тестирование\\СД"))) {
            paths
                    .filter(p -> p.getFileName().toString().startsWith("ROPC"))
                    .forEach(Parsing::getDataFromFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void getDataFromFile(Path path) {
        System.out.println("\n\n" + path);
        try (BufferedReader reader = new BufferedReader(new FileReader(String.valueOf(path)))) {
            String line;
            int i = 1;
            while ((line = reader.readLine()) != null) {
                if (i > 4 && i < 19)
                    parseLine(line, i - 4);
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        allParams[0] = "1";
        printAllParams();
    }

    private static void parseLine(String line, int position) {
        String regex = "(\\s*)=(\\s*)([0-9.]+)";

        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(line);

        while (matcher.find()) {
            allParams[position] = (matcher.group(3));
            position += 14;
        }

    }

    private static void printAllParams() {
        for (String allParam : allParams) {
            System.out.println(allParam);
        }
    }
}
