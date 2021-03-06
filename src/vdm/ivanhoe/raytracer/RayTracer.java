package vdm.ivanhoe.raytracer;

import vdm.ivanhoe.raytracer.classes.*;
import vdm.ivanhoe.raytracer.classes.Color;
import vdm.ivanhoe.raytracer.interfaces.Intersectable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * RayTracer
 */
public class RayTracer {
    private final int maxDepth = 5;
    private final Logger logger = Logger.getLogger("ImageWriter");

    private static final String FOLDER_PATH = System.getProperty("user.dir") + "/images";


    private Intersection intersections(Ray ray, Scene scene) {
        double closest = Double.POSITIVE_INFINITY;
        Intersection closestInter = null;

        for (int i = 0; i < scene.getThings().length; i++) {
            Intersection inter = scene.getThings()[i].intersect(ray);
            if (inter != null && inter.getDist() < closest) {
                closestInter = inter;
                closest = inter.getDist();
            }
        }
        return closestInter;
    }

    private double testRay(Ray ray, Scene scene) {
        Intersection isect = intersections(ray, scene);
        if (isect != null) {
            return isect.getDist();
        } else {
            return 0;
        }
    }

    private Color traceRay(Ray ray, Scene scene, double depth) {
        Intersection isect = intersections(ray, scene);
        if (isect == null) {
            return Color.background();
        } else {
            return shade(isect, scene, depth);
        }
    }

    private Color shade(Intersection isect, Scene scene, double depth) {
        Vector d = isect.getRay().getDir();
        Vector pos = Vector.plus(Vector.times(isect.getDist(), d), isect.getRay().getStart());
        Vector normal = isect.getThing().normal(pos);
        Vector reflectDir = Vector.minus(d, Vector.times(2, Vector.times(Vector.dot(normal, d), normal)));
        Color naturalColor = Color.plus(Color.background(), getNaturalColor(isect.getThing(), pos, normal, reflectDir, scene));
        Color reflectedColor = (depth >= this.maxDepth) ? Color.grey() : getReflectionColor(isect.getThing(), pos, normal, reflectDir, scene, depth);
        return Color.plus(naturalColor, reflectedColor);
    }

    private Color getReflectionColor(Intersectable intersectable, Vector pos, Vector normal, Vector rd, Scene scene, double depth) {
        return Color.scale(intersectable.getSurface().reflect(pos), traceRay(new Ray(pos, rd), scene, depth + 1));
    }

    private Color addLight(Color color, Light light, Intersectable intersectable, Vector pos, Vector norm, Vector rd, Scene scene) {
        Vector ldis = Vector.minus(light.getPos(), pos);
        Vector livec = Vector.normal(ldis);
        double neatIsect = testRay(new Ray(pos, livec), scene);
        boolean isInShadow = !(neatIsect == 0) && (neatIsect <= Vector.getLength(ldis));
        if (isInShadow) {
            return color;
        } else {
            double illum = Vector.dot(livec, norm);
            Color lightColor;
            if (illum > 0) {
                lightColor = Color.scale(illum, light.getColor());
            } else {
                lightColor = Color.defaultColor();
            }
            double specular = Vector.dot(livec, Vector.normal(rd));
            Color specularColor;
            if (specular > 0) {
                specularColor = Color.scale(
                        Math.pow(specular, intersectable.getSurface().getRoughness()),
                        light.getColor()
                );
            } else {
                specularColor = Color.defaultColor();
            }
            return Color.plus(
                    color,
                    Color.plus(
                            Color.times(intersectable.getSurface().diffuse(pos), lightColor),
                            Color.times(intersectable.getSurface().specular(pos), specularColor)
                    )
            );
        }
    }

    private Color getNaturalColor(Intersectable intersectable, Vector pos, Vector norm, Vector rd, Scene scene) {
        Color initialColor = Color.defaultColor();
        Color resultColor = addLight(initialColor, scene.getLights()[0], intersectable, pos, norm, rd, scene);
        for (int i = 1; i < scene.getLights().length; i++) {
            resultColor = addLight(resultColor, scene.getLights()[i], intersectable, pos, norm, rd, scene);
        }

        return resultColor;
    }


    private double recenterX(double x, int screenWidth) {
        return (x - (screenWidth / 2.0)) / 2.0 / screenWidth;
    }

    private double recenterY(double y, int screenHeight) {
        return -(y - (screenHeight / 2.0)) / 2.0 / screenHeight;
    }

    private Vector getPoint(double x, double y, Camera camera, int screenWidth, int screenHeight) {
        return Vector.normal(Vector.plus(
                camera.getForward(),
                Vector.plus(
                        Vector.times(recenterX(x, screenWidth), camera.getRight()),
                        Vector.times(recenterY(y, screenHeight), camera.getUp())
                ))
        );
    }

    public void renderToJFrame(Scene scene, Graphics graphics, int screenWidth, int screenHeight) {
        for (int y = 0; y < screenHeight; y++) {
            for (int x = 0; x < screenWidth; x++) {
                Color color = traceRay(new Ray(scene.getCamera().getPos(), getPoint(x, y, scene.getCamera(), screenWidth, screenHeight)), scene, 0);
                java.awt.Color c = Color.toDrawingColor(color);
                graphics.setColor(c);
                graphics.fillRect(x, y, x + 1, y + 1);
            }
        }
    }

    public void renderToImage(Scene scene, Graphics2D graphics, int screenWidth, int screenHeight, BufferedImage bufferedImage, String imageName) {
        for (int y = 0; y < screenHeight; y++) {
            for (int x = 0; x < screenWidth; x++) {
                Color color = traceRay(new Ray(scene.getCamera().getPos(), getPoint(x, y, scene.getCamera(), screenWidth, screenHeight)), scene, 0);
                java.awt.Color c = Color.toDrawingColor(color);
                graphics.setColor(c);
                graphics.fillRect(x, y, x + 1, y + 1);
            }
        }

        try {
            File file = new File(FOLDER_PATH + '/' + imageName + ".png");
            ImageIO.write(bufferedImage, "png", file);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "I/O error", e);
            throw new IllegalStateException("I/O error - may be missing directory " + FOLDER_PATH, e);
        }
    }
}
