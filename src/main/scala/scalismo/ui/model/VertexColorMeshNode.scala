/*
 * Copyright (C) 2016  University of Basel, Graphics and Vision Research Group
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package scalismo.ui.model

import java.io.File

import scalismo.geometry.Point3D
import scalismo.io.MeshIO
import scalismo.mesh.VertexColorMesh3D
import scalismo.ui.model.capabilities._
import scalismo.ui.model.properties._
import scalismo.ui.util.{FileIoMetadata, FileUtil}

import scala.util.{Failure, Success, Try}

class VertexColorMeshesNode(override val parent: GroupNode)
    extends SceneNodeCollection[VertexColorMeshNode]
    with Loadable {
  override val name: String = "Vertex Colored Triangle Meshes"

  override def loadMetadata: FileIoMetadata = FileIoMetadata.VertexColorMesh

  override def load(file: File): Try[Unit] = {
    val r = MeshIO.readVertexColorMesh3D(file)
    r match {
      case Failure(ex) => Failure(ex)
      case Success(mesh) =>
        add(mesh, FileUtil.basename(file))
        Success(())
    }
  }

  def add(mesh: VertexColorMesh3D, name: String): VertexColorMeshNode = {
    val node = new VertexColorMeshNode(this, mesh, name)
    add(node)
    node
  }
}

class VertexColorMeshNode(override val parent: VertexColorMeshesNode,
                          override val source: VertexColorMesh3D,
                          initialName: String)
    extends Transformable[VertexColorMesh3D]
    with InverseTransformation
    with Removeable
    with Renameable
    with HasLineWidth
    with HasOpacity
    with Saveable {
  name = initialName

  override def group: GroupNode = parent.parent

  override def remove(): Unit = parent.remove(this)

  override def inverseTransform(point: Point3D): Point3D = {
    val id = transformedSource.shape.pointSet.findClosestPoint(point).id
    source.shape.pointSet.point(id)
  }

  override def transform(untransformed: VertexColorMesh3D, transformation: PointTransformation): VertexColorMesh3D = {
    untransformed.transform(transformation)
  }

  override val opacity = new OpacityProperty()

  override val lineWidth = new LineWidthProperty()

  override def save(file: File): Try[Unit] = MeshIO.writeVertexColorMesh3D(transformedSource, file)

  override def saveMetadata: FileIoMetadata = FileIoMetadata.VertexColorMesh

}
