package vdm.ivanhoe.raytracer.classes;

import vdm.ivanhoe.raytracer.interfaces.Intersectable;

/**
 *  Intersection
 */
public class Intersection {
    private final Intersectable intersectable;
    private final Ray ray;
    private final double dist;

    Intersection(Intersectable intersectable, Ray ray, double dist) {
        this.intersectable = intersectable;
        this.ray = ray;
        this.dist = dist;
    }

    public Intersectable getThing() {
        return intersectable;
    }

    public Ray getRay() {
        return ray;
    }

    public double getDist() {
        return dist;
    }
}
