# ARCore ML sample

An [ARCore](https://developers.google.com/ar) sample demonstrating how to use
camera images as an input for machine learning algorithms, and how to use the
results of the inference model to create anchors in the AR scene.

<p align="center">
  <img width="200" src="docs/screenshot.png">
</p>

This sample uses [ML Kit's Object Detection](https://developers.google.com/ml-kit/vision/object-detection)
and (optionally) [Google's Cloud Vision API](https://cloud.google.com/vision/docs/object-localizer)
to infer object labels from camera images.

## Getting Started
To try this app, you'll need the following:

 * An ARCore compatible device running [Google Play Services for AR](https://play.google.com/store/apps/details?id=com.google.ar.core) (ARCore) 1.24 or later
 * Android Studio 4.1 or later
 
### Configure ML Kit's classification model
By default, this sample uses ML Kit's built-in coarse classifier, which is only built for five categories and provides limited information about the detected objects.

For better classification results:

1. Read [Label images with a custom model on Android](https://developers.google.com/ml-kit/vision/object-detection/custom-models/android)
   on ML Kit's documentation website.
2. Modify `MLKitObjectAnalyzer.kt` in `app/src/main/java/com/google/ar/core/examples/java/ml/classification/` to specify a custom model.

### \[Optional] Configure Google Cloud Vision API
This sample also supports results from [the Google Cloud Vision API](https://cloud.google.com/vision/docs/object-localizer) for even more information on detected objects.

To configure Google Cloud Vision APIs:

1. Follow steps for configuring a Google Cloud project, enabling billing, enabling the API, and enabling a service account on [Set up the Vision API documentation](https://cloud.google.com/vision/docs/setup).
2. Save the resulting service account key file to `app/src/main/res/raw/credentials.json`.

## License

    Copyright 2021 Google LLC

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        https://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
