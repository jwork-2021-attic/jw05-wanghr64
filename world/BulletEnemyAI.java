package world;

import java.util.*;

public class BulletEnemyAI extends EnemyAI {
    public BulletEnemyAI(Creature creature, World world, Player player) {
        super(creature, world, player);
    }

    @Override
    public void run() {
        while (true) {

            dstX = player.x();
            dstY = player.y();
            bfsRouter();

            if (rd.nextInt(15) == 0) {
                int dx = dstX - creature.x();
                int dy = dstY - creature.y();
                Bullet newBullet = null;
                if (Math.abs(dx) > Math.abs(dy)) {
                    if (dx < 0)
                        newBullet = new Bullet(world, 2, creature.x() + 1, creature.y(), '9');
                    else
                        newBullet = new Bullet(world, 0, creature.x() - 1, creature.y(), '9');
                } else {
                    if (dy < 0)
                        newBullet = new Bullet(world, 3, creature.x(), creature.y() - 1, '9');
                    else
                        newBullet = new Bullet(world, 1, creature.x(), creature.y() + 1, '9');
                }
                world.addBullet(newBullet);
                new Thread(newBullet).start();
            }

            if (rd.nextInt(5) <= 2) {
                Point nxtPoint = route.peekFirst();
                if (nxtPoint != null) {
                    boolean moved = creature.moveBy(nxtPoint.x - creature.x(), nxtPoint.y - creature.y());
                    if (moved)
                        route.pollFirst();
                }
            } else {
                int r = rd.nextInt(4);
                creature.moveBy(dx[r], dy[r]);
            }
            try {
                Thread.sleep(rd.nextInt(300) + 300);
            } catch (Exception e) {
            }
        }
    }
}
