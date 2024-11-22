package kr.kro.teamdodoco.extra_elytra.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ModMenuIntegration implements ModMenuApi
{

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parentScreen -> showConfig(parentScreen);
    }

    public static Screen showConfig(Screen parentScreen) {
        var modEnableOption = Option.<Boolean>createBuilder() // boolean is the type of option we'll be making
                .name(Text.literal("Enable Mod"))
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
                .name(Text.literal("Instant fly"))
                .binding(
                        false, // the default value
                        () -> ExtraElytraConfig.config.instantFly,
                        newVal ->
                        {
                            ExtraElytraConfig.config.instantFly = newVal;
                            ExtraElytraConfig.saveConfig();
                        }
                )
                .description(OptionDescription.of(Text.literal("Jump to fly, no weird double-jump needed!")))
                .controller(TickBoxControllerBuilder::create)
                .build();

        var speedCtrlOption = Option.<Boolean>createBuilder() // boolean is the type of option we'll be making
                .name(Text.literal("Speed control"))
                .binding(
                        true, // the default value
                        () -> ExtraElytraConfig.config.speedCtrl,
                        newVal ->
                        {
                            ExtraElytraConfig.config.speedCtrl = newVal;
                            ExtraElytraConfig.saveConfig();
                        }
                )
                .description(OptionDescription.of(Text.literal("Control your speed with the Forward and Back keys.\n"
                        + "(default: W and S)\n" + "No fireworks needed!")))
                .controller(TickBoxControllerBuilder::create)
                .build();

        var heightCtrlOption = Option.<Boolean>createBuilder() // boolean is the type of option we'll be making
                .name(Text.literal("Height control"))
                .binding(
                        false, // the default value
                        () -> ExtraElytraConfig.config.heightCtrl,
                        newVal ->
                        {
                            ExtraElytraConfig.config.heightCtrl = newVal;
                            ExtraElytraConfig.saveConfig();
                        }
                )
                .description(OptionDescription.of(Text.literal("Control your height with the Jump and Sneak keys.\n"
                        + "(default: Spacebar and Shift)\n" + "No fireworks needed!")))
                .controller(TickBoxControllerBuilder::create)
                .build();

        var stopInWaterOption = Option.<Boolean>createBuilder() // boolean is the type of option we'll be making
                .name(Text.literal("Stop flying in water"))
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
                .name(Text.literal("Show in chat when enabled"))
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
                .title(Text.literal("Extra Elytra Config"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Generic"))
                        .option(modEnableOption)
                        .option(instantFlyOption)
                        .option(speedCtrlOption)
                        .option(heightCtrlOption)
                        .option(stopInWaterOption)
                        .option(chatLogOption)
                        .build())
                .build()
                .generateScreen(parentScreen);
    }
}
