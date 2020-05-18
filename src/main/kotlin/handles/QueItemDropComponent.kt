package handles

import plugins.common.QueItem

interface QueItemDropComponent {
    fun dropNewItem(item: QueItem, index: Int): Boolean
    fun dropMoveItem(item: QueItem, fromIndex: Int, toIndex: Int): Boolean
}