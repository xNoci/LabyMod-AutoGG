package me.noci.labyaddon.server;

import me.noci.labyaddon.Core;
import net.labymod.api.events.TabListEvent;
import net.labymod.core.LabyModCore;
import net.labymod.servermanager.ChatDisplayAction;
import net.labymod.servermanager.Server;
import net.labymod.settings.elements.SettingsElement;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.PacketBuffer;

import java.util.List;

public class GommeHDNetServer extends Server {

    private Core core;

    public GommeHDNetServer(Core core) {
        super("GommeHD", "gommehd.net", "gommehd.com");
        this.core = core;
    }

    @Override
    public void onJoin(ServerData serverData) {
        if (!core.enabeled) return;
        core.getApi().displayMessageInChat("§aAuto GG ist aktuell aktiviert.");
    }

    /**
     * This method will be called when the client receives a chat message from this server
     *
     * @param clean     message without color codes
     * @param formatted message with color codes
     * @return how the chat should handle this message - should the message show up on the other chat or should it be hidden?
     */
    @Override
    public ChatDisplayAction handleChatMessage(String clean, String formatted) throws Exception {
        System.out.println(1);
        if (!core.enabeled) return ChatDisplayAction.NORMAL;
        System.out.println(2);
        if (core.indicators.contains(clean)) {

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (core.autoGGEnabled) {
                        if (core.isGGDelayEnabled()) {
                            sleep(core.getGGDelay());
                        }
                        LabyModCore.getMinecraft().getPlayer().sendChatMessage(core.autoMessage);
                    }
                    if (core.autoLeaveEnabled) {
                        sleep(core.leaveDelay);
                        LabyModCore.getMinecraft().getPlayer().sendChatMessage("/hub");
                    }
                }
            });
            thread.start();
        }
        return ChatDisplayAction.NORMAL;
    }

    @Override
    public void handlePluginMessage(String s, PacketBuffer packetBuffer) throws Exception {

    }

    @Override
    public void handleTabInfoMessage(TabListEvent.Type type, String s, String s1) throws Exception {

    }

    @Override
    public void fillSubSettings(List<SettingsElement> list) {

    }

    private void sleep(int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
