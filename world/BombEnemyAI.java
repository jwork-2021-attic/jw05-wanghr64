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
            Point nxtPoint = route.peekFirst();
            if (nxtPoint != null) {
                boolean moved = creature.moveBy(nxtPoint.x - creature.x(), nxtPoint.y - creature.y());
                if (moved)
                    route.pollFirst();
            }

            // int r = rd.nextInt(4);
            // creature.moveBy(dx[r], dy[r]);
            try {
                Thread.sleep(rd.nextInt(300) + 200);
            } catch (Exception e) {
            }
        }
    }
}
