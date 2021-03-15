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

package com.google.ar.core.examples.java.ml

import android.app.Activity
import android.widget.Toast
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.ar.core.ArCoreApk
import com.google.ar.core.Session
import com.google.ar.core.examples.java.common.helpers.CameraPermissionHelper
import com.google.ar.core.exceptions.CameraNotAvailableException

/**
 * Manages an ARCore Session using the Android Lifecycle API.
 * Before starting a Session, this class requests an install of ARCore, if necessary,
 * and asks the user for permissions, if necessary.
 */
class ARCoreSessionLifecycleHelper(
  val activity: Activity,
  val features: Set<Session.Feature> = setOf()
) : DefaultLifecycleObserver {
  var installRequested = false
  var sessionCache: Session? = null
    private set

  // Creating a Session may fail. In this case, sessionCache will remain null, and this function will be called with an exception.
  // See https://developers.google.com/ar/reference/java/com/google/ar/core/Session#Session(android.content.Context)
  // for more information.
  var exceptionCallback: ((Exception) -> Unit)? = null

  // After creating a session, but before Session.resume is called is the perfect time to setup a session.
  // Generally, you would use Session.configure or setCameraConfig here.
  // https://developers.google.com/ar/reference/java/com/google/ar/core/Session#public-void-configure-config-config
  // https://developers.google.com/ar/reference/java/com/google/ar/core/Session#setCameraConfig(com.google.ar.core.CameraConfig)
  var beforeSessionResume: ((Session) -> Unit)? = null

  // Creates a session. If ARCore is not installed, an installation will be requested.
  fun tryCreateSession(): Session? {
    // Request an installation if necessary.
    when (ArCoreApk.getInstance().requestInstall(activity, !installRequested)!!) {
      ArCoreApk.InstallStatus.INSTALL_REQUESTED -> {
        installRequested = true
        // tryCreateSession will be called again, so we return null for now.
        return null
      }
      ArCoreApk.InstallStatus.INSTALLED -> {
        // Left empty; nothing needs to be done
      }
    }

    // Create a session if ARCore is installed.
    return try {
      Session(activity, features)
    } catch (e: Exception) {
      exceptionCallback?.invoke(e)
      null
    }
  }

  override fun onResume(owner: LifecycleOwner) {
    if (!CameraPermissionHelper.hasCameraPermission(activity)) {
      CameraPermissionHelper.requestCameraPermission(activity)
      return
    }

    val session = tryCreateSession() ?: return
    try {
      beforeSessionResume?.invoke(session)
      session.resume()
      sessionCache = session
    } catch (e: CameraNotAvailableException) {
      exceptionCallback?.invoke(e)
    }
  }

  override fun onPause(owner: LifecycleOwner) {
    sessionCache?.pause()
  }

  override fun onDestroy(owner: LifecycleOwner) {
    // Explicitly close ARCore Session to release native resources.
    // Review the API reference for important considerations before calling close() in apps with
    // more complicated lifecycle requirements:
    // https://developers.google.com/ar/reference/java/arcore/reference/com/google/ar/core/Session#close()
    sessionCache?.close()
    sessionCache = null
  }

  fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
  ) {
    if (!CameraPermissionHelper.hasCameraPermission(activity)) {
      Toast.makeText(activity, "Camera permission is needed to run this application", Toast.LENGTH_LONG)
        .show()
      if (!CameraPermissionHelper.shouldShowRequestPermissionRationale(activity)) {
        // Permission denied with checking "Do not ask again".
        CameraPermissionHelper.launchPermissionSettings(activity)
      }
      activity.finish()
    }
  }
}
