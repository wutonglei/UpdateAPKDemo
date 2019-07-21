package com.wutong.updateapkdemo;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import constacne.UiType;
import listener.Md5CheckResultListener;
import listener.OnInitUiListener;
import listener.UpdateDownloadListener;
import model.UiConfig;
import model.UpdateConfig;
import update.UpdateAppUtils;

public class MainActivity extends AppCompatActivity {
//    private String apkUrl = "http://118.24.148.250:8080/yk/update_signed.apk";
    private String apkUrl = "http://192.168.43.196:8080/app101.apk";
    private String updateTitle = "发现新版本V2.0.0";
    private String updateContent = "1、Kotlin重构版\n2、支持自定义UI\n3、增加md5校验\n4、更多功能等你探索";
    EditText mEditText;
    TextView mTextView;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditText=findViewById(R.id.et);
        mTextView=findViewById(R.id.tv);
        String a=   packageCode(this)+"";
        mTextView.setText("版本号："+a);
        findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更新完后在新版本中使用 才有效果
                UpdateAppUtils.getInstance().deleteInstalledApk();
            }
        });

        findViewById(R.id.btn_java).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //设置 存储地址和 应用包保存名字
                String name="app"+mEditText.getText().toString().trim();
                apkUrl="http://192.168.43.196:8080/app"+mEditText.getText().toString().trim()+".apk";
                Log.i("trh", "onClick: "+apkUrl);
                UpdateConfig updateConfig = new UpdateConfig();
                updateConfig.setCheckWifi(true);
                updateConfig.setNeedCheckMd5(false);
                updateConfig.setApkSavePath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/01trh");
                updateConfig.setApkSaveName(name);
                updateConfig.setNotifyImgRes(R.drawable.ic_logo);
                UiConfig uiConfig = new UiConfig();
//                uiConfig.setUiType(UiType.PLENTIFUL);
                uiConfig.setUiType(UiType.CUSTOM);
                //注意这里将布局传递进去后  取消和立即更新的id必须是
                //btn_update_cancel   btn_update_sure  理由  里面写死的。
                uiConfig.setCustomLayoutId(R.layout.view_update_dialog_custom);
                UpdateAppUtils
                        .getInstance()
                        .apkUrl(apkUrl)
                        .updateTitle(updateTitle)
                        .updateContent(updateContent)
                        .uiConfig(uiConfig)
                        .updateConfig(updateConfig)
                        .setOnInitUiListener(new OnInitUiListener() {
                            /**
                             *
                             * @param view   就是 view_update_dialog_custom  布局 View
                             * @param updateConfig
                             * @param uiConfig
                             */
                            @Override
                            public void onInitUpdateUi(View view, UpdateConfig updateConfig, UiConfig uiConfig) {
                                view.findViewById(R.id.tv_update_content).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(MainActivity.this, "给点面子好吧", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setMd5CheckResultListener(new Md5CheckResultListener() {
                            @Override
                            public void onResult(boolean result) {

                            }
                        })
                        .setUpdateDownloadListener(new UpdateDownloadListener() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onDownload(int progress) {

                            }

                            @Override
                            public void onFinish() {

                            }

                            @Override
                            public void onError(@NotNull Throwable e) {

                            }
                        })
                        .update();
            }
        });
    }

    public static int packageCode(Context context) {
        PackageManager manager = context.getPackageManager();
        int code = 0;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            code = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }

}
