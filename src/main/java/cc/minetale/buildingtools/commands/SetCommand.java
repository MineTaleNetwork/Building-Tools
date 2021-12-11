package cc.minetale.buildingtools.commands;

import cc.minetale.buildingtools.Utils;
import cc.minetale.commonlib.util.MC;
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
        sender.sendMessage(MC.notificationMessage("BT",
                Component.text("Usage: //set <block>", NamedTextColor.GRAY)));
    }

    private void setSelection(CommandSender sender, CommandContext context) {
        Block block = context.get("block");

        var builder = Utils.getSenderAsBuilder(sender);
        if(builder == null) { return; }

        if(!builder.isBuilderMode()) {
            sender.sendMessage(MC.notificationMessage("BT",
                    Component.text("You need to be in builder mode to execute this command!", NamedTextColor.RED)));
            return;
        }

        var selection = builder.getSelection();

        if(selection == null || selection.isIncomplete()) {
            sender.sendMessage(MC.notificationMessage("BT",
                    Component.text("You don't have a complete selection!", NamedTextColor.RED)));
            return;
        }

        var instance = builder.getInstance();
        if(instance == null) { return; }

        var blocks = selection.getAllBlocks().parallel();

        AbsoluteBlockBatch batch = new AbsoluteBlockBatch();
        blocks.forEach(vec -> batch.setBlock(vec, block));

        long startTime = System.currentTimeMillis();
        batch.apply(instance, () -> {
            long totalTime = System.currentTimeMillis() - startTime;
            sender.sendMessage(MC.notificationMessage("BT",
                    Component.text("Successfully set " + selection.getSize() +
                            " blocks (in " + totalTime + "ms) as " + block, NamedTextColor.GREEN)));
        });
    }

}