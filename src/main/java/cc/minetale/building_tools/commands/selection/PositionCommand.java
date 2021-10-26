package cc.minetale.building_tools.commands.selection;

import cc.minetale.building_tools.Builder;
import cc.minetale.building_tools.Selection;
import cc.minetale.building_tools.Utils;
import cc.minetale.commonlib.util.MC;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.command.builder.arguments.number.ArgumentNumber;
import net.minestom.server.command.builder.arguments.relative.ArgumentRelativeBlockPosition;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.Instance;
import net.minestom.server.utils.location.RelativeVec;

public class PositionCommand extends Command {

    public PositionCommand() {
        super("/position", "/pos");

        setDefaultExecutor(this::defaultExecutor);

        var type = new ArgumentInteger("type");

        type.between(1,2)
                .setCallback(((sender, exception) -> {
                    var error = exception.getErrorCode();
                    if(error == ArgumentNumber.TOO_HIGH_ERROR || error == ArgumentNumber.TOO_LOW_ERROR) {
                        sender.sendMessage(MC.Chat.notificationMessage("Position", Component.text("Argument 'type' needs to be either 1 or 2", MC.CC.GRAY.getTextColor())));
                    }
                }));

        var position = new ArgumentRelativeBlockPosition("position");

        addSyntax(this::setCurrentPosition, type);
        addSyntax(this::setSpecifiedPosition, type, position);
    }

    private void defaultExecutor(CommandSender sender, CommandContext context) {
        sender.sendMessage(MC.Chat.notificationMessage("Position", Component.text("Usage: //position <type> [position]", MC.CC.GRAY.getTextColor())));
    }

    private void setCurrentPosition(CommandSender sender, CommandContext context) {
        int type = context.get("type");

        var builder = Utils.getSenderAsBuilder(sender);
        if(builder == null) { return; }

        var selection = builder.getSelection();
        var instance = builder.getInstance();

        setSelectionPosition(type, builder, selection, Utils.toBlockVector(builder.getPosition()), instance);
    }

    private void setSpecifiedPosition(CommandSender sender, CommandContext context) {
        int type = context.get("type");
        RelativeVec relativePos = context.get("position");

        var builder = Utils.getSenderAsBuilder(sender);
        if(builder == null) { return; }

        var absolutePos = relativePos.from(builder);

        var selection = builder.getSelection();
        var instance = builder.getInstance();

        setSelectionPosition(type, builder, selection, absolutePos, instance);
    }

    private void setSelectionPosition(int type, Builder builder, Selection selection, Vec pos, Instance instance) {
        if(selection == null || selection.getInstance() != instance) {
            selection = new Selection(
                    type == 1 ? pos : null,
                    type == 2 ? pos : null,
                    instance,
                    builder);

            builder.setSelection(selection);

            return;
        }

        if(type == 1) {
            selection.setPos1(pos);
        } else if(type == 2) {
            selection.setPos2(pos);
        }

        builder.sendMessage(MC.Chat.notificationMessage("Position", Component.text("Successfully set " + (type == 1 ? "pos1" : "pos2") + ": " + pos, MC.CC.GREEN.getTextColor())));
    }

}