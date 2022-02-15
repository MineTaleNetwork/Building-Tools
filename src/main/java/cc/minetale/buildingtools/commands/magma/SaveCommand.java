package cc.minetale.buildingtools.commands.magma;

import cc.minetale.buildingtools.Builder;
import cc.minetale.commonlib.util.Message;
import cc.minetale.magma.MagmaUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentWord;

public class SaveCommand extends Command {

    public SaveCommand() {
        super("/save");

        setDefaultExecutor(this::defaultExecutor);

        var name = new ArgumentWord("name");

        addSyntax(this::saveExecutor, name);
    }

    private void defaultExecutor(CommandSender sender, CommandContext context) {
        sender.sendMessage(Message.notification("BT",
                Component.text("Usage: //save <name>", NamedTextColor.GRAY)));
    }

    private void saveExecutor(CommandSender sender, CommandContext context) {
        String name = context.get("name");

        var builder = Builder.fromSender(sender);
        if(builder == null) { return; }

        var selection = builder.getSelection();

        if(selection == null || selection.isIncomplete()) {
            sender.sendMessage(Message.notification("BT",
                    Component.text("You don't have a complete selection!", NamedTextColor.RED)));
            return;
        }

        var location = MagmaUtils.getDefaultLocation(name);

        selection.save(location)
                .thenAccept(success -> {
                    //TODO Success is false even if it was successful
                    if(success) {
                        builder.sendMessage(Message.notification("BT",
                                Component.text("Successfully saved to " + location, NamedTextColor.GREEN)));
                    } else {
                        builder.sendMessage(Message.notification("BT",
                                Component.text("Couldn't save to " + location, NamedTextColor.RED)));
                    }
                });
    }

}