package world;

import java.util.*;

public class PowerBrotherAI extends PlayerAI {

    public PowerBrotherAI(Creature creature, List<String> messages) {
        super(creature, messages);
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
