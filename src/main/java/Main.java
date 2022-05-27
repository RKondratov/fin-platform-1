import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.logging.Logger;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.*;
import static util.FileNameUtil.*;

public class Main {
    private static final Logger log = Logger.getLogger(Main.class.getName());
    private static final Charset DEFAULT_CODE = UTF_8;
    private static final String FILE = "\\file%s.txt";

    public static void main(String[] args) throws IOException {
        final Scanner in = new Scanner(System.in);
        System.out.print("Set path to directory: ");
        String root = in.nextLine();
        if (!isExistsPath(root)){
            log.warning("No such directory. The program shuts down");
            return;
        }
        final Set<Path> tree = getFileTree(root);
        final Path testFile = Files.createFile(Paths.get(root + String.format(FILE, new Date().getTime())));
        final List<String> lines = new ArrayList<>();
        tree.forEach(path -> {
            if (Files.isReadable(path)) {
                try {
                    lines.addAll(Files.readAllLines(path, DEFAULT_CODE));
                } catch (MalformedInputException e) {
                    log.warning(String.format("File %s charset isn't %s", path.toAbsolutePath(), DEFAULT_CODE.name()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        Files.write(testFile, lines, APPEND);
    }

    private static Set<Path> getFileTree(String root) throws IOException {
        final Set<Path> set = new TreeSet<>((path1, path2) -> {
            String name1 = getNameWithoutExtension(path1);
            String name2 = getNameWithoutExtension(path2);
            return name1.compareTo(name2);
        });
        final File file = new File(root);
        Files.walkFileTree(file.toPath(), new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
                if (getFileExtension(path.toString()).equals("txt"))
                    set.add(path.toAbsolutePath());
                return FileVisitResult.CONTINUE;
            }
        });
        return set;
    }
}