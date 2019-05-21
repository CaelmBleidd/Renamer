import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Expected one argument, found " + args.length);
            return;
        }

        if (args[0] == null) {
            System.err.println("Required non-null argument.");
            return;
        }

        if (!Files.exists(Paths.get(args[0]))) {
            System.err.println("Path " + args[0] + " doesn't exist.");
            return;
        }

        Path path = Paths.get(args[0]);

        Renamer renamer = new Renamer();
        renamer.process(path);
    }
}
