package cl.svasquezm.reignnewsapp.helpers

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import cl.svasquezm.reignnewsapp.fragments.NewsFragment
import android.opengl.ETC1.getHeight
import android.widget.TextView
import cl.svasquezm.reignnewsapp.R


/**
 * Swipe to delete a news' item callback
 */
class NewsSwipeCallback(val adapter: NewsFragment.NewsAdapter) : ItemTouchHelper.SimpleCallback(0,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean { return false }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, p1: Int) {
        val position = viewHolder.adapterPosition
        adapter.deleteItem(position)
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val view = viewHolder.itemView

        // "Delete" text
        val paint = Paint().apply {
            color = Color.WHITE
            textSize = 22f
        }

        // Background
        val background = ColorDrawable(Color.RED)
        val offset = 10

        when {
            // To right
            dX > 0 -> {
                background.setBounds(view.left, view.top,
                        view.left + dX.toInt() + offset,
                        view.bottom)
                background.draw(c)

                // Set text at left
                c.drawText(view.context.getString(R.string.delete),
                        view.left.toFloat() + 10,
                        view.top.toFloat() + view.height.toFloat() / 2,
                        paint)
            }

            // To left
            dX < 0 -> {
                background.setBounds(view.right + dX.toInt() - offset,
                        view.top, view.right, view.bottom)
                background.draw(c)

                // Set text at right
                c.drawText(view.context.getString(R.string.delete),
                        view.right.toFloat() - 70,
                        view.top.toFloat() + view.height.toFloat() / 2,
                        paint)
            }
        }
    }
}