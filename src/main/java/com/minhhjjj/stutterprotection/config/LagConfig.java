package com.minhhjjj.stutterprotection.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class LagConfig {
    public static final ForgeConfigSpec CLIENT_CONFIG;
    public static final ForgeConfigSpec.BooleanValue SHOW_HUD;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("Lag Protection Settings");

        SHOW_HUD = builder
                .comment("Show FPS?")
                .define("showHud", true);

        builder.pop();

        CLIENT_CONFIG = builder.build();
    }
}
