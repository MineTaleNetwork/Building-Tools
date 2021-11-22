package cc.minetale.buildingtools;

import net.minestom.server.command.CommandSender;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import org.bson.Document;

public class Utils {

    //TODO Move these and similar to mLib
    public static Document vectorToDocument(Vec vec) {
        return new Document()
                .append("x", vec.x())
                .append("y", vec.y())
                .append("z", vec.z());
    }

    public static Vec documentToVector(Document document) {
        return new Vec(
                document.getDouble("x"),
                document.getDouble("y"),
                document.getDouble("z"));
    }

    public static Document positionToDocument(Pos pos) {
        return new Document()
                .append("x", pos.x())
                .append("y", pos.y())
                .append("z", pos.z())
                .append("yaw", pos.yaw())
                .append("pitch", pos.pitch());
    }

    public static Pos documentToPosition(Document document) {
        return new Pos(
                document.getDouble("x"),
                document.getDouble("y"),
                document.getDouble("z"),
                document.get("yaw", Float.class),
                document.get("pitch", Float.class));
    }

    public static Vec toBlockVector(Point point) {
        return new Vec(point.blockX(), point.blockY(), point.blockZ());
    }

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

}
