package cn.ucai.fulicenter.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.filicenter.R;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.dao.SharePrefrenceUtils;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.OnSetAvatarListener;
import cn.ucai.fulicenter.utils.ResultUtils;
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
OnSetAvatarListener mOnSetAvatarListener;
    private static final String TAG = PersonalSettingActivity.class.getSimpleName();
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
                mOnSetAvatarListener = new OnSetAvatarListener(mContext,R.id.layout_upload_avatar,
                        user.getMuserName(),I.AVATAR_TYPE_USER_PATH);
                break;
            case R.id.layout_user_username:
                CommonUtils.showLongToast(R.string.user_name_connot_be_empty);
                break;
            case R.id.layout_nickname:
                MFGT.gotoUpdateNick(mContext);
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

    @Override
    protected void onResume() {
        super.onResume();
        showInfo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.e("onActivityResult,requestCode="+requestCode+",resultCode="+requestCode);
        if (resultCode != RESULT_OK) {
            return;
        }
        mOnSetAvatarListener.setAvatar(requestCode,data,mivThumb);
        if (requestCode ==  I.REQUEST_CODE_NICK) {
            CommonUtils.showLongToast(R.string.update_user_nick_success);
        }
        if (requestCode == OnSetAvatarListener.REQUEST_CROP_PHOTO) {
            updateAvatar();
        }
    }

    private void updateAvatar() {
        File file = new File(OnSetAvatarListener.getAvatarPath(mContext,
               user.getMavatarPath()+"/"+user.getMuserName()+I.AVATAR_SUFFIX_JPG));
        L.e("file="+file.exists());
        L.e("file="+file.getAbsolutePath());
        final ProgressDialog pd = new ProgressDialog(mContext);
        pd.setMessage(getResources().getString(R.string.update_user_avarar));
        pd.show();
        NetDao.updateAvatar(mContext, user.getMuserName(), file, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                L.e("s="+s);
                Result result = ResultUtils.getResultFromJson(s,User.class);
                L.e("result="+result);
                if (result.getRetCode()!=0){
                    CommonUtils.showLongToast(R.string.update_user_avatar_fail);
                }else {
                    User u = (User) result.getRetData();
                    if (result.isRetMsg()){
                        FuLiCenterApplication.setUser(u);
                        ImageLoader.setAvatar(ImageLoader.getAvatarUrl(u),mContext,mivThumb);
                        CommonUtils.showLongToast(R.string.update_user_success);
                    }else {
                        CommonUtils.showLongToast(R.string.update_user_avatar_fail);
                    }
                }
                pd.dismiss();
            }

            @Override
            public void onError(String error) {
                CommonUtils.showLongToast(R.string.update_user_avatar_fail);
                L.e("error="+error);
            }
        });
    }

    private void showInfo() {
    user = FuLiCenterApplication.getUser();
        if (user!=null){
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user),mContext,mivThumb);
            mtvusername.setText(user.getMuserName());
            mtvNickname.setText(user.getMuserNick());
        }
    }
}
