package itsu.mcbe.mapbuilder;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.level.Position;

public class SwitchLevelCommand extends Command {

    public SwitchLevelCommand(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (!(commandSender instanceof ConsoleCommandSender)) {
            if (strings.length != 1) {
                commandSender.sendMessage("Usage: /switchlevel <worldname>");
                return false;
            }

            Player player = (Player) commandSender;

            if (Server.getInstance().loadLevel(strings[0])) {
                player.teleport(new Position(128, 128, 128, Server.getInstance().getLevelByName(strings[0])));
                player.sendMessage("Switched level.");
                return true;

            } else {
                player.sendMessage("An error occurred.");
                return true;
            }
        } else {
            commandSender.sendMessage("Run on the game.");
            return false;
        }
    }
}
