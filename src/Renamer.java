import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.SQLOutput;

public class Renamer {

    public void process(Path path) {
        try {
            Files.walk(path)
                 .filter(Files::isRegularFile)
                 .map(Path::toString)
                 .filter(fileName -> fileName.endsWith(".java") || fileName.endsWith(".kt"))
                 .filter(fileName -> fileName.charAt(fileName.lastIndexOf("/") + 1) != '.')
                 .forEach(this::renameFile);
        } catch (IOException e) {
            System.err.println("An error occurred during walking directory " + path);
            System.err.println("Message: " + e.getMessage());
            System.err.println("Cause: " + e.getCause());
        }
    }

    private void renameFile(String path) {
        var file = new File(path);
        var newFile = new File(path + ".2019");
        var result = file.renameTo(newFile);
        if (!result)
            System.err.println("An error occurred during renaming file " + path);
        else
            System.out.println(newFile);
    }
}
