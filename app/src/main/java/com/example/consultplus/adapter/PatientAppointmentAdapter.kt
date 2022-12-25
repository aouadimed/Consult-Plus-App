import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.consultplus.R
import com.example.consultplus.model.PatientBooking

class PatientAppointmentAdapter(val ItemList: MutableList<PatientBooking>) : RecyclerView.Adapter<PatientAppointmentAdapter.DoctorAppointmentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  PatientAppointmentAdapter.DoctorAppointmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.test_layout, parent, false)

        return  PatientAppointmentAdapter.DoctorAppointmentViewHolder(view)
    }

    override fun getItemCount() = ItemList.size

    class DoctorAppointmentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val firstname =itemView.findViewById<TextView>(R.id.first)
        val lastname =itemView.findViewById<TextView>(R.id.last)
        val bookDate =itemView.findViewById<TextView>(R.id.bookdate)
        val booktime =itemView.findViewById<TextView>(R.id.booktime)
        val button =itemView.findViewById<Button>(R.id.button)


    }

    override fun onBindViewHolder(holder: DoctorAppointmentViewHolder, position: Int) {
        val firstname = ItemList[position].doctor.firstname.capitalize()
        val lastname = ItemList[position].doctor.lastname.capitalize()
        val bookdate = ItemList[position].date
        val booktime = ItemList[position].time

        holder.firstname.text = firstname
        holder.lastname.text = lastname
        holder.bookDate.text = bookdate
        holder.booktime.text = booktime




    }

}