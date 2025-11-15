package com.westclient.setup;

import java.util.Arrays;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SetupCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "wc";
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("westclient");
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/wc [help]";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            GuiHandler.scheduleGuiOpen();
            return;
        }

        if (args.length == 1 && "help".equalsIgnoreCase(args[0])) {
            sender.addChatMessage(new ChatComponentText("WestClient commands:\n"
                + "/wc or /westclient - open the WestClient setup UI.\n"
                + "/wc help - show this help message."));
            return;
        }

        sender.addChatMessage(new ChatComponentText("Incorrect command usage. Try /wc or /wc help."));
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}

