import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class RenamerTest {
    private Renamer renamer;
    private File directory;

    @Before
    public void before() {
        renamer = new Renamer();
        directory = new File("testDirectory");
        directory.mkdir();
    }

    @After
    public void after() throws IOException {
        deleteDirectoryStream(directory.toPath());
    }

    @Test
    public void commonTest() throws IOException {
        List<String> fileNames = new ArrayList<>();
        List<String> expectedNames = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            String fileName = i + ".java";
            fileNames.add(fileName);
            expectedNames.add(fileName + ".2019");
        }
        for (int i = 0; i < 10; ++i) {
            String fileName = i + ".kt";
            fileNames.add(fileName);
            expectedNames.add(fileName + ".2019");
        }
        for (var name : fileNames) {
            assertTrue(new File(directory, name).createNewFile());
        }

        renamer.process(directory.toPath());
        List<String> foundNames = getFiles(directory);

        assertEquals(expectedNames.size(), foundNames.size());
        assertTrue(foundNames.containsAll(expectedNames) && expectedNames.containsAll(foundNames));
    }

    private List<String> getFiles(File directory) throws IOException {
        try (var stream = Files.walk(directory.toPath())) {
            return stream.filter(Files::isRegularFile)
                         .map(Path::toString)
                         .map(name -> name.substring(name.lastIndexOf("/") + 1))
                         .collect(Collectors.toList());
        }
    }

    @Test
    public void directoryJava() throws IOException {
        File file = new File(directory, "hello.java");
        assertTrue(file.mkdir());
        File fileJava = new File(directory, "hello.java/.java");
        assertTrue(fileJava.createNewFile());

        // Filename that you're expected depends on type of ".java" file. I think it's correct java file
        String expected = ".java.2019";
        renamer.process(Paths.get(directory + "/" + "hello.java"));
        var files = getFiles(file);

        assertEquals(1, files.size());
        assertEquals(expected, files.get(0));
    }

    private void deleteDirectoryStream(Path path) throws IOException {
        try (var stream = Files.walk(path)) {
            stream.sorted(Comparator.reverseOrder())
                  .map(Path::toFile)
                  .forEach(File::delete);
        }
    }

    @Test
    public void renameJava() throws IOException {
        File file = new File(directory, "test.java");
        assertTrue(file.createNewFile());
        renamer.process(file.toPath());
        var elems = getFiles(directory);
        assertEquals(1, elems.size());
        assertEquals(file.getName() + ".2019", elems.get(0));
    }

    @Test
    public void renameKt() throws IOException {
        File file = new File(directory, "test.kt");
        file.createNewFile();
        renamer.process(file.toPath());
        var elems = getFiles(directory);
        assertEquals(1, elems.size());
        assertEquals(file.getName() + ".2019", elems.get(0));
    }

    @Test
    public void renameHaskellFiles() throws IOException {
        File file = new File(directory, "test.hs");
        assertTrue(file.createNewFile());
        renamer.process(file.toPath());
        var elems = getFiles(directory);
        assertEquals(1, elems.size());
        assertEquals(file.getName(), elems.get(0));
    }

    @Test
    public void renameJavaWithoutDot() throws IOException {
        File file = new File(directory, "testjava");
        assertTrue(file.createNewFile());
        renamer.process(file.toPath());
        var elems = getFiles(directory);
        assertEquals(1, elems.size());
        assertEquals(file.getName(), elems.get(0));
    }

    @Test
    public void directoriesInsideDdirectories() throws IOException {
        List<String> fileNames = new ArrayList<>();
        List<String> expectedNames = new ArrayList<>();

        for (int i = 0; i < 10; ++i) {
            String fileName = i + ".java";
            fileNames.add(fileName);
            expectedNames.add(fileName + ".2019");
        }
        for (int i = 0; i < 10; ++i) {
            String fileName = i + ".kt";
            fileNames.add(fileName);
            expectedNames.add(fileName + ".2019");
        }
        for (var name : fileNames) {
            assertTrue(new File(directory + "/" + name).createNewFile());
        }

        File secondDirectory = new File(directory + "/second/third");
        assertTrue(secondDirectory.mkdirs());

        for (int i = 0; i < 10; ++i) {
            String fileName = i + ".java";
            fileNames.add(fileName);
            expectedNames.add(fileName + ".2019");
        }
        for (int i = 0; i < 10; ++i) {
            String fileName = i + ".kt";
            fileNames.add(fileName);
            expectedNames.add(fileName + ".2019");
        }

        for (int i = 0; i < 10; ++i) {
            String fileName = i + ".hs";
            fileNames.add(fileName);
            expectedNames.add(fileName);
        }
        for (var name : fileNames.subList(20, 50)) {
            assertTrue(new File(directory + "/second/" + name).createNewFile());
        }


        for (int i = 0; i < 10; ++i) {
            String fileName = i + ".java";
            fileNames.add(fileName);
            expectedNames.add(fileName + ".2019");
        }
        for (int i = 0; i < 10; ++i) {
            String fileName = i + ".kt";
            fileNames.add(fileName);
            expectedNames.add(fileName + ".2019");
        }
        for (var name : fileNames.subList(50, 70)) {
            assertTrue(new File(directory + "/second/third/" + name).createNewFile());
        }

        renamer.process(directory.toPath());
        List<String> foundNames = getFiles(directory);

        assertEquals(expectedNames.size(), foundNames.size());
        assertTrue(foundNames.containsAll(expectedNames) && expectedNames.containsAll(foundNames));
    }
}
