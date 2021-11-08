package world;

public class Bonus {

    public Bonus(World world, int type) {
        this.world = world;
        this.type = type;
    }

    private int type;

    public int type() {
        return type;
    }

    private World world;

    private int x;

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

    public char glyph() {
        switch (this.type) {
        case 0:
            return 'A';
        case 1:
            return 'B';
        case 2:
            return 'C';
        default:
            return 0;
        }
    }

    public void remove() {
        world.removeBonus(this);
    }
}
