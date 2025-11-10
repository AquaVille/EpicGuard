/*
 * EpicGuard is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * EpicGuard is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package me.xneox.epicguard.paper;

import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.paper.PaperCommandManager;
import me.xneox.epicguard.core.EpicGuard;
import me.xneox.epicguard.core.Platform;
import me.xneox.epicguard.core.command.CommandHandler;
import me.xneox.epicguard.core.placeholder.Placeholders;
import me.xneox.epicguard.paper.listener.*;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public final class EpicGuardPaper extends JavaPlugin implements Platform {
  private EpicGuard epicGuard;
  private final Logger logger = this.getSLF4JLogger();

  @Override
  public void onEnable() {
    this.epicGuard = new EpicGuard(this, getDataFolder().toPath());

    var pluginManager = getServer().getPluginManager();

    Stream.of(
            new PlayerPreLoginListener(this.epicGuard),
            new PlayerQuitListener(this.epicGuard),
            new PlayerPostLoginListener(this.epicGuard),
            new ServerPingListener(this.epicGuard),
            new PlayerSettingsListener(this.epicGuard)
    ).forEach(handler -> pluginManager.registerEvent(
            handler.clazz(),
            handler,
            handler.priority(),
            (l, event) -> handler.handle(event),
            this,
            handler.ignoreCancelled()
    ));

    try {
      final PaperCommandManager<CommandSender> commandManager = new PaperCommandManager<>(
              this,
              AsynchronousCommandExecutionCoordinator.simpleCoordinator(),
              Function.identity(),
              Function.identity()
      );
      //commandManager.registerBrigadier(); // BRIGADIER SEEMS TO BE A PROBLEM FOR 1.21
      new CommandHandler<>(epicGuard, commandManager).register();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    if (this.getServer().getPluginManager().isPluginEnabled("MiniPlaceholders")) {
      Placeholders.register();
    }
  }

  @Override
  public void onDisable() {
    this.epicGuard.shutdown();
  }

  @Override
  public @NotNull String platformVersion() {
    return getServer().getVersion();
  }

  @Override
  public @NotNull Logger logger() {
    return this.logger;
  }

  @Override
  public @Nullable Audience audience(final @NotNull UUID uuid) {
    return getServer().getPlayer(uuid);
  }

  @Override
  public void disconnectUser(final @NotNull UUID uuid, final @NotNull Component message) {
    final var player = getServer().getPlayer(uuid);
    if (player != null) {
      player.kick(message);
    }
  }

  @Override
  public void runTaskLater(final @NotNull Runnable task, final long seconds) {
    getServer().getAsyncScheduler().runDelayed(this, $ -> task.run(), seconds, TimeUnit.SECONDS);
  }

  @Override
  public void scheduleRepeatingTask(final @NotNull Runnable task, final long seconds) {
    getServer().getAsyncScheduler().runAtFixedRate(this, $ -> task.run(), 0, seconds, TimeUnit.SECONDS);
  }
}
