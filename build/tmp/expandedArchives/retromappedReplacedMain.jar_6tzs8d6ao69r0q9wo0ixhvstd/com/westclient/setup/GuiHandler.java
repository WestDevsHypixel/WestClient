package com.westclient.setup;

import com.westclient.setup.ui.SetupGuiScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiHandler {
    private static boolean shouldOpenGui = false;

    public static void scheduleGuiOpen() {
        shouldOpenGui = true;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && shouldOpenGui) {
            Minecraft.func_71410_x().func_147108_a(new SetupGuiScreen());
            shouldOpenGui = false;
        }
    }

    public static void register() {
        MinecraftForge.EVENT_BUS.register(new GuiHandler());
    }
}



