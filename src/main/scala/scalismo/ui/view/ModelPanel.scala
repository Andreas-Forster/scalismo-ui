package scalismo.ui.view

import scala.swing.{ BorderPanel, Orientation, SplitPane }

class ModelPanel(val frame: ScalismoFrame) extends BorderPanel {

  def setupPanels(): Unit = {
    val verticalSplit = new SplitPane(Orientation.Horizontal, nodesPanel, propertiesPanel) {
      resizeWeight = 0.5
    }
    // for safety, keep this out of the constructor, to make sure
    // that preferredSize returns something sensible
    verticalSplit.dividerLocation = (verticalSplit.preferredSize.height * 0.5).toInt

    // disabled for now, as it interferes with automatic size adjustion
    //val horizontalSplit = Component.wrap(new ExpandablePane(JSplitPane.HORIZONTAL_SPLIT, verticalSplit.peer))
    layout(verticalSplit) = BorderPanel.Position.Center
  }

  val nodesPanel = new NodesPanel(frame)
  val propertiesPanel = new NodePropertiesPanel(frame)

  setupPanels()
}