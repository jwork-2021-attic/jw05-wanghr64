package world;

import java.awt.Color;

public class Player extends Creature {
    public Player(World world, char glyph, Color color, int maxHP, int attack, int defense, int visionRadius) {
        super(world, glyph, color, maxHP, attack, defense, visionRadius);
    }

    public int digCount = 0;

    public static Color id2Color(int id) {
        switch (id) {
        case 0:
            return Color.LIGHT_GRAY;
        case 1:
            return Color.ORANGE;
        case 2:
            return Color.YELLOW;
        case 3:
            return Color.RED;
        case 4:
            return Color.BLUE;
        case 5:
            return Color.GREEN;
        case 6:
            return Color.CYAN;
        default:
            return Color.LIGHT_GRAY;
        }
    }
}
