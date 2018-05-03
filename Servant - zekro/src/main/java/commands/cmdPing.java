package commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.STATIC;
import util.createFilecmdPing;

import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.Math.toIntExact;



public class cmdPing implements Command {

    //colors for ping
    private Color getColorByPing(long ping) {
        if (ping < 100)
            return Color.CYAN;
        if (ping < 400)
            return Color.GREEN;
        if (ping < 700)
            return Color.ORANGE;
        return Color.RED;
    }

    //Timestamp
    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        //delete command
        event.getMessage().delete().complete();

        //cmdPing counter
        try {
            String contentcmdPing = new String(Files.readAllBytes(Paths.get("cmdpingcounter.txt")));
            STATIC.cmdPingCOUNTER = Integer.parseInt(contentcmdPing);
        } catch (Exception e) {
            System.out.println("Error at readFile (cmdPing)");
        }
        STATIC.cmdPingCOUNTER = STATIC.cmdPingCOUNTER +1;
        createFilecmdPing gcmdPing = new createFilecmdPing();
        gcmdPing.openFile();
        gcmdPing.addRecords();
        gcmdPing.closeFile();

        //get ping + output
        long ping = event.getJDA().getPing();
        event.getTextChannel().sendMessage(new EmbedBuilder().setColor(getColorByPing(ping)).setDescription(
                String.format(":ping_pong:  **Pong!**  `%s ms`  ", ping) + event.getAuthor().getAsMention()
        ).build()).queue();

    }

    @Override
    public void executed(boolean sucess, MessageReceivedEvent event) {
        //commandlog message
        EmbedBuilder builder = new EmbedBuilder();
        try {
            try {
                //user has at least one role
                builder.setAuthor("[" + event.getMember().getRoles().get(0).getName() + "] " + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator(), null, event.getAuthor().getAvatarUrl());
                builder.setDescription("```" + event.getMessage().getContentDisplay() + "```");
                builder.setFooter("#" + event.getTextChannel().getName() + " | " + getCurrentTimeStamp(), null);
                builder.setColor(Color.CYAN);
                event.getGuild().getTextChannelsByName("commandlog", true).get(0).sendMessage(builder.build()).queue();
            } catch (Exception e) {
                //user has no roles
                builder.setAuthor(event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator(), null, event.getAuthor().getAvatarUrl());
                builder.setDescription("```" + event.getMessage().getContentDisplay() + "```");
                builder.setFooter("#" + event.getTextChannel().getName() + " | " + getCurrentTimeStamp(), null);
                builder.setColor(Color.CYAN);
                event.getGuild().getTextChannelsByName("commandlog", true).get(0).sendMessage(builder.build()).queue();
            }
        } catch (IndexOutOfBoundsException e) {
            //there is no log channel. just ignore
        }
    }

    @Override
    public String help() {
        return null;
    }
}
