package world;

import java.util.*;

public class FireBrotherAI extends PlayerAI {

    public FireBrotherAI(Creature creature, List<String> messages) {
        super(creature, messages);
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