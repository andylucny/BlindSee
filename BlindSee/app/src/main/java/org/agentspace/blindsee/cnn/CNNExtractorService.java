package org.agentspace.blindsee.cnn;

import org.opencv.core.Mat;
import org.opencv.dnn.Net;

public interface CNNExtractorService {

    Net getConvertedNet(String clsModelPath, String tag);

    CNNDetection getPredictedLabel(Mat inputImage, Net dnnNet, String classesPath);
}
