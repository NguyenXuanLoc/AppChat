package com.example.appchat.ui.status


import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
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
import com.example.appchat.ui.playvideo.PlayVideoActivity
import com.example.appchat.widget.DialogAudio
import com.example.appchat.widget.DialogChooseImage
import com.example.fcm.common.ext.*
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.android.exoplayer2.util.Log
import kotlinx.android.synthetic.main.activity_status.*


class UploadStatusActivity : AppCompatActivity(), UploadStatusView,
    DialogChooseImage.ImageChooserListener,
    DialogAudio.AudioListener {
    private val mediaPlayer by lazy { MediaPlayer() }

    private val dialogUploadImage by lazy { DialogChooseImage(this) }
    private val dialogAudio by lazy { DialogAudio(this) }

    private val presenter by lazy { UploadStatusPresenter(this) }

    private val uriImage by lazy { ArrayList<Uri>() }
    private var uriVideo: Uri? = null
    private var uriAudio: Uri? = null
    private var checkAudio = true

    private val adapter: UploadStatusImageAdapter by lazy {
        UploadStatusImageAdapter(this, uriImage) { onClick(it) }
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
            checkAttach(uriVideo, uriAudio, uriImage, 0)
        }
        layoutImage.setOnClickListener {
            checkAttach(uriVideo, uriAudio, uriImage, 1)
        }
        layoutVideo.setOnClickListener {
            checkAttach(uriVideo, uriAudio, uriImage, 2)
        }
        imgDeleteAudio.setOnClickListener {
            if (uriAudio != null) uriAudio = null
            layoutPlayAudio.gone()
            imgDeleteAudio.gone()
        }
        imgDeleteVideo.setOnClickListener {
            if (uriVideo != null) uriVideo = null
            cardVideo.gone()
            imgDeleteVideo.gone()
        }
        imgVideo.setOnClickListener {
            bundleOf(Constant.URI to uriVideo.toString()).also {
                openActivityForResult(
                    PlayVideoActivity::class.java,
                    Constant.RESULT_VIDEO, it, Constant.URI
                )
            }
        }
    }

    private fun onClick(Position: Int) {
        uriImage.removeAt(Position)
        adapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                Constant.GET_IMAGE -> {
                    val clipData: ClipData? = data.clipData
                    if (clipData != null) {
                        for (i in 0 until clipData.itemCount) {
                            uriImage.add(clipData.getItemAt(i).uri)
                        }
                    } else {
                        data.data?.let { uriImage.add(it) }
                    }
                }
                Constant.OPEN_CAMERA -> {
                    val bitmap = data.extras!!["data"] as Bitmap?
                    val uri = FileUtil.getImageUri(this, bitmap)
                    uri?.let { uriImage.add(it) }
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
                    cardVideo.visible()
                    imgDeleteVideo.visible()
                }
            }
            adapter.notifyDataSetChanged()
        }
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
                        it,
                        checkAttachStatus(uriVideo, uriAudio, uriImage)
                    )
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
        playAudio(uriAudio, time)
    }

    // check Attach: only one attach when Upload status
    private fun checkAttach(
        uriVideo: Uri? = null, uriAudio: Uri? = null,
        listUri: ArrayList<Uri>, attach: Int
    ) {
        when (attach) {
            0 -> {
                if (uriAudio == null && listUri.size == 0) {
                    dialogAudio.show()
                } else toast(getString(R.string.notify_only_one))
            }
            1 -> {
                if (uriAudio == null && uriVideo == null)
                    dialogUploadImage.show()
                else toast(getString(R.string.notify_only_one))
            }
            2 -> {
                if (uriAudio == null && listUri.size == 0)
                    FileUtil.openVideo(this)
                else toast(getString(R.string.notify_only_one))
            }
        }
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

    // Check Attach to insert Attach to Database
    private fun checkAttachStatus(
        uriVideo: Uri? = null, uriAudio: Uri? = null, listUri: ArrayList<Uri>
    ): Int {
        return when {
            uriAudio != null -> 0
            uriVideo != null -> 1
            listUri.size > 0 -> 2
            else -> 3
        }
    }

    // check Uri to Upload to firebase
    private fun checkUri(
        uriVideo: Uri? = null, uriAudio: Uri? = null, listUri: ArrayList<Uri>,
        idStatus: String
    ) {
        when {
            uriAudio != null -> presenter.uploadFile(
                uriAudio = uriAudio,
                attach = 0,
                idStatus = idStatus
            )
            uriVideo != null -> presenter.uploadFile(
                uriVideo = uriVideo,
                attach = 1,
                idStatus = idStatus
            )
            listUri.size > 0 -> presenter.uploadFile(
                uriList = listUri,
                attach = 2,
                idStatus = idStatus, size = listUri.size
            )
        }
    }

    override fun getIdStatus(idStatus: String) {
        if (uriVideo != null || uriAudio != null || uriImage != null)
            checkUri(uriVideo, uriAudio, uriImage, idStatus)
        else finish()
    }

    override fun uploadSuccess() {
        finish()
    }
}