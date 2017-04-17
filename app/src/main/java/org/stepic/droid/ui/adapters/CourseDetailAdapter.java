package org.stepic.droid.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.stepic.droid.R;
import org.stepic.droid.model.CourseProperty;
import org.stepic.droid.ui.custom.LatexSupportableWebView;
import org.stepic.droid.util.resolvers.text.TextResolver;
import org.stepic.droid.util.resolvers.text.TextResult;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CourseDetailAdapter extends RecyclerView.Adapter<CourseDetailAdapter.GenericViewHolder> {

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
    public int getItemViewType(int position) {
        return TYPE_COURSE_PROPERTY;
    }

    @Override
    public GenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.course_property_item, parent, false);
        return new CoursePropertyViewHolder(v, textResolver, coursePropertyList);
    }

    @Override
    public void onBindViewHolder(GenericViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return coursePropertyList.size();
    }

    abstract static class GenericViewHolder extends RecyclerView.ViewHolder {

        protected final Context context;

        GenericViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }


        public abstract void bindData(int position);
    }

    static class CoursePropertyViewHolder extends GenericViewHolder {

        private final TextResolver textResolver;
        private final List<CourseProperty> courseProperties;

        @BindView(R.id.course_property_title)
        TextView coursePropertyTitle;

        @BindView(R.id.course_property_text_value)
        TextView coursePropertyValue;

        @BindView(R.id.course_property_html_value)
        LatexSupportableWebView latexSupportableWebView;

        public CoursePropertyViewHolder(View itemView, TextResolver textResolver, List<CourseProperty> courseProperties) {
            super(itemView);
            this.textResolver = textResolver;
            this.courseProperties = courseProperties;
            coursePropertyValue.setMovementMethod(LinkMovementMethod.getInstance());
        }

        @Override
        public void bindData(int position) {
            final CourseProperty courseProperty = courseProperties.get(position);

            coursePropertyTitle.setText(courseProperty.getTitle());

            TextResult textResult = textResolver.resolveCourseProperty(courseProperty.getCoursePropertyType(), courseProperty.getText(), context);
            if (!textResult.isNeedWebView()) {
                //it is plain
                latexSupportableWebView.setVisibility(View.GONE);
                coursePropertyValue.setVisibility(View.VISIBLE);
                coursePropertyValue.setText(textResult.getText());
            } else {
                //show webview
                latexSupportableWebView.setVisibility(View.VISIBLE);
                coursePropertyValue.setVisibility(View.GONE);
                latexSupportableWebView.setText(textResult.getText());
            }

        }
    }
}
