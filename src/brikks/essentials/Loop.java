package brikks.essentials;

public class Loop {
    @FunctionalInterface
    private interface ByteSupplier {
        byte perform();
    }

    private byte MIN;
    private byte MAX;
    private byte STEP;

    private byte position;
    private byte POINT;

    private ByteSupplier forwarder;
    private ByteSupplier backer;


    public Loop(final byte max) {
        this((byte) 0, (byte) 0, max, (byte) 1);
    }

    public Loop(final byte start, final byte max) {
        this(start, (byte) 0, max, (byte) 1);
    }

    public Loop(final byte start, final byte min, final byte max) {
        this(start, min, max, (byte) 1);
    }

    public Loop(final byte start, final byte min, final byte max, final byte step) {
        this.setRange(start, min, max);
        this.setStep(step);
    }


    public void setRange(final byte start, final byte min, final byte max) {
        if (min > max) {
            throw new IllegalArgumentException("Minimum must be less than maximum");
        }

        this.MIN = min;
        this.MAX = max;

        this.setPosition(start);
    }

    public void setPosition(final byte position) {
        if (this.MIN > position || position >= this.MAX) {
            throw new IllegalArgumentException("The start value must be in between min & max!");
        }

        this.position = position;
        this.POINT = position;
    }


    public void setStep(final byte step) {
        if (step == 0) {
            throw new IllegalArgumentException("The step cannot be zero!");
        }

        if (step > 0) {
            this.STEP = step;

            if (this.STEP == 1) {
                this.forwarder = this::simpleAdd;
                this.backer = this::simpleSubtract;
            } else if (this.STEP > (this.MAX - this.MIN)) {
                this.forwarder = this::complexAdd;
                this.backer = this::complexSubtract;
            } else {
                this.forwarder = this::add;
                this.backer = this::subtract;
            }
        } else {
            this.STEP = (byte) -step;

            if (this.STEP == 1) {
                this.forwarder = this::simpleSubtract;
                this.backer = this::simpleAdd;
            } else if (this.STEP > (this.MAX - this.MIN)) {
                this.forwarder = this::complexSubtract;
                this.backer = this::complexAdd;
            } else {
                this.forwarder = this::subtract;
                this.backer = this::add;
            }
        }
    }


    public byte goBack() {
        return this.updatePosition(this.backcast());
    }

    public boolean loopedBack() {
        return this.finishedLoop(this.position, this.forecast());
    }

    public byte backcast() {
        return this.backer.perform();
    }

    public byte current() {
        return this.position;
    }

    public byte forecast() {
        return this.forwarder.perform();
    }

    public boolean loopedForward() {
        return this.finishedLoop(this.backcast(), this.position);
    }

    public byte goForward() {
        return this.updatePosition(this.forecast());
    }


    private byte simpleAdd() {
        final byte position = (byte) (this.position + 1);

        if (position == this.MAX) {
            return this.MIN;
        } else {
            return position;
        }
    }

    private byte simpleSubtract() {
        final byte position = (byte) (this.position - 1);

        if (position + 1 == this.MIN) {
            return this.MAX;
        } else {
            return position;
        }
    }

    private byte add() {
        final byte position = (byte) (this.position + this.STEP);

        if (position >= this.MAX) {
            return (byte) (this.MIN + (position - this.MAX));
        } else {
            return position;
        }
    }

    private byte subtract() {
        final byte position = (byte) (this.position - this.STEP);

        if (position < this.MIN) {
            return (byte) (this.MAX - (this.MIN - position));
        } else {
            return position;
        }
    }

    private byte complexAdd() {
        final byte position = (byte) (this.position + this.STEP);

        if (position >= this.MAX) {
            return (byte) (this.MIN + (this.MIN % (this.MAX - this.MIN)));
        } else {
            return position;
        }
    }

    private byte complexSubtract() {
        final byte position = (byte) (this.position - this.STEP);

        if (position < this.MIN) {
            return (byte) (this.MAX - (position % (this.MAX - this.MIN)));
        } else {
            return position;
        }
    }


    private byte updatePosition(final byte newPosition) {
        this.position = newPosition;
        return position;
    }

    private boolean finishedLoop(final byte oldPosition, final byte newPosition) {
        return newPosition >= this.POINT && (oldPosition < this.POINT || oldPosition > newPosition);
    }

    public String toString() {
        return String.format("ByteLoop{MIN-MAX=%d-%d (STEP=%d), position=%d, START=%d}",
                this.MIN, this.MAX, this.STEP, this.position, this.POINT);
    }
}
