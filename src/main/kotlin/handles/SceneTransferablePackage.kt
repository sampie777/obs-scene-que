package handles

import objects.TScene

class SceneTransferablePackage(
    val scene: TScene,
    val fromIndex: Int,
    val isCopyingToSceneTransferDropComponent: Boolean
)