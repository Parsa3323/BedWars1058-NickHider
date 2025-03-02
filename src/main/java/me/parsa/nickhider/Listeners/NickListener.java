package me.parsa.nickhider.Listeners;


import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.events.gameplay.GameStateChangeEvent;
import com.andrei1058.bedwars.api.events.player.PlayerJoinArenaEvent;
import com.andrei1058.bedwars.api.events.player.PlayerLeaveArenaEvent;
import me.parsa.nickhider.NickHider;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NickListener implements Listener {

    private final Map<UUID, String> playersNames = new HashMap<>();

    @EventHandler
    public void arenaJoin(PlayerJoinArenaEvent event) {
        Player p = event.getPlayer();
        Bukkit.getScheduler().runTaskAsynchronously(NickHider.plugin, () -> {
            IArena arena = NickHider.bw.getArenaUtil().getArenaByPlayer(p); //
            if (arena != null && arena.getStatus() == GameState.waiting) {
                if (!playersNames.containsKey(p.getUniqueId())) {
                    playersNames.put(p.getUniqueId(), p.getDisplayName());
                }

                String ntodo = ChatColor.MAGIC + "";
                p.setPlayerListName(ntodo);
                NickHider.plugin.setPlayerPrefix(p, ntodo);
                p.setDisplayName(ntodo);

            }
        });

    }

    @EventHandler
    public void playerLeaveArena(PlayerLeaveArenaEvent e) {
        Player p = e.getPlayer();

        String name = playersNames.get(p.getUniqueId());
        playersNames.remove(p.getUniqueId());

        p.setDisplayName(name);
        NickHider.plugin.removePlayerPrefix(p);
        p.setPlayerListName(name);
    }

    @EventHandler
    public void stateChnage(GameStateChangeEvent event) {
        if (event.getNewState()== GameState.playing) {
            Bukkit.getScheduler().runTaskAsynchronously(NickHider.plugin, () -> {
                for (ITeam team : event.getArena().getTeams()) {
                    for (Player p : team.getMembers()) {
                        String name = playersNames.get(p.getUniqueId());
                        playersNames.remove(p.getUniqueId());
                        p.setDisplayName(name);
                        NickHider.plugin.removePlayerPrefix(p);
                        p.setPlayerListName(name);
                    }
                }
            });
        }

    }

}
