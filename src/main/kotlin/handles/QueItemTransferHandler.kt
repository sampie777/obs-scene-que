package handles

import plugins.common.QueItem
import java.awt.Component
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.util.logging.Logger
import javax.swing.JComponent
import javax.swing.JList
import javax.swing.SwingUtilities
import javax.swing.TransferHandler

class QueItemTransferHandler : TransferHandler() {

    private val logger = Logger.getLogger(QueItemTransferHandler::class.java.name)

    private val queItemObjectFlavor: DataFlavor = DataFlavor(QueItem::class.java, "QueItem")

    // Location where item was added
    private var dropIndex = -1

    // Check if item was dropped successfully
    private var dropped = false

    /**
     * When dragging an object:
     * Check if draggable object can be imported on the current location
     */
    override fun canImport(info: TransferSupport): Boolean {
        return info.isDrop
                && info.isDataFlavorSupported(queItemObjectFlavor)
                && isParentAQueItemDropComponent(info.component)
    }

    /**
     * When starting to drag an object:
     * Bundle up the selected item in a single liturgieItemList for export.
     */
    override fun createTransferable(c: JComponent): Transferable {
        val list = c as JList<*>

        // Get data that needs to be dragged
        val item: QueItem = list.selectedValue as QueItem
        logger.info("Start dragging QueItem: $item")

        val isCopyingToAQueItemDropComponent = isParentAQueItemDropComponent(c)

        return QueItemTransferable(
            queItemObjectFlavor,
            QueItemTransferablePackage(item, list.selectedIndex, isCopyingToAQueItemDropComponent)
        )
    }

    /**
     * We support copy and move actions.
     */
    override fun getSourceActions(c: JComponent): Int {
        return TransferHandler.COPY_OR_MOVE
    }

    /**
     * When dropping an object:
     * Perform the actual import.
     */
    override fun importData(info: TransferSupport): Boolean {
        if (!info.isDrop) {
            return false
        }

        val transferable = info.transferable

        val dropComponent = SwingUtilities.getAncestorOfClass(
            QueItemDropComponent::class.java,
            info.component
        ) as QueItemDropComponent

        // Get drop index
        val dropList = info.dropLocation as JList.DropLocation
        dropIndex = dropList.index

        // Get the object that is being dropped.
        val transferablePackage: QueItemTransferablePackage = try {
            transferable.getTransferData(queItemObjectFlavor) as QueItemTransferablePackage
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

        if (transferablePackage.isCopyingToQueItemDropComponent) {
            dropped = dropComponent.dropMoveItem(transferablePackage.fromIndex, dropIndex)
        } else {
            dropped = dropComponent.dropNewItem(transferablePackage.item, dropIndex)
        }
        return dropped
    }

    private fun isParentAQueItemDropComponent(component: Component): Boolean {
        return SwingUtilities.getAncestorOfClass(QueItemDropComponent::class.java, component) != null
    }
}