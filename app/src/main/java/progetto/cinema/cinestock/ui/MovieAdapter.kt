package progetto.cinema.cinestock.ui

import progetto.cinema.cinestock.MovieDetailActivity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import progetto.cinema.cinestock.R
import progetto.cinema.cinestock.data.Movie

class MovieAdapter(private val onClick: (Movie) -> Unit) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private var movies = emptyList<Movie>()

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleView: TextView = itemView.findViewById(R.id.movie_title)
        val descriptionView: TextView = itemView.findViewById(R.id.movie_description)
        val priceView: TextView = itemView.findViewById(R.id.movie_price)
        val imageView: ImageView = itemView.findViewById(R.id.movie_image)
        val buyButton: Button = itemView.findViewById(R.id.buy_button)

        fun bind(movie: Movie) {
            titleView.text = movie.title
            descriptionView.text = movie.description
            priceView.text = "$${movie.price}"
            imageView.setImageResource(movie.imageResId)
            buyButton.setOnClickListener { onClick(movie) }

            // Aggiungi un listener per l'elemento principale per aprire i dettagli del film
            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, MovieDetailActivity::class.java).apply {
                    putExtra("movie", movie)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val current = movies[position]
        holder.bind(current)
    }

    override fun getItemCount() = movies.size

    fun submitList(list: List<Movie>) {
        movies = list
        notifyDataSetChanged()
    }
}