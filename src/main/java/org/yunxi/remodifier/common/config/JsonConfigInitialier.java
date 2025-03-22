package org.yunxi.remodifier.common.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraftforge.fml.loading.FMLLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.yunxi.remodifier.Remodifier;
import org.yunxi.remodifier.common.config.toml.ReModifierConfig;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class JsonConfigInitialier {
    public static final Path ROOT = FMLLoader.getGamePath().resolve("config").resolve("remodifier").resolve("json");
    private static final String TYPE = "type", WEIGHT = "weight", ATTRIBUTES = "attributes", AMOUNTS = "amounts", OPERATION_ID = "operationId", RARITY = "rarity", MAX_LEVEL = "maxLevel", ITEM = "item";

    static {
        ROOT.toFile().mkdirs();
        mkdirs("qualities", "modifiers");
        if (ReModifierConfig.CRASHTHEGAME_IFCONFIG_HASERRORS.get()) generateExampleFile();
    }

    private static @NotNull FileWriter writeModifiersFile(File configFile) throws IOException {
        JsonObject config = new JsonObject();
        config.addProperty(TYPE, "ARMOR");
        config.addProperty(WEIGHT, 100);
        config.addProperty(ATTRIBUTES, "minecraft:generic.attack_speed");
        config.addProperty(AMOUNTS, "0.04");
        config.addProperty(OPERATION_ID, 2);
        config.addProperty(RARITY, 1);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileWriter fileWriter = new FileWriter(configFile);
        fileWriter.write(gson.toJson(config));
        return fileWriter;
    }

    private static @NotNull FileWriter writeQualitiesFile(File configFile) throws IOException {
        JsonObject config = new JsonObject();
        config.addProperty(ITEM, "minecraft:gold_ingot");
        config.addProperty(RARITY, 10);
        config.addProperty(MAX_LEVEL, 2);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileWriter fileWriter = new FileWriter(configFile);
        fileWriter.write(gson.toJson(config));
        return fileWriter;
    }

    private static void mkdirs(String @NotNull ... strings) {
        for (String s : strings) ROOT.resolve(s).toFile().mkdirs();
    }

    private static void generateExampleFile() {
        File modifiersViolent = new File(ROOT.resolve("modifier").toFile(), "violent.json");
        File qualitiesViolent = new File(ROOT.resolve("qualities").toFile(), "violent.json");
        if (!modifiersViolent.exists()) {
            try {
                FileWriter writer = writeModifiersFile(modifiersViolent);
                writer.close();
            } catch (Throwable throwable) {
                Remodifier.LOGGER.error("Error occurs during example json file generation", throwable);
            }
        }
        if (!qualitiesViolent.exists()) {
            try {
                FileWriter writer = writeQualitiesFile(qualitiesViolent);
                writer.close();
            } catch (Throwable throwable) {
                Remodifier.LOGGER.error("Error occurs during example json file generation", throwable);
            }
        }
    }

    private static final List<File> MODIFIER_JSONS = readFiles(ROOT.resolve("modifiers"));
    public static final List<? extends String> MODIFIER_NAMES = MODIFIER_JSONS.stream()
            .map(file -> {
                int i = file.getName().lastIndexOf(".");
                return (i != -1) ? file.getName().substring(0, i) : file.getName();
            }).collect(Collectors.toList());
    public static final List<String> MODIFIER_TYPES = getElements(MODIFIER_JSONS, TYPE);
    public static final List<String> MODIFIER_WEIGHTS = getElements(MODIFIER_JSONS, WEIGHT);
    public static final List<String> MODIFIER_ATTRIBUTES = getElements(MODIFIER_JSONS, ATTRIBUTES);
    public static final List<String> MODIFIER_AMOUNTS = getElements(MODIFIER_JSONS, AMOUNTS);
    public static final List<String> MODIFIER_OPERATION_ID = getElements(MODIFIER_JSONS, OPERATION_ID);
    public static final List<String> MODIFIER_RARITY = getElements(MODIFIER_JSONS, RARITY);

    private static final List<File> QUALITY_JSONS = readFiles(ROOT.resolve("qualities"));
    public static final List<? extends String> QUALITY_NAMES = QUALITY_JSONS.stream()
            .map(file -> {
                int i = file.getName().lastIndexOf(".");
                return (i != -1) ? file.getName().substring(0, i) : file.getName();
            }).collect(Collectors.toList());
    public static final List<String> QUALITY_ITEM = getElements(QUALITY_JSONS, ITEM);
    public static final List<String> QUALITY_RARITY = getElements(QUALITY_JSONS, RARITY);
    public static final List<String> QUALITY_MAX_LEVEL = getElements(QUALITY_JSONS, MAX_LEVEL);

    public void init() {
        Logger logger = LogManager.getLogger(JsonConfigInitialier.class);
        logger.info("Loaded modifiers json: {}", Arrays.toString(MODIFIER_NAMES.toArray()));
        logger.info("Loaded qualities json: {}", Arrays.toString(QUALITY_NAMES.toArray()));
    }

    private static @NotNull List<String> getElements(@NotNull Iterable<File> files, String element) {
        List<String> names = new ArrayList<>();
        for (File file : files) {
            try {
                FileReader fileReader = new FileReader(file);
                JsonObject configObject = JsonParser.parseReader(fileReader).getAsJsonObject();
                names.add(configObject.get(element).getAsString());
            } catch (Throwable throwable) {
                Remodifier.LOGGER.error("Error occurs during {} reading", file.getName(), throwable);
            }
        }
        return names;
    }

    private static @NotNull List<File> readFiles(@NotNull Path path) {
        List<File> fileList = new ObjectArrayList<>();
        File folder = path.toFile();
        if (!folder.exists() || !folder.isDirectory()) return Collections.emptyList();
        File[] files = folder.listFiles();
        if (files == null) return Collections.emptyList();
        for (File file : files) {
            if (!file.isFile() || !file.getName().toLowerCase().endsWith(".json")) continue;
            fileList.add(file);
        }
        return fileList;
    }

}
