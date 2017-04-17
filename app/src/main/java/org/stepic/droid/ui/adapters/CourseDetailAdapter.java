package org.stepic.droid.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.stepic.droid.R;
import org.stepic.droid.model.CourseProperty;
import org.stepic.droid.util.ColorUtil;
import org.stepic.droid.util.resolvers.text.TextResolver;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CourseDetailAdapter extends RecyclerView.Adapter<CourseDetailAdapter.GenericViewHolder> {

    private static final int TYPE_HEADER = 1;
    public static final int TYPE_COURSE_PROPERTY = 2;
    public static final int TYPE_INSTRUCTORS = 3;


    private final Context context;
    private final TextResolver textResolver;
    private final List<CourseProperty> coursePropertyList;

    public CourseDetailAdapter(Context context, TextResolver textResolver, List<CourseProperty> coursePropertyList) {
        this.context = context;
        this.textResolver = textResolver;
        this.coursePropertyList = coursePropertyList;
    }


    @Override
    public GenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.stub_text, parent, false);
        return new GenericViewHolder(v);
    }

    @Override
    public void onBindViewHolder(GenericViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return 30;
    }

    public static class GenericViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.stubTextView)
        TextView textView;


        public GenericViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        public void bindData(int position) {
            if (position % 2 == 0) {
                textView.setBackgroundColor(ColorUtil.INSTANCE.getColorArgb(R.color.stepic_orange_carrot, textView.getContext()));
            } else {
                textView.setBackgroundColor(ColorUtil.INSTANCE.getColorArgb(R.color.transparent, textView.getContext()));
            }
        }
    }
}
