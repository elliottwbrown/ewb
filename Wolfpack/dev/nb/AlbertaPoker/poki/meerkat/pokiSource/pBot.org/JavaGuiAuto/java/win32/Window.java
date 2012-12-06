package win32;

import java.util.List;
import java.util.ArrayList;

/**
 * Simple container class for holding basic Win32 Window information.
 */
public class Window {
    private int hwnd;
    private String text;
    private String windowClass;
    private List children;

    public Window(int hwnd, String text, String windowClass) {
        this.hwnd = hwnd;
        this.text = text;
        this.windowClass = windowClass;
    }

    public int getHwnd() {
        return hwnd;
    }

    public String getText() {
        return text;
    }

    public String getWindowClass() {
        return windowClass;
    }

    public List getChildren() {
        return children;
    }

    public void setChildren(List children) {
        this.children = children;
    }
}
