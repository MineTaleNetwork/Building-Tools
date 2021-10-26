package cc.minetale.building_tools;

import net.minestom.server.command.CommandSender;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;

public class Utils {

    public static final class Conditions {

        public static boolean requireBuilderMode(CommandSender sender) {
            var builder = getSenderAsBuilder(sender);
            return builder != null && builder.isBuilderMode();
        }

        public static boolean requireSelection(CommandSender sender) {
            var builder = getSenderAsBuilder(sender);
            return builder != null && builder.getSelection() != null;
        }

    }

    public static Builder getSenderAsBuilder(CommandSender sender) {
        if(!sender.isPlayer()) { return null; }

        var player = sender.asPlayer();
        if(!(player instanceof Builder)) { return null; }

        return (Builder) player;
    }

    public static Vec toBlockVector(Point point) {
        return new Vec(point.blockX(), point.blockY(), point.blockZ());
    }

}
