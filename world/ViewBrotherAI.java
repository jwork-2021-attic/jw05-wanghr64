package world;

import java.util.*;

public class ViewBrotherAI extends PlayerAI {

    public ViewBrotherAI(Creature creature, List<String> messages) {
        super(creature, messages);
    }

    @Override
    public void run() {
        allSee = true;
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
        }
        allSee = false;
    }
}