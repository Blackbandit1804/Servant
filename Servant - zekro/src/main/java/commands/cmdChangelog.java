package commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.requests.restaction.MessageAction;
import util.STATIC;
import util.createFilecmdChangelog;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class cmdChangelog implements Command {

    //Timestamp
    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    //Formatting for embeds
    EmbedBuilder info = new EmbedBuilder().setColor(Color.CYAN);

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        //cmdChangelog counter
        try {
            String contentcmdChangelog = new String(Files.readAllBytes(Paths.get("cmdchangelogcounter.txt")));
            STATIC.cmdChangelogCOUNTER = Integer.parseInt(contentcmdChangelog);
        } catch (Exception e) {
            System.out.println("Error at readFile (cmdChangelog)");
        }
        STATIC.cmdChangelogCOUNTER = STATIC.cmdChangelogCOUNTER +1;
        createFilecmdChangelog gcmdChangelog = new createFilecmdChangelog();
        gcmdChangelog.openFile();
        gcmdChangelog.addRecords();
        gcmdChangelog.closeFile();

        //delete command
        event.getMessage().delete().complete();

        //Output (as embed)
        EmbedBuilder builder = new EmbedBuilder();
        builder.setDescription(
                        "**Changelog** " + event.getAuthor().getAsMention() +

                        "\n\n2018-05-03 Alpha v1.4" +
                        "\n   - New Commands: `coinflip` / `cf`, `music` / `m`" +
                        "\n   - Music Bot (not functional yet)" +
                        "\n   - Reworked `ping` command and log files" +
                        "\n   - New alias: `changelog` -> `cl`" +
                        "\n   - Bug fixes" +

                        "\n\n2018-05-01 Alpha v1.3.1:" +
                        "\n   - Bug fixes" +

                        "\n\n2018-04-30 Alpha v1.3:" +
                        "\n   - Timestamps in log files" +
                        "\n   - Auto-reaction on 'charr' and 'tadaa'" +
                        "\n   - Bug fixes" +

                        "\n\n2018-04-30 Alpha v1.2:" +
                        "\n   - New Commands: `help`, `changelog` and `clear`" +
                        "\n   - Added permissions" +
                        "\n   - Graphical Update" +
                        "\n   - QoL Changes (Command gets deleted)" +
                        "\n   - Timestamps in log files" +
                        "\n   - Auto-reaction on 'charr' and 'tadaa'" +
                        "\n   - Bug fixes" +

                        "\n\n2018-04-06 Alpha v1.0:" +
                        "\n   - Alpha Release.");
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
