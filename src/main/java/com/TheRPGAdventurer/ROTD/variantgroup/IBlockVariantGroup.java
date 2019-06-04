package com.TheRPGAdventurer.ROTD.variantgroup;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * A group consisting of a collection of variants with one or more blocks registered for each one.
 *
 * @author Choonster
 */
public interface IBlockVariantGroup<VARIANT extends Enum<VARIANT> & IStringSerializable, BLOCK extends Block> {

    /**
     * Gets the name of this group.
     *
     * @return The group name
     */
    String getGroupName();

    /**
     * Gets the next variant in the variants collection.
     * <p>
     * If the specified variant is the last in the collection, the first variant is returned.
     * <p>
     * This is similar to (and adapted from) {@code BlockStateBase.cyclePropertyValue}.
     *
     * @param currentVariant The current variant
     * @return The next variant in the variants collection.
     */
    default VARIANT cycleVariant(final VARIANT currentVariant) {
        final Iterator<VARIANT> iterator = getVariants().iterator();

        while (iterator.hasNext()) {
            if (iterator.next().equals(currentVariant)) {
                if (iterator.hasNext()) {
                    return iterator.next();
                }

                return getVariants().iterator().next();
            }
        }

        return iterator.next();
    }

    /**
     * Gets this group's variants.
     *
     * @return The variants
     */
    Iterable<VARIANT> getVariants();

    /**
     * Gets this group's blocks.
     *
     * @return The blocks
     */
    Collection<BLOCK> getBlocks();

    /**
     * Registers this group's blocks.
     *
     * @param registry The block registry
     * @throws IllegalStateException If the blocks have already been registered
     */
    void registerBlocks(IForgeRegistry<Block> registry);

    /**
     * Registers this group's items.
     *
     * @param registry The item registry
     * @return The registered items
     * @throws IllegalStateException If the items have already been registered
     */
    List<? extends ItemBlock> registerItems(IForgeRegistry<Item> registry);
}