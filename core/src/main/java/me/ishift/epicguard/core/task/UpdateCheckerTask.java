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

package me.ishift.epicguard.core.task;

import me.ishift.epicguard.core.EpicGuard;
import me.ishift.epicguard.core.util.UpdateChecker;

public class UpdateCheckerTask implements Runnable {
    private final EpicGuard epicGuard;

    public UpdateCheckerTask(EpicGuard epicGuard) {
        this.epicGuard = epicGuard;
    }

    @Override
    public void run() {
        UpdateChecker.checkForUpdates(this.epicGuard);
        if (UpdateChecker.isAvailable()) {
            this.epicGuard.getLogger().info("New update is available: " + UpdateChecker.getRemoteVersion());
            this.epicGuard.getLogger().info("Download it here: https://www.spigotmc.org/resources/72369/");
        }
    }
}