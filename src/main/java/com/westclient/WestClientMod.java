package com.westclient;

import com.westclient.setup.GuiHandler;
import com.westclient.setup.SetupCommand;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(
        modid = WestClientMod.MOD_ID,
        name = WestClientMod.NAME,
        version = WestClientMod.VERSION
)
public class WestClientMod {
    public static final String MOD_ID = "westclient";
    public static final String NAME = "WestClient";
    public static final String VERSION = "1.0";

    @EventHandler
    public void init(FMLInitializationEvent event) {
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            GuiHandler.register();
            ClientCommandHandler.instance.registerCommand(new SetupCommand());
        }
    }
}

