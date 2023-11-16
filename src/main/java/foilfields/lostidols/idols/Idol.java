package foilfields.lostidols.idols;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockWithEntity;

public abstract class Idol extends BlockWithEntity implements BlockEntityProvider {
    protected Idol(Settings settings) {
        super(settings);
    }
}
