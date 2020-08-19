package com.thyrocare.btechapp.NewScreenDesigns.Adapters;

import android.app.Activity;
import android.graphics.Paint;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.MainMaterialModel;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.UpdateStockDialog;
import com.thyrocare.btechapp.R;


public class StockAvailabilityAdapter extends RecyclerView.Adapter<StockAvailabilityAdapter.ViewHolder> {
    MainMaterialModel mainMaterialModel;
    Activity activity;
    int opStock = 0, usedStock = 0, actualStock = 0;

    public StockAvailabilityAdapter(Activity activity, MainMaterialModel mainMaterialModel) {
        this.activity = activity;
        this.mainMaterialModel = mainMaterialModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.material_item_view_new, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.txtMaterialName.setText(mainMaterialModel.getMaterialDetails().get(position).getMaterialName());
        holder.txtMaterialName.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        holder.txtExpectedStock.setText(mainMaterialModel.getMaterialDetails().get(position).getExpectedStock());

        opStock = (int) Double.parseDouble(mainMaterialModel.getMaterialDetails().get(position).getOpeningStock());
        usedStock = (int) Double.parseDouble(mainMaterialModel.getMaterialDetails().get(position).getUsedStock());

        actualStock = opStock - usedStock;
//            holder.txtActualStock.setText(mainMaterialModel.getMaterialDetails().get(position).getOpeningStock());
        holder.txtActualStock.setText("" + actualStock);

        holder.txtMaterialName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainMaterialModel.MaterialDetailsBean materialDetailsBean = mainMaterialModel.getMaterialDetails().get(position);
                UpdateStockDialog updateStockDialog = new UpdateStockDialog(activity, materialDetailsBean, mainMaterialModel);
                updateStockDialog.setCancelable(false);
                updateStockDialog.show();
                int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.99);
                updateStockDialog.getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mainMaterialModel.getMaterialDetails().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtMaterialName, txtExpectedStock, txtActualStock;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMaterialName = (TextView) itemView.findViewById(R.id.txtMaterialName);
            txtExpectedStock = (TextView) itemView.findViewById(R.id.txtExpectedStock);
            txtActualStock = (TextView) itemView.findViewById(R.id.txtActualStock);
        }
    }
}