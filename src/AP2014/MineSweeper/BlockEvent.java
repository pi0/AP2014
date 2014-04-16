package AP2014.minesweeper;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

public class BlockEvent extends ComponentEvent{

    public final Object arg0;

    public final static int BLOCK_PRESSED=
            AWTEvent.RESERVED_ID_MAX+10;
    public final static int BLOCK_SHOWBLOCK=
            AWTEvent.RESERVED_ID_MAX+11;
    public final static int BLOCK_SHOWBLOCK_REQUEST=
            AWTEvent.RESERVED_ID_MAX+12;
    public final static int BLOCK_SHOWBLOCK_MOUSECHANGE=
            AWTEvent.RESERVED_ID_MAX+13;
    public final static int BLOCK_SHOWBLOCK_UPDATE=
            AWTEvent.RESERVED_ID_MAX+14;

    private BlockEvent(Component src, int id,Object arg0) {
        super(src,id);
        this.arg0=arg0;
    }

    public static BlockEvent blockClicked(Block src) {
        return new BlockEvent(src,BLOCK_PRESSED,null);
    }

    public static BlockEvent showBlock(Block src,ArrayList<Block> neighbours) {
        return new BlockEvent(src,BLOCK_SHOWBLOCK,neighbours);
    }

    public static BlockEvent showBlockRequest(Block dst) {
        return new BlockEvent(dst,BLOCK_SHOWBLOCK_REQUEST,dst);
    }

    public static BlockEvent mouseChange(Block src,Boolean state) {
        return new BlockEvent(src,BLOCK_SHOWBLOCK_MOUSECHANGE,state);
    }

    public static BlockEvent update(Block src) {
        return new BlockEvent(src,BLOCK_SHOWBLOCK_UPDATE,null);
    }

}
