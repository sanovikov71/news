package com.gmail.sanovikov71.tinkofftask;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;

public class Utils {

    public static Spanned fromHtml(String html) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return Html.fromHtml(html);
        } else {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT);
        }
    }
}
