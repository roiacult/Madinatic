package com.roacult.madinatic.utils

import com.google.android.material.appbar.AppBarLayout

abstract class AppBarStateChangeListener : AppBarLayout.OnOffsetChangedListener {

    var mCurrentState = State.IDLE
    private set

    enum class State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, i: Int) {
        when {
            i == 0 -> {
                if (mCurrentState != State.EXPANDED) {
                    onStateChanged(appBarLayout,
                        State.EXPANDED
                    )
                }
                mCurrentState = State.EXPANDED
            }
            Math.abs(i) >= appBarLayout.totalScrollRange -> {
                if (mCurrentState != State.COLLAPSED) {
                    onStateChanged(appBarLayout,
                        State.COLLAPSED
                    )
                }
                mCurrentState = State.COLLAPSED
            }
            else -> {
                if (mCurrentState != State.IDLE) {
                    onStateChanged(appBarLayout,
                        State.IDLE
                    )
                }
                mCurrentState = State.IDLE
            }
        }
    }

    abstract fun onStateChanged(appBarLayout: AppBarLayout, state: State)
}
