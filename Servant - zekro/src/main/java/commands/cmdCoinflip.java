package commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class cmdCoinflip implements Command {

    Random rand = new Random();

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

        //initialize embed builder
        EmbedBuilder builder = new EmbedBuilder();

        //random
        int coin = rand.nextInt(2) + 1;

        if (coin == 1) {
            builder.setDescription(event.getJDA().getGuildById("393696449730314242").getEmoteById("393696543309561856").getAsMention() + " **Head!** (Kopf) " + event.getAuthor().getAsMention());
            builder.setColor(Color.CYAN);
            event.getTextChannel().sendMessage(builder.build()).queue();
        } else {
            builder.setDescription(event.getJDA().getGuildById("436925371577925642").getEmoteById("441331873193394216").getAsMention() + " **Tail!** (Zahl) " + event.getAuthor().getAsMention());
            builder.setColor(Color.CYAN);
            event.getTextChannel().sendMessage(builder.build()).queue();
        }
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
