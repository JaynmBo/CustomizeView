package com.caobo.customizeview;

import android.os.Bundle;

import com.caobo.customizeview.view.AnnulusCustomizeView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private AnnulusCustomizeView mAnnulusCustomizeView1;
    private AnnulusCustomizeView mAnnulusCustomizeView2;
    private AnnulusCustomizeView mAnnulusCustomizeView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAnnulusCustomizeView1 = findViewById(R.id.mAnnulusCustomizeView1);
        mAnnulusCustomizeView2 = findViewById(R.id.mAnnulusCustomizeView2);
        mAnnulusCustomizeView3 = findViewById(R.id.mAnnulusCustomizeView3);
        setProgress();
    }

    /**
     * 模拟数据
     */
    private void setProgress() {
        new Thread() {
            @Override
            public void run() {
                for (int i = 1; i <= 100; i++) {

                    mAnnulusCustomizeView1.setProgress(i);
                    mAnnulusCustomizeView2.setProgress(i);
                    mAnnulusCustomizeView3.setProgress(i);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
