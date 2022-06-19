package vdm.ivanhoe.raytracer.classes;

import vdm.ivanhoe.raytracer.interfaces.Intersectable;

/**
 *  Scene
 */
public class Scene {
    private Intersectable[] intersectables;
    private Light[] lights;
    private Camera camera;

    public Scene(Intersectable[] intersectables, Light[] lights, Camera camera) {
        this.intersectables = intersectables;
        this.lights = lights;
        this.camera = camera;
    }

    public Intersectable[] getThings() {
        return intersectables;
    }

    public Light[] getLights() {
        return lights;
    }

    public Camera getCamera() {
        return camera;
    }
}
