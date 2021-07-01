package com.example.tasklist.extensions

import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class FabExtendingOnScrollListener(
    private val floatingActionButton: ExtendedFloatingActionButton
) : RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (dy > 0 && floatingActionButton.isExtended) {
            floatingActionButton.shrink()
        } else if (dy < 0 && !floatingActionButton.isExtended) {
            floatingActionButton.extend()
        }
    }
}
