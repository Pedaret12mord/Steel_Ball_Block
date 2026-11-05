//package net.chargablepipe.keeplooking4ward.block.custom;
//
//import net.minecraft.core.Direction;
//import net.minecraft.util.RandomSource;
//import net.minecraft.world.item.context.BlockPlaceContext;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.block.state.StateDefinition;
//import net.minecraft.world.level.block.state.properties.DirectionProperty;
//import org.jetbrains.annotations.Nullable;
//
//import java.util.Properties;
//
//public class SteelBallBlock extends Block {
//    public static DirectionProperty Facing = DirectionProperty.create("number",
//            Direction.UP,
//            Direction.NORTH,
//            Direction.EAST,
//            Direction.SOUTH,
//            Direction.WEST,
//            Direction.DOWN);
//
//    public SteelBallBlock(Properties properties)
//    {
//        super(properties);
//    }
//
//    @Nullable
//    @Override
//    public BlockState getStateForPlacement(BlockPlaceContext p_49820_) {
//        return this.defaultBlockState().setValue(Facing, )
//    }
//}
