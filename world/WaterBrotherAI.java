package world;

import java.util.*;

public class WaterBrotherAI extends PlayerAI {

    public WaterBrotherAI(Creature creature, World world, List<String> messages) {
        super(creature, world, messages);
    }

    @Override
    public void run() {
        onSkill = true;
        int r = 5;
        int x = player.x();
        int y = player.y();
        Creature enemy = null;
        for (int i = 0; i < r; ++i)
            for (int j = 0; j < r - i; ++j) {
                if (i == 0 && j == 0)
                    continue;
                enemy = world.creature(x + j, y + i);
                if (enemy != null)
                    player.attack(enemy);
                enemy = world.creature(x + j, y - i);
                if (enemy != null)
                    player.attack(enemy);
                enemy = world.creature(x - j, y + i);
                if (enemy != null)
                    player.attack(enemy);
                enemy = world.creature(x - j, y - i);
                if (enemy != null)
                    player.attack(enemy);
            }
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        onSkill = false;
    }
}