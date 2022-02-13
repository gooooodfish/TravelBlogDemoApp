package com.example.travelblog

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.travelblog.databinding.ActivityBlogDetailsBinding
import com.example.travelblog.http.Blog
import com.example.travelblog.http.BlogHttpClient
import com.google.android.material.snackbar.Snackbar

private const val IMAGE_URL = "https://bitbucket.org/dmytrodanylyk/travel-blog-resources/raw/" +
        "3436e16367c8ec2312a0644bebd2694d484eb047/images/sydney_image.jpg"

private const val AVATAR_URL = "https://bitbucket.org/dmytrodanylyk/travel-blog-resources/raw/" +
        "3436e16367c8ec2312a0644bebd2694d484eb047/avatars/avatar1.jpg"

class BlogDetailsActivity : AppCompatActivity() {
    companion object{
        private const val EXTRAS_BLOG = "EXTRAS_BLOG"

        fun start(activity: Activity, blog: Blog){
            val intent = Intent(activity, BlogDetailsActivity::class.java)
            intent.putExtra(EXTRAS_BLOG,blog)
            activity.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityBlogDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBlogDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.imageBack.setOnClickListener { finish() }
        intent.extras?.getParcelable<Blog>(EXTRAS_BLOG)?.let { blog ->
            showData(blog)
        }

    }

//    private fun loadData() {
//        BlogHttpClient.loadBlogArticles(
//            onSuccess = { list: List<Blog> ->
//                runOnUiThread { showData(list[0]) }
//            },
//            onError = {
//                runOnUiThread { showErrorSnackbar() }
//            }
//        )
//    }
//
//    private fun showErrorSnackbar() {
//        Snackbar.make(
//            binding.rootView,
//            "Error during loading blog articles",
//            Snackbar.LENGTH_INDEFINITE
//        ).run {
//            setActionTextColor(ContextCompat.getColor(context, R.color.pink_1))
//            setAction("Retry") {
//                loadData()
//                dismiss()
//            }
//        }.show()
//    }

    private fun showData(blog: Blog) {
        binding.progressBar.visibility = View.GONE
        Glide.with(this)
            .load(blog.getImageUrl())
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.imageMain)

        Glide.with(this)
            .load(blog.author.getAvatarUrl())
            .transform(CircleCrop())
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.imageAvatar)

        binding.textTitle.text = blog.title
        binding.textDate.text = blog.date
        binding.textAuthor.text = blog.author.name
        binding.textRating.text = blog.rating.toString()
        binding.textViews.text = String.format("(%d views)", blog.views)
        binding.textDescription.text =
            HtmlCompat.fromHtml(blog.description, HtmlCompat.FROM_HTML_MODE_LEGACY)

        binding.ratingBar.rating = blog.rating
    }

}