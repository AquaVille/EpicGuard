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

package me.ishift.epicguard.common;

import de.leonhard.storage.Yaml;

import java.util.Collections;
import java.util.List;

public class Messages {
    public static final Yaml FILE = new Yaml("messages_en_US", "plugins/EpicGuard");
    public static List<String> messageKickProxy;
    public static List<String> messageKickCountry;
    public static List<String> messagesKickServerList;
    public static List<String> messageKickBlacklist;
    public static List<String> messageKickVerify;
    public static List<String> messageKickNamecontains;
    public static String noPermission;
    public static String prefix;
    public static String notAllowedCommand;
    public static String blockedCommand;
    public static String playerOnly;
    public static String configReload;
    public static String usage;
    public static String playerNotFound;
    public static String blacklisted;
    public static String whitelisted;
    public static String unknownCommand;
    public static String reset;
    public static String statusOn;
    public static String statusOff;

    public static void load() {
        prefix = FILE.getOrSetDefault("prefix", "&8[&6&lEpicGuard&8] &7");

        messageKickProxy = FILE.getOrSetDefault("kick-messages.proxy", Collections.singletonList("&8[&6EpicGuard&8] &cYou have been detected for: &6Proxy/VPN."));
        messageKickCountry = FILE.getOrSetDefault("kick-messages.country", Collections.singletonList("&8[&6EpicGuard&8] &cYou have been detected for: &6Country GeoDetection."));
        messagesKickServerList = FILE.getOrSetDefault("kick-messages.server-list", Collections.singletonList("&8[&6EpicGuard&8] &cAdd our server to your &6server list&c, and then join again."));
        messageKickBlacklist = FILE.getOrSetDefault("kick-messages.blacklist", Collections.singletonList("&8[&6EpicGuard&8] &cYou have been detected for: &6IP Blacklist."));
        messageKickVerify = FILE.getOrSetDefault("kick-messages.rejoin", Collections.singletonList("&8[&6EpicGuard&8] &cPlease join our server &6again &cto verify that you are not a bot."));
        messageKickNamecontains = FILE.getOrSetDefault("kick-messages.namecontains", Collections.singletonList("&8[&6EpicGuard&8] &cYou have been detected for: &6NameContains (Change nickname or contact server admin)"));

        noPermission = FILE.getOrSetDefault("other.no-permission", "&cYou don't have permission to access this command!");
        notAllowedCommand = FILE.getOrSetDefault("other.not-allowed-command", "&fUnknown command. Type '/help' for help.");
        blockedCommand = FILE.getOrSetDefault("other.blocked-command", "&cThis command is not allowed!");
        playerOnly = FILE.getOrSetDefault("other.player-only", "&cThis command is player-only!");
        configReload = FILE.getOrSetDefault("other.config-reload", "&7Configuration has been &asuccesfully &7reloaded.");
        usage = FILE.getOrSetDefault("other.usage", "&7Correct usage: &f/{USAGE}&7.");
        playerNotFound = FILE.getOrSetDefault("other.player-not-found", "&cPlayer not found!");
        blacklisted = FILE.getOrSetDefault("other.address-blacklisted", "&7Succesfully &cblacklisted &7address: &c{ADDRESS}");
        whitelisted = FILE.getOrSetDefault("other.address-whitelisted", "&7Succesfully &awhitelisted &7address: &a{ADDRESS}");
        unknownCommand = FILE.getOrSetDefault("other.unknown-command", "&cCommand not found! Use &6/guard");
        reset = FILE.getOrSetDefault("other.reset", "&7Attack data and counters has been &ccleared&7.");
        statusOn = FILE.getOrSetDefault("other.status-on", "&7Attack status has been &aenabled.");
        statusOff = FILE.getOrSetDefault("other.status-off", "&7Attack status has been &cdisabled.");
    }
}
