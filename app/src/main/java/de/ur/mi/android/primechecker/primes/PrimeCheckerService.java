package de.ur.mi.android.primechecker.primes;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.concurrent.Executors;


/**
 * Service zur parallelisierten Überprüfung eines Primzahlkandidaten im Hintergrund der Anwendung
 *
 * Achtung: Der Service muss im Manifest registriert werden, bevor er in der App gestartet werden kann
 */
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
        /// Erstellen und parallelisiertes Ausführen der Primzahlprüfung über ein Runnable
        Executors.newSingleThreadExecutor().submit(new PrimeCheckerTask(candidate));
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * Gibt einen Intent zurück, über den ein PrimeCheckerService gestartet werden kann. Die zu prüfende
     * Zahl wird im Intent gespeichert und später in onStartCommand wieder ausgelesen. Durch die
     * Implementierung an dieser Stelle, stellen wir sicher, dass der Start-Intent korrekt erstellt wird.
     * Die Activity, die den Service nutzt und startet, kann mit sehr wenigen Informationen - benötigt wird
     * neben dem Context (Activity selbst) nur ein entsprechender Kandidat - einen passenden Serivce-Intent
     * starten.
     */
    public static Intent getServiceIntent(Context context, PrimeCandidate candidate) {
        Intent intent = new Intent(context, PrimeCheckerService.class);
        intent.putExtra(PRIME_CANDIDATE, candidate);
        return intent;
    }

    /**
     * Runnable für die parallelisierte Durchführung der Primzahlprüfung.
     */
    private class PrimeCheckerTask implements Runnable {

        private PrimeCandidate candidate;

        public PrimeCheckerTask(PrimeCandidate candidate) {
            this.candidate = candidate;
        }

        @Override
        public void run() {
            // Prüfung des übergebenen Primzahlkandidaten, der Thread blockiert, bis checkCandidate
            // ein Ergebnis zurückliefert: Bei großen Zahlen kann da dauern
            PrimeCandidate checkedCandidate = PrimeChecker.checkCandidate(candidate);
            // Erstellen des BroadcastIntents, um den Receiver über das Ergebnis der Prüfung zu informieren
            Intent intent = PrimeCheckerBroadcastReceiver.getPrimeCandidateCheckedIntent(checkedCandidate);
            sendBroadcast(intent);
            // Nach dem das Ergebnis per Broadcast verschickt wurde, kann der Service beendet werden
            PrimeCheckerService.this.stopSelf();
        }
    }
}
