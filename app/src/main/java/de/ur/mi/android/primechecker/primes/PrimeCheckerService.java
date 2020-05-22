package de.ur.mi.android.primechecker.primes;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.concurrent.Executors;

public class PrimeCheckerService extends Service {

    private static final String PRIME_CANDIDATE = "PRIME_CANDIDATE";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        PrimeCandidate candidate = (PrimeCandidate) intent.getSerializableExtra(PRIME_CANDIDATE);
        Executors.newSingleThreadExecutor().submit(new PrimeCheckerTask(candidate));
        return super.onStartCommand(intent, flags, startId);
    }

    public static Intent getServiceIntent(Context context, PrimeCandidate candidate) {
        Intent intent = new Intent(context, PrimeCheckerService.class);
        intent.putExtra(PRIME_CANDIDATE, candidate);
        return intent;
    }

    private class PrimeCheckerTask implements Runnable {

        private PrimeCandidate candidate;

        public PrimeCheckerTask(PrimeCandidate candidate) {
            this.candidate = candidate;
        }

        @Override
        public void run() {
            PrimeCandidate checkedCandidate = PrimeChecker.checkCandidate(candidate);
            Intent intent = PrimeCheckerBroadcastReceiver.getPrimeCandidateCheckedIntent(checkedCandidate);
            sendBroadcast(intent);
            PrimeCheckerService.this.stopSelf();
        }
    }
}
