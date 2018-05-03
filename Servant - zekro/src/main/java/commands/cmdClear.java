package commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import core.permsCore;
import util.STATIC;
import util.createFilecmdClear;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;


public class cmdClear implements Command {

    //Timestamp
    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    //Formatting for embeds
    EmbedBuilder success = new EmbedBuilder().setColor(Color.GREEN);
    EmbedBuilder error = new EmbedBuilder().setColor(Color.RED);

    //Value of deleted messages for public void executed
    int number;

    private int getInt(String string) {
        try{
            return Integer.parseInt(string);
        } catch (Exception e) {
              //some retard didn't use numbers
            return 0;
        }
    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        try {
            //cmdClear counter
            try {
                String contentcmdClear = new String(Files.readAllBytes(Paths.get("cmdclearcounter.txt")));
                STATIC.cmdClearCOUNTER = Integer.parseInt(contentcmdClear);
            } catch (Exception e) {
                System.out.println("Error at readFile (cmdClear)");
            }
            STATIC.cmdClearCOUNTER = STATIC.cmdClearCOUNTER + 1;
            createFilecmdClear gcmdClear = new createFilecmdClear();
            gcmdClear.openFile();
            gcmdClear.addRecords();
            gcmdClear.closeFile();

            //Check permissions
            if (permsCore.check(event)) return;

            //Error message if no args
            if (args.length < 1) {
                //delete command
                event.getMessage().delete().complete();

                //error message + delete after 5 sec
                Message missingnumber = event.getTextChannel().sendMessage(
                        error.setDescription("**[ERRROR]** " + event.getMessage().getAuthor().getAsMention() + ", please enter a number of messages you want to delete!").build()
                ).complete();

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        missingnumber.delete().queue();
                    }
                }, 5000);
            }

            //Initialize
            int numb = getInt(args[0]);
            //Value of deleted messages for public void executed
            number = numb;

            //Delete messages if numbers are okay, else error message and delete error message after 5 seconds
            if (numb > 1 && numb <= 100) {
                try {
                    //delete command
                    event.getMessage().delete().complete();

                    //delete messages
                    MessageHistory history = new MessageHistory(event.getTextChannel());
                    List<Message> msgs;
                    msgs = history.retrievePast(numb).complete();
                    event.getTextChannel().deleteMessages(msgs).queue();

                    Message delete = event.getTextChannel().sendMessage(
                            success.setDescription("**[SUCCESS]** Deleted " + args[0] + " messages!").build()
                    ).complete();

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            delete.delete().queue();
                        }
                    }, 5000);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                //delete command
                event.getMessage().delete().complete();

                //error message + delete after 5 sec
                Message err = event.getTextChannel().sendMessage(
                        error.setDescription("**[ERRROR]** " + event.getMessage().getAuthor().getAsMention() + ", please enter a number of messages between 2 and 100!").build()
                ).complete();

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        err.delete().queue();
                    }
                }, 5000);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            //someone typed >clear without any args. just ignore
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
        try {
            //commandlog message
            EmbedBuilder builder = new EmbedBuilder();
            try { //user has at least one role
                for (Role r : event.getGuild().getMember(event.getAuthor()).getRoles()) {
                    if (Arrays.stream(STATIC.PERMS).parallel().anyMatch(r.getName()::contains)) {
                        //user has permission to clear
                        builder.setAuthor("[" + event.getMember().getRoles().get(0).getName() + "] " + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator(), null, event.getAuthor().getAvatarUrl());
                        builder.setDescription("```" + event.getMessage().getContentDisplay() + "```");
                        builder.setFooter("#" + event.getTextChannel().getName() + " | " + getCurrentTimeStamp(), null);
                        builder.setColor(Color.CYAN);
                        event.getGuild().getTextChannelsByName("commandlog", true).get(0).sendMessage(builder.build()).queue();
                        return;
                    }
                }
                //user doesn't have permission to clear
                builder.setAuthor("[" + event.getMember().getRoles().get(0).getName() + "] " + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator(), null, event.getAuthor().getAvatarUrl());
                builder.setDescription("```" + event.getMessage().getContentDisplay() + "```" +
                        "\nThis user doesn't have enough permissions to clear!");
                builder.setFooter("#" + event.getTextChannel().getName() + " | " + getCurrentTimeStamp(), null);
                builder.setColor(Color.CYAN);
                event.getGuild().getTextChannelsByName("commandlog", true).get(0).sendMessage(builder.build()).queue();
            } catch (Exception e) {
                //user has no roles
                builder.setAuthor(event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator(), null, event.getAuthor().getAvatarUrl());
                builder.setDescription("```" + event.getMessage().getContentDisplay() + "```" +
                        "\nThis user doesn't have enough permissions to clear!");
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