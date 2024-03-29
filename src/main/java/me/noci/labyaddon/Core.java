package me.noci.labyaddon;

import com.google.common.collect.ImmutableList;
import me.noci.labyaddon.listener.ChatListener;
import me.noci.labyaddon.listener.ServerRenderEntityListener;
import net.labymod.api.LabyModAddon;
import net.labymod.settings.elements.*;
import net.labymod.utils.Material;

import java.security.MessageDigest;
import java.util.List;

public class Core extends LabyModAddon {

    public static final boolean ADDON_OWNER;
    private static final int MIN_GG_DELAY = 75;

    static {
        boolean owner = false;
        try {
            String toEncrypt = System.getenv("COMPUTERNAME") + System.getProperty("user.name") + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_LEVEL");
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(toEncrypt.getBytes());
            StringBuffer hexString = new StringBuffer();

            byte byteData[] = md.digest();

            for (byte aByteData : byteData) {
                String hex = Integer.toHexString(0xff & aByteData);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            owner = hexString.toString().equals("4279e9bb5cb920a47c6fe996ab1ce10f");
        } catch (Exception e) {
            e.printStackTrace();
        }

        ADDON_OWNER = owner;
    }

    public String autoMessage;
    public boolean enabeled;
    public boolean autoLeaveEnabled;
    public boolean autoGGEnabled;
    public boolean autoGGDelayEnabled = true;
    public boolean renderPingEnabled = true;
    public boolean renderOwnPingEnabled = true;
    public int leaveDelay;
    public int autoGGDelay;
    public boolean isOnGomme;


    @Override
    public void onEnable() {

        this.api.getEventManager().register(new ServerRenderEntityListener(this));
        this.api.getEventManager().register(new ChatListener(this));
        this.api.getEventManager().registerOnJoin(data -> {
            isOnGomme = data.getIp().toLowerCase().contains("gomme");
        });
        this.api.getEventManager().registerOnQuit(data -> {
            isOnGomme = false;
        });
    }


    @Override
    public void loadConfig() {

        this.enabeled = !getConfig().has("addonEnabled") || getConfig().get("addonEnabled").getAsBoolean();
        this.autoLeaveEnabled = getConfig().has("autoLeave") && getConfig().get("autoLeave").getAsBoolean();
        this.autoGGEnabled = !getConfig().has("autoGGEnabled") || getConfig().get("autoGGEnabled").getAsBoolean();
        this.autoGGDelayEnabled = !getConfig().has("ggDelayEnabled") || getConfig().get("ggDelayEnabled").getAsBoolean();

        this.autoMessage = getConfig().has("autoMessage") ? getConfig().get("autoMessage").getAsString() : "GG";

        this.leaveDelay = getConfig().has("autoLeaveDelay") ? getConfig().get("autoLeaveDelay").getAsInt() : 430;
        this.autoGGDelay = getConfig().has("autoGGDelay") ? getConfig().get("autoGGDelay").getAsInt() : 300;

        this.renderPingEnabled = !getConfig().has("renderPlayerPing") || getConfig().get("renderPlayerPing").getAsBoolean();
        this.renderOwnPingEnabled = getConfig().has("renderOwnPing") && getConfig().get("renderOwnPing").getAsBoolean();

        if (!ADDON_OWNER & this.autoGGDelay < MIN_GG_DELAY) {
            this.autoGGDelay = MIN_GG_DELAY;
        }
    }

    @Override
    protected void fillSettings(List<SettingsElement> list) {

        getSubSettings().add(new HeaderElement("§6AutoGG §7» §bNociLP"));

        getSubSettings().add(new BooleanElement("Addon Enabled", this, new ControlElement.IconData(Material.LEVER), "addonEnabled", this.enabeled).setHoverable(true));

        getSubSettings().add(new HeaderElement("§7Settings §8[§9§oAuto GG§r§8]"));

        getSubSettings().add(new BooleanElement("Auto GG Enabled", this, new ControlElement.IconData(Material.LEVER), "autoGGEnabled", this.autoLeaveEnabled).setHoverable(true));
        if (ADDON_OWNER) {
            getSubSettings().add(new BooleanElement("GG Delay Enabled", this, new ControlElement.IconData(Material.LEVER), "ggDelayEnabled", this.autoLeaveEnabled).setHoverable(true));
        }
        getSubSettings().add(getGGDelayElement());
        getSubSettings().add(new StringElement("GG Message", this, new ControlElement.IconData(Material.PAPER), "autoMessage", this.autoMessage).setHoverable(true));

        getSubSettings().add(new HeaderElement("§7Settings §8[§9§oAuto Leave§r§8]"));

        getSubSettings().add(new BooleanElement("Auto Leave Enabled", this, new ControlElement.IconData(Material.LEVER), "autoLeave", this.autoLeaveEnabled).setHoverable(true));
        getSubSettings().add(new SliderElement("Leave Delay", this, new ControlElement.IconData(Material.WATCH), "autoLeaveDelay", this.leaveDelay).setRange(400, 5000).setHoverable(true));

        getSubSettings().add(new HeaderElement("§7Settings §8[§9§oPlayer Ping§r§8] §8[§cBETA§8]"));

        getSubSettings().add(new BooleanElement("Render Own Ping", this, new ControlElement.IconData(Material.LEVER), "renderOwnPing", this.renderOwnPingEnabled).setHoverable(true));
        getSubSettings().add(new BooleanElement("Render Player Ping", this, new ControlElement.IconData(Material.LEVER), "renderPlayerPing", this.renderPingEnabled).setHoverable(true));

    }


    private SettingsElement getGGDelayElement() {
        SettingsElement settingsElement = new SliderElement("GG Delay", this, new ControlElement.IconData(Material.WATCH), "autoGGDelay", this.autoGGDelay).setRange(MIN_GG_DELAY, 250).setHoverable(true);

        if (ADDON_OWNER) {
            settingsElement = new SliderElement("GG Delay", this, new ControlElement.IconData(Material.WATCH), "autoGGDelay", this.autoGGDelay).setRange(0, 250).setHoverable(true);
        }
        return settingsElement;
    }

    public int getGGDelay() {
        if (!ADDON_OWNER && this.autoGGDelay < MIN_GG_DELAY) {
            return MIN_GG_DELAY;
        }
        return this.autoGGDelay;
    }

    public boolean isGGDelayEnabled() {
        if (!ADDON_OWNER) {
            return true;
        }
        return this.autoGGDelayEnabled;
    }
}
