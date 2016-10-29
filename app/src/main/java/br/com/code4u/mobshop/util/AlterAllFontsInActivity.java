package br.com.code4u.mobshop.util;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class AlterAllFontsInActivity {
	

    public static void overrideFonts(final Context context, final View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFonts(context, child);
             }
            } else if (v instanceof EditText ) {
                ((EditText) v).setTypeface(Typeface.DEFAULT);
            } else if (v instanceof TextView ) {
                ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/__HELVETICANEUE.TTF"));
            } 
        } catch (Exception ignored) {
        }
     }

}
