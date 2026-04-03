package com.minhhjjj.stutterprotection.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class LagConfig {
    public static final ModConfigSpec COMMON_CONFIG;
    public static final ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
    
    public static final ModConfigSpec.BooleanValue SHOW_HUD;

    static {
        builder.push("Lag Protection Settings");

        SHOW_HUD = builder
                .comment("Show FPS?")
                .define("showHud", true);

        builder.pop();
        COMMON_CONFIG = builder.build();
    }
}