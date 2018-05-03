package util;

import java.util.Formatter;

public class createFile {
    private Formatter x;

    public void openFile() {
        try {
            x = new Formatter("commandcounter.txt");
        } catch (Exception e) {
            System.out.println("Error at openFile");
        }
    }

    public void addRecords() {
        x.format("%s", STATIC.COUNTER);
    }
    public void closeFile() {
        x.close();
    }
}
