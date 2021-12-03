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
        return (char) ('A' + this.type);
    }

    public void remove() {
        world.removeBonus(this);
    }
}
