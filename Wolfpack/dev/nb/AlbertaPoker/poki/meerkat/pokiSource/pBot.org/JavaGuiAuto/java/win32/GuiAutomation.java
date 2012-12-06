package win32;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * Port of the winGuiAuto.py libraries.
 * Provides a set of utility methods for interacting with Win32 applications.
 */
public class GuiAutomation {
    public static final int WIN_UNKNOWN = 0;
    public static final int WIN_NT = 1;
    public static final String WIN_NT_NAME = "Windows NT";
    public static final int WIN_2K = 2;
    public static final String WIN_2K_NAME = "Windows 2000";
    public static final int WIN_XP = 3;
    public static final String WIN_XP_NAME = "Windows XP";

    /**
     * Returns a constant code for the version of Windows running.
     */
    public static int getWindowsVersion() {
        int version = WIN_UNKNOWN;
        String osName = System.getProperty("os.name");
        if (osName.equals(WIN_NT_NAME)) {
            version = WIN_NT;
        } else if (osName.equals(WIN_2K_NAME)) {
            version = WIN_2K;
        } else if (osName.equals(WIN_XP_NAME)) {
            version = WIN_XP;
        }
        return version;
    }

    /**
     * Find the hwnd of a top level window.
     * You can identify windows using captions, classes, a custom selection
     * function, or any combination of these. (Multiple selection criteria are
     * ANDed. If this isn't what's wanted, use a selection function.)
     * @param wantedText Text which required windows' captions must contain.
     * @param wantedClass Class to which required windows must belong.
     * @param selectionFunction Filter for the returned windows.  May be null in which case no filter is applied.
     * @return The hwnd of the first top window which matches the given criteria.
     */
    public static int findTopWindow(String wantedText, String wantedClass, SelectionFunction selectionFunction) throws GuiAutomationException {
        List topWindows = findTopWindows(wantedText, wantedClass, selectionFunction);
        if (topWindows == null || topWindows.size() == 0) {
            throw new GuiAutomationException("No top level window found for wantedText=" +
                    wantedText +
                    ", wantedClass=" +
                    wantedClass +
                    ", selectionFunction=" +
                    selectionFunction);
        }
        return ((Integer) topWindows.get(0)).intValue();
    }

    /**
     * Find the hwnd of top level windows.  You can identify windows using captions, classes, a custom selection
     * function, or any combination of these. (Multiple selection criteria are ANDed. If this isn't what's wanted,
     * use a selection function.)
     * @param wantedText Text which required windows' captions must contain.
     * @param wantedClass Class to which required windows must belong.
     * @param selectionFunction Filter for the returned windows.  May be null in which case no filter is applied.
     * @return A list containing the window handles of all top level windows matching the supplied selection criteria.
     * @throws GuiAutomationException
     */
    public static List findTopWindows(String wantedText, final String wantedClass, final SelectionFunction selectionFunction) throws GuiAutomationException {
        final List windows = new ArrayList();
        final String normalzedWantedText = normaliseText(wantedText);
        EnumFunction enumFunction = new EnumFunction() {
            public boolean EnumWindowsProcedure(int hwnd) {
                if (normalzedWantedText != null) {
                    String windowText = Win32GUI.GetWindowText(hwnd);
                    if (normaliseText(windowText).indexOf(normalzedWantedText) == -1) {
                        return true;
                    }
                }
                if (wantedClass != null) {
                    String windowClass = Win32GUI.GetClassName(hwnd);
                    if (!wantedClass.equals(windowClass)) {
                        return true;
                    }
                }
                if (selectionFunction != null) {
                    if (!selectionFunction.selectWindow(hwnd)) {
                        return true;
                    }
                }
                windows.add(new Integer(hwnd));
                return true;
            }

        };
        Win32GUI.EnumWindows(enumFunction);
        return windows;
    }

    /**
     * Dump all controls from a window into a nested list. Useful during development, allowing to you discover the
     * structure of the contents of a window, showing the text and class of all contained controls.
     * @param hwnd The window handle of the top level window to dump.
     * @return A nested list of Window objects for all of the window's children.
     */
    public static List dumpWindow(int hwnd) {
        ArrayList children = new ArrayList();
        final ArrayList childrenHwnds = new ArrayList();
        EnumFunction enumFunction = new EnumFunction() {
            public boolean EnumWindowsProcedure(int hwnd) {
                childrenHwnds.add(new Integer(hwnd));
                return true;
            }
        };
        Win32GUI.EnumChildWindows(hwnd, enumFunction);
        for (Iterator allChildrenHwnds = childrenHwnds.iterator(); allChildrenHwnds.hasNext();) {
            int childHwnd = ((Integer) allChildrenHwnds.next()).intValue();
            Window child = new Window(childHwnd, Win32GUI.GetWindowText(hwnd), Win32GUI.GetClassName(hwnd));
            child.setChildren(dumpWindow(childHwnd));
            children.add(child);
        }
        return children;
    }

    /**
     * Find a control. You can identify a control using caption, classe, a custom selection function, or any
     * combination of these. (Multiple selection criteria are ANDed. If this isn't what's wanted, use a
     * selection function.)
     * @param topHwnd The window handle of the top level window in which the required controls reside.
     * @param wantedText Text which the required control's captions must contain.
     * @param wantedClass Class to which the required control must belong.
     * @param selectionFunction Filter for the returned windows.  May be null in which case no filter is applied.
     * @return The window handle of the first control matching the supplied selection criteria.
     * @throws GuiAutomationException
     */
    public static int findControl(int topHwnd, String wantedText, String wantedClass, SelectionFunction selectionFunction) throws GuiAutomationException {
        List controls = findControls(topHwnd, wantedText, wantedClass, selectionFunction);
        if (controls == null || controls.size() == 0) {
            throw new GuiAutomationException("No control found for topHwnd=" + topHwnd +
                    ", wantedText=" + wantedText +
                    ", wantedClass=" + wantedClass +
                    ", selectionFunction=" + selectionFunction);
        }
        return ((Integer) controls.get(0)).intValue();
    }

    /**
     * Find controls. You can identify controls using captions, classes, a custom selection function, or any combination
     * of these. (Multiple selection criteria are ANDed. If this isn't what's wanted, use a selection function.)
     * @param topHwnd  The window handle of the top level window in which the required controls reside.
     * @param wantedText Text which the required controls' captions must contain.
     * @param wantedClass Class to which the required controls must belong.
     * @param selectionFunction Filter for the returned windows.  May be null in which case no filter is applied.
     * @return A list of the window handles of the controls matching the supplied selection criteria.
     * @throws GuiAutomationException
     */
    public static List findControls(int topHwnd, String wantedText, String wantedClass, SelectionFunction selectionFunction) throws GuiAutomationException {
        ArrayList controls = new ArrayList();
        final ArrayList childrenHwnds = new ArrayList();
        EnumFunction enumFunction = new EnumFunction() {
            public boolean EnumWindowsProcedure(int hwnd) {
                childrenHwnds.add(new Integer(hwnd));
                return true;
            }
        };
        Win32GUI.EnumChildWindows(topHwnd, enumFunction);
        String normalzedWantedText = normaliseText(wantedText);
        for (Iterator allChildrenHwnds = childrenHwnds.iterator(); allChildrenHwnds.hasNext();) {
            int childHwnd = ((Integer) allChildrenHwnds.next()).intValue();
            List descendentMatchingHwnds = findControls(childHwnd, wantedText, wantedClass, selectionFunction);
            controls.addAll(descendentMatchingHwnds);
            if (normalzedWantedText != null) {
                String windowText = Win32GUI.GetWindowText(childHwnd);
                if (normaliseText(windowText).indexOf(normalzedWantedText) == -1) {
                    continue;
                }
            }
            if (wantedClass != null) {
                String windowClass = Win32GUI.GetClassName(childHwnd);
                if (!wantedClass.equals(windowClass)) {
                    continue;
                }
            }
            if (selectionFunction != null) {
                if (!selectionFunction.selectWindow(childHwnd)) {
                    continue;
                }
            }
            controls.add(new Integer(childHwnd));
        }
        return controls;
    }

    /**
     * Get a window's main, top level menu.
     * @param hwnd The window handle of the top level window for which the top level menu is required.
     * @return The menu handle of the window's main, top level menu.
     */
    public static int getTopMenu(int hwnd) throws GuiAutomationException {
        return Win32GUI.GetMenu(hwnd);
    }

    /**
     * Activate a menu item.
     * @param hwnd The window handle of the top level window whose menu you wish to activate.
     * @param menuItemPath The path to the required menu item. This should be a sequence specifying the path through
     * the menu to the required item. Each item in this path can be specified either as an index, or as a menu name.
     */
    public static void activateMenuItem(int hwnd, List menuItemPath) throws GuiAutomationException {
        // By Axel Kowald (kowald@molgen.mpg.de)
        // Modified by S Brunning to accept strings in addition to indicies.
        // Ported to Java by G Hesselroth

        // Top level menu
        int hMenu = getTopMenu(hwnd);

        // Get top level menu's item count.
        int menuItemCount = Win32GUI.GetMenuItemCount(hMenu);

        // Walk down submenus
        for (ListIterator pathItems = menuItemPath.listIterator(); pathItems.nextIndex() < menuItemPath.size() - 1;) {
            Object item = pathItems.next();
            int subMenuID = getMenuPathItemID(hMenu, menuItemCount, item);
            MenuInfo submenuInfo = getMenuInfo(hMenu, subMenuID);
            hMenu = submenuInfo.getSubMenu();
            menuItemCount = submenuInfo.getItemCount();
        }

        Object lastItem = menuItemPath.get(menuItemPath.size() - 1);
        int lastItemID = getMenuPathItemID(hMenu, menuItemCount, lastItem);

        // Get required menu item's ID. (the one at the end).
        int menuItemID = Win32GUI.GetMenuItemID(hMenu, lastItemID);

        // Activate
        Win32GUI.PostMessage(hwnd, Win32Constants.WM_COMMAND, menuItemID, 0);
    }

    /**
     * Get various info about a menu item.
     * @param hMenu The menu in which the item is to be found.
     * @param itemIndex The item's index
     * @return Menu item information object.
     */
    public static MenuInfo getMenuInfo(int hMenu, int itemIndex) {
        MenuInfo menuInfo = new MenuInfo();

        // Menu state
        int menuState = Win32GUI.GetMenuState(hMenu, itemIndex, Win32Constants.MF_BYPOSITION);
        if (menuState == -1) {
            throw new GuiAutomationException("No such menu item, hMenu=" + hMenu + " uIDItem=" + itemIndex);
        }
        menuInfo.setHandle(hMenu);
        menuInfo.setChecked((menuState & Win32Constants.MF_CHECKED) != 0);
        menuInfo.setDisabled((menuState & Win32Constants.MF_DISABLED) != 0);
        menuInfo.setGreyed((menuState & Win32Constants.MF_GRAYED) != 0);
        menuInfo.setSeperator((menuState & Win32Constants.MF_SEPARATOR) != 0);
        // ... there are more, but these are the ones I'm interested in

        // Menu name
        menuInfo.setName(Win32GUI.GetMenuString(hMenu, itemIndex, Win32Constants.MF_BYPOSITION));

        // Sub menu info
        menuInfo.setItemCount(menuState >> 8);
        if ((menuState & Win32Constants.MF_POPUP) != 0) {
            menuInfo.setSubMenu(Win32GUI.GetSubMenu(hMenu, itemIndex));
        } else {
            menuInfo.setSubMenu(-1);
        }
        return menuInfo;
    }

    /**
     * Simulates a single mouse click on a button.
     * @param hwnd Window handle of the required button.
     */
    public static void clickButton(int hwnd) {
        sendNotifyMessage(hwnd, Win32Constants.BN_CLICKED);
    }

    /**
     * Simulates a single mouse click on a static
     * @param hwnd Window handle of the required static.
     */
    public static void clickStatic(int hwnd) {
        sendNotifyMessage(hwnd, Win32Constants.STN_CLICKED);
    }

    /**
     * Simulates a double mouse click on a static
     * @param hwnd Window handle of the required static.
     */
    public static void doubleClickStatic(int hwnd) {
        sendNotifyMessage(hwnd, Win32Constants.STN_DBLCLK);
    }

    /**
     * Returns the items in a combo box control.
     * @param hwnd Window handle for the combo box.
     * @return List of the text of the items in the combo box.
     */
    public static List getComboboxItems(int hwnd) {
        return getMultipleWindowValues(hwnd, Win32Constants.CB_GETCOUNT, Win32Constants.CB_GETLBTEXT);
    }

    /**
     * Selects a specified item in a Combo box control.
     * @param hwnd Window handle of the required combo box.
     * @param item The reqired item. Either an index, of the text of the required item.
     */
    public static void selectComboboxItem(int hwnd, Object item) {
        if (item instanceof Integer) {
            Win32GUI.SendMessage(hwnd, Win32Constants.CB_SETCURSEL, ((Integer) item).intValue(), 0);
            sendNotifyMessage(hwnd, Win32Constants.CBN_SELCHANGE);
        } else if (item instanceof String) {
            List items = getListboxItems(hwnd);
            selectComboboxItem(hwnd, new Integer(items.indexOf(item)));
        } else {
            throw new GuiAutomationException("Invalid item passed to selectListboxItem: " + item);
        }
    }

    /**
     * Returns the items in a list box control.
     * @param hwnd Window handle for the list box.
     * @return List box items.
     */
    public static List getListboxItems(int hwnd) {
        return getMultipleWindowValues(hwnd, Win32Constants.LB_GETCOUNT, Win32Constants.LB_GETTEXT);
    }

    /**
     * Selects a specified item in a list box control.
     * @param hwnd Window handle of the required list box.
     * @param item The reqired item. Either an index, of the text of the required item.
     */
    public static void selectListboxItem(int hwnd, Object item) {
        if (item instanceof Integer) {
            Win32GUI.SendMessage(hwnd, Win32Constants.LB_SETCURSEL, ((Integer) item).intValue(), 0);
            sendNotifyMessage(hwnd, Win32Constants.LBN_SELCHANGE);
        } else if (item instanceof String) {
            List items = getListboxItems(hwnd);
            selectListboxItem(hwnd, new Integer(items.indexOf(item)));
        } else {
            throw new GuiAutomationException("Invalid item passed to selectListboxItem: " + item);
        }
    }

    /**
     * Returns the text in an edit control.
     * @param hwnd Window handle for the edit control.
     * @return List of text lines in the edit control.
     */
    public static List getEditText(int hwnd) {
        return getMultipleWindowValues(hwnd, Win32Constants.EM_GETLINECOUNT, Win32Constants.EM_GETLINE);
    }

    /**
     * Set an edit control's text.
     * @param hwnd The edit control's hwnd.
     * @param text The new value for the control's text.
     * @param append true if the text should be appended and otherwise it will replace the existing text.
     */
    public static void setEditText(int hwnd, String text, boolean append) {
        // Set the current selection range, depending on append flag
        if (append) {
            Win32GUI.SendMessage(hwnd, Win32Constants.EM_SETSEL, -1, 0);
        } else {
            Win32GUI.SendMessage(hwnd, Win32Constants.EM_SETSEL, 0, -1);
        }

        // Send the text
        Win32GUI.SendMessageWithString(hwnd, Win32Constants.EM_REPLACESEL, 1, text);
    }

    /**
     * Set an edit control's text.
     * @param hwnd The edit control's hwnd.
     * @param text List of Strings.  Each element will become a seperate line in the control.
     * @param append true if the text should be appended and otherwise it will replace the existing text.
     */
    public static void setEditText(int hwnd, List text, boolean append) {
        // Set the current selection range, depending on append flag
        if (append) {
            Win32GUI.SendMessage(hwnd, Win32Constants.EM_SETSEL, -1, 0);
        } else {
            Win32GUI.SendMessage(hwnd, Win32Constants.EM_SETSEL, 0, -1);
        }

        StringBuffer finalText = new StringBuffer();
        for(Iterator textIterator = text.iterator(); textIterator.hasNext();) {
            finalText.append(textIterator.next());
            if(textIterator.hasNext()) {
               finalText.append("\r\n");
            }
        }
        // Send the text
        Win32GUI.SendMessageWithString(hwnd, Win32Constants.EM_REPLACESEL, 1, finalText.toString());
    }

    // Send a notify message to a control.
    private static void sendNotifyMessage(int hwnd, int msg) {
        Win32GUI.SendMessage(Win32GUI.GetParent(hwnd), Win32Constants.WM_COMMAND,
                buildWinLong(msg, Win32GUI.GetWindowLong(hwnd, Win32Constants.GWL_ID)),
                hwnd);
    }

    /**
     * A common pattern in the Win32 API is that in order to retrieve a series of values, you use one message to get a
     * count of available items, and another to retrieve them. This internal utility function performs the common
     * processing for this pattern.
     * @param hwnd Window handle for the window for which items should be retrieved.
     * @param countMessage Item count message.
     * @param dataMessage Value retrieval message.
     * @return List of the items returned based on the dataMessage.
     */
    private static List getMultipleWindowValues(int hwnd, int countMessage, int dataMessage) {
        int valueCount = Win32GUI.SendMessage(hwnd, countMessage, 0, 0);
        ArrayList result = new ArrayList();
        for (int itemIndex = 0; itemIndex < valueCount; itemIndex++) {
            result.add(Win32GUI.SendMessageReturnString(hwnd, dataMessage, itemIndex));
        }
        return result;
    }

    /**
     * Build a windows long parameter from high and low words.
     */
    private static int buildWinLong(int high, int low) {
        return ((high << 16) | low);
    }

    private static int getMenuPathItemID(int hMenu, int menuItemCount, Object item) {
        int itemID;
        if (item instanceof Integer) {
            itemID = ((Integer) item).intValue();
        } else if (item instanceof String) {
            itemID = findNamedSubMenu(hMenu, menuItemCount, (String) item);
        } else {
            throw new GuiAutomationException("Invalid object in path: " + item);
        }
        return itemID;
    }

    /**
     * Find the index number of a menu's submenu with a specific name.
     */
    private static int findNamedSubMenu(int hMenu, int menuItemCount, String itemName) {
        String normalizedItemName = normaliseText(itemName);
        for (int subMenuIndex = 0; subMenuIndex < menuItemCount; subMenuIndex++) {
            MenuInfo subMenuInfo = getMenuInfo(hMenu, subMenuIndex);
            if (normaliseText(subMenuInfo.getName()).startsWith(normalizedItemName)) {
                return subMenuIndex;
            }
        }
        throw new GuiAutomationException("No submenu found for hMenu=" + hMenu + ", hMenuItemCount=" + menuItemCount +
                ", submenuName=" + itemName);
    }

    private static String normaliseText(String text) {
        if (text == null) return null;
        return text.toLowerCase().replaceAll("&", "");
    }
}
