package org.stepic.droid.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.stepic.droid.R;
import org.stepic.droid.model.Section;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SmallSectionAdapter extends RecyclerView.Adapter<SmallSectionAdapter.SectionViewHolder> {
    private final static String SMALL_SECTION_TITLE_DELIMETER = ". ";

    private final Context context;
    private final List<Section> sections;

    public SmallSectionAdapter(Context context, List<Section> sections) {
        this.context = context;
        this.sections = sections;
    }

    public boolean isEmpty() {
        return sections == null || sections.isEmpty();
    }

    @Override
    public SectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.small_section_item, parent, false);
        return new SectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SectionViewHolder holder, int position) {
        if (position >= 0 && position < sections.size()) {
            holder.bindData(sections.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return sections.size();
    }

    static class SectionViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.section_title)
        TextView sectionTitle;

        SectionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindData(Section section) {
            String title = section.getTitle();
            int positionOfSection = section.getPosition();
            title = positionOfSection + SMALL_SECTION_TITLE_DELIMETER + title;
            sectionTitle.setText(title);
        }
    }
}
