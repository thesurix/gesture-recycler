[ ![Download](https://api.bintray.com/packages/thesurix/maven/gesture-recycler/images/download.svg) ](https://bintray.com/thesurix/maven/gesture-recycler/_latestVersion)
<a href="https://opensource.org/licenses/Apache-2.0" target="_blank"><img src="https://img.shields.io/badge/License-Apache_v2.0-blue.svg?style=flat"/></a> 
[![API](https://img.shields.io/badge/API-15%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=15)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-gesture--recycler-green.svg?style=true)](https://android-arsenal.com/details/1/3317)

# Gesture Recycler
This library provides swipe & drag and drop support for RecyclerView. Based on great example from [Android-ItemTouchHelper-Demo](https://github.com/iPaulPro/Android-ItemTouchHelper-Demo).

# Demo
![](https://media.giphy.com/media/l3dj2EhnAGk5ZwiTS/giphy.gif)

# Features
* item click/long press/double tap listener
* background view for swipeable items
* empty view
* undo
* swipe 
* long press drag
* manual mode drag
* support for different layout managers
* predefined drag & swipe flags for RecyclerView's layout managers
* DiffUtil feature

# Dependency

To use this library in your android project, just simply add the following dependency into your build.gradle

```sh
dependencies {
    compile 'com.thesurix.gesturerecycler:gesture-recycler:1.5.0'
}
```

# How to use?

```java
// Define your RecyclerView and adapter as usually
LinearLayoutManager manager = new LinearLayoutManager(getContext());
recyclerView.setHasFixedSize(true);
recyclerView.setLayoutManager(manager);

// Extend GestureAdapter and write your own
// ViewHolder items must extend GestureViewHolder
MonthsAdapter adapter = new MonthsAdapter(R.layout.linear_item);
adapter.setData(getMonths());
recyclerView.setAdapter(adapter);
```
### Swipe and drag & drop support:
```java
GestureManager gestureManager = new GestureManager.Builder(mRecyclerView)
                 // Enable swipe
                .setSwipeEnabled(true)
                 // Enable long press drag and drop 
                .setLongPressDragEnabled(true)
                 // Enable manual drag from the beginning, you need to provide View inside your GestureViewHolder
                .setManualDragEnabled(true)
                 // Use custom gesture flags
                 // Do not use those methods if you want predefined flags for RecyclerView layout manager 
                .setSwipeFlags(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT)
                .setDragFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN)
                .build();
```
### Background view for swipeable items:
```xml
<FrameLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <!-- Content of the background view (you can use regular layout or ViewStub for better performance)-->
    <ViewStub
            android:id="@+id/background_view_stub"
            android:inflatedId="@+id/background_view"
            android:layout="@layout/background_view_layout"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />
    
    <LinearLayout
            android:id="@+id/foreground_view"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:orientation="horizontal">
            <!-- Content of the top view -->
    </LinearLayout>
</FrameLayout>
```
```java
    // Override getForegroundView(), getBackgroundView() methods in ViewHolder to provide top and bottom view
    @Override
    public View getForegroundView() {
        return mForegroundView; // This view comes from R.id.foreground_view
    }

    @Override
    public View getBackgroundView() {
        return mBackgroundView; // This view comes from R.id.background_view_stub
    }
```
### Data callbacks:
```java
adapter.setDataChangeListener(new GestureAdapter.OnDataChangeListener<MonthItem>() {
            @Override
            public void onItemRemoved(@NonNull MonthItem item, int position) {
            }

            @Override
            public void onItemReorder(@NonNull MonthItem item, int fromPos, int toPos) {
            }
        });
```
### Data animations:
```java
// Support for data animations
adapter.add(month);
adapter.insert(month, 5);
adapter.remove(5);

// or
adapter.setData(months, diffUtilCallback)

// This will interrupt pending animations
adapter.setData(months)

```
### Item click events:

```java
// Attach DefaultItemClickListener or implement RecyclerItemTouchListener.ItemClickListener
recyclerView.addOnItemTouchListener(new RecyclerItemTouchListener<>(new DefaultItemClickListener<CustomItem>() {
            @Override
            public boolean onItemClick(@NonNull CustomItem item, int position) {
                // return true if the event is consumed
                return false;
            }

            @Override
            public void onItemLongPress(@NonNull CustomItem item, int position) {
            }

            @Override
            public boolean onDoubleTap(@NonNull CustomItem item, int position) {
                // return true if the event is consumed
                return false;
            }
        }));
```
### Empty view:
```xml
<FrameLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <android.support.v7.widget.RecyclerView
        android:id="@+id/recycler_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
    
    <!-- Define your empty view in layout -->
    <TextView
        android:id="@+id/empty_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:textAppearance="@android:style/TextAppearance.Large"
        android:text="No data"/>
</FrameLayout>
```
```java
// Pass null to disable empty view
View emptyView = view.findViewById(R.id.empty_view);
adapter.setEmptyView(emptyView);
```
### Undo:
```java
// Undo last data transaction (add, insert, remove, swipe, reorder)
adapter.undoLast();

// Set undo stack size
adapter.setUndoSize(2);
```

# Help
See examples.

# To do
* kotlin
* tests?

# Licence

```
Copyright 2018 thesurix

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

