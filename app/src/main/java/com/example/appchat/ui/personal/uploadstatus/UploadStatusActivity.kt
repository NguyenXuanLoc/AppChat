package com.example.appchat.ui.personal.uploadstatus


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
import com.example.appchat.widget.DialogLoading
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
    private val dialogLoading by lazy { DialogLoading(this) }

    private val presenter by lazy { UploadStatusPresenter(this) }
    private val adapter: UploadStatusImageAdapter by lazy {
        UploadStatusImageAdapter(this, uriImages) { onClick(it) }
    }

    private val uriImages by lazy { ArrayList<Uri>() }
    private var uriVideo: Uri? = null
    private var uriAudio: Uri? = null
    private var checkAudio = true

    private var threadAudio: Thread? = null
    private var threadVideo: Thread? = null
    private var threadPhoto: Thread? = null

    // number file Attach
    private var numberAttach = 0

    // number file Attach result
    private var countResult = 0


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
            FileUtil.openVideo(this)
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
        uriImages.removeAt(Position)
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
                            uriImages.add(clipData.getItemAt(i).uri)
                        }
                    } else {
                        data.data?.let { uriImages.add(it) }
                    }
                }
                Constant.OPEN_CAMERA -> {
                    val bitmap = data.extras!!["data"] as Bitmap?
                    val uri = FileUtil.getImageUri(this, bitmap)
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
        idStatus: String
    ) {
        if (uriAudio != null) {
            numberAttach++
            threadAudio = Thread {
                presenter.uploadFile(
                    uriAudio = uriAudio,
                    attach = 0,
                    idStatus = idStatus
                )
            }
        }
        if (uriVideo != null) {
            numberAttach++
            threadVideo = Thread {
                presenter.uploadFile(
                    uriVideo = uriVideo,
                    attach = 1,
                    idStatus = idStatus
                )
            }

        }
        if (uriImages != null) {
            numberAttach += uriImages.size
            threadPhoto = Thread {
                presenter.uploadFile(
                    uriList = listUri,
                    attach = 2,
                    idStatus = idStatus
                )
            }
        }
        threadVideo?.start()
        threadPhoto?.start()
        threadAudio?.start()
    }

    override fun getIdStatus(idStatus: String) {
        if (uriVideo != null || uriAudio != null || uriImages.size > 0)
            uploadFileAttach(uriVideo, uriAudio, uriImages, idStatus)
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