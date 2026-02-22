package com.example.lagprotection.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class LagConfig {
    public static final ForgeConfigSpec COMMON_CONFIG;
    public static final ForgeConfigSpec.BooleanValue SHOW_HUD;
    public static final ForgeConfigSpec.IntValue FPS_THRESHOLD;
    public static final ForgeConfigSpec.IntValue PROTECTION_DURATION_MS;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("Lag Protection Settings");

        SHOW_HUD = builder
                .comment("Show FPS?")
                .define("showHud", true);

        FPS_THRESHOLD = builder
                .comment("FPS threshold considered screen stuttering")
                .defineInRange("fpsThreshold", 10, 1, 240);

        PROTECTION_DURATION_MS = builder
                .comment("Remaining protection time (ms) when screen stuttering stops")
                .defineInRange("protectionDurationAfterLagMs", 5000, 0, 60000);

        builder.pop();

        COMMON_CONFIG = builder.build();
    }
}
