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

package me.xneox.epicguard.core.handler;

import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import me.xneox.epicguard.core.EpicGuard;
import me.xneox.epicguard.core.check.AbstractCheck;
import me.xneox.epicguard.core.check.AccountLimitCheck;
import me.xneox.epicguard.core.check.BlacklistCheck;
import me.xneox.epicguard.core.check.GeographicalCheck;
import me.xneox.epicguard.core.check.LockdownCheck;
import me.xneox.epicguard.core.check.NameSimilarityCheck;
import me.xneox.epicguard.core.check.NicknameCheck;
import me.xneox.epicguard.core.check.ProxyCheck;
import me.xneox.epicguard.core.check.ReconnectCheck;
import me.xneox.epicguard.core.check.ServerListCheck;
import me.xneox.epicguard.core.user.ConnectingUser;
import me.xneox.epicguard.core.util.LogUtils;
import me.xneox.epicguard.core.util.TextUtils;
import net.kyori.adventure.text.TextComponent;
import org.jetbrains.annotations.NotNull;

/**
 * Handler for PreLogin listeners. It performs every antibot check (except SettingsCheck).
 */
public abstract class PreLoginHandler {
  private final Set<AbstractCheck> pipeline = new TreeSet<>();
  private final EpicGuard epicGuard;

  public PreLoginHandler(EpicGuard epicGuard) {
    this.epicGuard = epicGuard;

    // This will be automatically sorted based on the configured priority.
    pipeline.add(new LockdownCheck(epicGuard));
    pipeline.add(new BlacklistCheck(epicGuard));
    pipeline.add(new NicknameCheck(epicGuard));
    pipeline.add(new GeographicalCheck(epicGuard));
    pipeline.add(new ServerListCheck(epicGuard));
    pipeline.add(new ReconnectCheck(epicGuard));
    pipeline.add(new AccountLimitCheck(epicGuard));
    pipeline.add(new NameSimilarityCheck(epicGuard));
    pipeline.add(new ProxyCheck(epicGuard));

    epicGuard.logger().info("Order of the detection pipeline: " +
        String.join(", ", this.pipeline.stream().map(check -> check.getClass().getSimpleName()).toList()));
  }

  /**
   * Handling the incoming connection, and returning an optional disconnect message.
   *
   * @param address Address of the connecting user.
   * @param nickname Nickname of the connecting user.
   * @return Disconnect message, or an empty Optional if undetected.
   */
  @NotNull
  public Optional<TextComponent> handle(@NotNull String address, @NotNull String nickname) {
    LogUtils.debug("Handling incoming connection: " + address + "/" + nickname);

    // Increment the connections per second and check if it's bigger than max-cps in config.
    if (this.epicGuard.attackManager().incrementConnectionCounter() >= this.epicGuard.config().misc().attackConnectionThreshold()) {
      this.epicGuard.logger().warn("Enabling attack-mode (" + this.epicGuard.attackManager().connectionCounter() + " con/s)");
      this.epicGuard.attackManager().attack(true);
    }

    // Make sure the connecting address is valid.
    // This is also a workaround for an issue with players connecting using GeyserMC. Proper fix needed.
    if (TextUtils.parseAddress(address) == null) {
      this.epicGuard.logger().warn("Skipping checks for [" + address + "/" + nickname + "]: invalid address.");
      return Optional.empty();
    }

    // Check if the user is whitelisted, if yes, return empty result (undetected).
    if (this.epicGuard.storageManager().addressMeta(address).whitelisted()) {
      LogUtils.debug("Skipping whitelisted user: " + address + "/" + nickname);
      return Optional.empty();
    }

    ConnectingUser user = new ConnectingUser(address, nickname);
    for (AbstractCheck check : this.pipeline) {
      if (check.isDetected(user)) {
        // Positive detection, kicking the player!
        LogUtils.debug(nickname + "/" + address + " detected by " + check.getClass().getSimpleName());
        return Optional.of(check.detectionMessage());
      }
    }

    // Checks finished with no detection, the player is considered legitimate and is allowed to join the server
    // Also we update his account nickname history.
    this.epicGuard.storageManager().updateAccounts(user);
    return Optional.empty();
  }
}
