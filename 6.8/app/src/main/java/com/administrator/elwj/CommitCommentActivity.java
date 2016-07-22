package com.administrator.elwj;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.administrator.bean.Constant;
import com.administrator.bean.Novelty;
import com.administrator.utils.ToastUtil;
import com.administrator.utils.VolleyUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * 提交评论页面，目前没有用到，可以删除
 * Created by xu on 2016/2/29.
 */
public class CommitCommentActivity extends AppCompatActivity {

    //评论内容编辑
    private EditText etComment;

    private Novelty mNovelty;
    private String commentText;

    private static final int ADD_COMMENT = 1;

    public static class MyHandler extends Handler{

        private WeakReference<CommitCommentActivity> mActivity;

        public MyHandler(CommitCommentActivity commitCommentActivity){
            mActivity = new WeakReference<CommitCommentActivity>(commitCommentActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            CommitCommentActivity commitCommentActivity = mActivity.get();
            if(commitCommentActivity != null) {
                switch (msg.what) {
                    case ADD_COMMENT:
                        String json = (String) msg.obj;
                        if (json != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                if (Integer.parseInt(jsonObject.get("result").toString()) == 1) {
                                    ToastUtil.showToast(commitCommentActivity.getApplicationContext(), jsonObject.get("message").toString());
//                                    Toast.makeText(commitCommentActivity.getApplicationContext(), jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();
                                    commitCommentActivity.setResult(RESULT_OK);
                                    commitCommentActivity.finish();
                                } else {
                                    ToastUtil.showToast(commitCommentActivity.getApplicationContext(),jsonObject.get("message").toString());
//                                    Toast.makeText(commitCommentActivity.getApplicationContext(), jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        break;
                    default:
                        break;
                }
            }

        }
    }

    private Handler handler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commit_comment);
        initViews();
        getIntentData();
    }

    private void getIntentData() {
        Intent data= getIntent();
        if(data != null){
            mNovelty = (Novelty) data.getSerializableExtra("Novelty");
        }
    }


    private void initViews() {

        TextView tvTitle = (TextView) findViewById(R.id.title);
        LinearLayout llCancel = (LinearLayout) findViewById(R.id.ll_commit_comment_cancel);
        ImageButton ibBack = (ImageButton) findViewById(R.id.hot_ib_back);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommitCommentActivity.this.finish();
            }
        });
        TextView tvSend = (TextView) findViewById(R.id.tv_commit_comment_send);
        CheckBox cbTransmit = (CheckBox) findViewById(R.id.cb_commit_commet);
        etComment = (EditText) findViewById(R.id.et_commit_comment_content);
        etComment.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                commentText = s.toString();
                if(s.length()==140){
                    ToastUtil.showToast(CommitCommentActivity.this,"最多输入140字");
//                    Toast.makeText(CommitCommentActivity.this, "最多输入140字",
//                            Toast.LENGTH_SHORT).show();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });

        tvTitle.setText("发评论");
        llCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commentText == null || commentText.equals("")) {
                    ToastUtil.showToast(CommitCommentActivity.this, "没有填写评论内容");
//                    Toast.makeText(CommitCommentActivity.this,"没有填写评论内容！",Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("xu_test", mNovelty.getId() + etComment.getText().toString());
                    VolleyUtils.NetUtils(((BaseApplication) getApplication()).getRequestQueue(), Constant.baseUrl + Constant.addNoveltyComment, new String[]{"novelty_id", "comment_content"}, new String[]{mNovelty.getId(), etComment.getText().toString()}, handler, ADD_COMMENT);
                }
            }
        });
    }

}

