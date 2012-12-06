package win32;

/**
 * Integration class of Java Native methods for calling parts of the Win32 API.
 */
public class Win32GUI {
    /**
     * The EnumWindows function enumerates all top-level windows on the screen by passing the handle to each window,
     * in turn, to an application-defined callback function. EnumWindows continues until the last top-level window
     * is enumerated or the callback function returns false.
     * @param function Application specific callback function.
     * @return true if the function succeeded and false otherwise.
     */
    native public static boolean EnumWindows(EnumFunction function);

    /**
     * The EnumChildWindows function enumerates the child windows that belong to the specified parent window by
     * passing the handle to each child window, in turn, to an application-defined callback function. EnumChildWindows
     * continues until the last child window is enumerated or the callback function returns false.
     * @param hwnd Handle to the parent window whose child windows are to be enumerated. If this parameter is NULL,
     * this function is equivalent to EnumWindows.
     * @param enumFunction Application specific callback function.
     */
    native public static void EnumChildWindows(int hwnd, EnumFunction enumFunction);

    /**
     * Retrieves the calling thread's last-error code value. The last-error code is maintained on a per-thread basis.
     * Multiple threads do not overwrite each other's last-error code.
     */
    native public static long GetLastError();

    /**
     * The GetWindowText function copies the text of the specified window's title bar (if it has one) into a buffer.
     * If the specified window is a control, the text of the control is copied. However, GetWindowText cannot retrieve
     * the text of a control in another application.
     * @param hwnd Handle to the window or control containing the text.
     * @return The text for the window.
     */
    native public static String GetWindowText(int hwnd);

    /**
     * Retrieves the name of the class to which the specified window belongs.
     * @param hwnd Handle to the window and, indirectly, the class to which the window belongs.
     * @return The class for the window.
     */
    native public static String GetClassName(int hwnd);

    /**
     * The GetMenu function retrieves a handle to the menu assigned to the specified window.
     * @param hwnd Handle to the window whose menu handle is to be retrieved.
     * @return The return value is a handle to the menu. If the specified window has no menu,
     * the return value is NULL. If the window is a child window, the return value is undefined.
     */
    native public static int GetMenu(int hwnd);

    /**
     * The GetMenuItemCount function determines the number of items in the specified menu.
     * @param hmenu Handle to the menu to be examined.
     * @return If the function succeeds, the return value specifies the number of items in the menu.
     * If the function fails, the return value is -1.
     */
    native public static int GetMenuItemCount(int hmenu);

    /**
     * The GetMenuItemID function retrieves the menu item identifier of a menu item located at the specified position
     * in a menu.
     * @param hMenu Handle to the menu that contains the item whose identifier is to be retrieved.
     * @param nPos Specifies the zero-based relative position of the menu item whose identifier is to be retrieved.
     * @return The return value is the identifier of the specified menu item. If the menu item identifier is 0 or
     * if the specified item opens a submenu, the return value is -1.
     */
    native public static int GetMenuItemID(int hMenu, int nPos);

    /**
     * The PostMessage function places (posts) a message in the message queue associated with the thread that created
     * the specified window and returns without waiting for the thread to process the message.
     * @param hwnd Handle to the window whose window procedure is to receive the message. The following values have
     * special meanings. HWND_BROADCAST - The message is posted to all top-level windows in the system, including
     * disabled or invisible unowned windows, overlapped windows, and pop-up windows. The message is not posted to
     * child windows. NULL - The function behaves like a call to PostThreadMessage with the dwThreadId parameter set
     * to the identifier of the current thread.
     * @param msg Specifies the message to be posted.
     * @param wparam Specifies additional message-specific information.
     * @param lparam Specifies additional message-specific information.
     */
    native public static void PostMessage(int hwnd, int msg, int wparam, int lparam);

    /**
     * The GetMenuState function retrieves the menu flags associated with the specified menu item. If the menu item
     * opens a submenu, this function also returns the number of items in the submenu.
     * @param hMenu Handle to the menu that contains the menu item whose flags are to be retrieved.
     * @param uID Specifies the menu item for which the menu flags are to be retrieved, as determined by the uFlags parameter.
     * @param uFlags Specifies how the uId parameter is interpreted. This parameter can be one of the following
     * values. MF_BYCOMMAND - Indicates that the uId parameter gives the identifier of the menu item. The MF_BYCOMMAND
     * flag is the default if neither the MF_BYCOMMAND nor MF_BYPOSITION flag is specified. MF_BYPOSITION - Indicates
     * that the uId parameter gives the zero-based relative position of the menu item.
     * @return If the specified item does not exist, the return value is -1. If the menu item opens a submenu, the
     * low-order byte of the return value contains the menu flags associated with the item, and the high-order byte
     * contains the number of items in the submenu opened by the item. Oherwise, the return value is a mask (Bitwise OR)
     * of the menu flags.
     */
    native public static int GetMenuState(int hMenu, int uID, int uFlags);

    /**
     * The GetSubMenu function retrieves a handle to the drop-down menu or submenu activated by the specified menu item.
     * @param hMenu Handle to the menu.
     * @param itemIndex Specifies the zero-based relative position in the specified menu of an item that activates a
     * drop-down menu or submenu.
     * @return If the function succeeds, the return value is a handle to the drop-down menu or submenu activated by the
     * menu item. If the menu item does not activate a drop-down menu or submenu, the return value is 0.
     */
    native public static int GetSubMenu(int hMenu, int itemIndex);

    /**
     * The GetMenuString function copies the text string of the specified menu item into the specified buffer.
     * @param hMenu Handle to the menu.
     * @param itemID Specifies the menu item to be changed, as determined by the uFlag parameter.
     * @param flag Specifies how the itemID parameter is interpreted. This parameter must be one of the following
     * values. MF_BYCOMMAND - Indicates that itemID gives the identifier of the menu item. If neither the MF_BYCOMMAND
     * nor MF_BYPOSITION flag is specified, the MF_BYCOMMAND flag is the default flag. MF_BYPOSITION - Indicates that
     * itemID gives the zero-based relative position of the menu item.
     * @return If the function succeeds, the return value is the text of the menu item. If the function fails, the
     * return value is null.
     */
    native public static String GetMenuString(int hMenu, int itemID, int flag);

    /**
     * The SendMessage function sends the specified message to a window or windows. It calls the window procedure for
     * the specified window and does not return until the window procedure has processed the message.
     * @param hwnd Handle to the window whose window procedure will receive the message. If this parameter is
     * HWND_BROADCAST, the message is sent to all top-level windows in the system, including disabled or invisible
     * unowned windows, overlapped windows, and pop-up windows; but the message is not sent to child windows.
     * @param msg Specifies the message to be posted.
     * @param wparam Specifies additional message-specific information.
     * @param lparam Specifies additional message-specific information.
     */
    native public static int SendMessage(int hwnd, int msg, int wparam, int lparam);
    native public static int SendMessageWithString(int hwnd, int msg, int wparam, String s);
    native public static String SendMessageReturnString(int hwnd, int msg, int wparam);

    /**
     * The GetParent function retrieves a handle to the specified window's parent or owner.
     * @param hwnd Handle to the window whose parent window handle is to be retrieved.
     * @return If the window is a child window, the return value is a handle to the parent window. If the window is a
     * top-level window, the return value is a handle to the owner window. If the window is a top-level unowned window
     * or if the function fails, the return value is NULL.
     */
    native public static int GetParent(int hwnd);

    /**
     * The GetWindowLong function retrieves information about the specified window. The function also retrieves the
     * 32-bit (long) value at the specified offset into the extra window memory.
     * @param hwnd Handle to the window and, indirectly, the class to which the window belongs.
     * @param index Specifies the zero-based offset to the value to be retrieved.
     */
    native public static int GetWindowLong(int hwnd, int index);
}
