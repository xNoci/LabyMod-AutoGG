package me.noci.labyaddon.listener;

import me.noci.labyaddon.Core;
import me.noci.labyaddon.ServerHandler;
import net.labymod.main.LabyMod;
import net.labymod.utils.Consumer;
import net.labymod.utils.ServerData;

public class OnServerJoinListener implements Consumer<ServerData> {
    @Override
    public void accept(ServerData serverData) {
        if (LabyMod.getInstance().getLabyPlay() != null) {


            if (serverData.getIp().toLowerCase().contains("gommehd")) {
                ServerHandler.setCurrentIP("gommehd");

                if (Core.getInstance().isAddonEnabled())
                    Core.getInstance().getApi().displayMessageInChat("§aAuto GG ist aktuell aktiviert.");
            }
        }
    }
}
