package de.ur.mi.android.primechecker.primes;

/**
 * Listener-Interface, über das der PrimeCheckerBroadcastReceiver einen angeschlossene Komponenten über
 * eingegangenen Broadcast, speziell den Abschluss einer Primzahlprüfung informiert.
 */

public interface PrimeCheckerBroadcastListener {

    /**
     * Wird vom BroadcastReceiver aufgerufen, wenn dieser das Ergebnis der Prüfung vom Service mitgeteilt
     * bekommen hat.
     */
    void onPrimeCandidateChecked(PrimeCandidate candidate);

}
