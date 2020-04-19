package handles

import objects.TScene
import java.awt.Component
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.util.logging.Logger
import javax.swing.JComponent
import javax.swing.JList
import javax.swing.SwingUtilities
import javax.swing.TransferHandler

class SceneTransferHandler : TransferHandler() {

    private val logger = Logger.getLogger(SceneTransferHandler::class.java.name)

    private val sceneObjectFlavor: DataFlavor = DataFlavor(TScene::class.java, "TScene")

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
                && info.isDataFlavorSupported(sceneObjectFlavor)
                && isParentSceneTranfserDropComponent(info.component)
    }

    /**
     * When starting to drag an object:
     * Bundle up the selected item in a single liturgieItemList for export.
     */
    override fun createTransferable(c: JComponent): Transferable {
        val list = c as JList<*>

        // Get data that needs to be dragged
        val scene: TScene = list.selectedValue as TScene
        logger.info("Start dragging Scene: $scene")

        val isCopyingToSceneTransferDropComponent = isParentSceneTranfserDropComponent(c)

        return SceneTransferable(sceneObjectFlavor,
            SceneTransferablePackage(scene, list.selectedIndex, isCopyingToSceneTransferDropComponent))
    }

    /**
     * We support copy and move actions.
     */
    override fun getSourceActions(c: JComponent): Int {
        return COPY_OR_MOVE
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
            SceneTransferDropComponent::class.java,
            info.component
        ) as SceneTransferDropComponent

        // Get drop index
        val dropList = info.dropLocation as JList.DropLocation
        dropIndex = dropList.index

        // Get the object that is being dropped.
        val sceneTransferablePackage: SceneTransferablePackage = try {
            transferable.getTransferData(sceneObjectFlavor) as SceneTransferablePackage
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

        if (sceneTransferablePackage.isCopyingToSceneTransferDropComponent) {
            dropped = dropComponent.dropMoveScene(sceneTransferablePackage.fromIndex, dropIndex)
        } else {
            dropped = dropComponent.dropNewScene(sceneTransferablePackage.scene, dropIndex)
        }
        return dropped
    }

    private fun isParentSceneTranfserDropComponent(component: Component): Boolean {
        return SwingUtilities.getAncestorOfClass(SceneTransferDropComponent::class.java, component) != null
    }
}