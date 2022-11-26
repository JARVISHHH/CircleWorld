package engine.game;

import engine.support.Vec2d;
import engine.support.Vec2i;
import javafx.scene.image.Image;

import javax.sound.sampled.*;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;

public class Resource {
    private static HashMap<String, Image> tag2Sprites = new HashMap<String, Image>();
    private static HashMap<String, Wrapper> tag2Wrapper = new HashMap<String, Wrapper>();

    public static void loadImage(String path, String tag, Vec2d size, Vec2i number) {
        if(tag2Sprites.containsKey(tag) && tag2Wrapper.containsKey(tag)) return;
        Image image = new Image(path, size.x, size.y, true, true);
        Resource.tag2Sprites.put(tag, image);
        Wrapper wrapper = new Wrapper(size, number);
        Resource.tag2Wrapper.put(tag, wrapper);
    }

    public static Image getImage(String tag) {
        if(!Resource.tag2Sprites.containsKey(tag)) {
            System.out.println("The image " + tag + " does not exist!");
            return null;
        }
        return Resource.tag2Sprites.get(tag);
    }

    public static Wrapper getWrapper(String tag) {
        if(!Resource.tag2Wrapper.containsKey(tag)) {
            System.out.println("The wrapper " + tag + " does not exist!");
            return null;
        }
        return Resource.tag2Wrapper.get(tag);
    }

}
