package cc.minetale.buildingtools.commands.magma;

import cc.minetale.buildingtools.Utils;
import cc.minetale.commonlib.util.MC;
import cc.minetale.magma.MagmaUtils;
import net.kyori.adventure.text.Component;
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
        sender.sendMessage(MC.Chat.notificationMessage("BT", Component.text("Usage: //save <name>", MC.CC.GRAY.getTextColor())));
    }

    private void saveExecutor(CommandSender sender, CommandContext context) {
        String name = context.get("name");

        var builder = Utils.getSenderAsBuilder(sender);
        if(builder == null) { return; }

        var selection = builder.getSelection();

        if(selection == null || selection.isIncomplete()) {
            sender.sendMessage(MC.Chat.notificationMessage("BT", Component.text("You don't have a complete selection!", MC.CC.RED.getTextColor())));
            return;
        }

        var location = MagmaUtils.getDefaultLocation(name);

        selection.save(location)
                .thenAccept(success -> {
                    //TODO Success is false even if it was successful
                    if(success) {
                        builder.sendMessage(MC.Chat.notificationMessage("BT", Component.text("Successfully saved to " + location, MC.CC.GREEN.getTextColor())));
                    } else {
                        builder.sendMessage(MC.Chat.notificationMessage("BT", Component.text("Couldn't save to " + location, MC.CC.RED.getTextColor())));
                    }
                });
    }

}