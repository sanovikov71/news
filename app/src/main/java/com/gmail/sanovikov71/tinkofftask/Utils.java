package com.gmail.sanovikov71.tinkofftask;

import android.os.Build;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;

public class Utils {

    @Nullable
    public static Spanned fromHtml(@Nullable String html) {
        if (html == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return Html.fromHtml(html);
        } else {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT);
        }
    }
}
