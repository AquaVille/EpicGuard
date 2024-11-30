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

package me.xneox.epicguard.core.command;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import me.xneox.epicguard.core.EpicGuard;
import me.xneox.epicguard.core.command.sub.*;
import me.xneox.epicguard.core.util.Constants;
import me.xneox.epicguard.core.util.TextUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;


/**
 * This class holds all registered subcommands, and handles the user command/tab suggestion input.
 */
public final class CommandHandler<A extends Audience> {
    private final EpicGuard epicGuard;
    private final CommandManager<A> commandManager;

    public CommandHandler(final EpicGuard epicGuard, final CommandManager<A> commandManager) {
        this.epicGuard = epicGuard;
        this.commandManager = commandManager;
    }

    public void register() {
        commandManager.command(builder()
                .handler(ctx -> {
                    ctx.getSender().sendMessage(Component.text("You are running EpicGuard v" + Constants.CURRENT_VERSION +
                            " on " + this.epicGuard.platform().platformVersion(), TextColor.color(0x99ff00)));
                    ctx.getSender().sendMessage(TextUtils.cachedComponent("<#99ff00> Run <bold><white>/epicguard help</bold> to see available commands and statistics"));
                }));

        final SubCommand[] subCommands = {
                new AnalyzeCommand(),
                new BlacklistCommand(),
                new HelpCommand(),
                new ReloadCommand(),
                new StatusCommand(),
                new WhitelistCommand(),
                new SaveCommand()
        };
        for (final SubCommand subCommand : subCommands) {
            subCommand.register(commandManager, epicGuard);
        }
    }

    private Command.Builder<A> builder() {
        return commandManager.commandBuilder("epicguard").permission("epicguard.admin");
    }
}
