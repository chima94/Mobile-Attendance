package com.example.smartattendancesystem.ui.main.camera

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.net.Uri
import android.provider.MediaStore
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartattendancesystem.R
import com.example.smartattendancesystem.ui.main.camera.util.BitmapUtil
import com.example.smartattendancesystem.ui.theme.typography
import com.example.smartattendancesystem.util.SignInSignUpTopAppBar
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.util.concurrent.Executors

@Composable
fun CameraScreen(
    onNavigateBack : () -> Unit
){

    val viewModel : CameraViewModel = hiltViewModel()
    val takePicture = remember { mutableStateOf(false)}
    val dialogState = remember{ mutableStateOf(DialogState(showDialog = false))}


    if(dialogState.value.showDialog){
        ShowDialog(
            dialogState.value.bitmap!!,
            onOkay = {

            },
            onDismiss = {
                dialogState.value = DialogState(false)
            }
        )
    }

    Scaffold(
        topBar = {
            SignInSignUpTopAppBar(
                topAppBarText = "Photo",
                onBackPressed = onNavigateBack
            )
        },
        content = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CameraPreview(takePicture, viewModel.options){bitmap ->
                    dialogState.value = DialogState(true, bitmap)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        takePicture.value = true
                    }
                ) {
                    Text(text = "Take picture")
                }
            }
        }
    )
}





@Composable
fun ShowDialog(
    bitmap: Bitmap,
    onOkay : () -> Unit,
    onDismiss : () -> Unit
){
    AlertDialog(
        title = { Text(text = "Captured Image", style = typography.h6)},
        text = {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.clip(RoundedCornerShape(16.dp))
            )
        },
        buttons = {
            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                TextButton(
                    onClick = onOkay,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = "Ok")
                }
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = "Cancel")
                }
            }
        },
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(16.dp)
    )
}


@SuppressLint("RestrictedApi")
@Composable
fun CameraPreview(
    takePicture: MutableState<Boolean>,
    options: FaceDetectorOptions,
    onCaptureImage : (Bitmap) -> Unit
) {
    val lifeCycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val cameraProviderFuture = remember{ProcessCameraProvider.getInstance(context)}
    val cameraExecutor = remember{ Executors.newSingleThreadExecutor()}
    val lensFacing = remember{CameraSelector.LENS_FACING_FRONT}

    val previewView = remember{
        PreviewView(context).apply {
            id = R.id.previewView
        }
    }


    AndroidView({previewView}){
        cameraProviderFuture.addListener({

            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
            val imageAnalyzer = ImageAnalysis.Builder()
                .setTargetResolution(android.util.Size(640, 480))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, FaceImageDetectorAnalyzer(takePicture, options, onCaptureImage))
                }

            val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

            try{
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifeCycleOwner, cameraSelector, preview, imageAnalyzer
                )
            }catch (e : Exception){
                Timber.i("Camera Preview, use case binding failed $e")
            }

        }, ContextCompat.getMainExecutor(context))
    }

}




private class FaceImageDetectorAnalyzer(
    private val takePicture: MutableState<Boolean>,
    val options: FaceDetectorOptions,
    val onCaptureImage: (Bitmap) -> Unit,
) : ImageAnalysis.Analyzer{

    private var bitmap : Bitmap? = null
    private var detector : FaceDetector? = null

    init {
        detector = FaceDetection.getClient(options)
    }

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(image: ImageProxy) {
        val mediaImage = image.image
        if(mediaImage != null){
            if(takePicture.value){
                bitmap = BitmapUtil.getBitmap(image)
            }
            val inputImage = InputImage.fromMediaImage(mediaImage, image.imageInfo.rotationDegrees)
            detector?.process(inputImage)?.addOnSuccessListener { faces ->
                onDetection(faces, bitmap)
                image.close()
            }

        }

    }

    private fun onDetection(faces: List<Face>, bitmap: Bitmap?) {
        for(face in faces){
            val crop : Bitmap?
            if(takePicture.value && bitmap != null){
                crop = cropBitmap(bitmap, RectF(face.boundingBox))
                takePicture.value = false
                onCaptureImage(crop)
            }
        }
    }

    private fun cropBitmap(bitmap : Bitmap, rect : RectF) : Bitmap{
        val w = rect.right - rect.left
        val h = rect.bottom - rect.top
        val ret = Bitmap.createBitmap(w.toInt(), h.toInt(), bitmap.config)
        val canvas = Canvas(ret)
        canvas.drawBitmap(bitmap, -rect.left, -rect.top, null)
        return ret
    }

}


private fun getImageUri(activity : Activity, bitmap: Bitmap) : Uri {
    val bytes = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path = MediaStore.Images.Media.insertImage(
        activity.contentResolver,
        bitmap,
        "title",
        null
    )
    return Uri.parse(path.toString())
}



