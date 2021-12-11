package cc.minetale.buildingtools.commands;

import cc.minetale.buildingtools.Utils;
import cc.minetale.commonlib.util.MC;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;

public class ModeCommand extends Command {

    public ModeCommand() {
        super("/buildermode", "/bm");

        setDefaultExecutor(this::defaultExecutor);
    }

    private void defaultExecutor(CommandSender sender, CommandContext context) {
        var builder = Utils.getSenderAsBuilder(sender);
        if(builder == null) { return; }

        var builderMode = builder.isBuilderMode();
        if(!builderMode) {
            sender.sendMessage(MC.notificationMessage("BT",
                    Component.text("Builder mode has been enabled", NamedTextColor.GREEN)));
        } else {
            sender.sendMessage(MC.notificationMessage("BT",
                    Component.text("Builder mode has been disabled", NamedTextColor.RED)));
        }
        builder.setBuilderMode(!builderMode);
    }

}