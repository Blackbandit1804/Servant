package util;

import java.util.Formatter;

public class createFilecmdPing {

    private Formatter xcmdPing;

    public void openFile() {
        try {
            xcmdPing = new Formatter("cmdpingcounter.txt");
        } catch (Exception e) {
            System.out.println("Error at openFile (cmdPing)");
        }
    }

    public void addRecords() {
        xcmdPing.format("%s", STATIC.cmdPingCOUNTER);
    }
    public void closeFile() {
        xcmdPing.close();
    }
}
