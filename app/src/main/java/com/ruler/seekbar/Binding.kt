package com.ruler.seekbar

import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.BindingAdapter
import com.ruler.seekbar.databinding.ItemScaleBinding

@BindingAdapter("scaleCount", "selectedPosition")
fun ConstraintLayout.showCount(scaleCount: Int?, selectedPosition: Int?) {

    this.removeAllViews()
    if (scaleCount == null || scaleCount < 0) {
        return
    }
    val count = scaleCount - 1
    val set = ConstraintSet()
    this.id = View.generateViewId()

    val viewIdList = arrayListOf<Int>()

    repeat(count) {
        val binding = ItemScaleBinding.inflate(LayoutInflater.from(context), this, false)
        binding.filled = selectedPosition ?: 0 >= it+1
        binding.position = (it + 1).toString()
        binding.executePendingBindings()
        val view = binding.root
        view.id = View.generateViewId()
        viewIdList.add(view.id)
        this.addView(view)
        set.clone(this)
        set.connect(view.id, ConstraintSet.TOP, this.id, ConstraintSet.TOP, 0)
        set.connect(view.id, ConstraintSet.BOTTOM, this.id, ConstraintSet.BOTTOM, 0)
        set.applyTo(this)
    }

    viewIdList.forEachIndexed { index, id ->
        if (index == 0) {
            val view = this.getViewById(id)
            set.clone(this)

            if (viewIdList.size == 1) {
                set.connect(view.id, ConstraintSet.END, this.id, ConstraintSet.END, 0)
                set.connect(view.id, ConstraintSet.START, this.id, ConstraintSet.START, 0)
            } else {
                val nextView = this.getViewById(viewIdList[index + 1])
                set.connect(view.id, ConstraintSet.START, this.id, ConstraintSet.START, 0)
                set.connect(view.id, ConstraintSet.END, nextView.id, ConstraintSet.START, 0)
            }
            set.applyTo(this)

        } else if (index == viewIdList.size.minus(1)) {
            val view = this.getViewById(id)

            val prevView = this.getViewById(viewIdList[index - 1])
            set.clone(this)

            set.connect(view.id, ConstraintSet.START, prevView.id, ConstraintSet.END, 0)
            set.connect(view.id, ConstraintSet.END, this.id, ConstraintSet.END, 0)
            set.applyTo(this)

        } else {
            val view = this.getViewById(id)
            val prevView = this.getViewById(viewIdList[index - 1])
            val nextView = this.getViewById(viewIdList[index + 1])

            set.clone(this)
            set.connect(view.id, ConstraintSet.END, nextView.id, ConstraintSet.START, 0)
            set.connect(view.id, ConstraintSet.START, prevView.id, ConstraintSet.END, 0)
            set.applyTo(this)

        }
    }
}