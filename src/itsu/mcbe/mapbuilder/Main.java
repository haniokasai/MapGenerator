package itsu.mcbe.mapbuilder;

import cn.nukkit.Server;
import cn.nukkit.level.format.LevelProviderManager;
import cn.nukkit.level.format.anvil.Anvil;
import cn.nukkit.level.format.leveldb.LevelDB;
import cn.nukkit.level.format.mcregion.McRegion;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.plugin.PluginBase;

import java.io.File;

public class Main extends PluginBase {

    @Override
    public void onEnable() {
        new File("./plugins/MapGenerator/images").mkdirs();

        LevelProviderManager.addProvider(Server.getInstance(), Anvil.class);
        LevelProviderManager.addProvider(Server.getInstance(), McRegion.class);
        LevelProviderManager.addProvider(Server.getInstance(), LevelDB.class);

        Generator.addGenerator(MapGenerator.class, MapGenerator.NAME, MapGenerator.TYPE_MAPGENERATOR);

        Server.getInstance().getCommandMap().register("generatelevel", new GenerateMapCommand("generatelevel"));
        Server.getInstance().getCommandMap().register("switchlevel", new SwitchLevelCommand("switchlevel"));
    }

}
