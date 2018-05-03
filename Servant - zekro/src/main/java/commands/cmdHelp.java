package commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.STATIC;
import util.createFilecmdChangelog;
import util.createFilecmdHelp;

import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class cmdHelp implements Command {

    //Timestamp
    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    EmbedBuilder info = new EmbedBuilder().setColor(Color.CYAN);

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        //cmdHelp counter
        try {
            String contentcmdHelp = new String(Files.readAllBytes(Paths.get("cmdhelpcounter.txt")));
            STATIC.cmdHelpCOUNTER = Integer.parseInt(contentcmdHelp);
        } catch (Exception e) {
            System.out.println("Error at readFile (cmdHelp)");
        }
        STATIC.cmdHelpCOUNTER = STATIC.cmdHelpCOUNTER +1;
        createFilecmdHelp gcmdHelp = new createFilecmdHelp();
        gcmdHelp.openFile();
        gcmdHelp.addRecords();
        gcmdHelp.closeFile();

        //delete command
        event.getMessage().delete().complete();

        //Output (as embed)
        EmbedBuilder builder = new EmbedBuilder();
        builder.setDescription(
                        "**Help** " + event.getAuthor().getAsMention() +

                        "\n\n**Commands:**" +
                        "\n`>help` - Shows this message." +
                        "\n`>ping` - Shows the ping in ms'." +
                        "\n`>stats` - Shows statistics of this bot." +
                        "\n`>changelog` / `>cl` - Shows the changes per version of this bot." +
                        "\n`>clear <amount>` - Deletes messages. (Admin only)" +
                        "\n`>coinflip` / `>cf` - Throws a coin." +
                        "\n\n**Music Bot (YouTube):**" +
                        "\n`>music` / `>m` - Music Bot" +
                        "\n   - `play <url>` / `p <url>` - Plays a song or playlist." +
                        "\n   - `skip` / `s` - Skips the current song." +
                        "\n   - `stop` - Stops the bot." +
                        "\n   - `shuffle` - Shuffles the current playlist." +
                        "\n   - `now` / `np` - Shows name, duration and author of the current song." +
                        "\n   - `queue` - Shows the current queue.");
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
