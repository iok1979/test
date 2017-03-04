package ru.insros.viewdirections;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

/**
 *
 * @author Prusakov A.N. <prusakovan@insros.ru>
 */
public class Test {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        final StringBuffer tab = new StringBuffer("  |-- > ");

        // TODO code application logic here
        Iterable<Path> rootDirectories = FileSystems.getDefault().getRootDirectories();

        for (Path p : rootDirectories) {
            System.out.println(p);
        }

        Path startP = Paths.get("..");
        Path nstart = startP.toAbsolutePath();
        Files.walkFileTree(nstart, new FileVisitor<Path>() {
            private long summa = 0;
            private long totalSum = 0;

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
                String r = tab.toString() + file.getFileName() + " * " + file.getNameCount();
                //System.out.println(r + " size: " + attr.size());
                summa += attr.size();
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                //tab.append("--");
                //System.out.println("DIR: - " + dir.getFileName() + " * " + dir.getNameCount());

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                //tab.deleteCharAt(tab.length()-2);
                if (summa > 0) {
                    totalSum += summa;
                    long kb = totalSum/1024;
                    System.out.println(String.format("Dir %s size: %,d (Totalsum: %,d kB)",dir.getFileName(), summa, kb));
                }
                summa = 0;
                return FileVisitResult.CONTINUE;
            }
        });
    }

}
