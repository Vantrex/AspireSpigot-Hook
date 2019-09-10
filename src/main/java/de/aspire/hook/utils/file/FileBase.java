package de.aspire.hook.utils.file;


import de.aspire.hook.AspireSpigotHook;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileBase {

    // Credits @TeamHardcore

    private String fileName;
    private YamlConfiguration configuration;
    private File file;

    public FileBase(String fileName) {

        this.fileName = fileName;
    }

    private void setupConfig() {
        if (file == null) {
            this.file = new File(AspireSpigotHook.getInstance().getDataFolder() , this.fileName + ".yml");
        }
        if (!new File(AspireSpigotHook.getInstance().getDataFolder() + "").exists()) {
            try {
                new File(AspireSpigotHook.getInstance().getDataFolder() + "").mkdir();
                this.file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(FileBase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        this.configuration = YamlConfiguration.loadConfiguration(this.file);
    }

    public YamlConfiguration getConfig() {
        if (this.configuration == null) {
            setupConfig();
        }
        return this.configuration;
    }

    public void setConfig(YamlConfiguration configuration) {
        this.configuration = configuration;
    }

    public File getFile() {
        return file;
    }

    public void saveConfig() {
        if ((this.file == null) || (this.configuration == null)) {
            return;
        }
        try {
            this.configuration.save(this.file);
        } catch (IOException ex) {
            Logger.getLogger(FileBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
