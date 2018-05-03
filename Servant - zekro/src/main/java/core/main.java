package core;

import commands.*;
import listeners.commandListener;
import listeners.readyListener;
import listeners.voiceListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import util.secrets;

import javax.security.auth.login.LoginException;

public class main {

    public static JDABuilder builder;

    public static void main(String[] Args) {

        builder = new JDABuilder(AccountType.BOT);

        builder.setToken(secrets.TOKEN);
        builder.setAutoReconnect(true);

        builder.setStatus(OnlineStatus.ONLINE);
        builder.setGame(Game.listening("your heart. | >help"));

        addListeners();
        addCommands();

        try {
            JDA jda = builder.buildBlocking();
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void addCommands() {

        commandHandler.commands.put("ping", new cmdPing());
        commandHandler.commands.put("stats", new cmdStats());
        commandHandler.commands.put("help", new cmdHelp());
        commandHandler.commands.put("changelog", new cmdChangelog());
        commandHandler.commands.put("cl", new cmdChangelog()); //alias changelog
        commandHandler.commands.put("clear", new cmdClear()); //Mod only
        commandHandler.commands.put("music", new cmdMusic());
        commandHandler.commands.put("m", new cmdMusic()); //alias music
        commandHandler.commands.put("coinflip", new cmdCoinflip());
        commandHandler.commands.put("cf", new cmdCoinflip()); //alias coinflip

    }

    public static void addListeners() {
        builder.addEventListener(new readyListener());
        builder.addEventListener(new voiceListener());
        builder.addEventListener(new commandListener());

    }
}
