package handles

import gui.quickAccessButtons.QuickAccessButton
import plugins.common.QueItem
import java.awt.Component
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.util.logging.Logger
import javax.swing.JComponent
import javax.swing.JList
import javax.swing.SwingUtilities
import javax.swing.TransferHandler

class QueItemTransferHandler(var queItem: QueItem? = null) : TransferHandler() {

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
    override fun createTransferable(component: JComponent): Transferable {
        var fromIndex = -1
        val item: QueItem

        if (queItem != null) {
            item = queItem!!
        } else if (component is JList<*>) {
            // Get data that needs to be dragged
            item = component.selectedValue as QueItem
            fromIndex = component.selectedIndex
        } else if (component is QuickAccessButton) {
            // Get data that needs to be dragged
            item = component.getQueItem() ?: throw IllegalArgumentException("QueItem is null")
        } else {
            logger.severe("Failed to get a valid QueItem from transfer handler")
            throw IllegalArgumentException("Failed to get a valid QueItem from transfer handler")
        }

        logger.info("Start dragging QueItem: $item")

        val isCopyingToAQueItemDropComponent = isParentAQueItemDropComponent(component)

        return QueItemTransferable(
            queItemObjectFlavor,
            QueItemTransferablePackage(item, fromIndex, isCopyingToAQueItemDropComponent)
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

        val dropComponent =
            if (info.component is QueItemDropComponent) {
                info.component as QueItemDropComponent
            } else {
                SwingUtilities.getAncestorOfClass(
                    QueItemDropComponent::class.java,
                    info.component
                ) as QueItemDropComponent
            }

        if (info.dropLocation is JList.DropLocation) {
            val dropList = info.dropLocation as JList.DropLocation
            dropIndex = dropList.index
        }

        // Get the object that is being dropped.
        val transferablePackage: QueItemTransferablePackage = try {
            transferable.getTransferData(queItemObjectFlavor) as QueItemTransferablePackage
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

        if (transferablePackage.isCopyingToQueItemDropComponent) {
            try {
                dropped = dropComponent.dropMoveItem(transferablePackage.item, transferablePackage.fromIndex, dropIndex)
            } catch (e: Exception) {
                logger.warning("Failed to drop Move Item")
                e.printStackTrace()
            }
        } else {
            try {
                dropped = dropComponent.dropNewItem(transferablePackage.item, dropIndex)
            } catch (e: Exception) {
                logger.warning("Failed to drop New Item")
                e.printStackTrace()
            }
        }
        return dropped
    }

    private fun isParentAQueItemDropComponent(component: Component): Boolean {
        return component is QueItemDropComponent
                || SwingUtilities.getAncestorOfClass(QueItemDropComponent::class.java, component) != null
    }
}