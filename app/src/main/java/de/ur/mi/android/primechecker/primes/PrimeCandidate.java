package de.ur.mi.android.primechecker.primes;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Diese Klasse bildet eine Zahl ab, die darauf getestet werden soll, ob diese "prim" ist, d.h. ob
 * es sich um eine Primzahl handelt. Für den Primzahlkandidat werde der numerische Wert der Zahl,
 * der Status der Überprüfung und deren Ergebnis gespeichert.
 */
public class PrimeCandidate implements Serializable {

    /**
     * Enum zur Abbildung des aktuellen Prüfungstatus. Vor und während der Überprüfung des Kandidaten
     * ist der Status "Pending", nach Abschluss der Überprüfung soll der Wert auf "Checked" gesetzt
     * werden.
     */
    public enum State {
        PENDING("Pending ..."),
        CHECKED("Checked");

        public final String text;

        State(String text) {
            this.text = text;
        }

    }

    /**
     * Enum zur Abbildung des Ergebnis der Überprüfung. Vor und während der Überprüfung des Kandidaten
     * ist das Ergebnis "Unavailable". Nach abgeschlossener Prüfung soll das Ergebnis entweder auf
     * "Prime" (der Primzahlkandidat ist eine Primzahl) oder auf "Not a Prime" (Der Kandidat ist keine
     * Primzahl) gesetzt werden.
     */
    public enum Result {
        NOT_AVAILABLE("Unavailable"),
        NO_PRIME("Not A Prime"),
        IS_PRIME("Prime");

        public final String text;

        Result(String text) {
            this.text = text;
        }
    }

    private State state;
    private Result result;
    /**
     * Hier wird die Zahl gespeichert, die geprüft werden soll. Als Datentyp wird BigInteger
     * verwendet, was theoretisch einen beliebig großen Wert erlaubt: BigInteger erlaubt die
     * Angabe eines numerischen Wert als String, also als beliebig lange Abfolge von Ziffern und
     * stellt die notwendigen mathematischen Operationen zum Rechnen mit solchen Werten als Methoden
     * bereit (add, subtract, multiply, divide, mod).
     */
    private BigInteger candidate;
    private BigInteger smallestDivisor;

    public PrimeCandidate(BigInteger candidate) {
        this.candidate = candidate;
        this.state = State.PENDING;
        this.result = Result.NOT_AVAILABLE;
        this.smallestDivisor = BigInteger.valueOf(1);
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setResult(Result result) {
        this.result = result;
        this.state = State.CHECKED;
    }

    public void setSmallestDivisor(BigInteger smallestDivisor) {
        this.smallestDivisor = smallestDivisor;
    }

    public BigInteger getValue() {
        return candidate;
    }

    public State getState() {
        return state;
    }

    public Result getResult() {
        return result;
    }

    public BigInteger getSmallestDivisor() {
        return this.smallestDivisor;
    }
}
