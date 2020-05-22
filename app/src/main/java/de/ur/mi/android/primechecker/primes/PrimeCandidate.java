package de.ur.mi.android.primechecker.primes;

import java.io.Serializable;
import java.math.BigInteger;

public class PrimeCandidate implements Serializable {

    public enum State {
        PENDING("Pending ..."),
        CHECKED("Checked");

        public final String text;

        State(String text) {
            this.text = text;
        }

    }

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
