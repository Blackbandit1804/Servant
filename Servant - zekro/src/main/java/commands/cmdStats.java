package commands;

import core.commandHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.STATIC;
import util.createFilecmdStats;

import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class cmdStats implements Command {

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
        //cmdStats counter
        try {
            String contentcmdString = new String(Files.readAllBytes(Paths.get("cmdstatscounter.txt")));
            STATIC.cmdStatsCOUNTER = Integer.parseInt(contentcmdString);
        } catch (Exception e) {
            System.out.println("Error at readFile (cmdStats)");
        }
        STATIC.cmdStatsCOUNTER = STATIC.cmdStatsCOUNTER +1;
        createFilecmdStats gcmdStats = new createFilecmdStats();
        gcmdStats.openFile();
        gcmdStats.addRecords();
        gcmdStats.closeFile();

        //compare cmd counters
        //cmdStats
        try {
            String comparecmdStats = new String(Files.readAllBytes(Paths.get("cmdstatscounter.txt")));
            STATIC.comparecmdStats = Integer.parseInt(comparecmdStats);
        } catch (Exception e) {
            System.out.println("Error at readFile (compare cmdStats)");
        }
        //cmdPing
        try {
            String comparecmdPing = new String(Files.readAllBytes(Paths.get("cmdpingcounter.txt")));
            STATIC.comparecmdPing = Integer.parseInt(comparecmdPing);
        } catch (Exception e) {
            System.out.println("Error at readFile (compare cmdPing)");
        }
        //cmdChangelog
        try {
            String comparecmdChangelog = new String(Files.readAllBytes(Paths.get("cmdchangelogcounter.txt")));
            STATIC.comparecmdChangelog = Integer.parseInt(comparecmdChangelog);
        } catch (Exception e) {
            System.out.println("Error at readFile (compare cmdChangelog)");
        }
        //cmdHelp
        try {
            String comparecmdHelp = new String(Files.readAllBytes(Paths.get("cmdhelpcounter.txt")));
            STATIC.comparecmdHelp = Integer.parseInt(comparecmdHelp);
        } catch (Exception e) {
            System.out.println("Error at readFile (compare cmdHelp)");
        }
        //cmdClear
        try {
            String comparecmdClear = new String(Files.readAllBytes(Paths.get("cmdclearcounter.txt")));
            STATIC.comparecmdClear = Integer.parseInt(comparecmdClear);
        } catch (Exception e) {
            System.out.println("Error at readFile (compare cmdClear)");
        }
        //Choose most used command
        if        (STATIC.comparecmdPing >= STATIC.comparecmdStats
                && STATIC.comparecmdPing >= STATIC.comparecmdHelp
                && STATIC.comparecmdPing >= STATIC.comparecmdClear
                && STATIC.comparecmdPing >= STATIC.comparecmdChangelog) {
            STATIC.mostUsedCommand = "ping";
            STATIC.mostUsedCommandValue = STATIC.comparecmdPing;
        } else if (STATIC.comparecmdStats >= STATIC.comparecmdPing
                && STATIC.comparecmdStats >= STATIC.comparecmdHelp
                && STATIC.comparecmdStats >= STATIC.comparecmdClear
                && STATIC.comparecmdStats >= STATIC.comparecmdChangelog) {
            STATIC.mostUsedCommand = "stats";
            STATIC.mostUsedCommandValue = STATIC.comparecmdStats;
        } else if (STATIC.comparecmdHelp >= STATIC.comparecmdPing
                && STATIC.comparecmdHelp >= STATIC.comparecmdStats
                && STATIC.comparecmdHelp >= STATIC.comparecmdClear
                && STATIC.comparecmdHelp >= STATIC.comparecmdChangelog) {
            STATIC.mostUsedCommand = "help";
            STATIC.mostUsedCommandValue = STATIC.comparecmdHelp;
        } else if (STATIC.comparecmdClear >= STATIC.comparecmdPing
                && STATIC.comparecmdClear >= STATIC.comparecmdStats
                && STATIC.comparecmdClear >= STATIC.comparecmdHelp
                && STATIC.comparecmdClear >= STATIC.comparecmdChangelog) {
            STATIC.mostUsedCommand = "clear";
            STATIC.mostUsedCommandValue = STATIC.comparecmdClear;
        } else if (STATIC.comparecmdChangelog >= STATIC.comparecmdPing
                && STATIC.comparecmdChangelog >= STATIC.comparecmdStats
                && STATIC.comparecmdChangelog >= STATIC.comparecmdHelp
                && STATIC.comparecmdChangelog >= STATIC.comparecmdClear) {
            STATIC.mostUsedCommand = "changelog";
            STATIC.mostUsedCommandValue = STATIC.comparecmdChangelog;
        }

        //delete command
        event.getMessage().delete().complete();

        //Output (as embed)
        EmbedBuilder builder = new EmbedBuilder();
        builder.setDescription(
                "**Statistics** " + event.getAuthor().getAsMention() +

                "\n\n**Commands:**" +
                "\nPeople used `" + STATIC.COUNTER + "` commands yet." +
                "\nThe most popular command is: `>" + STATIC.mostUsedCommand + "` with `" + STATIC.mostUsedCommandValue + "` usages." +
                "\n" +
                "\n**Bot Information:** " +
                "\nVersion: Alpha v1.4" +
                "\nAuthor: Tancred#9056" +
                "\nLatest Update: 2018-05-03");
        builder.setColor(Color.decode("0xb8687d"));
        builder.setThumbnail(event.getJDA().getSelfUser().getAvatarUrl()+"?size=64");
        event.getTextChannel().sendMessage(builder.build()).queue();
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
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
