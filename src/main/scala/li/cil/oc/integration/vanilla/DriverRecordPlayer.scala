package li.cil.oc.integration.vanilla

import li.cil.oc.api.driver.EnvironmentProvider
import li.cil.oc.api.driver.NamedBlock
import li.cil.oc.api.machine.Arguments
import li.cil.oc.api.machine.Callback
import li.cil.oc.api.machine.Context
import li.cil.oc.api.network.ManagedEnvironment
import li.cil.oc.api.prefab.DriverTileEntity
import li.cil.oc.integration.ManagedTileEntityEnvironment
import li.cil.oc.util.ResultWrapper.result
import net.minecraft.block.Block
import net.minecraft.block.BlockJukebox
import net.minecraft.init.Blocks
import net.minecraft.item.Item
import net.minecraft.item.ItemRecord
import net.minecraft.item.ItemStack
import net.minecraft.util.BlockPos
import net.minecraft.world.World

object DriverRecordPlayer extends DriverTileEntity {
  override def getTileEntityClass: Class[_] = classOf[BlockJukebox.TileEntityJukebox]

  override def createEnvironment(world: World, pos: BlockPos): ManagedEnvironment =
    new Environment(world.getTileEntity(pos).asInstanceOf[BlockJukebox.TileEntityJukebox])

  final class Environment(tileEntity: BlockJukebox.TileEntityJukebox) extends ManagedTileEntityEnvironment[BlockJukebox.TileEntityJukebox](tileEntity, "jukebox") with NamedBlock {
    override def preferredName = "jukebox"

    override def priority = 0

    @Callback(doc = "function():string -- Get the title of the record currently in the jukebox.")
    def getRecord(context: Context, args: Arguments): Array[AnyRef] = {
      val record = tileEntity.getRecord
      if (record != null && record.getItem.isInstanceOf[ItemRecord]) {
        result(record.getItem.asInstanceOf[ItemRecord].getRecordNameLocal)
      }
      else null
    }

    @Callback(doc = "function() -- Start playing the record currently in the jukebox.")
    def play(context: Context, args: Arguments): Array[AnyRef] = {
      val record = tileEntity.getRecord
      if (record != null && record.getItem.isInstanceOf[ItemRecord]) {
        tileEntity.getWorld.playAuxSFXAtEntity(null, 1005, tileEntity.getPos, Item.getIdFromItem(record.getItem))
        result(true)
      }
      else null
    }

    @Callback(doc = "function() -- Stop playing the record currently in the jukebox.")
    def stop(context: Context, args: Arguments): Array[AnyRef] = {
      tileEntity.getWorld.playAuxSFX(1005, tileEntity.getPos, 0)
      tileEntity.getWorld.playRecord(tileEntity.getPos, null)
      null
    }
  }

  object Provider extends EnvironmentProvider {
    override def getEnvironment(stack: ItemStack): Class[_] = {
      if (stack != null && Block.getBlockFromItem(stack.getItem) == Blocks.jukebox)
        classOf[Environment]
      else null
    }
  }

}
