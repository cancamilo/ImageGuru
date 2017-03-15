package com.example.coco.imageguru.AppModels;

/**
 * Created by Coco on 12.03.2017.
 */

public interface ImageFilter {

    public String filterName = "";

    //Methods
    public void ApplyFilter();

    public void GetFilteredImage();
}
