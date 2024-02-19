package persistance.entity;

public enum Unit {
    GRAMS(0), MILLILITERS(0), PIECES(0);
    int count;
    Unit(int count) {
        this.count = count;
    }
    public void setCount(int count) {
        this.count = count;
    }
}
