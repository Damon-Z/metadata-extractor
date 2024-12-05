`ImageMetadataExtractor` determines the file's type automatically from the first bytes of the file. It uses the `FileTypeDetector` class for this purpose, and you can also use it in your code if needed.

For example:

```java
FileType fileType = FileTypeDetector.detectFileType(myStream);

if (fileType == FileType.Jpeg) {
    // ...
} else if (fileType == FileType.Png) {
    // ...
```

The detector will return `FileType.Unknown` if it cannot determine the file type.

Note that detection is based on only the first few bytes of the file and does not guarantee that the image and/or metadata can be successfully decoded.