package li.cil.oc.api.driver;

/**
 * This interface can be added to <em>environments</em> generated by block
 * drivers to provide a 'preferred name' in case the driver is merged with
 * other block drivers (interface based drivers such as for <tt>IInventory</tt>).
 * <p/>
 * This was previously to be implemented on the driver itself, but that has been
 * deprecated. Implement it in the environment returned from the block driver's
 * {@link SidedBlock#createEnvironment(net.minecraft.world.World, net.minecraft.util.BlockPos, net.minecraft.util.EnumFacing)}
 * method instead.
 */
public interface NamedBlock {
    /**
     * The preferred name, in case the driver is merged with others.
     *
     * @return the preferred name.
     */
    String preferredName();

    /**
     * This is used to determine which name to use in case multiple
     * environments with this interface are merged.
     * <p/>
     * If multiple named environments are merged the name of the environment
     * with the <em>highest</em> priority is selected. Negative values are
     * allowed. The recommended default value is therefore zero.
     * <p/>
     * If multiple environments with the same priority exist, the
     * result is unspecified. It will usually result in the environment
     * of the driver that was registered first.
     *
     * @return the priority with which to use this name.
     */
    int priority();
}
