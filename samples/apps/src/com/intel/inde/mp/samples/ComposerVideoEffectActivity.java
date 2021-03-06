//
//               INTEL CORPORATION PROPRIETARY INFORMATION
//  This software is supplied under the terms of a license agreement or
//  nondisclosure agreement with Intel Corporation and may not be copied
//  or disclosed except in accordance with the terms of that agreement.
//        Copyright (c) 2013-2014 Intel Corporation. All Rights Reserved.
//

package com.intel.inde.mp.samples;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import com.intel.inde.mp.Uri;
import com.intel.inde.mp.samples.controls.TimelineItem;

import java.util.ArrayList;
import java.util.List;

public class ComposerVideoEffectActivity extends ActivityWithTimeline implements View.OnClickListener {
    TimelineItem mItem;

    Spinner mEffects;

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.composer_transcode_activity);

        ((Button)findViewById(R.id.action)).setText("Apply Video Effect");
        ((Spinner)findViewById(R.id.effect)).setVisibility(View.VISIBLE);

        init();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mItem != null) {
            mItem.updateView();
        }
    }

    private void init() {
        mItem = (TimelineItem) findViewById(R.id.timelineItem);
        mItem.setEventsListener(this);
        mItem.enableSegmentPicker(false);

        ((Button) findViewById(R.id.action)).setOnClickListener(this);

        mEffects = (Spinner) findViewById(R.id.effect);

        fillEffectsList();
    }

    private void fillEffectsList() {
        List<String> list = new ArrayList<String>();

        list.add("Effect 1 (Sepia)");
        list.add("Effect 2 (Grayscale)");
        list.add("Effect 3 (Inverse)");
        list.add("Effect 4 (Text Overlay)");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mEffects.setAdapter(dataAdapter);
    }

    public void action() {
        Uri uri = mItem.getUri();

        if (uri == null) {
            showToast("Please select a valid video file first");

            return;
        }

        mItem.stopVideoView();

        Intent intent = new Intent();
        intent.setClass(this, ComposerVideoEffectCoreActivity.class);

        Bundle b = new Bundle();
        b.putString("srcMediaName1", mItem.getMediaFileName());
        intent.putExtras(b);
        b.putString("dstMediaPath", mItem.genDstPath(mItem.getMediaFileName(), mItem.getVideoEffectName(mEffects.getSelectedItemPosition())));
        b.putInt("effectIndex", mEffects.getSelectedItemPosition());
        intent.putExtras(b);
        b.putString("srcUri1", uri.getString());
        intent.putExtras(b);

        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.action: {
                action();
            }
            break;
        }
    }
}
