package itsu.mcbe.mapbuilder;

import cn.nukkit.Server;
import cn.nukkit.block.*;
import cn.nukkit.block.BlockGravel;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.level.generator.object.ore.OreType;
import cn.nukkit.level.generator.populator.impl.PopulatorOre;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapGenerator extends Generator {

    static final int TYPE_MAPGENERATOR = 4;
    static final String NAME = "MapGenerator";

    private BufferedImage image;

    private ChunkManager chunkManager;
    private NukkitRandom random;
    private final List<Populator> populators = new ArrayList<>();

    @Override
    public int getId() {
        return TYPE_MAPGENERATOR;
    }

    public MapGenerator(Map<String, Object> options) {
        PopulatorOre ores = new PopulatorOre();
        ores.setOreTypes(new OreType[]{
                new OreType(new BlockOreCoal(), 20, 16, 0, 256),
                new OreType(new BlockOreIron(), 20, 8, 0, 64),
                new OreType(new BlockOreRedstone(), 8, 7, 0, 16),
                new OreType(new BlockOreLapis(), 1, 6, 0, 32),
                new OreType(new BlockOreGold(), 2, 8, 0, 32),
                new OreType(new BlockOreDiamond(), 1, 7, 0, 16),
                new OreType(new BlockStone(), 20, 32, 0, 230),
                new OreType(new BlockGravel(), 20, 16, 0, 256),
                new OreType(new BlockSnow(),20,32,200,256)
        });
        this.populators.add(ores);

        if (options.containsKey("source")) {
            try {
                image = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(String.valueOf(options.get("source"))));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void init(ChunkManager chunkManager, NukkitRandom nukkitRandom) {
        this.chunkManager = chunkManager;
        this.random = nukkitRandom;

        if (image == null) {
            try {
                image = ImageIO.read(new File("./plugins/MapGenerator/images/" + chunkManager.getSeed() + ".mapimg"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void generateChunk(int chunkX, int chunkZ) {
        this.generateChunk(chunkManager.getChunk(chunkX, chunkZ));
    }

    private void generateChunk(FullChunk chunk) {
        chunk.setGenerated();

        int startX = 16 * chunk.getX();
        int startY = 16 * chunk.getZ();

        if (startX > image.getWidth() || startY > image.getHeight()) {
            for (int x = 0; x < 16; ++x) {
                for (int z = 0; z < 16; ++z) {
                    chunk.setBlock(x, 1, z, 0);
                }
            }
            return;
        }

        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                try {
                    int color = image.getRGB(startX + x, startY + z);
                    double r = color >> 16 & 0xff;
                    double g = (color >> 8) & 0xff;
                    double b = color & 0xff;

                    int blockHeight = (int) Math.round((r + g + b) / 3.0d);
                    if (blockHeight != 255) {
                        for (int h = 0; h < blockHeight; ++h) {
                            chunk.setBlock(x, h, z, 2);
                        }
                    } else {
                        chunk.setBlock(x, 1, z, 9);
                        chunk.setBlock(x, 0, z, 1);
                    }

                } catch (ArrayIndexOutOfBoundsException e) {
                    chunk.setBlock(x, 1, z, 0);
                }
            }
        }
    }

    @Override
    public void populateChunk(int chunkX, int chunkZ) {
        this.random.setSeed(0xdeadbeef ^ (chunkX << 8) ^ chunkZ ^ this.chunkManager.getSeed());
        for (Populator populator : this.populators) {
            populator.populate(this.chunkManager, chunkX, chunkZ, this.random, chunkManager.getChunk(chunkX, chunkZ));
        }
    }

    @Override
    public Map<String, Object> getSettings() {
        return null;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Vector3 getSpawn() {
        return new Vector3(0, 128, 0);
    }

    @Override
    public ChunkManager getChunkManager() {
        return chunkManager;
    }

}
