package com.hexbit.rutmath.ui.fragment.addSubList

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import com.hexbit.rutmath.R
import com.hexbit.rutmath.data.model.ExerciseType


/**
 * Adapter that contains exercise list.
 *
 */
class AddSubListAdapter(
    private val exercises: ArrayList<ExerciseType>,
    private val clickCallback: (exerciseType: ExerciseType) -> Unit
) : RecyclerView.Adapter<AddSubViewHolder>() {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddSubViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.normal_exercise_item, null, false)
        return AddSubViewHolder(view, clickCallback)
    }

    override fun getItemCount(): Int = exercises.size

    override fun onBindViewHolder(holder: AddSubViewHolder, position: Int) {
        holder.bind(exercises[position])
    }

}