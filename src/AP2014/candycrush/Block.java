package AP2014.candycrush;

import org.omg.CORBA.PRIVATE_MEMBER;

import java.awt.*;

class Block {
    private char value;
    private int x,y;

    public Block(char value,int x,int y) {

        this.value=value;
        this.x=x;
        this.y=y;
    }

    public boolean isEmpty() {
        return (value=='\0');
    }

    public void swapWith(Block block) {
        char t=block.value;
        block.value=value;
        value=t;
    }


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }

    public Color getColor() {
        Color[] colors={Color.red,Color.green,Color.blue,Color.pink,Color.cyan,
                Color.orange,Color.magenta,Color.yellow,Color.white};
        return colors[(getValue()-'a')%colors.length];
    }

    public boolean isJoinableWith(Block b){
        return (b.getValue()==getValue());
    }

}