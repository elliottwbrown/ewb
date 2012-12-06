package win32.test;

import junit.framework.TestCase;
import win32.GuiAutomation;
import win32.GuiAutomationException;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 * GuiAutomation test cases.
 */
public class GuiAutomationTest extends TestCase {
    static {
        System.loadLibrary("Win32GUINative");
    }

    public GuiAutomationTest() {
        super("GuiAutomation test cases");
    }

    public GuiAutomationTest(String name) {
        super(name);
    }

    public void testNotepadAutomation() throws Exception {
        // Test - drives notepad.
        // NT/2K/XP notepads have a different menu stuctures.
        int windowsVersion = GuiAutomation.getWindowsVersion();
        assertTrue("Windows version unknown", windowsVersion != GuiAutomation.WIN_UNKNOWN);
        // Launch Notepad
        Runtime.getRuntime().exec("notepad");
        Thread.sleep(500);
        int notepadWindow = GuiAutomation.findTopWindow(null, "Notepad", null);

        List windowChildren = GuiAutomation.dumpWindow(notepadWindow);
        assertNotNull("Notepad children", windowChildren);

        int topMenuID = GuiAutomation.getTopMenu(notepadWindow);
        assertTrue("MenuID > 0", topMenuID > 0);

        List path = new ArrayList();
        if (windowsVersion == GuiAutomation.WIN_NT) {
            path.add("search");
            path.add("replace");
        } else {
            path.add("edit");
            path.add("replace");
        }
        GuiAutomation.activateMenuItem(notepadWindow, path);

        Thread.sleep(500);
        int replaceDialog = GuiAutomation.findTopWindow("Replace", null, null);
        assertTrue("ReplaceDialog > 0", replaceDialog > 0);

        int editControl = GuiAutomation.findControl(replaceDialog, null, "Edit", null);
        assertTrue("EditControl > 0", editControl > 0);
        List editText = new ArrayList();
        editText.add("Hello, mate!");
        GuiAutomation.setEditText(editControl, editText, false);
        Thread.sleep(500);
        List actualText = GuiAutomation.getEditText(editControl);
        assertEquals("Edited Text", editText, actualText);

        int cancelButton = GuiAutomation.findControl(replaceDialog, "Cancel", "Button", null);
        GuiAutomation.clickButton(cancelButton);

        path.clear();
        path.add("format");
        path.add("font");
        GuiAutomation.activateMenuItem(notepadWindow, path);
        Thread.sleep(500);
        int fontDialog = GuiAutomation.findTopWindow("Font", null, null);
        assertTrue("fontDialog > 0", fontDialog > 0);

        List fontDialogDump = GuiAutomation.dumpWindow(fontDialog);
        assertTrue("Font Dialog Dump size", fontDialogDump.size() > 0);

        List fontCombos = GuiAutomation.findControls(fontDialog, null, "ComboBox", null);
        Integer fontCombo = new Integer(0);
        List fontComboItems = null;
        for (int i = 0; i < fontCombos.size(); i++) {
            fontCombo = (Integer) fontCombos.get(i);
            fontComboItems = GuiAutomation.getComboboxItems(fontCombo.intValue());
            if (fontComboItems.contains("Arial")) {
                break;
            }
        }
        assertTrue("FontComboItems length", fontComboItems.size() > 0);
        Random r = new Random();
        GuiAutomation.selectComboboxItem(fontCombo.intValue(), fontComboItems.get(r.nextInt(fontComboItems.size())));
        Thread.sleep(500);

        int okButton = GuiAutomation.findControl(fontDialog, "OK", "Button", null);
        GuiAutomation.clickButton(okButton);

        int editArea = GuiAutomation.findControl(notepadWindow, null, "Edit", null);
        editText.clear();
        editText.add("Hello, again!");
        GuiAutomation.setEditText(editArea, editText, false);
        Thread.sleep(500);
        actualText = GuiAutomation.getEditText(editArea);
        assertEquals("Edit text", editText, actualText);
        editText.clear();
        editText.add("You still there?");
        GuiAutomation.setEditText(editArea, editText, false);
        Thread.sleep(500);
        actualText = GuiAutomation.getEditText(editArea);
        assertEquals("Edit text", editText, actualText);
        editText.clear();
        editText.add("Here comes");
        editText.add("two lines!");
        GuiAutomation.setEditText(editArea, editText, false);
        Thread.sleep(500);
        actualText = GuiAutomation.getEditText(editArea);
        assertEquals("Edit text", editText, actualText);
        List additionalText = new ArrayList();
        additionalText.add("\r\nAnd a 3rd one!");
        GuiAutomation.setEditText(editArea, additionalText, true);
        Thread.sleep(500);
        editText.add("And a 3rd one!");
        actualText = GuiAutomation.getEditText(editArea);
        assertEquals("Edit text", editText, actualText);

        path.clear();
        path.add("file");
        path.add("exit");
        GuiAutomation.activateMenuItem(notepadWindow, path);
        Thread.sleep(1000);

        int saveDialog = GuiAutomation.findTopWindow("Notepad", "#32770", null);
        int noButton = GuiAutomation.findControl(saveDialog, "no", "Button", null);
        GuiAutomation.clickButton(noButton);

        Runtime.getRuntime().exec("C:/Program Files/Windows NT/Accessories/wordpad.exe");
        Thread.sleep(1000);
        int wordpadWindow = GuiAutomation.findTopWindow("WordPad", null, null);

        path.clear();
        path.add(new Integer(0));
        path.add(new Integer(0));
        GuiAutomation.activateMenuItem(wordpadWindow, path);
        Thread.sleep(500);

        int newDialog = GuiAutomation.findTopWindow("New", "#32770", null);
        try {
            GuiAutomation.findControl(newDialog, null, "Banana", null);
            fail("Expected error due to invalid control");
        } catch (GuiAutomationException e) {
        }

        int docType = GuiAutomation.findControl(newDialog, null, "ListBox", null);
        assertTrue("DocType > 0", docType > 0);
        List typeListBox = GuiAutomation.getListboxItems(docType);
        assertTrue("ListBoxSize > 0", typeListBox.size() > 0);

        Integer index = new Integer(r.nextInt(typeListBox.size()));
        GuiAutomation.selectListboxItem(docType, index);
        Thread.sleep(500);
        int okButton2 = GuiAutomation.findControl(newDialog, "OK", "Button", null);
        GuiAutomation.clickButton(okButton2);

        Thread.sleep(500);
        path.clear();

        path.add("file");
        path.add("exit");
        GuiAutomation.activateMenuItem(wordpadWindow, path);
    }
}
