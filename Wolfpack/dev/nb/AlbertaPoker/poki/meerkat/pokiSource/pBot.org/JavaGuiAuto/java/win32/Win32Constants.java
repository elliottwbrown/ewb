package win32;

public interface Win32Constants {
    public static final int WM_COMMAND = 0x111;

    public static final int MF_BYPOSITION = 0x400;
    public static final int MF_CHECKED = 0x8;
    public static final int MF_DISABLED = 0x2;
    public static final int MF_GRAYED = 0x1;
    public static final int MF_SEPARATOR = 0x800;
    public static final int MF_POPUP = 0x10;

    public static final int BN_CLICKED = 0;

    public static final int STN_CLICKED = 0;
    public static final int STN_DBLCLK = 1;

    public static final int HWND_BROADCAST = 0xFFFF;

    public static final int GWL_ID = -12;

    public static final int CB_GETCOUNT = 0x146;
    public static final int CB_GETLBTEXT = 0x148;

    public static final int LB_GETCOUNT = 0x18B;
    public static final int LB_GETTEXT = 0x189;

    public static final int LB_SETCURSEL = 0x186;
    public static final int LBN_SELCHANGE = 1;

    public static final int EM_GETLINE = 0xC4;
    public static final int EM_GETLINECOUNT = 0xBA;
    public static final int EM_REPLACESEL = 0xC2;
    public static final int EM_SETSEL = 0xB1;

    public static final int CB_SETCURSEL = 0x14E;
    public static final int CBN_SELCHANGE = 1;

}
