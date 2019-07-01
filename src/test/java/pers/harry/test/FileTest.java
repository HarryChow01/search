package pers.harry.test;

import java.io.File;
import java.io.IOException;

public class FileTest {

    public static void main(String[] args) {
        System.out.println("File.pathSeparator: " + File.pathSeparator);
        System.out.println("File.separator: " + File.separator);

        File testFile = new File("data/data1.txt");
        System.out.println("abspath: " + testFile.getAbsolutePath());
        System.out.println("getPath: " + testFile.getPath());
        System.out.println("getName: " + testFile.getName());
        System.out.println("getParent: " + testFile.getParent());
        System.out.println("data1.txt exist: " + testFile.exists());

        File parentFile = testFile.getParentFile();
        //parentFile.mkdir();
        parentFile.mkdirs();

        try {
            testFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("data1.txt exist: " + testFile.exists());

    }
}
