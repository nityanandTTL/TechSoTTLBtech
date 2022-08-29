package com.thyrocare.btechapp.NewScreenDesigns.Adapters;

import android.app.Activity;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.widget.ImageViewCompat;
import androidx.viewpager.widget.PagerAdapter;

import com.ablanco.zoomy.Zoomy;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.GetBtechCertifcateResponseModel;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import java.util.ArrayList;

public class DisplayBtechCertificateViewPagerAdapter extends PagerAdapter {

    Activity mActivity;
    Global globalclass;
    ArrayList<GetBtechCertifcateResponseModel.Certificatess> certificatessArrayList;

    public DisplayBtechCertificateViewPagerAdapter(Activity activity, ArrayList<GetBtechCertifcateResponseModel.Certificatess> certificatessArrayList) {
        this.mActivity = activity;
        globalclass = new Global(mActivity);
        this.certificatessArrayList = certificatessArrayList;
    }

    @Override
    public int getCount() {
        return certificatessArrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((RelativeLayout) object);
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = LayoutInflater.from(mActivity).inflate(R.layout.certificate_pager_item, container, false);


        AppCompatImageView imageView = (AppCompatImageView) itemView.findViewById(R.id.img_certificate);
        String ImageURL = !InputUtils.isNull(certificatessArrayList.get(position).getCertificate()) ? certificatessArrayList.get(position).getCertificate().replace("\\", "/") : "";
        if (Patterns.WEB_URL.matcher(ImageURL).matches()) {
            globalclass.DisplayImagewithDefaultImage(mActivity, ImageURL, imageView);
            Zoomy.Builder builder = new Zoomy.Builder(mActivity).target(imageView).enableImmersiveMode(false);
            builder.register();
        }

//        globalclass.DisplayImagewithDefaultImage(mActivity, "https://techso.thyrocare.cloud/techsoapi/Images/BtechSelfi/20072020884544334.jpg", imageView);

        container.addView(itemView);

        return itemView;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
