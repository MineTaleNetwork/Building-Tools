package cc.minetale.buildingtools.commands.magma;

import cc.minetale.buildingtools.Utils;
import cc.minetale.commonlib.util.MC;
import cc.minetale.magma.MagmaReader;
import cc.minetale.magma.MagmaUtils;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentWord;

public class LoadCommand extends Command {

    public LoadCommand() {
        super("/load");

        setDefaultExecutor(this::defaultExecutor);

        var name = new ArgumentWord("name");

        addSyntax(this::loadExecutor, name);
    }

    private void defaultExecutor(CommandSender sender, CommandContext context) {
        sender.sendMessage(MC.Chat.notificationMessage("Load", Component.text("Usage: //load <name>", MC.CC.GRAY.getTextColor())));
    }

    private void loadExecutor(CommandSender sender, CommandContext context) {
        String name = context.get("name");

        var builder = Utils.getSenderAsBuilder(sender);
        if(builder == null) { return; }

        if(!builder.isBuilderMode()) {
            sender.sendMessage(MC.Chat.notificationMessage("Load", Component.text("You need to be in builder mode to execute this command!", MC.CC.RED.getTextColor())));
            return;
        }

        MagmaReader.read(MagmaUtils.getDefaultLocation(name))
                .thenAccept(region -> {
                    //TODO
                });
    }

}
