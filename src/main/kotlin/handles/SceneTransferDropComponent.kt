package handles

import objects.TScene

interface SceneTransferDropComponent {
    fun dropNewScene(scene: TScene, index: Int): Boolean
    fun dropMoveScene(fromIndex: Int, toIndex: Int): Boolean
}