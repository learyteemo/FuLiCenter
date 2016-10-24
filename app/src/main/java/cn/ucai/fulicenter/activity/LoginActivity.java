package cn.ucai.fulicenter.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.filicenter.R;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.dao.SharePrefrenceUtils;
import cn.ucai.fulicenter.dao.UserDao;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.ResultUtils;
import cn.ucai.fulicenter.views.DisplayUtils;

public class LoginActivity extends BaseActivity {
    private static final String TAG = LoginActivity.class.getCanonicalName();

    @Bind(R.id.edUserName)
    EditText medUserName;
    @Bind(R.id.edPassword)
    EditText medPassword;
    @Bind(R.id.btnLogin)
    Button mbtnLogin;
    @Bind(R.id.btnRegister)
    Button mbtnRegister;

    String username;
    String password;

    LoginActivity mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mContext = this;
        super.onCreate(savedInstanceState);

    }
    @Override
    protected void initView() {
        DisplayUtils.initBackWithTitle(this,"账户登录");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }
    @OnClick({R.id.btnLogin,R.id.btnRegister})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnLogin:
                checkedInput();
                break;
            case R.id.btnRegister:
                MFGT.gotoRegisterActivity(this);
                break;
        }
    }

    private void checkedInput(){
        username = medUserName.getText().toString().trim();
        password = medPassword.getText().toString().trim();
        if (TextUtils.isEmpty(username)){
            CommonUtils.showLongToast(R.string.user_name_connot_be_empty);
            medUserName.requestFocus();
            return;
        }else if (TextUtils.isEmpty(password)){
            CommonUtils.showLongToast(R.string.password_connot_be_empty);
            medPassword.requestFocus();
            return;
        }
        login();
    }

    private void login() {
       final ProgressDialog pd = new ProgressDialog(mContext);
        pd.setMessage(getResources().getString(R.string.logining));
        pd.show();
        NetDao.login(mContext, username, password, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String str) {
                Result result = ResultUtils.getResultFromJson(str, User.class);
                L.e(TAG,"result="+result);
                if (result==null){
                    CommonUtils.showLongToast(R.string.login_fail);
                }else {
                    if (result.isRetMsg()){
                        User user = (User) result.getRetData();
                        L.e(TAG,"user = "+user);
                        /*String strUser = result.getRetData().toString();
                        OkHttpUtils<User> utils = new OkHttpUtils<>(mContext);
                        User user = utils.parseJson(strUser, User.class);
                        */
                        UserDao dao = new UserDao(mContext);

                        boolean isSuccess = dao.saveUser(user);
                        if (isSuccess){
                            SharePrefrenceUtils.getInstance(mContext).saveUser(user.getMuserName());
                            FuLiCenterApplication.setUser(user);
                            MFGT.finish(mContext);
                        }else {
                            CommonUtils.showLongToast(R.string.user_database_error);
                        }
                        /*dao.saveUser(user);
                        L.e(TAG,"user = "+user);

                        MFGT.finish(mContext);*/
                    }else {
                         if (result.getRetCode()==I.MSG_LOGIN_UNKNOW_USER){
                             CommonUtils.showLongToast(R.string.login_fail_unknow_user);
                         }else if (result.getRetCode()==I.MSG_LOGIN_ERROR_PASSWORD){
                             CommonUtils.showLongToast(R.string.login_fail_error_password);
                         }else {
                             CommonUtils.showLongToast(R.string.login_fail);
                         }
                    }
                }
                pd.dismiss();
            }

            @Override
            public void onError(String error) {
                pd.dismiss();
                CommonUtils.showLongToast(error);
                L.e(TAG,"error"+error);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK && requestCode== I.REQUEST_CODE_REGISTER){
            String name = data.getStringExtra(I.User.USER_NAME);
            medUserName.setText(name);
        }
    }
}
