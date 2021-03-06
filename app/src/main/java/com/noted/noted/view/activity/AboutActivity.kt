package com.noted.noted.view.activity

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.transition.TransitionManager
import com.danielstone.materialaboutlibrary.MaterialAboutActivity
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard
import com.danielstone.materialaboutlibrary.model.MaterialAboutList
import com.google.android.play.core.review.ReviewManagerFactory
import com.noted.noted.BuildConfig
import com.noted.noted.R
import io.noties.markwon.Markwon

class AboutActivity : MaterialAboutActivity() {

    override fun getActivityTitle(): CharSequence? {
        return "About"
    }

    override fun getMaterialAboutList(context: Context): MaterialAboutList {

        val notedCard = MaterialAboutCard.Builder()
            .addItem(MaterialAboutTitleItem.Builder()
                .text("Noted")
                .icon(R.mipmap.ic_launcher_round)
                .build())
            .addItem(MaterialAboutActionItem.Builder()
                .text("Version")
                .subText(BuildConfig.VERSION_NAME)
                .icon(R.drawable.ic_info_black)
                .build())
            .addItem(MaterialAboutActionItem.Builder()
                .text("Changelog")
                .icon(R.drawable.ic_restore)
                .setOnClickAction {
                    showChangelogWindow()
                }
                .build())
            .addItem(MaterialAboutActionItem.Builder()
                .text("License")
                .icon(R.drawable.ic_class)
                .setOnClickAction {
                    startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://github.com/YahiaAngelo/Noted-Android/blob/master/LICENSE")))
                }
                .build())
            .build()

        val authorCard = MaterialAboutCard.Builder()
            .title("Author")
            .addItem(MaterialAboutActionItem.Builder()
                .text("Yahia Mostafa")
                .subText("@YahiaAngelo")
                .icon(R.drawable.ic_person)
                .setOnClickAction {
                    startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://github.com/YahiaAngelo")))
                }
                .build())
            .addItem(MaterialAboutActionItem.Builder()
                .text("Follow on Twitter")
                .icon(R.drawable.ic_logo_twitter)
                .setOnClickAction {
                    startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://twitter.com/YahiaAngelo_")))
                }
                .build())
            .build()

        val appInfoCard = MaterialAboutCard.Builder()
            .title("About")
            .addItem(MaterialAboutActionItem.Builder()
                .text("Version")
                .subText(BuildConfig.VERSION_NAME)
                .icon(R.drawable.ic_info_black)
                .build())
            .addItem(MaterialAboutActionItem.Builder()
                .text("Check source code")
                .icon(R.drawable.ic_logo_github)
                .setOnClickAction {
                    startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://github.com/YahiaAngelo/Noted-Android")))
                }
                .build())
            .addItem(MaterialAboutActionItem.Builder()
                .text("Rate this app")
                .icon(R.drawable.ic_star_rate)
                .setOnClickAction {
                    val manager = ReviewManagerFactory.create(this)
                    val request = manager.requestReviewFlow()
                    request.addOnCompleteListener { playRequest ->
                        if (playRequest.isSuccessful) {
                            // We got the ReviewInfo object
                            val reviewInfo = playRequest.result
                            val flow = manager.launchReviewFlow(this, reviewInfo)
                            flow.addOnCompleteListener { _ ->
                            }
                        } else {
                           Toast.makeText(this, "Sorry, Something went wrong", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .build())
            .addItem(MaterialAboutActionItem.Builder()
                .text("Sponsor this project")
                .icon(R.drawable.ic_logo_paypal)
                .setOnClickAction {
                    startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://paypal.me/YahiaMostafa")))
                }
                .build())
            .addItem(MaterialAboutActionItem.Builder()
                .text("Contact us")
                .icon(R.drawable.ic_email)
                .setOnClickAction {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:") // only email apps should handle this
                            putExtra(Intent.EXTRA_EMAIL, "yahia.mostafa.elsayed@gmail.com")
                            putExtra(Intent.EXTRA_SUBJECT, "Noted")
                        }
                        if (intent.resolveActivity(packageManager) != null) {
                            startActivity(intent)
                        }
                }
                .build())
            .build()

        return MaterialAboutList.Builder()
            .addCard(notedCard)
            .addCard(authorCard)
            .addCard(appInfoCard)
            .build()

    }

    private fun showChangelogWindow(){
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.changelog_view,null)
        val popupWindow = PopupWindow(
            view, // Custom view to show in popup window
            LinearLayout.LayoutParams.WRAP_CONTENT, // Width of popup window
            LinearLayout.LayoutParams.WRAP_CONTENT // Window height
        )
        popupWindow.enterTransition = android.transition.Fade()
        popupWindow.exitTransition = android.transition.Fade()
        popupWindow.setBackgroundDrawable(ColorDrawable())
        popupWindow.isOutsideTouchable = true
        popupWindow.elevation = 10.0F

        val markwon = Markwon.create(this)
        val changelogTextView = view.findViewById<TextView>(R.id.changelog_text)
        markwon.setMarkdown(changelogTextView, resources.getString(R.string.app_changelog))
        TransitionManager.beginDelayedTransition(this.recyclerView)
        popupWindow.showAtLocation(
            this.recyclerView, // Location to display popup window
            Gravity.CENTER, // Exact position of layout to display popup
            0, // X offset
            0 // Y offset
        )

    }
}