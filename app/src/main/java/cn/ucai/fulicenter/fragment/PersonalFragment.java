package cn.ucai.fulicenter.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.filicenter.R;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.activity.MainActivity;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.dao.UserDao;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.ResultUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalFragment extends BaseFragment {
    private static final String TAG = PersonalFragment.class.getSimpleName();
    @Bind(R.id.ivthumb)
    ImageView mivthumb;
    TextView mTvUserName;
    MainActivity mContext;
    @Bind(R.id.tvset)
    TextView tvset;
    User user;
    @Bind(R.id.tvCollect)
    TextView tvCollect;

    public PersonalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        ButterKnife.bind(this, view);
        mContext = (MainActivity) getActivity();
        mTvUserName = (TextView) view.findViewById(R.id.tvUserName);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        user = FuLiCenterApplication.getUser();
        L.e(TAG, "user=" + user);
        if (user == null) {
            MFGT.gotoLoginActivity(mContext);
        } else {
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), mContext, mivthumb);
            mTvUserName.setText(user.getMuserNick());
        }
    }

    @Override
    protected void setListener() {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.tvset, R.id.layout_title})
    public void onClick() {
        MFGT.gotoPersonalSettingActivity(mContext);
    }

    private void syncUserInfo() {
        NetDao.syncUserInfo(mContext, user.getMuserName(), new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                Result result = ResultUtils.getResultFromJson(s, User.class);
                if (result != null) {
                    User u = (User) result.getRetData();
                    if (!user.equals(u)) {
                        UserDao dao = new UserDao(mContext);
                        boolean b = dao.saveUser(u);
                        if (b) {
                            FuLiCenterApplication.setUser(u);
                            user = u;
                            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), mContext, mivthumb);
                            mTvUserName.setText(user.getMuserNick());
                        }
                    }
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void syncCollectsCount() {
        NetDao.getCollectsCount(mContext, user.getMuserName(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
            @Override
            public void onSuccess(MessageBean result) {
                if (result != null && result.isSuccess()) {
                    tvCollect.setText(result.getMsg());

                } else {
                    tvCollect.setText(String.valueOf(0));
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        user = FuLiCenterApplication.getUser();
        if (user != null) {
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), mContext, mivthumb);
            mTvUserName.setText(user.getMuserNick());
            syncUserInfo();
            syncCollectsCount();
        }
    }

    @OnClick(R.id.layout_collects)
    public void onClickCollects() {
        MFGT.gotoCollectsActivity(mContext);
    }
}
