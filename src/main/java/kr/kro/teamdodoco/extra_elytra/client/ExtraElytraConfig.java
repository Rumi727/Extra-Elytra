package kr.kro.teamdodoco.extra_elytra.client;

import net.fabricmc.api.ClientModInitializer;

import java.io.*;

public class ExtraElytraConfig implements ClientModInitializer
{
    public static ExtraElytraConfigFile config;

    static final File configFile = new File("config/extra_elytra_config.toml");

    @Override
    public void onInitializeClient()
    {
        loadConfig();
    }

    public static void loadConfig()
    {
        config = new ExtraElytraConfigFile();

        if (!configFile.exists())
        {
            saveConfig();
        }
        else
        {
            try
            {
                FileReader rw = new FileReader(configFile);
                BufferedReader br = new BufferedReader(rw);

                String readLine;
                while ((readLine = br.readLine()) != null)
                {
                    switch (readLine) {
                        case "enableMod = true" -> config.enableMod = true;
                        case "instantFly = true" -> config.instantFly = true;
                        case "speedCtrl = true" -> config.speedCtrl = true;
                        case "heightCtrl = true" -> config.heightCtrl = true;
                        case "hovering = true" -> config.hovering = true;
                        case "stopInWater = true" -> config.stopInWater = true;
                        case "chatLog = true" -> config.chatLog = true;
                    }
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void saveConfig()
    {
        try
        {
            FileWriter fw = new FileWriter(configFile);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("enableMod = " + config.enableMod);
            bw.newLine();

            bw.write("instantFly = " + config.instantFly);
            bw.newLine();

            bw.write("speedCtrl = " + config.speedCtrl);
            bw.newLine();

            bw.write("heightCtrl = " + config.heightCtrl);
            bw.newLine();

            bw.write("hovering = " + config.hovering);
            bw.newLine();

            bw.write("stopInWater = " + config.stopInWater);
            bw.newLine();

            bw.write("chatLog = " + config.chatLog);
            bw.newLine();

            bw.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
