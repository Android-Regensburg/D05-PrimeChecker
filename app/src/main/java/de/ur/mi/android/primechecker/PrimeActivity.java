package de.ur.mi.android.primechecker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigInteger;

import de.ur.mi.android.primechecker.primelist.PrimeCandidateAdapter;
import de.ur.mi.android.primechecker.primes.PrimeCandidate;
import de.ur.mi.android.primechecker.primes.PrimeCheckerBroadcastListener;
import de.ur.mi.android.primechecker.primes.PrimeCheckerBroadcastReceiver;
import de.ur.mi.android.primechecker.primes.PrimeCheckerService;

public class PrimeActivity extends AppCompatActivity implements PrimeCheckerBroadcastListener {

    private EditText primeCandidateInput;
    private PrimeCandidateAdapter candidates;

    private PrimeCheckerBroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBroadcastReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterBroadcastReceiver();
    }

    private void registerBroadcastReceiver() {
        unregisterBroadcastReceiver();
        broadcastReceiver = new PrimeCheckerBroadcastReceiver(this);
        this.registerReceiver(broadcastReceiver, PrimeCheckerBroadcastReceiver.getIntentFilter());
    }

    private void unregisterBroadcastReceiver() {
        if (broadcastReceiver != null) {
            this.unregisterReceiver(broadcastReceiver);
            broadcastReceiver = null;
        }
    }

    private void initUI() {
        setContentView(R.layout.activity_prime);
        RecyclerView candidatesView = findViewById(R.id.log_prime_candidate_list);
        primeCandidateInput = findViewById(R.id.prime_candidate_input);
        candidates = new PrimeCandidateAdapter(this);
        candidatesView.setAdapter(candidates);
        findViewById(R.id.prime_candidate_input_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onInputButtonClicked();
            }
        });
    }

    private void onInputButtonClicked() {
        String candidateText = primeCandidateInput.getText().toString();
        try {
            BigInteger candidateInteger = new BigInteger(candidateText);
            PrimeCandidate candidate = new PrimeCandidate(candidateInteger);
            candidates.addOrUpdateCandidate(candidate);
            Intent serviceIntent = PrimeCheckerService.getServiceIntent(this, candidate);
            startService(serviceIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPrimeCandidateChecked(PrimeCandidate candidate) {
        candidates.addOrUpdateCandidate(candidate);
    }
}
