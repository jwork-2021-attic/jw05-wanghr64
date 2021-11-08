package world;

import java.awt.Color;

public class Player extends Creature {
    public Player(World world, char glyph, Color color, int maxHP, int attack, int defense, int visionRadius) {
        super(world, glyph, color, maxHP, attack, defense, visionRadius);
    }

    public int digCount = 0;
}
