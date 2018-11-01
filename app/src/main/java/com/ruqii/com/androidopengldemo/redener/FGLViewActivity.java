package com.ruqii.com.androidopengldemo.redener;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.ruqii.com.androidopengldemo.R;

/**
 * author:黄汝琪 on 2018/10/31.
 * email:huangruqi88@163.com
 */
public class FGLViewActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQ_CHOOSE = 0x0101;

    private Button mChange;
    private FGLView mGLView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fglview);
        init();
    }

    private void init() {
        mChange = findViewById(R.id.mChange);
        mGLView = findViewById(R.id.mFGLView);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mChange:
//                Intent intent = new Intent(this, ChooseActivity.class);
//                startActivityForResult(intent, REQ_CHOOSE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mGLView.setShape((Class<? extends Shape>) data.getSerializableExtra("name"));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
    }
}
