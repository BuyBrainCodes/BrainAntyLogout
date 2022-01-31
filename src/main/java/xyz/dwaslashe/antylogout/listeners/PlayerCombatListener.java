package xyz.dwaslashe.antylogout.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import xyz.dwaslashe.antylogout.Main;
import xyz.dwaslashe.antylogout.objects.Logout;
import xyz.dwaslashe.antylogout.utils.Api;
import xyz.dwaslashe.antylogout.utils.RegionApi;

import java.util.Arrays;
import java.util.List;

public class PlayerCombatListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Logout logout = Logout.get(e.getPlayer());
        if (Main.pluginConfig.getFlags().getPlayerinteract().isEnable()) {
            if (logout.getTime() > System.currentTimeMillis() && e.getClickedBlock() != null && e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                List<Material> types = Arrays.asList(Material.FURNACE, Material.CHEST, Material.SHULKER_BOX, Material.HOPPER, Material.DROPPER, Material.DISPENSER, Material.BARREL, Material.TRAPPED_CHEST, Material.ENDER_CHEST);
                if (types.contains(e.getClickedBlock().getType())) {
                    e.setCancelled(true);
                    e.setUseInteractedBlock(Event.Result.DENY);
                    Api.sendMessage(e.getPlayer(), Main.pluginConfig.getFlags().getPlayerinteract().getMessage());
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && !e.isCancelled() && (e.getDamager() instanceof Player)) {
            Logout logout = Logout.get(((Player) e.getDamager()).getPlayer());
            Logout logout_damager = Logout.get(((Player) e.getEntity()).getPlayer());
            logout.setTime("20s");
            logout_damager.setTime("20s");
            if (e.getDamager() instanceof Player) {
                logout.setAttacker(((Player) e.getDamager()).getPlayer());
                logout_damager.setAttacker(((Player) e.getDamager()).getPlayer());
            }
            logout.create();
            logout_damager.create();
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Logout logout = Logout.get(e.getPlayer());
        String command = e.getMessage().split(" ")[0].toLowerCase();
        if (Main.pluginConfig.getFlags().getPlayercommand().isEnable()) {
            if (logout.getTime() > System.currentTimeMillis()) {
                for (String string : Main.pluginConfig.getFlags().getPlayercommand().getBlacklistcommands()) {
                    if (string.toLowerCase().equalsIgnoreCase(command)) {
                        Api.sendMessage(e.getPlayer(), Main.pluginConfig.getFlags().getPlayercommand().getMessage());
                        e.setCancelled(true);
                        break;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onListening(PlayerMoveEvent event) {
        Logout logout = Logout.get(event.getPlayer());
        Location locationTo = event.getTo();
        Location locationFrom = event.getFrom();
        int xTo = locationTo.getBlockX();
        int yTo = locationTo.getBlockY();
        int zTo = locationTo.getBlockZ();
        int xFrom = locationFrom.getBlockX();
        int yFrom = locationFrom.getBlockY();
        int zFrom = locationFrom.getBlockZ();
        if (Main.pluginConfig.getFlags().getPlayerRegionMove().isEnable()) {
            if (xTo != xFrom || yTo != yFrom || zTo != zFrom) {
                Player player = event.getPlayer();
                if (logout.getTime() > System.currentTimeMillis()) {
                    for (String unavailableRegion : Main.pluginConfig.getAntylogout().getBlacklistregions()) {
                        if (RegionApi.getRegion(locationTo, unavailableRegion) && logout.getTime() > System.currentTimeMillis()) {
                            Api.sendMessage(event.getPlayer(), Main.pluginConfig.getFlags().getPlayerRegionMove().getMessage());
                            player.setVelocity(event.getTo().toVector().subtract(locationFrom.toVector()).multiply(-3));
                            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_FALL, 1.0F, 1.0F);
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        Logout logout = Logout.get(e.getPlayer());
        Location locationTo = e.getTo();
        Location locationFrom = e.getFrom();
        int xTo = locationTo.getBlockX();
        int yTo = locationTo.getBlockY();
        int zTo = locationTo.getBlockZ();
        int xFrom = locationFrom.getBlockX();
        int yFrom = locationFrom.getBlockY();
        int zFrom = locationFrom.getBlockZ();
        if (Main.pluginConfig.getFlags().getPlayerRegionTeleport().isEnable()) {
            if (xTo != xFrom || yTo != yFrom || zTo != zFrom) {
                Player player = e.getPlayer();
                if (logout.getTime() > System.currentTimeMillis()) {
                    for (String unavailableRegion : Main.pluginConfig.getAntylogout().getBlacklistregions()) {
                        if (RegionApi.getRegion(locationTo, unavailableRegion) && logout.getTime() > System.currentTimeMillis()) {
                            Api.sendMessage(player, Main.pluginConfig.getFlags().getPlayerRegionTeleport().getMessage());
                            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_FALL, 1.0F, 1.0F);
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Logout logout = Logout.get(e.getEntity().getPlayer());
        if (logout.getTime() > System.currentTimeMillis()) {
            logout.getPlayer().getWorld().strikeLightningEffect(logout.getPlayer().getLocation());
            logout.setTime("0s");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Logout logout = Logout.get(e.getPlayer());
        if (Main.pluginConfig.getFlags().getPlayerQuit().isEnable()) {
            if (logout.getTime() > System.currentTimeMillis()) {
                e.getPlayer().setHealth(0.0D);
                e.setQuitMessage(Api.fixColor(Main.pluginConfig.getFlags().getPlayerQuit().getBroadcast().replace("{PLAYER}", e.getPlayer().getName()).replace("{LAST_ATTACKER}", (logout.getAttacker() != null && logout.getAttacker().isOnline() ? logout.getAttacker().getName() : "Nie wiadomo kto"))));
                e.setQuitMessage(Api.fixColor("&c&lANTY-LOGOUT &8>> &7Gracz &f" + e.getPlayer().getName() + " &7wylogował się podczas walki! Ostatni atakujący to &f" + (logout.getAttacker() != null && logout.getAttacker().isOnline() ? logout.getAttacker().getName() : "Nie wiadomo kto")));
                logout.remove();
            }
        }

    }
}
