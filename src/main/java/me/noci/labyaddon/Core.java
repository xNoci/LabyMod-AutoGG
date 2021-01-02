package me.noci.labyaddon;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import me.noci.labyaddon.listener.MessageReceiveListener;
import me.noci.labyaddon.listener.OnServerJoinListener;
import net.labymod.api.LabyModAddon;
import net.labymod.settings.elements.*;
import net.labymod.utils.Material;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class Core extends LabyModAddon {

    private static final int MIN_GG_DELAY = 75;

    private static Core instance;
    private String autoMessage;
    private String hwid;

    private ArrayList<String> indicators;

    private boolean addonEnabled;
    private boolean autoLeaveEnabled;
    private boolean autoGGEnabled;
    private boolean autoGGDelayEnabled = true;

    private int leaveDelay;
    private int autoGGDelay;


    @Override
    public void onEnable() {
        instance = this;
        this.getApi().getEventManager().register(new MessageReceiveListener());
        this.getApi().getEventManager().registerOnJoin(new OnServerJoinListener());
    }


    @Override
    public void loadConfig() {

        loadIndicators();

        this.addonEnabled = !getConfig().has("addonEnabled") || getConfig().get("addonEnabled").getAsBoolean();
        this.autoLeaveEnabled = getConfig().has("autoLeave") && getConfig().get("autoLeave").getAsBoolean();
        this.autoGGEnabled = !getConfig().has("autoGGEnabled") || getConfig().get("autoGGEnabled").getAsBoolean();
        this.autoGGDelayEnabled = !getConfig().has("ggDelayEnabled") || getConfig().get("ggDelayEnabled").getAsBoolean();

        this.autoMessage = getConfig().has("autoMessage") ? getConfig().get("autoMessage").getAsString() : "GG";

        this.leaveDelay = getConfig().has("autoLeaveDelay") ? getConfig().get("autoLeaveDelay").getAsInt() : 430;
        this.autoGGDelay = getConfig().has("autoGGDelay") ? getConfig().get("autoGGDelay").getAsInt() : 300;

        if (!isOwnAcc() & this.autoGGDelay < MIN_GG_DELAY) {
            this.autoGGDelay = MIN_GG_DELAY;
        }
    }

    @Override
    protected void fillSettings(List<SettingsElement> list) {

        getSubSettings().add(new HeaderElement("§6AutoGG §7» §bNociLP"));

        getSubSettings().add(new BooleanElement("Addon Enabled", this, new ControlElement.IconData(Material.LEVER), "addonEnabled", this.addonEnabled).setHoverable(true));

        getSubSettings().add(new HeaderElement("§7Settings §8[§9§oAuto GG§r§8]"));

        getSubSettings().add(new BooleanElement("Auto GG Enabled", this, new ControlElement.IconData(Material.LEVER), "autoGGEnabled", this.autoLeaveEnabled).setHoverable(true));
        if (isOwnAcc()) {
            getSubSettings().add(new BooleanElement("GG Delay Enabled", this, new ControlElement.IconData(Material.LEVER), "ggDelayEnabled", this.autoLeaveEnabled).setHoverable(true));
        }
        getSubSettings().add(getGGDelayElement());
        getSubSettings().add(new StringElement("GG Message", this, new ControlElement.IconData(Material.PAPER), "autoMessage", this.autoMessage).setHoverable(true));

        getSubSettings().add(new HeaderElement("§7Settings §8[§9§oAuto Leave§r§8]"));

        getSubSettings().add(new BooleanElement("Auto Leave Enabled", this, new ControlElement.IconData(Material.LEVER), "autoLeave", this.autoLeaveEnabled).setHoverable(true));
        getSubSettings().add(new SliderElement("Leave Delay", this, new ControlElement.IconData(Material.WATCH), "autoLeaveDelay", this.leaveDelay).setRange(400, 5000).setHoverable(true));
    }

    private void loadIndicators() {

        indicators = new ArrayList<String>();
        if (!getConfig().has("indicators")) {
            JsonArray defaultIndicator = new JsonArray();

            defaultIndicator.add(new JsonPrimitive("-= Statistiken dieser Runde =-"));
            defaultIndicator.add(new JsonPrimitive("-= Statistics of this game =-"));
            defaultIndicator.add(new JsonPrimitive("-\\= Statistics of this game \\=-"));
            defaultIndicator.add(new JsonPrimitive("-\\= Statistiken dieser Runde \\=-"));

            getConfig().add("indicators", defaultIndicator);
            saveConfig();
        }

        JsonArray jsonArray = getConfig().get("indicators").getAsJsonArray();
        for (JsonElement jsonElement : jsonArray) {
            indicators.add(jsonElement.getAsString());
        }
    }

    private SettingsElement getGGDelayElement() {
        SettingsElement settingsElement = new SliderElement("GG Delay", this, new ControlElement.IconData(Material.WATCH), "autoGGDelay", this.autoGGDelay).setRange(MIN_GG_DELAY, 250).setHoverable(true);

        if (isOwnAcc()) {
            settingsElement = new SliderElement("GG Delay", this, new ControlElement.IconData(Material.WATCH), "autoGGDelay", this.autoGGDelay).setRange(0, 250).setHoverable(true);
        }
        return settingsElement;
    }

    public boolean isAddonEnabled() {
        return addonEnabled;
    }

    public boolean isAutoLeaveEnabled() {
        return autoLeaveEnabled;
    }

    public int getLeaveDelay() {
        return leaveDelay;
    }

    public String getAutoMessage() {
        return autoMessage;
    }

    public static Core getInstance() {
        return instance;
    }

    public int getAutoGGDelay() {
        if (!isOwnAcc() && this.autoGGDelay < MIN_GG_DELAY) {
            return MIN_GG_DELAY;
        }
        return this.autoGGDelay;
    }

    public boolean isAutoGGEnabled() {
        return this.autoGGEnabled;
    }

    public boolean isAutoGGDelayEnabled() {
        if (!isOwnAcc()) {
            return true;
        }
        return this.autoGGDelayEnabled;
    }

    public ArrayList<String> getIndicators() {
        return this.indicators;
    }

    public boolean isOwnAcc() {
        if (hwid == null) {
            hwid = getHWID();
        }
        return hwid.equals("4279e9bb5cb920a47c6fe996ab1ce10f");
    }

    public static String getHWID() {
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

            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }

}
