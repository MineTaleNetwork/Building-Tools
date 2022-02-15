package cc.minetale.buildingtools;

import cc.minetale.buildingtools.commands.ModeCommand;
import cc.minetale.buildingtools.commands.SetCommand;
import cc.minetale.buildingtools.commands.magma.LoadCommand;
import cc.minetale.buildingtools.commands.magma.SaveCommand;
import cc.minetale.buildingtools.commands.selection.PositionCommand;
import cc.minetale.buildingtools.commands.selection.SelectCommand;
import cc.minetale.commonlib.util.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.extensions.Extension;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.network.ConnectionManager;
import net.minestom.server.scoreboard.Sidebar;

import java.util.Arrays;

public class BuildingTools extends Extension {

    private static Instance instance;

    private static final ConnectionManager CONNECTION_MANAGER = MinecraftServer.getConnectionManager();
    private static final InstanceManager INSTANCE_MANAGER = MinecraftServer.getInstanceManager();
    private static final CommandManager COMMAND_MANAGER = MinecraftServer.getCommandManager();

    @Override public void initialize() {
        CONNECTION_MANAGER.setPlayerProvider(Builder::new);

        Arrays.asList(
                new SaveCommand(),
                new LoadCommand(),

                new PositionCommand(),
                new SelectCommand(),

                new SetCommand(),

                new ModeCommand()
        ).forEach(COMMAND_MANAGER::register);

        instance = INSTANCE_MANAGER.createInstanceContainer();

        MinecraftServer.getGlobalEventHandler()
                .addChild(EventNode.type("buildingTools", EventFilter.ENTITY)
                        .addListener(PlayerLoginEvent.class, event -> event.setSpawningInstance(instance))
                        .addListener(PlayerSpawnEvent.class, event -> {
                            if(!event.isFirstSpawn()) { return; }

                            var builder = Builder.fromPlayer(event.getPlayer());
                            builder.setGameMode(GameMode.CREATIVE);
                            builder.setAllowFlying(true);
                            builder.setFlying(true);

                            var sidebar = new Sidebar(Component.text("MineTale Network", NamedTextColor.GOLD, TextDecoration.BOLD));
                            sidebar.createLine(new Sidebar.ScoreboardLine("15", Message.scoreboardSeparator(), 15));
                            sidebar.createLine(new Sidebar.ScoreboardLine("1", Message.scoreboardSeparator(), 1));
                            sidebar.addViewer(builder);

                            builder.setSidebar(sidebar);
                            builder.setBuilderMode(false);
                        })
                ).setPriority(9999);
    }

    @Override public void terminate() { }

}
