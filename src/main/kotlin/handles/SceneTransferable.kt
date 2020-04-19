package handles

import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.awt.datatransfer.UnsupportedFlavorException
import java.io.IOException

class SceneTransferable(
    private val objectFlavor: DataFlavor,
    private val sceneTransferablePackage: SceneTransferablePackage
) : Transferable {

    override fun getTransferDataFlavors(): Array<DataFlavor> {
        return arrayOf(objectFlavor)
    }

    override fun isDataFlavorSupported(dataFlavor: DataFlavor): Boolean {
        return objectFlavor == dataFlavor
    }

    @Throws(UnsupportedFlavorException::class, IOException::class)
    override fun getTransferData(dataFlavor: DataFlavor): SceneTransferablePackage {
        if (!isDataFlavorSupported(dataFlavor)) {
            throw UnsupportedFlavorException(dataFlavor)
        }
        return sceneTransferablePackage
    }
}