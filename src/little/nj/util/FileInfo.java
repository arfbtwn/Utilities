package little.nj.util;

import java.io.File;

/**
 * A simple class for parsing a file path into component
 * parts useful on a local system.
 *
 * It is designed to be thread safe, in particular it uses
 * an internal lock and does not require external
 * synchronisation.
 */
public class FileInfo
{
    private static final FileUtil UTIL = new FileUtil();

    private final Object _lock_ = new Object();

    String path, dir, name, ext;

    boolean modified;

    public FileInfo(String path)
    {
        this.path = path;
    }

    public final String path()
    {
        synchronized (_lock_)
        {
            return modified ? refresh() : path;
        }
    }

    public String dir() { synchronized (_lock_) { return dir == null ? dir = UTIL.getDirectory(path) : dir; } }
    public String name() { synchronized (_lock_) { return name == null ? name = UTIL.getFilename(path) : name; } }
    public String ext() { synchronized (_lock_) { return ext == null ? ext = UTIL.getExtension(path) : ext; } }

    public void dir(String newDir)
    {
        synchronized (_lock_)
        {
            dir = newDir.charAt(newDir.length() - 1) != File.separatorChar
                    ? newDir + File.separatorChar
                    : newDir;

            modified();
        }
    }

    public void name(String newName) { synchronized (_lock_) { name = newName; modified(); } }
    public void ext(String newExt) { synchronized (_lock_) { ext = newExt; modified(); } }

    /**
     * Subclasses are encouraged to override this
     *
     * @return The string used to populate the path member
     */
    protected String refreshInternal()
    {
        return String.format("%s%s.%s", dir(), name(), ext());
    }

    private String refresh()
    {
        synchronized (_lock_)
        {
            path = refreshInternal();
            modified = false;
        }
        return path;
    }

    private void modified() { modified = true; }

    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof FileInfo ? ((FileInfo)obj).path() == path() : false;
    }

    @Override
    public int hashCode() { return path().hashCode(); }
}
