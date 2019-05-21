import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;

public class Renamer {

    // In this method I think that ".java" and ".kt" are correct java and kotlin files, because idea recognizes them like that
    void process(Path path) {
        var ends = new HashSet<String>();
        ends.add(".java");
        ends.add(".kt");

        try (var stream = Files.walk(path)) {
            stream.filter(Files::isRegularFile)
                  .map(Path::toString)
                  .filter(fileName -> ends.stream().anyMatch(fileName::endsWith))
                  .forEach(this::renameFile);

        } catch (IOException e) {
            System.err.println("An error occurred during walking directory " + path);
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
