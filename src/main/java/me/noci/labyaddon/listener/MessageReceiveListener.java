package me.noci.labyaddon.listener;

import me.noci.labyaddon.Core;
import me.noci.labyaddon.ServerHandler;
import net.labymod.api.events.MessageReceiveEvent;
import net.labymod.core.LabyModCore;

public class MessageReceiveListener implements MessageReceiveEvent {


    @Override
    public boolean onReceive(String rawMessage, String message) {
        if (!ServerHandler.isCurrentlyPlayingOn("gommehd")) return false;
        if (!Core.getInstance().isAddonEnabled()) return false;

        if (isRoundOver(message)) {

            final boolean autoGGEnabled = Core.getInstance().isAutoGGEnabled();
            final boolean autoLeaveEnabled = Core.getInstance().isAutoLeaveEnabled();
            final boolean ggDelayEnabled = Core.getInstance().isAutoGGDelayEnabled();

            final int ggDelay = Core.getInstance().getAutoGGDelay();
            final int leaveDelay = Core.getInstance().getLeaveDelay();

            final String autoGGMessage = Core.getInstance().getAutoMessage();

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    if (autoGGEnabled) {
                        handleAutoMessage(autoGGMessage, ggDelayEnabled, ggDelay);
                    }

                    if (autoLeaveEnabled) {
                        handleAutoLeave(leaveDelay);
                    }

                }
            });

            thread.start();
        }
        return false;
    }

    private void handleAutoMessage(String autoGGMessage, boolean delayEnabled, int delay) {
        if (delayEnabled) {
            sleep(delay);
        }

        LabyModCore.getMinecraft().getPlayer().sendChatMessage(autoGGMessage);
    }

    private void handleAutoLeave(int leaveDelay) {
        sleep(leaveDelay);
        LabyModCore.getMinecraft().getPlayer().sendChatMessage("/hub");
    }

    private void sleep(int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean isRoundOver(String message) {
        return Core.getInstance().getIndicators().contains(message);
    }
}
