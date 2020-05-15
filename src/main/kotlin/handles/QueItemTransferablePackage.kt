package handles

import plugins.common.QueItem

class QueItemTransferablePackage(
    val item: QueItem,
    val fromIndex: Int,
    val isCopyingToQueItemDropComponent: Boolean
)