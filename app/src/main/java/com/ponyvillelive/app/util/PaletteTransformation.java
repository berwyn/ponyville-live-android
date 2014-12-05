package com.ponyvillelive.app.util;

import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;

import com.squareup.picasso.Transformation;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by berwyn on 02/09/14.
 */
public class PaletteTransformation implements Transformation {

    private String tag;
    private Callback cb;
    private static Map<String, Palette> paletteMap;

    static {
        paletteMap = new HashMap<>();
    }

    public PaletteTransformation(String tag, Callback cb) {
        this.tag = tag;
        this.cb = cb;
    }

    @Override
    public Bitmap transform(Bitmap bitmap) {
        if(!paletteMap.containsKey(tag)) {
            paletteMap.put(tag, Palette.generate(bitmap));
        }
        cb.setPalette(paletteMap.get(tag));
        cb.colorise();
        return bitmap;
    }

    @Override
    public String key() {
        return "Palette";
    }

    public interface Callback {
        public Palette getPalette();
        public void setPalette(Palette palette);
        public void colorise();
    }
}
