package de.ur.mi.android.primechecker.primes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * BroadcastReceiver zum Abfangen der Prüfergebnisse aus dem Service:
 *
 * 1. Die Activity erstellt eine Instanz dieses Receivers und registriert ihn für PRIME_CANDIDATE_CHECKED-Intents
 * 2. Der Service versendet, sobald das Ergebnis der Primzahlprüfung vorliegt einen PRIME_CANDIDATE_CHECKED-Intent
 * mit den Prüfergebnissen
 * 3. Der Receiver fängt diesen Broadcast ab und leitet das Ergebnis an die Activity weiter, die sich
 * beim Erstellen des Receivers als PrimeCheckerBroadcastListener angmeldet hat.
 */

public class PrimeCheckerBroadcastReceiver extends BroadcastReceiver {

    private static final String PRIME_CANDIDATE_CHECKED = "de.ur.mi.android.task.primechecker.PRIME_CANDIDATE_CHECKED";
    private static final String PRIME_CANDIDATE = "PRIME_CANDIDATE";

    private PrimeCheckerBroadcastListener listener;

    public PrimeCheckerBroadcastReceiver(PrimeCheckerBroadcastListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Stellt sicher, dass nur dann der Listener informiert wird, wenn ein passender Intent (Action)
        // abgefangen wird.
        if (intent.getAction().equals(PRIME_CANDIDATE_CHECKED)) {
            // Auslesen des PrimeCandiates aus dem Intent (Vgl. getPrimeCandidateCheckedIntent)
            PrimeCandidate checkedCandidate = (PrimeCandidate) intent.getSerializableExtra(PRIME_CANDIDATE);
            // Information des Listeners
            listener.onPrimeCandidateChecked(checkedCandidate);
        }
    }

    /**
     * Gibt einen Intentfilter zurück, um einen PrimeCheckerBroadcastReceiver für die korrekten Intents
     * zu registrieren. Durch die Implementierung an dieser Stelle verstecken wir Details, wie z.B.
     * den Namen der Intent-Action vor anderen Komponenten der Anwendung. Der Receiver selbst stellt
     * sicher, dass er für die korrekten Intents registriert wird.
     */
    public static IntentFilter getIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(PRIME_CANDIDATE_CHECKED);
        return filter;
    }

    /**
     * Gibt einen Intent zurück, mit dem alle existierenden PrimeCheckerBroadcastReceiver über das
     * Ergebnis einer Primzahlprüfung informiert werden.  Durch die Implementierung an dieser Stelle
     * kann der Receiver sicherstellen, dass die korrekte Intent-Action verwendet und das Ergebnis
     * (der PrimeCandidate) korrekt im Intent gespeichert wird.
     */
    public static Intent getPrimeCandidateCheckedIntent(PrimeCandidate candidate) {
        Intent intent = new Intent();
        intent.setAction(PRIME_CANDIDATE_CHECKED);
        intent.putExtra(PRIME_CANDIDATE, candidate);
        return intent;
    }
}
