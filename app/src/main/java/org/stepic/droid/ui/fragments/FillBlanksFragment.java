package org.stepic.droid.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.squareup.otto.Subscribe;

import org.stepic.droid.R;
import org.stepic.droid.events.InternetIsEnabledEvent;
import org.stepic.droid.events.comments.NewCommentWasAddedOrUpdateEvent;
import org.stepic.droid.events.steps.StepWasUpdatedEvent;
import org.stepic.droid.model.Attempt;
import org.stepic.droid.model.FillBlankComponent;
import org.stepic.droid.model.Reply;
import org.stepic.droid.ui.adapters.FillBlanksAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class FillBlanksFragment extends StepAttemptFragment {

    private RecyclerView recyclerContainer;
    private final List<FillBlankComponent> componentList = new ArrayList<>();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View fillBlanksView = ((LayoutInflater) this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_fill_blanks, attemptContainer, false);
        recyclerContainer = ButterKnife.findById(fillBlanksView, R.id.recycler);
        attemptContainer.addView(fillBlanksView);

        recyclerContainer.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected void showAttempt(Attempt attempt) {
        componentList.clear();
        componentList.addAll(attempt.getDataset().getFillBlankComponents());
        FillBlanksAdapter fillBlanksAdapter = new FillBlanksAdapter(componentList);
        recyclerContainer.setAdapter(fillBlanksAdapter);
        recyclerContainer.getAdapter().notifyDataSetChanged();
    }

    @Override
    protected Reply generateReply() {
        //// TODO: 20.01.17 make it from user changing
        List<String> blanks = new ArrayList<>();
        blanks.add("First one");
        blanks.add("Second");
        blanks.add("    etc");
        return new Reply.Builder()
                .setBlanks(blanks)
                .build();
    }

    @Override
    protected void blockUIBeforeSubmit(boolean needBlock) {
        //// TODO: 20.01.17   block UI
    }

    @Override
    protected void onRestoreSubmission() {
        Reply reply = submission.getReply();
        //// TODO: 20.01.17   fill blanks from reply
        reply.getBlanks();
    }

    @Subscribe
    @Override
    public void onInternetEnabled(InternetIsEnabledEvent enabledEvent) {
        super.onInternetEnabled(enabledEvent);
    }

    @Subscribe
    public void onNewCommentWasAdded(NewCommentWasAddedOrUpdateEvent event) {
        super.onNewCommentWasAdded(event);
    }

    @Subscribe
    public void onStepWasUpdated(StepWasUpdatedEvent event) {
        super.onStepWasUpdated(event);
    }

}
