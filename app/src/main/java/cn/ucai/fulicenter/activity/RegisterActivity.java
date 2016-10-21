package cn.ucai.fulicenter.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.filicenter.R;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.views.DisplayUtils;

public class RegisterActivity extends BaseActivity {
    private static final String TAG = RegisterActivity.class.getCanonicalName();

    @Bind(R.id.re_edUserName)
    EditText mredUserName;
    @Bind(R.id.re_edNickName)
    EditText mredNickName;
    @Bind(R.id.edConfirmPassword)
    EditText medConfirmPassword;
    @Bind(R.id.edPassword)
    EditText medPassword;
    String userName;
    String nickName;
    String password;
    String confirmPwd;
    RegisterActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        mContext = this;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        DisplayUtils.initBackWithTitle(this, "账户注册");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

    @OnClick(R.id.btn_register)
    public void checkedInput() {
        userName = mredUserName.getText().toString().trim();
        nickName = mredNickName.getText().toString().trim();
        password = medPassword.getText().toString().trim();
        confirmPwd = medConfirmPassword.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            CommonUtils.showLongToast(R.string.user_name_connot_be_empty);
            mredUserName.requestFocus();
            return;
        } else if (!userName.matches("[a-zA-Z]\\w{5,15}")) {
            CommonUtils.showLongToast(R.string.illegal_user_name);
            mredUserName.requestFocus();
            return;
        } else if (TextUtils.isEmpty(nickName)) {
            CommonUtils.showLongToast(R.string.nick_name_connot_be_empty);
            mredNickName.requestFocus();
            return;
        } else if (TextUtils.isEmpty(password)) {
            CommonUtils.showLongToast(R.string.password_connot_be_empty);
            medPassword.requestFocus();
        } else if (TextUtils.isEmpty(confirmPwd)) {
            CommonUtils.showLongToast(R.string.confirm_password_connot_be_empty);
            medConfirmPassword.requestFocus();
            return;
        } else if (!password.equals(confirmPwd)) {
            CommonUtils.showLongToast(R.string.two_input_password);
            medConfirmPassword.requestFocus();
            return;
        }
        register();
    }

    private void register() {
        final ProgressDialog pd = new ProgressDialog(mContext);
        pd.setMessage(getResources().getString(R.string.registering));
        pd.show();
        NetDao.register(mContext, userName, nickName, password, new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                pd.dismiss();
                if (result == null) {
                    CommonUtils.showLongToast(R.string.register_fail);
                } else {
                    if (result.isRetMsg()) {
                        CommonUtils.showLongToast(R.string.register_success);
                        setResult(RESULT_OK,new Intent().putExtra(I.User.USER_NAME,userName));
                        MFGT.finish(mContext);
                    } else {
                        CommonUtils.showLongToast(R.string.register_fail_exists);
                        mredUserName.requestFocus();
                    }
                }
            }
            @Override
            public void onError(String error) {
                pd.dismiss();
                CommonUtils.showLongToast(error);
            }
        });
    }
}
