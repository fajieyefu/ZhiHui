package com.example.administrator.zhihuiyinshui.activity.layout;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.administrator.zhihuiyinshui.R;

/**
 * Created by Fajieyefu on 2016/8/20.
 */
public class EditLayout extends LinearLayout implements View.OnClickListener {
    private EditText edit;
    public EditLayout(Context context) {
        super(context);
    }

    public EditLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.edit_layout,this);
        edit = (EditText) findViewById(R.id.edit);
        final ImageView imageView = (ImageView) findViewById(R.id.eliminate);
        imageView.setOnClickListener(this);
        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    imageView.setVisibility(VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                imageView.setVisibility(VISIBLE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.eliminate:
                edit.setText("");
                break;
        }
    }
}
