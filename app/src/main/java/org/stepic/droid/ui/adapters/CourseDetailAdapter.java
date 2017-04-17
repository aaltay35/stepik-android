package org.stepic.droid.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import org.stepic.droid.R;
import org.stepic.droid.model.CourseProperty;
import org.stepic.droid.model.User;
import org.stepic.droid.ui.custom.LatexSupportableWebView;
import org.stepic.droid.util.resolvers.text.TextResolver;
import org.stepic.droid.util.resolvers.text.TextResult;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CourseDetailAdapter extends RecyclerView.Adapter<CourseDetailAdapter.GenericViewHolder> {

    private static final int TYPE_COURSE_PROPERTY = 2;
    private static final int TYPE_INSTRUCTORS = 3;

    private int POST_PROPERTIES_ITEMS_COUNT;


    private final Context context;
    private final TextResolver textResolver;
    private final List<CourseProperty> coursePropertyList;
    private List<User> instructors;
    private Activity activity;

    public CourseDetailAdapter(Context context, TextResolver textResolver, List<CourseProperty> coursePropertyList) {
        this.context = context;
        this.textResolver = textResolver;
        this.coursePropertyList = coursePropertyList;

        POST_PROPERTIES_ITEMS_COUNT = 0;
    }

    public void setInstructors(List<User> instructors, Activity activity) {
        this.instructors = instructors;
        this.activity = activity;
        POST_PROPERTIES_ITEMS_COUNT = 1;
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(TYPE_INSTRUCTORS, 1);
    }

    @Override
    public int getItemViewType(int position) {
        if (position < coursePropertyList.size()) {
            return TYPE_COURSE_PROPERTY;
        } else {
            return TYPE_INSTRUCTORS;
        }
    }

    @Override
    public GenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_COURSE_PROPERTY) {
            View v = LayoutInflater.from(context).inflate(R.layout.course_property_item, parent, false);
            return new CoursePropertyViewHolder(v, textResolver, coursePropertyList);
        } else {
            View v = LayoutInflater.from(context).inflate(R.layout.fragment_course_detailed_footer, parent, false);
            return new InstructorViewHolder(v, instructors, activity);
        }
    }

    @Override
    public void onBindViewHolder(GenericViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return coursePropertyList.size() + POST_PROPERTIES_ITEMS_COUNT;
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

    static class InstructorViewHolder extends GenericViewHolder {

        private final Activity activity;
        @BindView(R.id.instructors_carousel)
        RecyclerView instructorsCarousel;

        InstructorViewHolder(View itemView, List<User> instructors, Activity activity) {
            super(itemView);
            this.activity = activity;
            instructorsCarousel.setAdapter(new InstructorAdapter(instructors, activity));
            instructorsCarousel.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));

            instructorsCarousel.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    centeringRecycler(this);
                    return true;
                }
            });
        }

        @Override
        public void bindData(int position) {
            //do nothing, it is already set
        }

        private void centeringRecycler(ViewTreeObserver.OnPreDrawListener listener) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int widthOfScreen = size.x;

            int widthOfAllItems = instructorsCarousel.getMeasuredWidth();
            if (widthOfAllItems != 0) {
                instructorsCarousel.getViewTreeObserver().removeOnPreDrawListener(listener);
            }
            if (widthOfScreen > widthOfAllItems) {
                int padding = (int) (widthOfScreen - widthOfAllItems) / 2;
                instructorsCarousel.setPadding(padding, 0, padding, 0);
            } else {
                instructorsCarousel.setPadding(0, 0, 0, 0);
            }
        }
    }
}
