package de.ur.mi.android.primechecker.primes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class PrimeCheckerBroadcastReceiver extends BroadcastReceiver {

    private static final String PRIME_CANDIDATE_CHECKED = "de.ur.mi.android.task.primechecker.PRIME_CANDIDATE_CHECKED";
    private static final String PRIME_CANDIDATE = "PRIME_CANDIDATE";

    private PrimeCheckerBroadcastListener listener;

    public PrimeCheckerBroadcastReceiver(PrimeCheckerBroadcastListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(PRIME_CANDIDATE_CHECKED)) {
            PrimeCandidate checkedCandidate = (PrimeCandidate) intent.getSerializableExtra(PRIME_CANDIDATE);
            listener.onPrimeCandidateChecked(checkedCandidate);
        }
    }

    public static IntentFilter getIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(PRIME_CANDIDATE_CHECKED);
        return filter;
    }

    public static Intent getPrimeCandidateCheckedIntent(PrimeCandidate candidate) {
        Intent intent = new Intent();
        intent.setAction(PRIME_CANDIDATE_CHECKED);
        intent.putExtra(PRIME_CANDIDATE, candidate);
        return intent;
    }
}
