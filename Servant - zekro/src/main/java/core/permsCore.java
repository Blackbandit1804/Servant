package core;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.STATIC;

import java.awt.*;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class permsCore {

    public static boolean check(MessageReceivedEvent event) {

        EmbedBuilder warning = new EmbedBuilder().setColor(Color.ORANGE);

        for (Role r : event.getGuild().getMember(event.getAuthor()).getRoles()) {
            if (Arrays.stream(STATIC.PERMS).parallel().anyMatch(r.getName()::contains)) {
                return false;
            }

        }
        //delete command
        event.getMessage().delete().queue();
        //warning message
        Message warn = event.getTextChannel().sendMessage(
                warning.setDescription("**[WARNING]** " + event.getAuthor().getAsMention() + ", you don't have the permissions to use this command!").build()
        ).complete();
        //delete warning message after 5 seconds
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                warn.delete().queue();
            }
        }, 5000);
        return true;
    }
}