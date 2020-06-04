package vianh.nva.canvas;

import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.databinding.BindingAdapter;

public class BindingUtils {

    @BindingAdapter("onCheck")
    public static void onCheck(CheckBox cb, CompoundButton.OnCheckedChangeListener listener) {
        cb.setOnCheckedChangeListener(listener);
    }
}
