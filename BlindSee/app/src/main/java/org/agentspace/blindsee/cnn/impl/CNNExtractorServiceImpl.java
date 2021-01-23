package org.agentspace.blindsee.cnn.impl;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.agentspace.blindsee.cnn.CNNExtractorService;
import org.agentspace.blindsee.cnn.CNNDetection;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.imgproc.Imgproc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class CNNExtractorServiceImpl implements CNNExtractorService {

    private static final int TARGET_IMG_WIDTH = 224;
    private static final int TARGET_IMG_HEIGHT = 224;

    private static final double SCALE_FACTOR = 1 / 255.0;

    private static final Scalar MEAN = new Scalar(0.485, 0.456, 0.406);
    private static final Scalar STD = new Scalar(0.229, 0.224, 0.225);

    private String TAG;

    private ArrayList<String> getImgLabels(String imgLabelsFilePath) {
        ArrayList<String> imgLabels = new ArrayList();
        BufferedReader bufferReader;
        try {
            bufferReader = new BufferedReader(new FileReader(imgLabelsFilePath));
            String fileLine;
            while ((fileLine = bufferReader.readLine()) != null) {
                imgLabels.add(fileLine);
            }
        } catch (IOException ex) {
            Log.i(TAG, "ImageNet classes were not obtained");
        }
        return imgLabels;
    }

    public static Mat centerCrop(Mat inputImage) {
        int y1 = Math.round((inputImage.rows() - TARGET_IMG_HEIGHT) / 2);
        int y2 = Math.round(y1 + TARGET_IMG_HEIGHT);
        int x1 = Math.round((inputImage.cols() - TARGET_IMG_WIDTH) / 2);
        int x2 = Math.round(x1 + TARGET_IMG_WIDTH);

        Rect centerRect = new Rect(x1, y1, (x2 - x1), (y2 - y1));
        Mat croppedImage = new Mat(inputImage, centerRect);

        return croppedImage;
    }

    private Mat getPreprocessedImage(Mat image) {
        Imgproc.cvtColor(image, image, Imgproc.COLOR_RGBA2RGB);

        // create empty Mat images for float conversions
        Mat imgFloat = new Mat(image.rows(), image.cols(), CvType.CV_32FC3);

        // convert input image to float type
        image.convertTo(imgFloat, CvType.CV_32FC3, SCALE_FACTOR);

        // resize input image
        Imgproc.resize(imgFloat, imgFloat, new Size(256, 256));

        // crop input image
        imgFloat = centerCrop(imgFloat);

        // prepare DNN input
        Mat blob = Dnn.blobFromImage(
                imgFloat,
                1.0, /* default scalefactor */
                new Size(TARGET_IMG_WIDTH, TARGET_IMG_HEIGHT), /* target size */
                MEAN,  /* mean */
                true, /* swapRB */
                false /* crop */
        );

        // divide on std
        Core.divide(blob, STD, blob);

        return blob;
    }

    private CNNDetection getPredictedClass(Mat classificationResult, String classesPath) {
        final int[] indices = { 844, 508, 590, 591, 620, 673, 681, 664, 904, 905, 753, 799, 818, 861, 898, 896, 530, 531, 826, 836, 837, 953, 954, 950, 951, 952, 937, 879, 504, 440, 441, 434, 532, 736, 846, 423, 559, 765, 968, 899 };

        ArrayList<String> imgLabels = getImgLabels(classesPath);

        if (imgLabels.isEmpty()) {
            return new CNNDetection("Empty label",0.0);
        }

        // obtain max prediction result
        //Core.MinMaxLocResult mm = Core.minMaxLoc(classificationResult);
        //int x = (int) mm.maxLoc.x;
        //int y = (int) mm.maxLoc.y;

        // obtain max prediction from limited set of categories
        int x = 0, y = 0;
        double confidence = 0.0;
        for (int i=0; i<indices.length; i++) {
            if (classificationResult.get(y, indices[i])[0] > confidence){
                x = indices[i];
                confidence = classificationResult.get(y, x)[0];
            }
        }
        return new CNNDetection(imgLabels.get(x), confidence);
    }

    @Override
    public Net getConvertedNet(String clsModelPath, String tag) {
        TAG = tag;
        Net convertedNet = Dnn.readNetFromONNX(clsModelPath);
        Log.i(TAG, "Network was successfully loaded");
        return convertedNet;
    }

    @Override
    public CNNDetection getPredictedLabel(Mat inputImage, Net dnnNet, String classesPath) {
        // preprocess input frame
        Mat inputBlob = getPreprocessedImage(inputImage);
        // set OpenCV model input
        dnnNet.setInput(inputBlob);
        // provide inference
        Mat classification = dnnNet.forward();
        return getPredictedClass(classification, classesPath);
    }
}