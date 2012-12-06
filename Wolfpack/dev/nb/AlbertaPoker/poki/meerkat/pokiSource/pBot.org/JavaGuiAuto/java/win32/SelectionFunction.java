package win32;

/**
 * Interfaces for the enumerateWindowsProcedure callback.
 */
interface SelectionFunction {
    /**
     * Filter method to select a specific window.
     * @param hwnd Handle to a window
     * @return true to select the given window.
     */
    public boolean selectWindow(int hwnd);
}
