package com.example.appchat.ui.personal.uploadstatus


import android.Manifest
import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import com.example.appchat.R
import com.example.appchat.common.Constant
import com.example.appchat.common.ext.countTime
import com.example.appchat.common.util.FileUtil
import com.example.appchat.common.util.RealPathUtils
import com.example.appchat.common.util.PermissionUtil
import com.example.appchat.ui.playvideo.PlayVideoActivity
import com.example.appchat.widget.DialogAudio
import com.example.appchat.widget.DialogChooseImage
import com.example.appchat.widget.DialogLoading
import com.example.fcm.common.ext.*
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.android.exoplayer2.util.Log
import kotlinx.android.synthetic.main.activity_status.*
import kotlin.collections.ArrayList


@Suppress("DEPRECATION")
class UploadStatusActivity : AppCompatActivity(), UploadStatusView,
    DialogChooseImage.ImageChooserListener,
    DialogAudio.AudioListener {
    private val mediaPlayer by lazy { MediaPlayer() }
    private val dialogUploadImage by lazy { DialogChooseImage(this) }
    private val dialogAudio by lazy { DialogAudio(this) }
    private val dialogLoading by lazy { DialogLoading(this) }

    private val presenter by lazy { UploadStatusPresenter(this) }
    private val adapter: UploadStatusImageAdapter by lazy {
        UploadStatusImageAdapter(this, uriImages) { onClick(it) }
    }

    private val uriImages by lazy { ArrayList<Uri>() }
    private var uriVideo: Uri? = null
    private var uriAudio: Uri? = null
    private var uriThumbnail: Uri? = null

    private var checkAudio = true

    // number file Attach
    private var numberAttach = 0

    // number file Attach result
    private var countResult = 0

    companion object {
        private const val RC_PERMISSION_READ_STORAGE = 1
        private const val RC_PERMISSION_WRITE_STORAGE = 2
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(this)
        setContentView(R.layout.activity_status)
        init()
        eventHandle()

    }


    private fun init() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        dialogAudio.setAudioListener(this)
        dialogUploadImage.setImageChooserListener(this)

        rclImage.layoutManager = GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
        rclImage.setHasFixedSize(true)
        adapter.updateThumbRatio(true)
        rclImage.adapter = adapter

    }

    private fun eventHandle() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FileUtil.checkPermissions(this)
        }

        //listener
        layoutAudio.setOnClickListener {
            dialogAudio.show()
        }
        layoutImage.setOnClickListener {
            dialogUploadImage.show()
        }
        layoutVideo.setOnClickListener {
            if (PermissionUtil.isGranted(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    RC_PERMISSION_READ_STORAGE
                ) && PermissionUtil.isGranted(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    RC_PERMISSION_WRITE_STORAGE
                )
            ) {
                FileUtil.openVideo(this)
            }
        }
        imgDeleteAudio.setOnClickListener {
            if (uriAudio != null) uriAudio = null
            layoutPlayAudio.gone()
            imgDeleteAudio.gone()
        }
        imgDeleteVideo.setOnClickListener {
            uriVideo = null
            uriThumbnail = null
            sdvVideo.gone()
            imgDeleteVideo.gone()
        }
        sdvVideo.setOnClickListener {
            bundleOf(Constant.URI to uriVideo.toString()).also {
                openActivityForResult(
                    PlayVideoActivity::class.java,
                    Constant.RESULT_VIDEO, it, Constant.URI
                )
            }
        }
    }

    private fun onClick(Position: Int) {
        uriImages.removeAt(Position)
        adapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null && resultCode != Activity.RESULT_CANCELED) {
            when (requestCode) {
                Constant.GET_IMAGE -> {
                    val clipData: ClipData? = data.clipData
                    if (clipData != null) {
                        for (i in 0 until clipData.itemCount) {
                            uriImages.add(clipData.getItemAt(i).uri)
                        }
                    } else {
                        data.data?.let { uriImages.add(it) }
                    }
                }
                Constant.OPEN_CAMERA -> {
                    val bitmap = data.extras!!["data"] as Bitmap?
                    val uri = bitmap?.let { FileUtil.getImageUriFromBitmap(this, it) }
                    uri?.let { uriImages.add(it) }

                }
                Constant.GET_VIDEO -> {
                    bundleOf(Constant.URI to data.data.toString()).also {
                        openActivityForResult(
                            PlayVideoActivity::class.java,
                            Constant.RESULT_VIDEO, it, Constant.URI
                        )
                    }
                }
                Constant.RESULT_VIDEO -> {
                    uriVideo = Uri.parse(data.getStringExtra(Constant.URI))
                    var bitmap = getVideoThumbnail(RealPathUtils.getPath(this, uriVideo))
                    sdvVideo.setImageBitmap(bitmap)
                    sdvVideo.visible()
                    imgDeleteVideo.visible()
                    uriThumbnail = bitmap?.let { FileUtil.getImageUriFromBitmap(this, it) }
                }
            }
            adapter.notifyDataSetChanged()
        }
    }


    private fun getVideoThumbnail(videoPath: String): Bitmap? {
        return ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Video.Thumbnails.MINI_KIND)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_status, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_upload -> {
                getUser()?.let {
                    presenter.uploadStatus(
                        edtStatus.text.toString(),
                        it
                    )
                    dialogLoading.show()
                }
            }
            else -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    //Audio
    override fun onClickAudio(uri: Uri, time: Long) {
        uriAudio = uri
        layoutPlayAudio.visible()
        imgDeleteAudio.visible()
        lblTimeAudio.text = (time / 1000).toString()
        playAudio(uriAudio, time)
    }

    private fun playAudio(uriAudio: Uri?, time: Long) {
        layoutPlayAudio.setOnClickListener {
            checkAudio = if (checkAudio) {
                uriAudio?.let { it1 -> mediaPlayer.setDataSource(this, it1) }
                lblTimeAudio.countTime(false, time)
                lvAudio.playAnimation()
                try {
                    mediaPlayer.prepare()
                } catch (e: Exception) {
                    Log.e("TAG", e.message.toString())
                }
                mediaPlayer.start()
                false
            } else {
                lvAudio.cancelAnimation()
                mediaPlayer.reset()
                true
            }
        }
    }

    // check Uri to Upload to firebase
    private fun uploadFileAttach(
        uriVideo: Uri? = null, uriAudio: Uri? = null, listUri: ArrayList<Uri>,
        idStatus: String, idUser: String
    ) {
        if (uriAudio != null) {
            numberAttach++
            presenter.uploadFile(
                uriAudio = uriAudio,
                attach = 0,
                idStatus = idStatus, idUser = idUser
            )
        }
        if (uriVideo != null) {
            numberAttach++
            presenter.uploadFile(
                uriVideo = uriVideo,
                attach = 1,
                idStatus = idStatus,
                idUser = idUser, uriThumbnail = uriThumbnail
            )
        }
        if (uriImages != null) {
            numberAttach += uriImages.size
            presenter.uploadFile(
                uriList = listUri,
                attach = 2,
                idStatus = idStatus,
                idUser = idUser
            )
        }
    }

    override fun getIdStatus(idStatus: String) {
        if (uriVideo != null || uriAudio != null || uriImages.size > 0)
            uploadFileAttach(uriVideo, uriAudio, uriImages, idStatus, getUser()?.id.toString())
        else {
            toast(getString(R.string.upload_status_success))
            dialogLoading.dismiss()
            finish()
        }
    }

    override fun uploadSuccess() {
        countResult++
        if (countResult == numberAttach) {
            numberAttach = 0
            toast(getString(R.string.upload_status_success))
            dialogLoading.dismiss()
            finish()
        }
    }


}