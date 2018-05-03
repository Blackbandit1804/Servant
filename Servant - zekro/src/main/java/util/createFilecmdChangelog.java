package util;

import java.util.Formatter;

public class createFilecmdChangelog {

    private Formatter xcmdChangelog;

    public void openFile() {
        try {
            xcmdChangelog = new Formatter("cmdchangelogcounter.txt");
        } catch (Exception e) {
            System.out.println("Error at openFile (cmdChangelog)");
        }
    }

    public void addRecords() {
        xcmdChangelog.format("%s", STATIC.cmdChangelogCOUNTER);
    }
    public void closeFile() {
        xcmdChangelog.close();
    }
}
