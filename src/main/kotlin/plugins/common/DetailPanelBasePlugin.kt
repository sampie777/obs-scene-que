package plugins.common

import javax.swing.JComponent

interface DetailPanelBasePlugin : BasePlugin {

    /**
     * Renders the panel component for the detail panel (right bottom panel of the main split pane)
     */
    fun detailPanel(): JComponent
}