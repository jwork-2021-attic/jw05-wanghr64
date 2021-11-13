package world;

import java.util.*;

public class WaterBrotherAI extends PlayerAI {

    public WaterBrotherAI(Creature creature, World world, List<String> messages) {
        super(creature, world, messages);
    }

    @Override
    public void run() {
        onSkill = true;
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        onSkill = false;
    }
}