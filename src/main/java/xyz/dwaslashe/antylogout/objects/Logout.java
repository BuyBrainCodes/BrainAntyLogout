package xyz.dwaslashe.antylogout.objects;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.dwaslashe.antylogout.Main;
import xyz.dwaslashe.antylogout.managers.LogoutManager;
import xyz.dwaslashe.antylogout.utils.Api;
import xyz.dwaslashe.antylogout.utils.TimerApi;

import java.util.Iterator;

public class Logout {
    private Player player;
    private long time;
    private Player attacker;

    public static Logout get(Player player) {
        Iterator var1 = LogoutManager.getLogouts().iterator();

        Logout logout;
        do {
            if (!var1.hasNext()) {
                return new Logout(player);
            }

            logout = (Logout)var1.next();
        } while(!logout.getPlayer().getName().equalsIgnoreCase(player.getName()));

        return logout;
    }

    public Player getAttacker() {
        return this.attacker;
    }

    public void setAttacker(Player attacker) {
        this.attacker = attacker;
    }

    private boolean exists() {
        return LogoutManager.getLogouts().contains(this);
    }

    public Logout(Player player) {
        this.player = player;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = TimerApi.parseDateDiff(time, true);
    }

    public Player getPlayer() {
        return this.player;
    }

    public void create() {
        if (!this.exists()) {
            LogoutManager.getLogouts().add(this);
            if (Main.pluginConfig.getAntylogout().getBossBar().isEnable()) {
                final BossBar bar = Bukkit.createBossBar(Api.fixColor(Main.pluginConfig.getAntylogout().getBossBar().getWaiting().replace("{TIME}", TimerApi.secondsToString(Logout.this.getTime()))), Main.pluginConfig.getAntylogout().getBossBar().getColor(), BarStyle.SOLID, new BarFlag[0]);
                bar.addPlayer(this.player);
                (new BukkitRunnable() {
                    public void run() {
                        if (Logout.this.player != null && Logout.this.player.isOnline()) {
                            if (Logout.this.time > System.currentTimeMillis()) {
                                bar.setTitle(Api.fixColor(Main.pluginConfig.getAntylogout().getBossBar().getWaiting().replace("{TIME}", TimerApi.secondsToString(Logout.this.getTime()))));
                            } else {
                                bar.setTitle(Api.fixColor(Main.pluginConfig.getAntylogout().getBossBar().getSucces()));
                                bar.setColor(BarColor.GREEN);
                                bar.setProgress(1);
                                Bukkit.getScheduler().runTaskLater(Main.getMain(), new Runnable() {
                                    public void run() {
                                        bar.setVisible(false);
                                        bar.removePlayer(Logout.this.player);
                                        Logout.this.remove();
                                    }
                                }, 3 * 20L);
                                this.cancel();
                            }
                        } else {
                            Logout.this.remove();
                            this.cancel();
                        }

                    }
                }).runTaskTimer(Main.getMain(), 0L, 20L);
            }
            if (Main.pluginConfig.getAntylogout().getActionBar().isEnable()) {
                (new BukkitRunnable() {
                    public void run() {
                        if (Logout.this.player != null && Logout.this.player.isOnline()) {
                            if (Logout.this.time > System.currentTimeMillis()) {
                                player.spigot().sendMessage(
                                        ChatMessageType.ACTION_BAR,
                                        TextComponent.fromLegacyText(Api.fixColor(Main.pluginConfig.getAntylogout().getActionBar().getWaiting().replace("{TIME}", TimerApi.secondsToString(Logout.this.getTime())))));
                            } else {
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Api.fixColor(Main.pluginConfig.getAntylogout().getActionBar().getSucces())));
                                Bukkit.getScheduler().runTaskLater(Main.getMain(), new Runnable() {
                                    public void run() {
                                        Logout.this.remove();
                                    }
                                }, 3 * 20L);
                                this.cancel();
                            }
                        } else {
                            Logout.this.remove();
                            this.cancel();
                        }

                    }
                }).runTaskTimer(Main.getMain(), 0L, 20L);
            }
        }

    }

    public void remove() {
        if (this.exists()) {
            LogoutManager.getLogouts().remove(this);
        }

    }
}