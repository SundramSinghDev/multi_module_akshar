package com.pronted.presentation.home

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.base.BaseActivity
import com.base.Trace
import com.base.inflateActivity
import com.isseiaoki.simplecropview.CropImageView
import com.isseiaoki.simplecropview.callback.CropCallback
import com.isseiaoki.simplecropview.callback.LoadCallback
import com.isseiaoki.simplecropview.callback.SaveCallback
import com.pronted.R
import com.pronted.utils.extentions.IMAGE_CROP_MODE
import com.pronted.utils.extentions.IMAGE_PATH
import com.pronted.utils.extentions.IMAGE_TYPE
import java.io.File
import java.io.FileOutputStream
import com.pronted.databinding.ActivityImageCropBinding
import com.pronted.utils.extentions.CROP_IMAGE_REQUEST

class ActivityImageCrop : BaseActivity<ActivityImageCropBinding>() {
    private var imagePath: String? = null
    private var imageType: Int? = 0
    private var cropMode: CropImageView.CropMode = CropImageView.CropMode.SQUARE
    private var savedBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding =
            inflateActivity(this, R.layout.activity_image_crop) as ActivityImageCropBinding
        init()
    }

    override fun resume() {
        super.resume()

    }

    override fun destroy() {
        super.destroy()
        Trace.i("Destroyed.")
    }

    private fun init() {

        setActionBarView()
        intent.extras?.let {
            imagePath = it.getString(IMAGE_PATH, null)
            imageType = it.getInt(IMAGE_TYPE, 0)
            cropMode = CropImageView.CropMode.values()[it.getInt(
                IMAGE_CROP_MODE,
                CropImageView.CropMode.SQUARE.id
            )]
        }
        title(getString(R.string.image_crop))
        toolBar(
            show = true,
            back = true,
            backGround = R.color.amber_500
        )
        if (imagePath != null) {
            initCropper()
        } else {
            close()
        }
    }

    override fun title(title: String) {
        supportActionBar?.title = title
    }

    fun toolBar(show: Boolean, back: Boolean, backGround: Int? = null) {
        binding.toolbar.visibility = if (show) View.VISIBLE else View.GONE
        if (backGround == null) binding.appBarLayout.setBackgroundResource(android.R.color.transparent)
        else binding.appBarLayout.setBackgroundResource(backGround)
        supportActionBar?.setDisplayHomeAsUpEnabled(back)
        supportActionBar?.setDisplayShowHomeEnabled(back)
    }

    fun setActionBarView() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_left_white)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun close() {
        Handler().postDelayed({
            showSnackBar(getString(R.string.failed_try_again))
            finish()
        }, 1000)
    }

    private fun initCropper() {
        binding.cropImageView.startLoad(
            Uri.parse("file://$imagePath"),
            object : LoadCallback {
                override fun onError(e: Throwable) {
                    Trace.e(e.message)
                    close()
                }

                override fun onSuccess() {
                    ready()
                }
            })
        binding.fab.setOnClickListener {
            binding.cropImageView.startCrop(

                Uri.parse(imagePath),

                object : CropCallback {
                    override fun onError(e: Throwable) {
                        Trace.e(e.message)
                        close()
                    }

                    override fun onSuccess(bitmap: Bitmap?) {
                        if (bitmap != null) {
                            if (bitmap.width < 300 && bitmap.height < 300) {
                                showSnackBar(getString(R.string.profile_picture_crop_size_too_small))
                            } else {
                                imagePath?.let {
                                    SetThumbnail(bitmap, File(it)).execute()
                                }
                            }
                        } else {
                            close()
                        }
                    }
                },

                object : SaveCallback {
                    override fun onError(e: Throwable?) {

                    }

                    override fun onSuccess(outputUri: Uri?) {

                    }
                }
            )
        }
    }


    @SuppressLint("StaticFieldLeak")
    private inner class SetThumbnail internal constructor(
        private var bitmap: Bitmap,
        private var thumbPath: File
    ) :
        AsyncTask<Void, Void, Void?>() {

        override fun doInBackground(vararg params: Void): Void? {
            try {
                savedBitmap = bitmap
                val fos = FileOutputStream(thumbPath)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos)
                fos.flush()
                fos.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(aVoid: Void?) {
            thumbPath.let {
                val intent = Intent()
                intent.putExtras(
                    bundleOf(
                        Pair(IMAGE_PATH, it.absolutePath),
                        Pair(IMAGE_TYPE, imageType)
                    )
                )
                setResult(CROP_IMAGE_REQUEST, intent)
            }
            finish()
        }
    }

    private fun ready() {
        try {
            binding.cropImageView.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.light_grey
                )
            )
            binding.cropImageView.setOverlayColor(ContextCompat.getColor(this, R.color.black_80))
            binding.cropImageView.setFrameColor(
                ContextCompat.getColor(
                    this,
                    R.color.amber_500
                )
            )
            binding.cropImageView.setHandleColor(
                ContextCompat.getColor(
                    this,
                    R.color.amber_500
                )
            )
            binding.cropImageView.setGuideColor(
                ContextCompat.getColor(
                    this,
                    R.color.amber_500
                )
            )
            binding.cropImageView.setFrameStrokeWeightInDp(2)
            binding.cropImageView.setGuideStrokeWeightInDp(1)
            binding.cropImageView.setHandleSizeInDp(8)
            binding.cropImageView.setTouchPaddingInDp(20)
            binding.cropImageView.setHandleShowMode(CropImageView.ShowMode.SHOW_ALWAYS)
            binding.cropImageView.setGuideShowMode(CropImageView.ShowMode.SHOW_ALWAYS)
            binding.cropImageView.setCropMode(cropMode)
            binding.cropImageView.setOutputMaxSize(300, 300)
            binding.cropImageView.setMinFrameSizeInDp(100)
        } catch (e: Exception) {
            e.printStackTrace()
            close()
        }
    }

    override fun onBackPressed() {
        finish()
    }
}