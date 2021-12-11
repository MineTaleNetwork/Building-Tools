package cc.minetale.buildingtools;

import cc.minetale.flame.util.FlamePlayer;
import lombok.Getter;
import lombok.Setter;
import net.minestom.server.network.player.PlayerConnection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Getter @Setter
public class Builder extends FlamePlayer {

    private boolean builderMode;
    @Nullable private Selection selection;

    public Builder(@NotNull UUID uuid, @NotNull String username, @NotNull PlayerConnection playerConnection) {
        super(uuid, username, playerConnection);
    }

}
