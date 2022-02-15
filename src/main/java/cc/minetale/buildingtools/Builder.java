package cc.minetale.buildingtools;

import cc.minetale.flame.util.FlamePlayer;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;
import net.minestom.server.network.player.PlayerConnection;
import net.minestom.server.scoreboard.Sidebar;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Getter @Setter
public class Builder extends FlamePlayer {

    private boolean builderMode;
    @Nullable private Selection selection;
    private Sidebar sidebar;

    public Builder(@NotNull UUID uuid, @NotNull String username, @NotNull PlayerConnection playerConnection) {
        super(uuid, username, playerConnection);
    }

    public static Builder fromSender(CommandSender sender) {
        if (sender instanceof Builder builder)
            return builder;

        return null;
    }

    public static Builder fromPlayer(Player player) {
        if (player instanceof Builder builder)
            return builder;

        return null;
    }

    public void setBuilderMode(boolean enabled) {
        this.builderMode = enabled;

        var content = Component.text()
                .append(Component.text("Builder Mode: ", NamedTextColor.GOLD, TextDecoration.BOLD),
                        Component.text(enabled ? "Enabled" : "Disabled", enabled ? NamedTextColor.GREEN : NamedTextColor.RED))
                .build();

        if(this.sidebar.getLine("2") == null) {
            this.sidebar.createLine(new Sidebar.ScoreboardLine("2", content, 2));
        } else {
            this.sidebar.updateLineContent("2", content);
        }
    }

}
