package com.flyjingfish.switchkeyboard;

import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;

public class SysNotifyUtils {

    public static  CharSequence getClickableContentFromHtml(String html, OnContentClickListener onHtmlClickListener) {
        Spanned spanned = Html.fromHtml(html);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(spanned);
        SpannableStringBuilder newSpannable = deleteNAndR(spannableStringBuilder);
        URLSpan[] urlSpans = newSpannable.getSpans(0, spanned.length(), URLSpan.class);
        for (final URLSpan span : urlSpans) {
            setLinkClickEnabled(newSpannable, span, onHtmlClickListener);
        }
        return newSpannable;
    }

    private static void setLinkClickEnabled(final SpannableStringBuilder spannableStringBuilder,
                                            final URLSpan urlSpan, OnContentClickListener onHtmlClickListener) {
        int start = spannableStringBuilder.getSpanStart(urlSpan);
        int end = spannableStringBuilder.getSpanEnd(urlSpan);
        int flags = spannableStringBuilder.getSpanFlags(urlSpan);
        ClickableSpan clickableSpan = new ClickableSpan() {

            public void onClick(View view) {
                if (onHtmlClickListener != null) {
                    onHtmlClickListener.onClickContent(urlSpan.getURL());
                }
            }
        };
        spannableStringBuilder.setSpan(clickableSpan, start, end, flags);
    }

    public static SpannableStringBuilder deleteNAndR(SpannableStringBuilder ssb) {
        if (TextUtils.isEmpty(ssb.toString())) {
            return ssb;
        }

        if (ssb.length() == 1) {
            char ch = ssb.charAt(0);
            if ((ch == '\r') || (ch == '\n')) {
                return new SpannableStringBuilder();
            }
            return ssb;
        }

        int lastIdx = ssb.length() - 1;
        char lastChar = ssb.charAt(lastIdx);

        if (lastChar != '\n' && lastChar != '\r') {
            lastIdx++;
            return new SpannableStringBuilder(ssb.subSequence(0, lastIdx));
        }

        return deleteNAndR(new SpannableStringBuilder(ssb.subSequence(0, lastIdx)));
    }

    public interface OnContentClickListener {
        void onClickContent(String url);
    }
}
