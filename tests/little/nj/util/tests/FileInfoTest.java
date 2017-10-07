package little.nj.util.tests;

import little.nj.util.FileInfo;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FileInfoTest
{
    @Test
    public void test_FileInfo()
    {
        String path = "/Foo/Bar.java";

        FileInfo fi = new FileInfo(path);

        System.out.printf("Path: %s%n", fi.path());
        System.out.printf("Dir:  %s%n", fi.dir());
        System.out.printf("File: %s%n", fi.name());
        System.out.printf("Ext:  %s%n", fi.ext());

        assertEquals(path, fi.path());

        fi.dir("Bar");
        fi.name("Foo");
        fi.ext("cpp");

        System.out.printf("Path: %s%n", fi.path());
        System.out.printf("Dir:  %s%n", fi.dir());
        System.out.printf("File: %s%n", fi.name());
        System.out.printf("Ext:  %s%n", fi.ext());

        assertEquals("Bar/Foo.cpp", fi.path());
    }
}
