package com.example.lagprotection.client;

import com.example.lagprotection.config.LagConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fml.config.ModConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;

public class ClothConfigScreen {

    public static Screen create(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.literal("Lag Protection Config"));

        builder.setSavingRunnable(() -> {
            LagConfig.COMMON_CONFIG.save();
        });

        ConfigCategory category = builder.getOrCreateCategory(Component.literal("Setting"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        category.addEntry(entryBuilder.startBooleanToggle(
                        Component.literal("Show FPS"),
                        LagConfig.SHOW_HUD.get())
                .setDefaultValue(true)
                .setSaveConsumer(LagConfig.SHOW_HUD::set)
                .build());

        category.addEntry(entryBuilder.startIntField(
                        Component.literal("FPS threshold considered screen stuttering"),
                        LagConfig.FPS_THRESHOLD.get())
                .setDefaultValue(10)
                .setMin(1)
                .setMax(240)
                .setSaveConsumer(LagConfig.FPS_THRESHOLD::set)
                .build());

        category.addEntry(entryBuilder.startIntField(
                        Component.literal("Remaining protection time when screen stuttering stops"),
                        LagConfig.PROTECTION_DURATION_MS.get())
                .setDefaultValue(5000)
                .setMin(0)
                .setMax(60000)
                .setSaveConsumer(LagConfig.PROTECTION_DURATION_MS::set)
                .build());

        return builder.build();
    }
}
