package org.example;

public class Block {
    int y;
    int x;
    TypeOfBlock type;

    public Block(int y, int x) {
        this.y = y;
        this.x = x;
        this.type = TypeOfBlock.None;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public TypeOfBlock getType() {
        return type;
    }

    public void setType(TypeOfBlock type) {
        this.type = type;
    }
}
