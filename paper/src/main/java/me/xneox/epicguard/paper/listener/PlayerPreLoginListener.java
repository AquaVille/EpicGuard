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

package me.xneox.epicguard.paper.listener;

import me.xneox.epicguard.core.EpicGuard;
import me.xneox.epicguard.core.handler.PreLoginHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class PlayerPreLoginListener extends PreLoginHandler implements PaperListener<AsyncPlayerPreLoginEvent> {
  public PlayerPreLoginListener(EpicGuard epicGuard) {
    super(epicGuard);
  }

  @Override
  public void handle(AsyncPlayerPreLoginEvent event) {
    final String address = event.getAddress().getHostAddress();
    final String nickname = event.getName();

    this.onPreLogin(address, nickname).ifPresent(result ->
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, result));
  }

  @Override
  public Class<AsyncPlayerPreLoginEvent> clazz() {
    return AsyncPlayerPreLoginEvent.class;
  }

  @Override
  public EventPriority priority() {
    return EventPriority.LOWEST;
  }
}
