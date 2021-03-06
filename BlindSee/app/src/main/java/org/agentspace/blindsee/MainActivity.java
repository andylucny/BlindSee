package org.agentspace.blindsee;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

import org.agentspace.blindsee.cnn.CNNExtractorService;
import org.agentspace.blindsee.cnn.CNNDetection;
import org.agentspace.blindsee.cnn.impl.CNNExtractorServiceImpl;
import org.agentspace.android.Speecher;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.dnn.Net;
import org.opencv.imgproc.Imgproc;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.agentspace.lang.Sk;

public class MainActivity extends CameraActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String TAG = MainActivity.class.getName();

    private static final String IMAGENET_CLASSES = "imagenet_classes_sk.txt";
    private static final String MODEL_FILE = "pytorch_mobilenet.onnx";

    private CameraBridgeViewBase mOpenCvCameraView;
    private Net opencvNet;

    private CNNExtractorService cnnService;
    private Speecher speecher;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully!");
                    mOpenCvCameraView.enableView();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        // OpenCV manager initialization
        OpenCVLoader.initDebug();
        mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        
        // initialize Text to Speech
        speecher = new Speecher(this,"SK"); // "SK" or ""

        // initialize implementation of CNNExtractorService
        this.cnnService = new CNNExtractorServiceImpl();
        // configure camera listener
        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.CameraView);
        mOpenCvCameraView.setVisibility(CameraBridgeViewBase.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
    }

    @Override
    protected List<? extends CameraBridgeViewBase> getCameraViewList() {
        return Collections.singletonList(mOpenCvCameraView);
    }

    public void onCameraViewStarted(int width, int height) {
        // obtaining converted network
        String onnxModelPath = getPath(MODEL_FILE, this);
        if (onnxModelPath.trim().isEmpty()) {
            Log.i(TAG, "Failed to get model file");
            return;
        }
        opencvNet = cnnService.getConvertedNet(onnxModelPath, TAG);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat frame = inputFrame.rgba();
        String classesPath = getPath(IMAGENET_CLASSES, this);
        CNNDetection detection = cnnService.getPredictedLabel(frame, opencvNet, classesPath);

        // place the predicted label on the image
        String label = Sk.utf82usa(detection.label);
        Imgproc.putText(frame, label, new Point(100, 100), Imgproc.FONT_HERSHEY_SIMPLEX, 2, new Scalar(255, 121, 0), 3);
        Imgproc.putText(frame, String.format("%.5f", detection.confidence), new Point(100, 200), Imgproc.FONT_HERSHEY_SIMPLEX, 2, new Scalar(255, 121, 0), 3);

        if (detection.confidence > 8.0) {
            // speak the seen label
            if (!speecher.isSpeaking()) speecher.speak(detection.label);
        }
        
        return frame;
    }

    public void onCameraViewStopped() {
    }

    private static String getPath(String file, Context context) {
        AssetManager assetManager = context.getAssets();
        BufferedInputStream inputStream;
        try {
            // read the defined data from assets
            inputStream = new BufferedInputStream(assetManager.open(file));
            byte[] data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
            // Create copy file in storage.
            File outFile = new File(context.getFilesDir(), file);
            FileOutputStream os = new FileOutputStream(outFile);
            os.write(data);
            os.close();
            // Return a path to file which may be read in common way.
            return outFile.getAbsolutePath();
        } catch (IOException ex) {
            Log.i(TAG, "Failed to upload a file");
        }
        return "";
    }

}