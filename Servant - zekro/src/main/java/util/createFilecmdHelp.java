package util;

import java.util.Formatter;

public class createFilecmdHelp {

    private Formatter xcmdHelp;

    public void openFile() {
        try {
            xcmdHelp = new Formatter("cmdhelpcounter.txt");
        } catch (Exception e) {
            System.out.println("Error at openFile (cmdHelp)");
        }
    }

    public void addRecords() {
        xcmdHelp.format("%s", STATIC.cmdHelpCOUNTER);
    }
    public void closeFile() {
        xcmdHelp.close();
    }
}