package cc.minetale.buildingtools.commands;

import cc.minetale.buildingtools.Builder;
import cc.minetale.commonlib.util.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentBlockState;
import net.minestom.server.instance.batch.AbsoluteBlockBatch;
import net.minestom.server.instance.block.Block;

public class SetCommand extends Command {

    public SetCommand() {
        super("/set");

        setDefaultExecutor(this::defaultExecutor);

        var block = new ArgumentBlockState("block");

        addSyntax(this::setSelection, block);
    }

    private void defaultExecutor(CommandSender sender, CommandContext context) {
        sender.sendMessage(Message.notification("BT",
                Component.text("Usage: //set <block>", NamedTextColor.GRAY)));
    }

    private void setSelection(CommandSender sender, CommandContext context) {
        Block block = context.get("block");

        var builder = Builder.fromSender(sender);
        if(builder == null) { return; }

        if(!builder.isBuilderMode()) {
            sender.sendMessage(Message.notification("BT",
                    Component.text("You need to be in builder mode to execute this command!", NamedTextColor.RED)));
            return;
        }

        var selection = builder.getSelection();

        if(selection == null || selection.isIncomplete()) {
            sender.sendMessage(Message.notification("BT",
                    Component.text("You don't have a complete selection!", NamedTextColor.RED)));
            return;
        }

        var instance = builder.getInstance();
        if(instance == null) { return; }

        var batch = new AbsoluteBlockBatch();
        selection.getAllBlocks().forEach(vec -> batch.setBlock(vec, block));

        var startTime = System.currentTimeMillis();
        batch.apply(instance, () -> {
            var totalTime = System.currentTimeMillis() - startTime;
            sender.sendMessage(Message.notification("BT",
                    Component.text("Successfully set " + selection.getSize() +
                            " blocks (in " + totalTime + "ms) as " + block, NamedTextColor.GREEN)));
        });
    }

}