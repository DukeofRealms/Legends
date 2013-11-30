package com.mcthepond.champs.library.configuration;

import com.mcthepond.champs.library.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * @author YoshiGenius
 */
public class BaseConfiguration {

    private static BaseConfiguration instance = new BaseConfiguration();

    private File file;
    private YamlConfiguration config;
    private boolean loaded = false;

    public static BaseConfiguration getInstance() {
        return instance;
    }

    public File getFile() {
        return this.file;
    }

    public YamlConfiguration getConfig() {
        return this.config;
    }

    public void load(File file) {
        if (file != null && file.getName().endsWith(".yml") && !loaded) {
            this.loaded = true;
            this.file = file;
            this.config = YamlConfiguration.loadConfiguration(file);
        }
    }

}
