package cn.ucai.fulicenter.activity;

import android.os.Bundle;
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
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.views.DisplayUtils;

public class LoginActivity extends BaseActivity {

    @Bind(R.id.ivBack)
    ImageView mivBack;
    @Bind(R.id.tvTitle)
    TextView mtvTitle;
    @Bind(R.id.commit_login_title)
    RelativeLayout mcommitLoginTitle;
    @Bind(R.id.edUserName)
    EditText medUserName;
    @Bind(R.id.edPassword)
    EditText medPassword;
    @Bind(R.id.btnLogin)
    Button mbtnLogin;
    @Bind(R.id.btnRegister)
    Button mbtnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
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
                break;
            case R.id.btnRegister:
                MFGT.gotoRegisterActivity(this);

                break;
        }
    }
}
