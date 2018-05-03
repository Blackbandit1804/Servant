package util;

import java.util.Formatter;

public class createFilecmdStats {

    private Formatter xcmdStats;

    public void openFile() {
        try {
            xcmdStats = new Formatter("cmdstatscounter.txt");
        } catch (Exception e) {
            System.out.println("Error at openFile (cmdStats)");
        }
    }

    public void addRecords() {
        xcmdStats.format("%s", STATIC.cmdStatsCOUNTER);
    }
    public void closeFile() {
        xcmdStats.close();
    }
}
