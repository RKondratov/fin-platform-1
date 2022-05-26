import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.logging.Logger;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Main {
    private static final Logger log = Logger.getLogger(Main.class.getName());
    private static final Charset DEFAULT_CODE = UTF_8;
    public static final String FILE = "\\file%s.txt";

    public static void main(String[] args) throws IOException {
        final Scanner in = new Scanner(System.in);
        System.out.print("Set path to directory: ");
        String root = in.nextLine();
        if (!Files.exists(Paths.get(root))) {
            log.warning("No such directory. The program shuts down");
            return;
        }
        final Set<Path> tree = getFileTree(root);
        final Path testFile = Files.createFile(Paths.get(root + String.format(FILE, new Date().getTime())));
        final List<String> lines = new ArrayList<>();
        tree.forEach(t -> {
            if (Files.isReadable(t)) {
                try {
                    lines.addAll(Files.readAllLines(t, DEFAULT_CODE));
                } catch (IOException e) {
                    log.warning(String.format("File %s charset isn't %s", t.toAbsolutePath(), DEFAULT_CODE.name()));
                }
            }
        });
        Files.write(testFile, lines, StandardOpenOption.APPEND);
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

    private static String getNameWithoutExtension(Path path) {
        String fileName = path.toFile().getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
    }

    private static String getFileExtension(String fullName) {
        String fileName = new File(fullName).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }
}