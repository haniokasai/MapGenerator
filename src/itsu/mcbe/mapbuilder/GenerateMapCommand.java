package itsu.mcbe.mapbuilder;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class GenerateMapCommand extends Command {

    public GenerateMapCommand(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (commandSender instanceof ConsoleCommandSender || commandSender.isOp()) {
            if (strings.length != 2) {
                commandSender.sendMessage("Usage: /generatemap <worldname> <imagesource>");
                return false;
            }

            if (new File("./plugins/worlds/" + strings[0]).exists()) {
                commandSender.sendMessage("The world is already exists!");
                return false;
            }

            if (!new File("./plugins/MapGenerator/" + strings[1]).exists()) {
                commandSender.sendMessage("Map image does not found.");
                return false;
            }

            long time = System.currentTimeMillis();

            try {
                Utils.copyFile(new File("./plugins/MapGenerator/" + strings[1]), new File("./plugins/MapGenerator/images/" + time + ".mapimg"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            Server.getInstance().generateLevel(strings[0], time, MapGenerator.class, new HashMap<String, Object>(){{put("source", "./plugins/MapGenerator/" + strings[1]);}});
            Server.getInstance().loadLevel(strings[0]);

            commandSender.sendMessage("Generated level: " + strings[0]);

            return true;
        }
        return false;
    }

}
