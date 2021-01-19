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
    private static String initialPath = "C:\\Работа\\Export\\Записка расчётная\\Тестирование\\СД";
    private static ArrayList<Path> allProvenPaths = new ArrayList<>();
    private static String[] allParams = new String[57];
    private static ConnectionDB insertIntoDB = new ConnectionDB();

    public static void main(String[] args) throws IOException {
//        File initialPath = new File("S:\\a.senkevich\\");
//        getFolders(initialPath);
        printPath();
    }

    /*
        public static void getFolders(File folder) throws IOException {
            File[] folderEntries = folder.listFiles();
            for (File entry : folderEntries) {
                if (entry.isDirectory()) {
                    findLastModifiedFile(entry.getAbsolutePath());
                }
            }
        }

        public static void findLastModifiedFile(String pathFolder) throws IOException {
            Path dir = Paths.get(pathFolder);
            if (Files.isDirectory(dir)) {
                Optional<Path> opPath = Files.list(dir)
                        .filter(p -> !Files.isDirectory(p))
                        .filter(p -> p.getFileName().toString().startsWith("ROPC"))
                        .sorted((p1, p2) -> Long.valueOf(p2.toFile().lastModified())
                                .compareTo(p1.toFile().lastModified()))
                        .findFirst();

                if (opPath.isPresent()) {
                    getDataFromFile(opPath.get());
                }
            }
        }
    */
    private static void printPath() {
        try (Stream<Path> paths = Files.walk(Paths.get(initialPath))) {
            paths
                    .filter(p -> p.getFileName().toString().startsWith("ROPC"))
                    .sorted((p1, p2) -> Long.compare(p2.toFile().lastModified(), p1.toFile().lastModified()))
                    .forEach(Parsing::checkedPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void checkedPath(Path path) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        allParams[0] = "1";
        insertIntoDB.executeSQL(allParams);
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
}
