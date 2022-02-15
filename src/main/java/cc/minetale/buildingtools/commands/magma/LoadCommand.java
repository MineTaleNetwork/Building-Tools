package cc.minetale.buildingtools.commands.magma;

import cc.minetale.buildingtools.Builder;
import cc.minetale.commonlib.util.Message;
import cc.minetale.magma.MagmaReader;
import cc.minetale.magma.MagmaUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
        sender.sendMessage(Message.notification("BT",
                Component.text("Usage: //load <name>", NamedTextColor.GRAY)));
    }

    private void loadExecutor(CommandSender sender, CommandContext context) {
        String name = context.get("name");

        var builder = Builder.fromSender(sender);
        if(builder == null) { return; }

        if(!builder.isBuilderMode()) {
            sender.sendMessage(Message.notification("BT",
                    Component.text("You need to be in builder mode to execute this command!", NamedTextColor.RED)));
            return;
        }

        MagmaReader.read(MagmaUtils.getDefaultLocation(name))
                .thenAccept(region -> {
                    //TODO
                });
    }

}
