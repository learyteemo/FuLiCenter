package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.filicenter.R;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.dao.SharePrefrenceUtils;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.views.DisplayUtils;

public class PersonalSettingActivity extends BaseActivity {

    @Bind(R.id.ivThumb)
    ImageView mivThumb;
    PersonalSettingActivity mContext;
    /* @Bind(R.id.ivBack)
     ImageView mivBack;*/

    @Bind(R.id.tvusername)
    TextView mtvusername;
    @Bind(R.id.tvNickname)
    TextView mtvNickname;
    User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_persional_setting);
        ButterKnife.bind(this);
        mContext = this;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        DisplayUtils.initBackWithTitle(mContext, "个人资料");
    }

    @Override
    protected void initData() {
        user = FuLiCenterApplication.getUser();
        if (user != null) {
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), mContext, mivThumb);
            mtvusername.setText(user.getMuserName());
            mtvNickname.setText(user.getMuserNick());
        } else {
            finish();
        }
    }

    @Override
    protected void setListener() {
    }

    @OnClick({R.id.layout_user_avatar, R.id.layout_user_username, R.id.layout_nickname})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_user_avatar:
                break;
            case R.id.layout_user_username:
                break;
            case R.id.layout_nickname:
                break;

        }
    }

    private void logout() {
        if (user != null) {
            SharePrefrenceUtils.getInstance(mContext).removeUser();
            FuLiCenterApplication.setUser(null);
            MFGT.gotoLoginActivity(mContext);
        }
        MFGT.finish(mContext);

    }

    @OnClick(R.id.btn)
    public void onClickBack() {
        logout();
    }
}
