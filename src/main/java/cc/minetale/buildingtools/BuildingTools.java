package cc.minetale.buildingtools;

import cc.minetale.buildingtools.commands.ModeCommand;
import cc.minetale.buildingtools.commands.SetCommand;
import cc.minetale.buildingtools.commands.magma.LoadCommand;
import cc.minetale.buildingtools.commands.magma.SaveCommand;
import cc.minetale.buildingtools.commands.selection.PositionCommand;
import cc.minetale.buildingtools.commands.selection.SelectCommand;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.extensions.Extension;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.network.ConnectionManager;

import java.util.Arrays;

public class BuildingTools extends Extension {

    private static Instance instance;

    private static final ConnectionManager CONNECTION_MANAGER = MinecraftServer.getConnectionManager();
    public static final InstanceManager INSTANCE_MANAGER = MinecraftServer.getInstanceManager();

    @Override public void initialize() {
        CONNECTION_MANAGER.setPlayerProvider(Builder::new);

        Arrays.asList(
                new SaveCommand(),
                new LoadCommand(),

                new PositionCommand(),
                new SelectCommand(),

                new SetCommand(),

                new ModeCommand()
        ).forEach(command -> MinecraftServer.getCommandManager().register(command));

        instance = INSTANCE_MANAGER.createInstanceContainer();

        MinecraftServer.getGlobalEventHandler()
                .addChild(EventNode.type("spawn", EventFilter.ENTITY)
                        .addListener(PlayerLoginEvent.class, event -> {
                            var player = event.getPlayer();
                            player.setGameMode(GameMode.CREATIVE);

                            player.setAllowFlying(true);
                            player.setFlying(true);

                            event.setSpawningInstance(instance);
                        }));
    }

    @Override public void terminate() { }

}
