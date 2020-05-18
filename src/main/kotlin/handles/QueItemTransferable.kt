package handles

import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.awt.datatransfer.UnsupportedFlavorException
import java.io.IOException

class QueItemTransferable(
    private val objectFlavor: DataFlavor,
    private val queItemTransferablePackage: QueItemTransferablePackage
) : Transferable {

    override fun getTransferDataFlavors(): Array<DataFlavor> {
        return arrayOf(objectFlavor)
    }

    override fun isDataFlavorSupported(dataFlavor: DataFlavor): Boolean {
        return objectFlavor == dataFlavor
    }

    @Throws(UnsupportedFlavorException::class, IOException::class)
    override fun getTransferData(dataFlavor: DataFlavor): QueItemTransferablePackage {
        if (!isDataFlavorSupported(dataFlavor)) {
            throw UnsupportedFlavorException(dataFlavor)
        }
        return queItemTransferablePackage
    }
}