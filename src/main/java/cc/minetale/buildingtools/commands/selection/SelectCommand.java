package cc.minetale.buildingtools.commands.selection;

import cc.minetale.buildingtools.Selection;
import cc.minetale.buildingtools.Utils;
import cc.minetale.commonlib.util.MC;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.relative.ArgumentRelativeBlockPosition;
import net.minestom.server.utils.location.RelativeVec;

public class SelectCommand extends Command {

    public SelectCommand() {
        super("/select", "/sel");

        setDefaultExecutor(this::defaultExecutor);

        var pos1 = new ArgumentRelativeBlockPosition("pos1");
        var pos2 = new ArgumentRelativeBlockPosition("pos2");

        addSyntax(this::setSelection, pos1, pos2);
    }

    private void defaultExecutor(CommandSender sender, CommandContext context) {
        sender.sendMessage(MC.Chat.notificationMessage("Select", Component.text("Usage: //select <pos1> <pos2>", MC.CC.GRAY.getTextColor())));
    }

    private void setSelection(CommandSender sender, CommandContext context) {
        RelativeVec pos1 = context.get("pos1");
        RelativeVec pos2 = context.get("pos2");

        var builder = Utils.getSenderAsBuilder(sender);
        if(builder == null) { return; }

        var selection = builder.getSelection();
        var instance = builder.getInstance();

        var absPos1 = pos1.from(builder);
        var absPos2 = pos2.from(builder);

        if(selection == null) {
            selection = new Selection(
                    absPos1,
                    absPos2,
                    instance,
                    builder);

            builder.setSelection(selection);
        } else {
            selection.setPositions(absPos1, absPos2);
            selection.setInstance(instance);
        }

        sender.sendMessage(MC.Chat.notificationMessage("Select", Component.text("Successfully created a selection with size of " + selection.getSize() + " at pos1: " + absPos1 + " pos2: " + absPos2, MC.CC.GREEN.getTextColor())));
    }

}