Image Tagger

Image Tagger is a simple software application for predicting an image's keywords using a deep learning model based on resnet.

It allows photographers to automate the image tagging process. 📸

---

## Instructions

TBD

Photo credit: [https://unsplash.com/@ndcphoto](https://unsplash.com/@ndcphoto)

## Development

If you want to build the application yourself, you will need Java 21 JDK.

The release archive is in the [releases page](https://github.com/dnutiu/ImageTagger/releases).


### Building and Running from source

To build from source you will need Java 21 JDK and Gradle.

Due to some GitHub limitations that do not allow me to upload large files, you'll need to download the AIModels
zip file which contains the deep learning models and place them into the `ImageTagger/src/main/resources/AIModels` path.

To build the project run:

```bash
gradle build
```

To run:

```bash
gradle run
```

# Blog

You can visit my tech blog at [https://blog.nuculabs.dev](https://blog.nuculabs.dev).