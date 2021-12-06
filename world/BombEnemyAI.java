package world;

import java.util.*;

public class BombEnemyAI extends EnemyAI {
    public BombEnemyAI(Creature creature, World world, Player player) {
        super(creature, world, player);
    }

    @Override
    public void run() {
        while (true) {
            if (dstX != player.x() || dstY != player.y()) {
                dstX = player.x();
                dstY = player.y();
                bfsRouter();
            }

            if (Math.abs(player.x() - creature.x()) < 2 && Math.abs(player.y() - creature.y()) < 2) {
                player.modifyHP(-10);
                world.remove(creature);
                break;
            }

            Point nxtPoint = route.peekFirst();
            if (nxtPoint != null) {
                boolean moved = creature.moveBy(nxtPoint.x - creature.x(), nxtPoint.y - creature.y());
                if (moved)
                    route.pollFirst();
            }

            try {
                Thread.sleep(rd.nextInt(200) + 100);
            } catch (Exception e) {
            }
        }
    }
}
