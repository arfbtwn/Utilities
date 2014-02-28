package little.nj.util;

import java.io.File;

/**
 * A simple class for parsing a file path into component
 * parts useful on a local system.
 */
public class FileInfo
{
    private static final FileUtil UTIL = new FileUtil();

    String path, dir, name, ext;

    boolean modified;

    /**
     * Construct a new FileInfo
     * 
     * @param path
     * @throws IllegalArgumentException if 'path' is null
     */
    public FileInfo(String path)
    {
        if (null == path)
            throw new IllegalArgumentException("path");
        
        this.path = path;
    }

    public final String path()
    {
        if (modified) {
            path = refresh();
            modified = false;
        }
        
        return path;
    }

    public String dir() { 
        return dir == null ? dir = UTIL.getDirectory(path) 
                           : dir; 
    
    }
    
    public String name() { 
        return name == null ? name = UTIL.getFilename(path) 
                            : name; 
    }
    
    public String ext() { 
        return ext == null ? ext = UTIL.getExtension(path) 
                           : ext; 
    }

    public void dir(String newDir)
    {
        dir = newDir.charAt(newDir.length() - 1) != File.separatorChar
                ? newDir + File.separatorChar
                : newDir;

        modified();
    }

    public void name(String newName) { name = newName; modified(); }
    
    public void ext(String newExt) { ext = newExt; modified(); }

    /**
     * Subclasses are encouraged to override this
     *
     * @return The string used to populate the path member
     */
    protected String refresh()
    {
        return String.format("%s%s.%s", dir(), name(), ext());
    }

    /**
     * This method signals
     */
    protected void modified() { modified = true; }

    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof FileInfo ? ((FileInfo)obj).path() == path() 
                                       : false;
    }

    @Override
    public int hashCode() { return path().hashCode(); }
}
