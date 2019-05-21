import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Renamer {

    // In this method I think that ".java" and ".kt" are correct java and kotlin files, because idea recognizes them like that
    void process(Path path) {
        try (var stream = Files.walk(path)) {
            stream.filter(Files::isRegularFile)
                  .map(Path::toString)
                  .filter(fileName -> fileName.endsWith(".java") || fileName.endsWith(".kt"))
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
