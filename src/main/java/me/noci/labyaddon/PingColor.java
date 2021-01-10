package me.noci.labyaddon;

public enum PingColor {

    GOOD(0x55FF55, 0x5555FF55),
    NORMAL(0x00AA00, 0x5500AA00),
    OKAY(0xFF5555, 0x55FF5555),
    BAD(0xFF5555, 0x55FF5555);

    private final int RGB;
    private final int RGBA;

    PingColor(int rgbColor, int rgbaColor) {
        this.RGB = rgbColor;
        this.RGBA = rgbaColor;
    }

    public int getRGB() {
        return this.RGB;
    }

    public int getRGBA() {
        return this.RGBA;
    }

    public static PingColor getColor(int ping) {
        PingColor color = GOOD;

        if (ping > 150) {
            color = NORMAL;
        }
        if (ping > 300) {
            color = OKAY;
        }
        if (ping > 600) {
            color = BAD;
        }
        return color;
    }

}
