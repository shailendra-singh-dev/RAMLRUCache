
package com.itexico.ramlrucache.cache;


import android.content.Context;
import android.graphics.Bitmap;

import com.itexico.ramlrucache.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Some simple test data to use for this sample app.
 */
public class Images {

    private int mLogoWidth = 0;

    private int mLogoHeight = 0;

    private static List<Integer> mThumbnailIDs = null;

    private final Context mContext;

    public Images(final Context context) {
        mContext = context;
        init();
    }

    private void init() {
        mLogoWidth = mContext.getResources().getDimensionPixelSize(R.dimen.thumbnail_width);
        mLogoHeight = mContext.getResources().getDimensionPixelSize(R.dimen.thumbnail_height);

        mThumbnailIDs = new ArrayList<Integer>();
        for (int i = 0; i < 50; i++) {
            mThumbnailIDs.add(R.drawable.image_23);
        }
    }

    public Bitmap getBitmapAtIndex(final int index) {
        if(index >= mThumbnailIDs.size()){
            return null;
        }

        final int imageId = mThumbnailIDs.get(index);
        final Bitmap channelLogo = Utils.decodeSampledBitmapFromResource(mContext.getResources(), imageId,
                mLogoWidth, mLogoHeight);
        return channelLogo;
    }

    public int getImagesSize() {
        return mThumbnailIDs.size();
    }

}
