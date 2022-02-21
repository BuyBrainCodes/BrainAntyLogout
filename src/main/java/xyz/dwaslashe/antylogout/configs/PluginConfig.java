package xyz.dwaslashe.antylogout.configs;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.*;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.boss.BarColor;

import java.util.Arrays;
import java.util.List;

@Getter @Setter
@Header("#")
@Header("#By dwaslashe")
@Header("#Contact discord dwaslashe v2#5620")
@Header("#")
@Header("#Team BrainCodes")
@Header("#")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public class PluginConfig extends OkaeriConfig {

    private Antylogout antylogout = new Antylogout();
    private Flags flags = new Flags();
    private Core core = new Core();

    //Core
    @Getter @Setter
    @Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
    public static class Core extends OkaeriConfig {

        @Comment("Your license:")
        private String license = "";

    }

    //Flags
    @Getter @Setter
    @Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
    public static class Flags extends OkaeriConfig {

        @Comment("Flags")
        private PlayerInteract playerinteract = new PlayerInteract();

        @Getter @Setter
        public static class PlayerInteract extends OkaeriConfig {

            private boolean enable = true;
            private String message = " &8>> &cInterakcja z tym blokiem podczas pvp jest zablokowana";

        }

        private PlayerCommand playercommand = new PlayerCommand();

        @Getter @Setter
        public static class PlayerCommand extends OkaeriConfig {

            private boolean enable = true;
            private List<String> blacklistcommands = Arrays.asList("/spawn", "/tpa", "/home", "/dom");
            private String message = " &8>> &cKomenda jest wyłączona podczas walki!";

        }

        private PlayerRegionMove playerRegionMove = new PlayerRegionMove();

        @Getter @Setter
        public static class PlayerRegionMove extends OkaeriConfig {

            private boolean enable = true;
            private String message = " &8>> &cTen region jest niedostępny podczas walki!";

        }

        private PlayerRegionTeleport playerRegionTeleport = new PlayerRegionTeleport();

        @Getter @Setter
        public static class PlayerRegionTeleport extends OkaeriConfig {

            private boolean enable = true;
            private String message = " &8>> &cTen region jest niedostępny podczas walki!";

        }

        private PlayerQuit playerQuit = new PlayerQuit();

        @Getter @Setter
        public static class PlayerQuit extends OkaeriConfig {

            private boolean enable = true;
            private String broadcast = "&c&lANTY-LOGOUT &8>> &7Gracz &f{PLAYER} &7wylogował się podczas walki! Ostatni atakujący to &f{LAST_ATTACKER}";
            @Comment("#Gdy ostatni atakujący to nikt")
            private String lastattackernull = "Nie wiadomo kto";

        }
    }

    //Antylogout
    @Getter @Setter
    @Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
    public static class Antylogout extends OkaeriConfig {

        @Comment("Block regions")
        private List<String> blacklistregions = Arrays.asList("end", "spawn", "nether");

        private ActionBar actionBar = new ActionBar();

        @Getter @Setter
        public static class ActionBar extends OkaeriConfig {

            private boolean enable = true;
            private String waiting = "&8>> &7Nie logaj sie przez &#39ff14{TIME} &8- &#fe019a&lBuyBrain &8<<";
            private String succes = "&8>> &#39ff14Skończyłeś już walke! Teraz możesz się wylogować! &8<<";

        }

        private BossBar bossBar = new BossBar();

        @Getter @Setter
        public static class BossBar extends OkaeriConfig {

            private boolean enable = true;
            private BarColor color = BarColor.RED;
            private String waiting = "&8>> &7Nie logaj sie przez &#39ff14{TIME} &8- &#fe019a&lBuyBrain &8<<";
            private String succes = "&8>> &#39ff14Skończyłeś już walke! Teraz możesz się wylogować! &8<<";

        }
    }
}
