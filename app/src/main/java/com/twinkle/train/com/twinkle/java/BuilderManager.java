package com.twinkle.train.com.twinkle.java;

import com.nightonke.boommenu.BoomButtons.HamButton;
import com.twinkle.train.R;

/**
 * Created by Weiping Huang at 23:44 on 16/11/21
 * For Personal Open Source
 * Contact me at 2584541288@qq.com or nightonke@outlook.com
 * For more projects: https://github.com/Nightonke
 */
public class BuilderManager {

    private static int[] imageResources = new int[]{
            R.drawable.s1,
            R.drawable.s2,
            R.drawable.s3,
            R.drawable.s4,

    };

    private static int[] textResources = new int[]{
            R.string.jwgl_stu_info,
            R.string.jwgl_stu_score,
            R.string.jwgl_stu_ttb,
            R.string.jwgl_stu_exam,

    };

    private static int imageResourceIndex = 0;

    private static int getImageResource() {
        if (imageResourceIndex >= imageResources.length) imageResourceIndex = 0;
        return imageResources[imageResourceIndex++];
    }

    private static int textResourceIndex = 0;

    private static int getTextResource() {
        if (textResourceIndex >= textResources.length) textResourceIndex = 0;
        return textResources[textResourceIndex++];
    }


    public static HamButton.Builder getHamButtonBuilder() {
        return new HamButton.Builder()
                .normalImageRes(getImageResource())
                .textSize(20)
                .normalTextRes(getTextResource());

    }


    private static BuilderManager ourInstance = new BuilderManager();

    public static BuilderManager getInstance() {
        return ourInstance;
    }

    private BuilderManager() {
    }
}
