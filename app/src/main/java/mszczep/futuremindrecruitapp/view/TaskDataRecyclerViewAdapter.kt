package mszczep.futuremindrecruitapp.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import mszczep.futuremindrecruitapp.R
import mszczep.futuremindrecruitapp.model.db.TableRecruitmentData

class RecyclerViewAdapter(val list: List<TableRecruitmentData>): RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val circularProgressDrawable = CircularProgressDrawable(holder.itemView.context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        holder.title.text = list[position].title
        holder.description.text = list[position].description
        holder.modificationDate.text = list[position].modificationDate

        Glide.with(holder.itemView)
            .load(list[position].imageUrl)
            .placeholder(circularProgressDrawable)
            .into(holder.image)
    }

    override fun getItemCount(): Int {
        return list.size
    }

}
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val title: TextView = view.findViewById(R.id.item_title)
        val description: TextView = view.findViewById(R.id.item_description)
        val modificationDate: TextView = view.findViewById(R.id.item_modification_date)
        val image: ImageView = view.findViewById(R.id.item_image)
    }
