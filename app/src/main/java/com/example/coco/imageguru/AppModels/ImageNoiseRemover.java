package com.example.coco.imageguru.AppModels;

/**
 * Created by Coco on 13.03.2017.
 */

public class ImageNoiseRemover implements ImageFilter {

    int diameter = 5;
    int sigmaColor = 5;
    int sigmaSpace = 5;

    public ImageNoiseRemover(int diameter, int sigmaColor, int sigmaSpace){
        this.diameter = diameter;
        this.sigmaColor = sigmaColor;
        this.sigmaSpace = sigmaSpace;
    }

    @Override
    public void ApplyFilter() {

    }

    @Override
    public void GetFilteredImage() {

    }
}
