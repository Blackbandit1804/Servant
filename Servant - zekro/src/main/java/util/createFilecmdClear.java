package util;

import java.util.Formatter;

public class createFilecmdClear {

    private Formatter xcmdClear;

    public void openFile() {
        try {
            xcmdClear = new Formatter("cmdclearcounter.txt");
        } catch (Exception e) {
            System.out.println("Error at openFile (cmdClear)");
        }
    }

    public void addRecords() {
        xcmdClear.format("%s", STATIC.cmdClearCOUNTER);
    }
    public void closeFile() {
        xcmdClear.close();
    }
}