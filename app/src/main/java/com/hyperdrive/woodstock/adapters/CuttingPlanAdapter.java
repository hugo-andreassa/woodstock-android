package com.hyperdrive.woodstock.adapters;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.holders.CuttingPlanHolder;
import com.hyperdrive.woodstock.models.BudgetItemModel;
import com.hyperdrive.woodstock.models.CuttingPlanModel;
import com.hyperdrive.woodstock.ui.budgetitem.BudgetItemActionFragment;
import com.hyperdrive.woodstock.ui.cuttingplan.CuttingPlanActionFragment;

import java.util.List;
import java.util.Locale;

public class CuttingPlanAdapter extends RecyclerView.Adapter<CuttingPlanHolder> {
    private static final String TAG = "CUTTING_PLAN_ACTIVITY";

    private final Context mContext;
    private final List<CuttingPlanModel> mCuttingPlans;
    private final Long budgetItemId;

    public CuttingPlanAdapter(List<CuttingPlanModel> mCuttingPlans, Long budgetItemId, Context mContext) {
        this.mContext = mContext;
        this.mCuttingPlans = mCuttingPlans;
        this.budgetItemId = budgetItemId;
    }

    @NonNull
    @Override
    public CuttingPlanHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CuttingPlanHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_cutting_plan, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CuttingPlanHolder holder, int position) {
        CuttingPlanModel cuttingPlan = mCuttingPlans.get(position);

        holder.quantity.setText(String.format(Locale.getDefault(),
                "Item %d - Quantidade: %d",
                position+1, cuttingPlan.getQuantity()
        ));
        holder.measures.setText(String.format(Locale.getDefault(),
                "Altura: %.0f X Largura: %.0f",
                cuttingPlan.getHeight(), cuttingPlan.getWidth()
        ));
        holder.comment.setText(cuttingPlan.getComment());

        // Altura
        double largura = cuttingPlan.getHeight();
        largura = convertCentimetersToInches(largura);

        // Largura
        double altura = cuttingPlan.getWidth();
        altura = convertCentimetersToInches(altura);

        Log.e(TAG, altura +"");
        Log.e(TAG, largura +"");

        Log.e(TAG, convertInchesToDp(altura)+"Convert");

        ViewGroup.LayoutParams layoutParams = holder.pieceDesign.getLayoutParams();
        layoutParams.width = convertInchesToDp(altura);
        layoutParams.height = convertInchesToDp(largura);
        holder.pieceDesign.setLayoutParams(layoutParams);

        holder.moreButton.setOnClickListener((v) -> {
            callFragment(v, cuttingPlan);
        });
    }

    @Override
    public int getItemCount() {
        return mCuttingPlans != null ? mCuttingPlans.size() : 0;
    }

    private void callFragment(View v, CuttingPlanModel cuttingPlan) {
        AppCompatActivity activity = (AppCompatActivity) v.getContext();

        CuttingPlanActionFragment cuttingPlanActionFragment =
                CuttingPlanActionFragment.newInstance(budgetItemId, cuttingPlan);

        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.cuttingplan_fragment_layout, cuttingPlanActionFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private double convertCentimetersToInches(Double value) {
        double inch = value * 0.39370;

        if(inch >= 300) {
            return 300.0;
        }

        return inch;
    }

    private int convertInchesToDp(double value) {
        DisplayMetrics mDisplayMetric = mContext.getApplicationContext().getResources().getDisplayMetrics();
        double px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_IN, (float) value, mDisplayMetric);
        double dp = Math.round(px / (mDisplayMetric.xdpi / DisplayMetrics.DENSITY_DEFAULT));

        return (int) Math.round(dp / 50.0);
    }

    public void updateData(List<CuttingPlanModel> cuttingPlans) {
        this.mCuttingPlans.clear();
        this.mCuttingPlans.addAll(cuttingPlans);
        notifyDataSetChanged();
    }
}

