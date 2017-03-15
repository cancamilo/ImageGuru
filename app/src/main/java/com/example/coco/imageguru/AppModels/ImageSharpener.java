package com.example.coco.imageguru.AppModels;

import com.example.coco.imageguru.AppModels.ImageFilter;

/**
 * Created by Coco on 12.03.2017.
 */

public class ImageSharpener implements ImageFilter {

    // Filter Properties
    private int kernelSize;
    private int stdDeviation;

    // Constructors
    public ImageSharpener(){
        this.kernelSize = 5;
        this.stdDeviation = 5;
    }

    public void ImageSharpener(int kernel, int stdDeviation) {
        this.kernelSize = kernel;
        this.stdDeviation = stdDeviation ;
    }

    // Getters
    public int KernelSize() {
        return kernelSize;
    }

    public int StdDeviation(){
        return stdDeviation;
    }

    // Setters
    public void SetKernelSize(int k) {
        this.kernelSize = k;
    }

    public void SetStdDeviation(int s){
        this.stdDeviation = s;
    }

    @Override
    public void ApplyFilter() {

    }

    @Override
    public void GetFilteredImage() {

    }
}
