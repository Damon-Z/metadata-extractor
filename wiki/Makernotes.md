The Exif specification defines well-known tags that cover the most common metadata. Manufacturers often wish to store metadata that is specific to the features of their device in the Exif data too. For this purpose, _makernotes_ are used.

Metadata Extractor supports many common manufacturer makernotes. Some manufacturers have created multiple versions of their makernote standard over the years. In some cases, two or more manufacturers may share the same makernote standard.

## Expanding makernote support

Most manufacturers do not publish specifications for their makernote data. Knowledge of makernotes is mainly due to community research and experimentation.

Metadata Extractor indicates when it observes a tag value it cannot understand. If you'd like to help add support for an unsupported tag, the first step is to see whether someone online has already decoded the tag. If so, you can just straight to the coding step below.

If you have access to a camera that has a feature not currently supported by the library, you can try to work out where those values are stored in the makernote. To do this, take multiple photos while varying that setting and try to determine which tag maps to that feature. If you decode a new tag, either open a pull request that adds support for your image or capture your findings in an issue report so that others can further your work.

To add support in code, find the relevant `*MakernoteDirectory` to identify the `Tag` to update. You should rename the tag from "Unknown" to something that clearly and succinctly describes the data. If the data value requires custom presentation logic, extend the corresponding `*MakernoteDescriptor` class, adding a `Get*Description` method and calling it from the `switch` in `GetDescription`.

## Adding makernotes

To add a whole new makernote, you'll need to take a few steps.

Firstly, create your new `*MakernoteDirectory` and `*MakernoteDescriptor` classes. Look to existing implementations for inspiration.

Secondly, you'll need to add logic to `ExifTiffHandler.ProcessMakernote` to invoke parsing on your makernote. There is no standard approach to this. Many manufacturers prefix their makernote with special characters, which you can compare against to determine when your new makernote should be used. Alternatively you may need to look at the camera's "make" string. There are plenty of examples in this method for inspiration.

The makernote tag is a pointer to another location in the file at which a TIFF IFD should be located. You don't need to know the exact details of what a TIFF IFD are, only that if this pointer is off by even a single byte then it will not be successfully read. If you look through the existing code in `ExifTiffHandler.ProcessMakernote` you'll see some makernotes require amending the `ifdOffset` value or shifting the base offset of the `reader`. It may be necessary to experiment with values here, perhaps looking at the data in a hex editor to determine the correct adjustments.
