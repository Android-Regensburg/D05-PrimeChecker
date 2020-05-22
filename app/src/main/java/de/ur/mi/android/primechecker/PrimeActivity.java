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


/**
 * Diese App prüft, ob eine von den NutzerInnen eingegeben Zahl eine Primzahl ist. Es können beliebig
 * große Zahlen (BigInteger) getestet werden. Die Verwendung bzw. Funktion der Anwendung lässt sich
 * wie folgt beschreiben:
 *
 * - NutzerInnen geben über ein Textfeld eine Zahl ein
 * - Die Zahl wird in einer Liste als PrimeCandidate angezeigt
 * - Im Hintergrund prüft ein Service, ob es sich bei der Zahl um eine Primzahl handelt
 * - Sobald der Service ein Ergebnis zurück liefert, wird der Eintrag in der Liste entsprechend angepasst
 *
 * Durch die Auslagerung in einen Service könne beliebig große und beliebig viele Zahlen gleichzeitig
 * getestet werden, ohne dass der UI Thread der Anwendung blockiert wird.
 *
 * Service und Activity kommunizieren über einen BroadcastReceiver (https://developer.android.com/guide/components/broadcasts):
 *
 * - Die Activity erstellt beim Start einen Receiver, registriert diesen für die relevanten Broadcast-Intents
 * und verknüpft sich selbst als PrimeCheckerBroadcastListener mit dem Receiver
 * - Der Service sendet nach Prüfung der Zahl enen Broadcast-Intent mit dem Ergebnis, der vom Receiver
 * abgefangen wird
 * - Der Receiver informiert die Activity über das Listener-Interface über das Ergebnis der Prüfung
 * - Sobald die Acitivty gestoppt wird, wird auch der Receiver deregistriert
 *
 */
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

    /**
     * Erstellt und registriert den Receiver, um Nachrichten (Broadcasts) aus dem Service abzufangen
     */
    private void registerBroadcastReceiver() {
        unregisterBroadcastReceiver();
        broadcastReceiver = new PrimeCheckerBroadcastReceiver(this);
        this.registerReceiver(broadcastReceiver, PrimeCheckerBroadcastReceiver.getIntentFilter());
    }

    /**
     * Deregistriert den Receiver um Problemem zu vermeiden, die auftreten können, wenn der Service
     * nach Beendigung der Activity noch kkurzfristig aktiv ist und theoretisch Broadcasts aussenden
     * könnte.
     */
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
            // Erstellen des Testkandidaten
            PrimeCandidate candidate = new PrimeCandidate(candidateInteger);
            // Darstellung in der Liste
            candidates.addOrUpdateCandidate(candidate);
            // Erstellen des Intents, mit dem der Service für die Überprüfung des Kandidaten gestartet werden kann
            Intent serviceIntent = PrimeCheckerService.getServiceIntent(this, candidate);
            // Starten des Services
            startService(serviceIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPrimeCandidateChecked(PrimeCandidate candidate) {
        // Aktualisierung der Liste mit den Ergebnissen, die - über den BroadcastReceiver - vom Service
        // verschickt wurden
        candidates.addOrUpdateCandidate(candidate);
    }
}
