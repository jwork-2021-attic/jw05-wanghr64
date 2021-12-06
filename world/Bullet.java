package world;

public class Bullet implements Runnable {

    Bullet(World world, int direction, int x, int y, char glyph) {
        this.glyph = glyph;
        this.world = world;
        this.direction = direction;
        this.x = x;
        this.y = y;
    }

    private World world;

    private int x;

    private int direction;
    // 0:right 1:down 2:left 3:up

    private char glyph;
    private static final int[] dx;
    private static final int[] dy;

    public char glyph() {
        return this.glyph;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int x() {
        return x;
    }

    private int y;

    public void setY(int y) {
        this.y = y;
    }

    public int y() {
        return y;
    }

    private int attackValue;

    public int attackValue() {
        return this.attackValue;
    }

    public boolean moveBy(int mx, int my) {
        Creature other = world.creature(x + mx, y + my);
        Tile tile = world.tile(x + mx, y + my);

        if (other != null && Player.class.isAssignableFrom(other.getClass())) {
            int damage = Math.max(0, this.attackValue() - other.defenseValue());
            damage = (int) (Math.random() * damage) + 1;
            other.modifyHP(-damage);
            world.removeBullet(this);
            return false;
        }

        if (tile == Tile.WALL) {
            world.removeBullet(this);
            return false;
        }

        this.x += mx;
        this.y += my;

        return true;
    }

    static {
        dx = new int[] { 1, 0, -1, 0 };
        dy = new int[] { 0, 1, 0, -1 };
    }

    @Override
    public void run() {
        while (true) {
            if (!this.moveBy(dx[direction], dy[direction]))
                break;
            try {
                Thread.sleep(200);
            } catch (Exception e) {
            }
        }
    }
}
