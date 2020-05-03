package com.roacult.madinatic.utils

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import iammert.com.view.scalinglib.ScalingLayout

class ScaleBehavior(context: Context, attribute: AttributeSet) :
    CoordinatorLayout.Behavior<ScalingLayout> (context, attribute) {

    override fun layoutDependsOn(parent: CoordinatorLayout, child: ScalingLayout, dependency: View): Boolean {
        return dependency is AppBarLayout
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: ScalingLayout, dependency: View): Boolean {
        val totalRange = (dependency as AppBarLayout).totalScrollRange
        child.setProgress((-dependency.y) / totalRange)
        if (totalRange + dependency.y > child.measuredHeight.toFloat() / 2) {
            child.translationY =
                totalRange + dependency.y - child.measuredHeight.toFloat() / 2
        } else {
            child.translationY = 0F
        }
        return super.onDependentViewChanged(parent, child, dependency)
    }
}
