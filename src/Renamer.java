import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class Renamer {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Expected one argument, found " + args.length);
            return;
        }

        if (args[0] == null) {
            System.out.println("Required non-null argument.");
            return;
        }

        Path path = Paths.get(args[0]);
        try {
            Files.walk(path)
                 .map(Path::toString)
                 .filter(fileName -> fileName.endsWith(".java") | fileName.endsWith(".kt"))
                 .forEach(Renamer::renameFile);
        } catch (IOException e) {
            System.err.println("An error occurred while walking directory " + path);
            System.err.println("Message: " + e.getMessage());
        }
    }

    private static void renameFile(String path) {
        var file = new File(path);
        var result = file.renameTo(new File(path + ".2019"));
        if (!result)
            System.err.println("An error occurred while renaming file " + path);
    }
}
