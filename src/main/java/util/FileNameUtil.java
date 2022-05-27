package util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileNameUtil {
    public static String getNameWithoutExtension(Path path) {
        String fileName = path.toFile().getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
    }

    public static String getFileExtension(String fullName) {
        String fileName = new File(fullName).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

    public static boolean isExistsPath(String root) {
        try {
            return Files.exists(Paths.get(root));
        } catch (InvalidPathException e){
            return false;
        }
    }
}
