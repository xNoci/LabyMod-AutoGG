package me.noci.labyaddon.listener;

import me.noci.labyaddon.Core;
import me.noci.labyaddon.languages.Languages;
import net.labymod.api.events.MessageReceiveEvent;
import net.labymod.core.LabyModCore;

public class ChatListener implements MessageReceiveEvent {

    public Core core;

    public ChatListener(Core core) {
        this.core = core;
    }

    @Override
    public boolean onReceive(String formatted, String clean) {
        if (!core.isOnGomme) return false;
        if (!core.enabeled) return false;
        if (!Languages.getRoundEndIndicators().contains(clean)) return false;

        Thread thread = new Thread(() -> {
            if (core.autoGGEnabled) {
                if (core.isGGDelayEnabled()) {
                    sleep(core.getGGDelay());
                }
                LabyModCore.getMinecraft().getPlayer().sendChatMessage(core.autoMessage);
            }
            if (core.autoLeaveEnabled) {
                sleep(core.leaveDelay);
                //CHECK IF PLAYER IS IN REPLAY
                LabyModCore.getMinecraft().getPlayer().sendChatMessage("/hub");
            }
        });
        thread.start();

        return false;
    }

    private void sleep(int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
