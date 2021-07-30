package com.github.charlyb01.xpstorage_trinkets.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "xp_storage_trinkets")
public class ModConfig implements ConfigData {
    @ConfigEntry.Gui.RequiresRestart
    public boolean xpConduitAllowed = true;

    @ConfigEntry.BoundedDiscrete(min = 100, max = 2000)
    @ConfigEntry.Gui.RequiresRestart
    public int xpConduitMaxDamage = 1000;

    @ConfigEntry.Gui.RequiresRestart
    public boolean xpSaverAllowed = true;

    @ConfigEntry.BoundedDiscrete(min = 1, max = 10)
    @ConfigEntry.Gui.RequiresRestart
    public int xpSaverMaxDamage = 1;


    public static ModConfig get() {
        return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }
}
