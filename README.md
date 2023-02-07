# CircleButtonView

<img src="/preview/preview.gif" alt="sample" title="sample" width="300" height="600" align="right" />

[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
[![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=14)

This is an Android project that contains a custom view that contains a number of buttons around a centered button in a circular way. Additionally there is a demo project to demonstrate the possibilities this custom view provides. This demo project is illustrated in the left GIF.

USAGE
-----
To create a this custom view add a CircleButtonView in your Layout XML and add the CircularButtonView library to your project.

```groovy
  implementation project(':circlebuttons')
```
XML
----

```xml
<de.brainlezz.circlebuttons.CircleButtonView
            android:id="@+id/circleButtonView"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:layout_gravity="center"
            app:cb_angle_offset="45"
            app:cb_element_count="4"
            app:cb_inner_circle_portion="50"
            app:cb_element_spacing="10" />
```

You can use the following properties in your XML to change your CircleButtonView.

| Properties                       | Type                                                         | Default          |
| -------------------------------- | ------------------------------------------------------------ | ---------------- |
| `app:cb_inner_color`             | color                                                        | GREY             |
| `app:cb_outer_color`             | color                                                        | GREY             |
| `app:cb_highlight_color`         | color                                                        | LTGREY           |
| `app:cb_border_color`            | color                                                        | BLACK            |
| `app:cb_angle_offset`            | int                                                          | 0                |
| `app:cb_element_count`           | int                                                          | 4                |
| `app:cb_element_spacing`         | int                                                          | 10               |
| `app:cb_border_width`            | int                                                          | 5                |
| `app:cb_inner_circle_portion`    | int                                                          | 50               |
| `app:cb_center_enabled`          | boolean                                                      | true             |


Kotlin
----

All attributes can be set progamatically like this:
```kotlin
        var circleButtonView = findViewById<de.brainlezz.circlebuttons.CircleButtonView>(R.id.circleButtonView)

        // setting colors
        circleButtonView.innerColor = Color.RED
        circleButtonView.outerColor = Color.BLUE
        circleButtonView.highlightColor = Color.GREEN
        circleButtonView.borderColor = Color.YELLOW
        
        // setting dimensions
        circleButtonView.borderWidth = 5
        circleButtonView.elementSpacing = 10
        
        //setting button properties 
        circleButtonView.angleOffset = 90f          // rotation of all buttons, if an image is provided 
                                                    // the image is not rotated
        circleButtonView.circleElementCount = 8
        circleButtonView.innerCirclePortion = 80    // defines how much percent of the image is used for 
                                                    // the inner button
```

Icon Provider
---
To customize icons of all buttons it is possible to set a CircleButtonView.IconProvider like this:

```kotlin
      circleButtonView.iconProvider = object : CircleButtonView.IconProvider {
          override fun getIconForIndex(i: Int): Bitmap? {
              // return a bitmap that should be drawn on the button with index i
            return null
          }
      }
```

Click Events
---
To react on click events on all buttons it is possible to set a CircleButtonView.OnElementClickedListener like this:

```kotlin
      circleButtonView.addClickListener(object : CircleButtonView.OnElementClickedListener{
          override fun onElementClicked(i: Int) {
              // handle the click on button with index i
          }
      })
```

Indexes
---

To get the correct icons for the buttons and to react to the correct click event it is necessary to know that the indexes are clockwise with index 0 for our inner button, as you can see here:

<img src="/preview/circlebuttonview_indexes.png" alt="sample" title="sample" width="300" height="300" align="center" />

LICENCE
-----

CircularImageView by [Ramon Schubert](https://github.com/brainlezz) is licensed under a [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).