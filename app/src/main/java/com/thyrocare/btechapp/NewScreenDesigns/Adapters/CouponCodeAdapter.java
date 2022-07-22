package com.thyrocare.btechapp.NewScreenDesigns.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thyrocare.btechapp.BtechInterfaces.AppInterfaces;
import com.thyrocare.btechapp.NewScreenDesigns.Activities.AddEditBenificaryActivity;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.models.api.response.CouponCodeResponseModel;
import com.thyrocare.btechapp.utils.app.InputUtils;

public class CouponCodeAdapter extends RecyclerView.Adapter<CouponCodeAdapter.viewHolder> {
    Activity activity;
    Boolean subtextVisibilityFlag = false;
    CouponCodeResponseModel couponCodeResponseModel;
    AddEditBenificaryActivity addEditBenificaryActivity;
    AppInterfaces.getClickedCoupon getClickedCoupon;


    public CouponCodeAdapter(AddEditBenificaryActivity addEditBenificaryActivityClass, CouponCodeResponseModel couponCodeResponseModel, AppInterfaces.getClickedCoupon getClickedCoupon) {
        this.activity = addEditBenificaryActivityClass;
        this.couponCodeResponseModel = couponCodeResponseModel;
        this.addEditBenificaryActivity = addEditBenificaryActivityClass;
        this.getClickedCoupon = getClickedCoupon;

    }

    @NonNull
    @Override
    public CouponCodeAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_coupon_item, parent, false);
        return new viewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull final CouponCodeAdapter.viewHolder holder, final int position) {

        holder.tv_couponHead.setText(couponCodeResponseModel.getData().get(position).getCouponHead());
        holder.tv_couponSubtext.setText(couponCodeResponseModel.getData().get(position).getCouponSubHead());
        holder.tv_couponCode.setText(couponCodeResponseModel.getData().get(position).getCouponCode());


        holder.tv_show_moreless.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!subtextVisibilityFlag) {
                    holder.tv_couponSubtext.setVisibility(View.VISIBLE);
                    holder.tv_show_moreless.setText("View less");
                    subtextVisibilityFlag = true;
                } else {
                    holder.tv_couponSubtext.setVisibility(View.GONE);
                    holder.tv_show_moreless.setText("View more");
                    subtextVisibilityFlag = false;
                }
            }
        });
        holder.tv_show_moreless.setVisibility(InputUtils.isNull(couponCodeResponseModel.getData().get(position).getCouponSubHead()) ? View.GONE : View.VISIBLE);

        holder.tv_ApplycouponCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getClickedCoupon.onApplyClick(couponCodeResponseModel.getData().get(position).getCouponCode());

            }
        });
    }

    @Override
    public int getItemCount() {
        return couponCodeResponseModel.getData().size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView img_couponimage;
        TextView tv_couponHead, tv_couponSubtext, tv_show_moreless, tv_couponCode, tv_ApplycouponCode;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            img_couponimage = itemView.findViewById(R.id.img_couponimage);
            tv_couponHead = itemView.findViewById(R.id.tv_couponHead);
            tv_couponSubtext = itemView.findViewById(R.id.tv_couponSubtext);
            tv_show_moreless = itemView.findViewById(R.id.tv_show_moreless);
            tv_couponCode = itemView.findViewById(R.id.tv_couponCode);
            tv_ApplycouponCode = itemView.findViewById(R.id.tv_ApplycouponCode);

            tv_couponSubtext.setVisibility(View.GONE);
        }
    }
}
