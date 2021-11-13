package world;

import java.util.*;

public class PowerBrotherAI extends PlayerAI {

    public PowerBrotherAI(Creature creature, World world, List<String> messages) {
        super(creature, world, messages);
    }

    @Override
    public void run() {
        onSkill = true;
        try {
            Thread.sleep(500);
        } catch (Exception e) {
            // TODO: handle exception
        }
        onSkill = false;
    }
}
