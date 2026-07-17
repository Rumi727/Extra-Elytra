package kr.kro.teamdodoco.extra_elytra.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ModMenuIntegration implements ModMenuApi
{

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parentScreen -> showConfig(parentScreen);
    }

    public static Screen showConfig(Screen parentScreen) {
        var modEnableOption = Option.<Boolean>createBuilder() // boolean is the type of option we'll be making
                .name(Component.literal("Enable Mod"))
                .binding(
                        true, // the default value
                        () -> ExtraElytraConfig.config.enableMod,
                        newVal ->
                        {
                            ExtraElytraConfig.config.enableMod = newVal;
                            ExtraElytraConfig.saveConfig();
                        }
                )
                .controller(TickBoxControllerBuilder::create)
                .build();

        var instantFlyOption = Option.<Boolean>createBuilder() // boolean is the type of option we'll be making
                .name(Component.literal("Instant fly"))
                .binding(
                        false, // the default value
                        () -> ExtraElytraConfig.config.instantFly,
                        newVal ->
                        {
                            ExtraElytraConfig.config.instantFly = newVal;
                            ExtraElytraConfig.saveConfig();
                        }
                )
                .description(OptionDescription.of(Component.literal("Jump to fly, no weird double-jump needed!")))
                .controller(TickBoxControllerBuilder::create)
                .build();

        var speedCtrlOption = Option.<Boolean>createBuilder() // boolean is the type of option we'll be making
                .name(Component.literal("Speed control"))
                .binding(
                        true, // the default value
                        () -> ExtraElytraConfig.config.speedCtrl,
                        newVal ->
                        {
                            ExtraElytraConfig.config.speedCtrl = newVal;
                            ExtraElytraConfig.saveConfig();
                        }
                )
                .description(OptionDescription.of(Component.literal("Control your speed with the Forward and Back keys.\n"
                        + "(default: W and S)\n" + "No fireworks needed!")))
                .controller(TickBoxControllerBuilder::create)
                .build();

        var heightCtrlOption = Option.<Boolean>createBuilder() // boolean is the type of option we'll be making
                .name(Component.literal("Height control"))
                .binding(
                        false, // the default value
                        () -> ExtraElytraConfig.config.heightCtrl,
                        newVal ->
                        {
                            ExtraElytraConfig.config.heightCtrl = newVal;
                            ExtraElytraConfig.saveConfig();
                        }
                )
                .description(OptionDescription.of(Component.literal("Control your height with the Jump and Sneak keys.\n"
                        + "(default: Spacebar and Shift)\n" + "No fireworks needed!")))
                .controller(TickBoxControllerBuilder::create)
                .build();

        var hoveringOption = Option.<Boolean>createBuilder() // boolean is the type of option we'll be making
                .name(Component.literal("Hovering"))
                .binding(
                        false, // the default value
                        () -> ExtraElytraConfig.config.hovering,
                        newVal ->
                        {
                            ExtraElytraConfig.config.hovering = newVal;
                            ExtraElytraConfig.saveConfig();
                        }
                )
                .description(OptionDescription.of(Component.literal("If height control is enabled, holding shift and space together will hover.")))
                .controller(TickBoxControllerBuilder::create)
                .build();

        var stopInWaterOption = Option.<Boolean>createBuilder() // boolean is the type of option we'll be making
                .name(Component.literal("Stop flying in water"))
                .binding(
                        true, // the default value
                        () -> ExtraElytraConfig.config.stopInWater,
                        newVal ->
                        {
                            ExtraElytraConfig.config.stopInWater = newVal;
                            ExtraElytraConfig.saveConfig();
                        }
                )
                .controller(TickBoxControllerBuilder::create)
                .build();

        var chatLogOption = Option.<Boolean>createBuilder() // boolean is the type of option we'll be making
                .name(Component.literal("Show in chat when enabled"))
                .binding(
                        false, // the default value
                        () -> ExtraElytraConfig.config.chatLog,
                        newVal ->
                        {
                            ExtraElytraConfig.config.chatLog = newVal;
                            ExtraElytraConfig.saveConfig();
                        }
                )
                .controller(TickBoxControllerBuilder::create)
                .build();



        return YetAnotherConfigLib.createBuilder()
                .title(Component.literal("Extra Elytra Config"))
                .category(ConfigCategory.createBuilder()
                        .name(Component.literal("Generic"))
                        .option(modEnableOption)
                        .option(instantFlyOption)
                        .option(speedCtrlOption)
                        .option(heightCtrlOption)
                        .option(hoveringOption)
                        .option(stopInWaterOption)
                        .option(chatLogOption)
                        .build())
                .build()
                .generateScreen(parentScreen);
    }
}
