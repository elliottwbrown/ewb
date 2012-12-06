package win32;

/**
 * The EnumWindowsProc function is an application-defined callback function used with the EnumWindows or
 * EnumDesktopWindows function. It receives top-level window handles.
 */
public interface EnumFunction {
    /**
     * To continue enumeration, the callback function must return true.
     * To stop enumeration, it must return false.
     * @param hwnd The hwnd of the window for the callback.
     * @return true if enumeration should continue and false otherwise.
     */
    boolean EnumWindowsProcedure(int hwnd);
}
