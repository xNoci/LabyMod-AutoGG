package me.noci.labyaddon.listener;

import me.noci.labyaddon.Core;
import me.noci.labyaddon.PingColor;
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
        if (Minecraft.getMinecraft().getCurrentServerData() == null) return;
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

        drawPing(entity.getUniqueID(), sneaking, y);

        GlStateManager.depthMask(true);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.popMatrix();
    }

    private static void drawPing(UUID uuid, boolean sneaking, double y) {
        NetworkPlayerInfo playerInfo = LabyMod.getInstance().getPlayerListDataCache().get(uuid);
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
        int ping = -1;
        if (playerInfo != null) {
            ping = playerInfo.getResponseTime();
        }

        String pingDisplay = ping <= 0 ? "?" : ping + " ms";
        PingColor color = PingColor.getColor(ping);
        fontRenderer.drawString(pingDisplay, (float) -fontRenderer.getStringWidth(pingDisplay) / 2, (float) y, sneaking ? color.getRGBA() : color.getRGB(), false);
    }

}
