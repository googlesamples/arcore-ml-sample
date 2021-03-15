/*
 * Copyright 2021 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.ar.core.examples.java.ml.classification.utils

import com.google.cloud.vision.v1.NormalizedVertex

object VertexUtils {
  /**
   * Convert a [NormalizedVertex] to an absolute coordinate pair.
   */
  fun NormalizedVertex.toAbsoluteCoordinates(
    imageWidth: Int,
    imageHeight: Int,
  ): Pair<Int, Int> {
    return (x * imageWidth).toInt() to (y * imageHeight).toInt()
  }

  /**
   * Rotates a coordinate pair according to [imageRotation].
   */
  fun Pair<Int, Int>.rotateCoordinates(
    imageWidth: Int,
    imageHeight: Int,
    imageRotation: Int,
  ): Pair<Int, Int> {
    val (x, y) = this
    return when (imageRotation) {
      0 -> x to y
      180 -> imageWidth - x to imageHeight - y
      90 -> y to imageWidth - x
      270 -> imageHeight - y to x
      else -> error("Invalid imageRotation $imageRotation")
    }
  }

  /**
   * Calculate a point using the average of points in the bounding polygon.
   */
  fun List<NormalizedVertex>.calculateAverage(): NormalizedVertex {
    var averageX = 0f
    var averageY = 0f
    for (vertex in this) {
      averageX += vertex.x / size
      averageY += vertex.y / size
    }
    return NormalizedVertex.newBuilder().setX(averageX).setY(averageY).build()
  }

}