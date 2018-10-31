package Utilities;

public final class Vector2D {
    public double x, y;

    // constructor for zero vector
    public Vector2D() {
        this.x = 0;
        this.y = 0;
    }

    // constructor for vector with given coordinates
    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // constructor that copies the argument vector
    public Vector2D(Vector2D v) {
        this.x = v.x;
        this.y = v.y;
    }

    // set coordinates
    public Vector2D set(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }

    // set coordinates based on argument vector
    public Vector2D set(Vector2D v) {
        this.x = v.x;
        this.y = v.y;
        return this;
    }

    // compare for equality (note Object type argument)
    public boolean equals(Object o) {
        if (o instanceof Vector2D) {
            Vector2D v = (Vector2D) o;
            return x == v.x && y == v.y;
        } else {
            return false;
        }
    }

    // String for displaying vector as text
    public String toString() {
        return ("(" + this.x + ", " + this.y + ")");
    }

    //  magnitude (= "length") of this vector
    public double mag() {
        return (Math.sqrt((Math.pow(this.x, 2)) + (Math.pow(this.y, 2))));
    }

    // angle between vector and horizontal axis in radians in range [-PI,PI]
    // can be calculated using Math.atan2
    public double angle() {
        return Math.atan2(this.y, this.x);
    }

    // angle between this vector and another vector in range [-PI,PI]
    public double angle(Vector2D other) {
        return (Math.atan2(this.x * other.y - this.y * other.x, this.x * other.x + this.y * other.y));
    }

    // add argument vector
    public Vector2D add(Vector2D v) {
        this.x += v.x;
        this.y += v.y;
        return this;
    }

    // add values to coordinates
    public Vector2D add(double x, double y) {
        this.x += x;
        this.y += y;
        return this;
    }

    // weighted add - surprisingly useful
    public Vector2D addScaled(Vector2D v, double fac) {
        this.x += fac * v.x;
        this.y += fac * v.y;
        return this;
    }

    // subtract argument vector
    public Vector2D subtract(Vector2D v) {
        this.x -= v.x;
        this.y -= v.y;
        return this;
    }

    // subtract values from coordinates
    public Vector2D subtract(double x, double y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    // multiply with factor
    public Vector2D mult(double fac) {
        this.x = (this.x * fac);
        this.y = (this.y * fac);
        return this;
    }

    // rotate by angle given in radians
    public Vector2D rotate(double angle) {
        double x2 = (this.x * Math.cos(angle)) - (this.y * Math.sin(angle));
        double y2 = (this.x * Math.sin(angle)) + (this.y * Math.cos(angle));
        this.x = x2;
        this.y = y2;
        return this;
    }

    // "dot product" ("scalar product") with argument vector
    public double dot(Vector2D v) {
        return ((this.x * v.x) + (this.y * v.y));
    }

    // distance to argument vector
    public double dist(Vector2D v) {
        return Math.hypot((this.x - v.x), (this.y - v.y));
    }

    // normalise vector so that magnitude becomes 1
    public Vector2D normalise() {
        double length = mag();
        if(length != 0){
            this.x = x/length;
            this.y = y/length;
        }
        return this;
    }

    // wrap-around operation, assumes w> 0 and h>0
    // remember to manage negative values of the coordinates
    public Vector2D wrap(double w, double h) {
        x = (x + w) % w;
        y = (y + h) % h;
        return this;
    }

    // construct vector with given polar coordinates
    public static Vector2D polar(double angle, double mag) {
        Vector2D vector = new Vector2D();
        vector.x = mag * Math.cos(angle);
        vector.y = mag * Math.sin(angle);
        return vector;
    }
}