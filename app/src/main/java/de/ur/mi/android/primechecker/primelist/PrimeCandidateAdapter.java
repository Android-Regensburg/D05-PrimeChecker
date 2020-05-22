package de.ur.mi.android.primechecker.primelist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigInteger;
import java.util.ArrayList;

import de.ur.mi.android.primechecker.R;
import de.ur.mi.android.primechecker.primes.PrimeCandidate;

public class PrimeCandidateAdapter extends RecyclerView.Adapter<PrimeCandidateAdapter.PrimeCandidateViewHolder> {

    private ArrayList<PrimeCandidate> candidates;
    private Context context;

    public static class PrimeCandidateViewHolder extends RecyclerView.ViewHolder {
        public View entryView;

        public PrimeCandidateViewHolder(View v) {
            super(v);
            entryView = v;
        }
    }

    public PrimeCandidateAdapter(Context context) {
        this.candidates = new ArrayList<>();
        this.context = context;
    }

    public void addOrUpdateCandidate(PrimeCandidate candidate) {
        PrimeCandidate existingCandidate = getCandidateByValue(candidate.getValue());
        if (existingCandidate != null) {
            existingCandidate.setResult(candidate.getResult());
            existingCandidate.setSmallestDivisor(candidate.getSmallestDivisor());
        } else {
            candidates.add(candidate);
        }
        this.notifyDataSetChanged();
    }

    private PrimeCandidate getCandidateByValue(BigInteger value) {
        for (PrimeCandidate candidate : candidates) {
            if (candidate.getValue().equals(value)) {
                return candidate;
            }
        }
        return null;
    }

    @NonNull
    @Override
    public PrimeCandidateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.prime_candidate_entry, parent, false);
        PrimeCandidateViewHolder vh = new PrimeCandidateViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull PrimeCandidateViewHolder holder, int position) {
        PrimeCandidate candidate = candidates.get(position);
        TextView value = holder.entryView.findViewById(R.id.prime_candidate_value);
        TextView state = holder.entryView.findViewById(R.id.prime_candidate_state);
        TextView result = holder.entryView.findViewById(R.id.prime_candidate_result);
        ProgressBar progress = holder.entryView.findViewById(R.id.prime_candidate_pending_indicator);
        value.setText(candidate.getValue().toString());
        state.setText(candidate.getState().text);
        result.setText(candidate.getResult().text);
        if(candidate.getState() == PrimeCandidate.State.CHECKED) {
            progress.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return candidates.size();
    }


}
