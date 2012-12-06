package win32;

/**
 * Simple container class for Menu information.
 */
public class MenuInfo {
    private String name;
    private int handle;
    private int itemCount;
    private int subMenu;
    private boolean checked;
    private boolean greyed;
    private boolean disabled;
    private boolean seperator;

    public MenuInfo() {
    }

    public MenuInfo(int hMenu, int itemCount) {
        setHandle(hMenu);
        setItemCount(itemCount);
    }

    public int getHandle() {
        return handle;
    }

    public void setHandle(int handle) {
        this.handle = handle;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isGreyed() {
        return greyed;
    }

    public void setGreyed(boolean greyed) {
        this.greyed = greyed;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isSeperator() {
        return seperator;
    }

    public void setSeperator(boolean seperator) {
        this.seperator = seperator;
    }

    public int getSubMenu() {
        return subMenu;
    }

    public void setSubMenu(int subMenu) {
        this.subMenu = subMenu;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
