[ ![Download](https://api.bintray.com/packages/thesurix/maven/gesture-recycler/images/download.svg) ](https://bintray.com/thesurix/maven/gesture-recycler/_latestVersion)
<a href="https://opensource.org/licenses/Apache-2.0" target="_blank"><img src="https://img.shields.io/badge/License-Apache_v2.0-blue.svg?style=flat"/></a> 
[![API](https://img.shields.io/badge/API-15%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=15)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-gesture--recycler-green.svg?style=true)](https://android-arsenal.com/details/1/3317)

# Gesture Recycler
This library provides swipe & drag and drop support for RecyclerView. Based on great example from [Android-ItemTouchHelper-Demo](https://github.com/iPaulPro/Android-ItemTouchHelper-Demo).

# Demo
![](http://i.giphy.com/xT9DPGkRUkPiH3Qum4.gif)

# Features
* swipe 
* long press drag
* manual mode drag
* support for different layout managers
* predefined drag & swipe flags for RecyclerView's layout managers

# Dependency

To use this library in your android project, just simply add the following dependency into your build.gradle

```sh
dependencies {
    compile 'com.thesurix.gesturerecycler:gesture-recycler:1.1.0'
}
```

# How to use?

Define your RecyclerView and adapter as usually:
```java
final LinearLayoutManager manager = new LinearLayoutManager(getContext());
recyclerView.setHasFixedSize(true);
recyclerView.setLayoutManager(manager);

// Extend GestureAdapter and write your own
// ViewHolder items must extend GestureViewHolder
final MonthsAdapter adapter = new MonthsAdapter(getContext(), R.layout.linear_item);
adapter.setData(getMonths());
recyclerView.setAdapter(adapter);
```
To enable swipe/drag and drop support:
```java
GestureManager gestureManager = new GestureManager.Builder(mRecyclerView)
                 // Enable swipe
                .setSwipeEnabled(true)
                 // Enable long press drag and drop 
                .setLongPressDragEnabled(true)
                 // Enable manual drag from the begging, you need to provide View inside your GestureViewHolder
                .setManualDragEnabled(true)
                 // Use custom gesture flags
                 // Do not use this method if you want predefined flagsfor RecyclerView layout manager 
                .setGestureFlags(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.UP | ItemTouchHelper.DOWN)
                .build();
```

Any callbacks? Sure:
```java
adapter.setDataChangeListener(new GestureAdapter.OnDataChangeListener<MonthItem>() {
            @Override
            public void onItemRemoved(final MonthItem item, final int position) {
            }

            @Override
            public void onItemReorder(final MonthItem item, final int fromPos, final int toPos) {
            }
        });
```
To support all animations use GestureAdapter interface:
```java
// Support for data animations
adapter.add(month);
adapter.insert(month, 5);
adapter.remove(5);

// This will interrupt pending animations
adapter.setData(months)
```
## Old way (v1.0.0):
To enable swipe/drag and drop support:
```java
final GestureTouchHelperCallback touchHelperCallback = new GestureTouchHelperCallback(adapter);
// Enable swipe
touchHelperCallback.setSwipeEnabled(true);
// Enable long press drag and drop 
touchHelperCallback.setLongPressDragEnabled(true);

// Sets predefined swipe & drag and drop flags based on layout manager type
touchHelperCallback.setGestureFlagsForLayout(manager);

//or use your own
touchHelperCallback.setDragFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN);
touchHelperCallback.setSwipeFlags(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);

final ItemTouchHelper touchHelper = new ItemTouchHelper(touchHelperCallback);
touchHelper.attachToRecyclerView(recyclerView); 
```
To enable manual drag:
```java
// Enable manual drag, you need to provide View inside your GestureViewHolder
touchHelperCallback.setManualDragEnabled(true);
// Attach default listener for drag triggering
adapter.setGestureListener(new GestureListener(touchHelper));
```
# Help
See examples.

# To do
* item click listener
* undo?
* empty view?
* tests?

# Licence

```
Copyright 2016 thesurix

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
