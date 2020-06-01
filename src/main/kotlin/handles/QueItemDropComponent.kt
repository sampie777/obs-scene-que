package handles

import objects.que.QueItem

interface QueItemDropComponent {
    fun dropNewItem(item: QueItem, index: Int): Boolean
    fun dropMoveItem(item: QueItem, fromIndex: Int, toIndex: Int): Boolean
}