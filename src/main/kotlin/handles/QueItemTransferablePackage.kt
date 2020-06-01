package handles

import objects.que.QueItem

class QueItemTransferablePackage(
    val item: QueItem,
    val fromIndex: Int,
    val isCopyingToQueItemDropComponent: Boolean
)