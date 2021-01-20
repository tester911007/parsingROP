package parsing;

import connection.ConnectionDB;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Parsing {
    private static final String INITIAL_PATH = "C:\\Работа\\Export\\Записка расчётная\\Тестирование\\СД\\";
    private static ArrayList<Path> allProvenPaths = new ArrayList<>();
    private static String[] allParams = new String[57];
    private static ConnectionDB insertIntoDB = new ConnectionDB();

    public static void main(String[] args) {
        sortFilesByDate();
    }

    private static void sortFilesByDate() {
        try (Stream<Path> paths = Files.walk(Paths.get(INITIAL_PATH))) {
            paths
                    .filter(p -> p.getFileName().toString().startsWith("ROPC"))
                    .sorted((p1, p2) -> Long.compare(p2.toFile().lastModified(), p1.toFile().lastModified()))
                    .forEach(Parsing::checkPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Проверка наличия родительского пути в списке уже проверенных
    private static void checkPath(Path path) {
        if (!allProvenPaths.contains(path.getParent())) {
            allProvenPaths.add(path.getParent());
            getDataFromFile(path);
        }
    }

    private static void getDataFromFile(Path path) {
        System.out.println(path);
        try (BufferedReader reader = new BufferedReader(new FileReader(String.valueOf(path)))) {
            String line;
            int i = 1;
            while ((line = reader.readLine()) != null) {
                if (i > 4 && i < 19)
                    parseLine(line, i - 4);
                i++;
            }
            allParams[0] = "1";
            insertIntoDB.executeSQL(allParams);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Построчная обработка
    private static void parseLine(String line, int position) {
        String regex = "(\\s*)=(\\s*)([0-9.]+)";
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(line);

        while (matcher.find()) {
            allParams[position] = (matcher.group(3));
            position += 14;
        }
    }
}
