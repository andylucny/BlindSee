package org.agentspace.blindsee.cnn;

public class CNNDetection {
    public String label;
    public double confidence;
    public CNNDetection(String label, double confidence) {
        this.label = label;
        this.confidence = confidence;
    }
}
