package com.android.example.leanback.fastlane;

import android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter;
import android.util.Log;

import com.android.example.leanback.data.Video;

/**
 * Created by chihau on 30-11-15.
 */
public class DetailsDescriptionPresenter extends AbstractDetailsDescriptionPresenter{
    @Override
    protected void onBindDescription(ViewHolder viewHolder, Object o) {
        Video video = (Video) o;

        if (video != null) {
            Log.d("Presenter", String.format("%s, %s, %s", video.getTitle(), video.getThumbUrl(), video.getDescription()));
            viewHolder.getTitle().setText(video.getTitle());
            viewHolder.getSubtitle().setText(String.valueOf(video.getRating()));
            viewHolder.getBody().setText(video.getDescription());
        }

    }

}
