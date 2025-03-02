package me.parsa.nickhider;

import com.andrei1058.bedwars.api.BedWars;
import me.parsa.nickhider.Listeners.NickListener;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class NickHider extends JavaPlugin {

    private static Chat chat;

    private static Permission perms;

    public static BedWars bw;

    public static NickHider plugin;

    @Override
    public void onEnable() {
        Bukkit.getLogger().severe("Hooking into bedwars1058");
        if (Bukkit.getPluginManager().getPlugin("BedWars1058") == null) {
            getLogger().severe("BedWars1058 was not found. Disabling...");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }


        bw = Bukkit.getServicesManager().getRegistration(BedWars.class).getProvider();
        plugin = this;

        Bukkit.getLogger().severe("Hooking into vault");
        if (!setupVault()) {
            getLogger().severe("Could not hook into Vault! Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        Bukkit.getLogger().severe("Registering events");
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new NickListener(), this);
        Bukkit.getLogger().severe("Done");//.
    }

    private boolean setupVault() {
        RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }

        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(Permission.class);
        if (permissionProvider != null) {
            perms = permissionProvider.getProvider();
        }

        return chat != null && perms != null;
    }

    public void setPlayerPrefix(Player player, String prefix) {
        if (chat != null) {
            chat.setPlayerPrefix(player, prefix);
        }
    }

    public void removePlayerPrefix(Player player) {
        if (chat != null) {
            chat.setPlayerPrefix(player, null);
        }
    }

    public String getPlayerPrefix(Player player) {
        if (chat != null) {
            return chat.getPlayerPrefix(player);
        }
        return "";
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
