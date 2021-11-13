package world;

import java.util.*;

public class EnemyAI extends CreatureAI {

    Random rd;
    int[] dx;
    int[] dy;
    Player player;
    boolean finished;
    boolean[][] visited;
    Deque<int[]> route;
    int dstX;
    int dstY;

    public EnemyAI(Creature creature, World world, Player player) {
        super(creature, world);
        this.player = player;
        this.world = world;
        dstX = player.x();
        dstY = player.y();
        route = new LinkedList<>();
        visited = new boolean[world.width()][world.height()];
        dfsRouter(creature.x(), creature.y());
        rd = new Random();
        dx = new int[] { 1, 0, -1, 0 };
        dy = new int[] { 0, 1, 0, -1 };
    }

    @Override
    public void onEnter(int x, int y, Tile tile) {
        if (tile.isGround()) {
            creature.setX(x);
            creature.setY(y);
        }
    }

    @Override
    public void run() {
        while (true) {
            if (!canSee(player.x(), player.y())) {
                int rdIndex = rd.nextInt(4);
                creature.moveBy(dx[rdIndex], dy[rdIndex]);
            } else {
                if (dstX != player.x() || dstY != player.y()) {
                    dstX = player.x();
                    dstY = player.y();
                    for (int i = 0; i < world.width(); ++i)
                        for (int j = 0; j < world.height(); ++j)
                            visited[i][j] = false;
                    dfsRouter(creature.x(), creature.y());
                    for (int[] xy : route) {
                        System.out.printf("(%d, %d)", xy[0], xy[1]);
                    }
                    System.out.println();
                }

                // int[] m = route.pollFirst();
                /*
                 * if (m != null) creature.moveBy(m[0] - creature.x(), m[1] - creature.y());
                 * else { System.out.println("deque is null"); }
                 */

            }
            try {
                Thread.sleep(rd.nextInt(1000));
            } catch (Exception e) {
            }
        }
    }

    private void dfsRouter(int x, int y) {
        if (visited[x][y])
            return;
        visited[x][y] = true;
        route.offerLast(new int[] { x, y });
        if (x == dstX && y == dstY) {
            finished = true;
            return;
        }
        int[][] ds = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
        for (int[] d : ds) {
            int xx = x + d[0];
            int yy = y + d[1];
            if (!finished && xx >= 0 && xx < world.width() && yy >= 0 && yy < world.height()
                    && world.tile(xx, yy) == Tile.FLOOR)
                dfsRouter(xx, yy);
        }
        if (!finished)
            route.pollLast();
    }
}
