package circle_loop;

import java.util.function.Supplier;

public class ByteLoop {
    private byte MIN;
    private byte MAX;
    private byte STEP;

    private byte position;
    private byte POINT;

    private Supplier<Byte> toMove;
    private Supplier<Byte> toBack;


    public ByteLoop(final byte max) {
        this.MIN = 0;
        this.MAX = max;

        this.setPosition((byte) 0);

        this.STEP = 1;
        this.toMove = this::simpleAdd;
        this.toBack = this::simpleSubtract;
    }

    public ByteLoop(final byte start, final byte max) {
        this.MIN = 0;
        this.MAX = max;

        this.setPosition(start);

        this.STEP = 1;
        this.toMove = this::simpleAdd;
        this.toBack = this::simpleSubtract;
    }

    public ByteLoop(final byte start, final byte min, final byte max) {
        this.MIN = min;
        this.MAX = max;

        this.setPosition(start);

        this.STEP = 1;
        this.toMove = this::simpleAdd;
        this.toBack = this::simpleSubtract;
    }

    public ByteLoop(final byte start, final byte min, final byte max, final byte step) {
        this.MIN = min;
        this.MAX = max;

        this.setPosition(start);

        this.setStep(step);
    }


    public void setRange(final byte min, final byte max) {
        this.setRange(this.POINT, min, max);
    }

    public void setRange(final byte start, final byte min, final byte max) {
        if (min >= max) {
            throw new IllegalArgumentException("Minimum must be less than maximum");
        }
        this.setPosition(start);

        this.MIN = min;
        this.MAX = max;
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

            if (this.STEP > (this.MAX - this.MIN)) {
                this.toMove = this::complexAdd;
                this.toBack = this::complexSubtract;
            } else {
                this.toMove = this::add;
                this.toBack = this::subtract;
            }
        } else {
            this.STEP = (byte) -step;

            if (this.STEP > (this.MAX - this.MIN)) {
                this.toMove = this::complexSubtract;
                this.toBack = this::complexAdd;
            } else {
                this.toMove = this::subtract;
                this.toBack = this::add;
            }
        }
    }


    public byte getMinimum() {
        return this.MIN;
    }

    public byte getMaximum() {
        return this.MAX;
    }

    public byte getStep() {
        return this.STEP;
    }


    public byte goBack() {
        this.position = this.backcast();
        return position;
    }

    public boolean loopedBack() {
        return this.finishedLoop(this.position, this.backcast());
    }

    public byte backcast() {
        return this.toBack.get();
    }

    public byte current() {
        return this.position;
    }

    public byte forecast() {
        return this.toMove.get();
    }

    public boolean loopedForward() {
        return this.finishedLoop(this.backcast(), this.position);
    }

    public byte goForward() {
        this.position = this.forecast();
        return position;
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


    private boolean finishedLoop(final byte newPosition, final byte oldPosition) {
        return newPosition > this.POINT && oldPosition <= this.POINT;
    }

    public String toString() {
        return String.format("ByteLoop{MIN-MAX=%d-%d (STEP=%d), position=%d, START=%d}",
                this.MIN, this.MAX, this.STEP, this.position, this.POINT);
    }
}
