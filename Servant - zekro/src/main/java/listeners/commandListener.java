package listeners;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import util.STATIC;
import core.*;
import util.createFile;

import java.nio.file.Files;
import java.nio.file.Paths;

public class commandListener extends ListenerAdapter {

    public void onMessageReceived(MessageReceivedEvent event) {

        if (event.getMessage().getContentDisplay().startsWith(STATIC.PREFIX) && !event.getMessage().getAuthor().isBot()) {
            try {
                String content = new String(Files.readAllBytes(Paths.get("commandcounter.txt")));
                STATIC.COUNTER = Integer.parseInt(content);
            } catch (Exception e) {
                System.out.println("Error at readFile");
            }
            STATIC.COUNTER = STATIC.COUNTER + 1;
            createFile g = new createFile();
            g.openFile();
            g.addRecords();
            g.closeFile();
            commandHandler.handleCommand(commandHandler.parser.parse(event.getMessage().getContentDisplay(), event));
        }
    }
}