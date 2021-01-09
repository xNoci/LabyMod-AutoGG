package me.noci.labyaddon.listener;

import me.noci.labyaddon.Core;
import net.labymod.api.events.RenderEntityEvent;
import net.labymod.core.LabyModCore;
import net.labymod.main.LabyMod;
import net.labymod.user.User;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;

import java.util.UUID;

public class ServerRenderEntityListener implements RenderEntityEvent {

    private Core core;

    public ServerRenderEntityListener(Core core) {
        this.core = core;
    }

    @Override
    public void onRender(Entity entity, double x, double y, double z, float partialTicks) {
        if(Minecraft.getMinecraft().getCurrentServerData() == null) return;
        if (!core.enabeled || entity == null || Minecraft.getMinecraft().gameSettings.hideGUI)
            return;

        if (LabyMod.getSettings().showMyName && !core.renderOwnPingEnabled && entity.getUniqueID().equals(LabyMod.getInstance().getPlayerUUID()))
            return;
        if (!core.renderPingEnabled && !entity.getUniqueID().equals(LabyMod.getInstance().getPlayerUUID())) return;

        UUID uuid = entity.getUniqueID();
        User user = LabyMod.getInstance().getUserManager().getUser(uuid);
        if (entity.getName() != null) {
            if (entity instanceof AbstractClientPlayer && (((AbstractClientPlayer) entity).getTeam() == null || ((AbstractClientPlayer) entity).getTeam().getNameTagVisibility() != Team.EnumVisible.NEVER)) {
                this.renderPlayerPing((AbstractClientPlayer) entity, user, x, y, z, partialTicks);
            }
        }
    }

    private void renderPlayerPing(AbstractClientPlayer entity, User user, double x, double y, double z, float partialTicks) {
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        float fixedPlayerViewX = renderManager.playerViewX * ((Minecraft.getMinecraft().gameSettings.thirdPersonView == 2) ? -1 : 1);
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
        double tagY = y + 2.4 + user.getMaxNameTagHeight();
        Scoreboard scoreboard = entity.getWorldScoreboard();
        if (scoreboard != null) {
            ScoreObjective scoreObjective = scoreboard.getObjectiveInDisplaySlot(2);
            if (scoreObjective != null) {
                tagY += LabyMod.getInstance().getDrawUtils().getFontRenderer().FONT_HEIGHT * 1.15f * 0.02666667f;
            }
        }


        if (user.getSubTitle() != null && !entity.isSneaking()) {
            double size = user.getSubTitleSize();
            tagY += size / 6;
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, tagY, z);
        GlStateManager.rotate(-renderManager.playerViewY, 0, 1, 0);
        GlStateManager.rotate(fixedPlayerViewX, 1, 0, 0);
        float scale = 0.02666667f;
        boolean self = entity.equals(LabyModCore.getMinecraft().getPlayer());
        boolean sneaking = entity.isSneaking();

        if (self) {
            GlStateManager.translate(0, sneaking ? -0.07999999821186066 : 0.18, 0.0);
        } else {
            GlStateManager.translate(0.0, sneaking ? ((double) (-0.22f + Math.abs(fixedPlayerViewX) / 500.0f)) : 0.17, 0.0);
        }


        GlStateManager.enableAlpha();
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.scale(-scale, -scale, -scale);
        GlStateManager.color(1, 1, 1, 1);

        String rawPing = getPingAsString(entity.getUniqueID());
        String displayPing = rawPing.equals("?") ? rawPing : rawPing + " ms";


        fontRenderer.drawString(displayPing, (float) x - fontRenderer.getStringWidth(displayPing) / 2, (float) y, getPingColorFromString(rawPing, sneaking), false);

        GlStateManager.depthMask(true);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.popMatrix();
    }

    public static String getPingAsString(UUID uuid) {
        NetworkPlayerInfo playerInfo = LabyMod.getInstance().getPlayerListDataCache().get(uuid);
        if (playerInfo == null) return "?";
        int ping = playerInfo.getResponseTime();
        if (ping <= 0) return "?";

        StringBuilder pingString = new StringBuilder();

        pingString.append(ping);

        return pingString.toString();
    }

    public static int getPingColorFromString(String ping, boolean sneaking) {
        if (ping.equals("?")) {
            return sneaking ? 1431699285 : 5635925;
        } else {
            int pingInt = Integer.parseInt(ping);
            if (pingInt <= 20) {
                return sneaking ? 1431699285 : 5635925;
            } else if (pingInt <= 50) {
                return sneaking ? 1426106880 : 43520;
            } else if (pingInt <= 75) {
                return sneaking ? 1426106880 : 16777045;
            } else if (pingInt <= 100) {
                return sneaking ? 1442818560 : 16755200;
            } else if (pingInt <= 200) {
                return sneaking ? 1442796885 : 16733525;
            } else {
                return sneaking ? 1437204480 : 11141120;
            }
        }
    }

}
