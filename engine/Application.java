package engine;

import engine.support.FXFrontEnd;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This is your main Application class that you will contain your
 * 'draws' and 'ticks'. This class is also used for controlling
 * user input.
 */
public class Application extends FXFrontEnd {

  protected HashMap<String, Integer> screensName2Index;
  protected ArrayList<Screen> screens;
  protected Screen activeScreen;
  protected final double aspectRatio = currentStageSize.x / currentStageSize.y;
  public Application(String title) {
    super(title);
    screensName2Index = new HashMap<String, Integer>();
    screens = new ArrayList<Screen>();
  }
  public Application(String title, Vec2d windowSize, boolean debugMode, boolean fullscreen) {
    super(title, windowSize, debugMode, fullscreen);
  }

  /**
   * Called periodically and used to update the state of your game.
   * @param nanosSincePreviousTick	approximate number of nanoseconds since the previous call
   */
  @Override
  protected void onTick(long nanosSincePreviousTick) {
    if(activeScreen != null) activeScreen.onTick(nanosSincePreviousTick);
  }

  /**
   * Called after onTick().
   */
  @Override
  protected void onLateTick() {
    // Don't worry about this method until you need it. (It'll be covered in class.)
  }

  /**
   *  Called periodically and meant to draw graphical components.
   * @param g		a {@link GraphicsContext} object used for drawing.
   */
  @Override
  protected void onDraw(GraphicsContext g) {
    if(activeScreen != null) activeScreen.onDraw(g);
  }

  /**
   * Called when a key is typed.
   * @param e		an FX {@link KeyEvent} representing the input event.
   */
  @Override
  protected void onKeyTyped(KeyEvent e) {
    if(activeScreen != null) activeScreen.onKeyTyped(e);
  }

  /**
   * Called when a key is pressed.
   * @param e		an FX {@link KeyEvent} representing the input event.
   */
  @Override
  protected void onKeyPressed(KeyEvent e) {
    if(activeScreen != null) activeScreen.onKeyPressed(e);
  }

  /**
   * Called when a key is released.
   * @param e		an FX {@link KeyEvent} representing the input event.
   */
  @Override
  protected void onKeyReleased(KeyEvent e) {
    if(activeScreen != null) activeScreen.onKeyReleased(e);
  }

  /**
   * Called when the mouse is clicked.
   * @param e		an FX {@link MouseEvent} representing the input event.
   */
  @Override
  protected void onMouseClicked(MouseEvent e) {
    if(activeScreen != null) activeScreen.onMouseClicked(e);
  }

  /**
   * Called when the mouse is pressed.
   * @param e		an FX {@link MouseEvent} representing the input event.
   */
  @Override
  protected void onMousePressed(MouseEvent e) {
    if(activeScreen != null) activeScreen.onMousePressed(e);
  }

  /**
   * Called when the mouse is released.
   * @param e		an FX {@link MouseEvent} representing the input event.
   */
  @Override
  protected void onMouseReleased(MouseEvent e) {
    if(activeScreen != null) activeScreen.onMouseReleased(e);
  }

  /**
   * Called when the mouse is dragged.
   * @param e		an FX {@link MouseEvent} representing the input event.
   */
  @Override
  protected void onMouseDragged(MouseEvent e) {
    if(activeScreen != null) activeScreen.onMouseDragged(e);
  }

  /**
   * Called when the mouse is moved.
   * @param e		an FX {@link MouseEvent} representing the input event.
   */
  @Override
  protected void onMouseMoved(MouseEvent e) {
    if(activeScreen != null) activeScreen.onMouseMoved(e);
  }

  /**
   * Called when the mouse wheel is moved.
   * @param e		an FX {@link ScrollEvent} representing the input event.
   */
  @Override
  protected void onMouseWheelMoved(ScrollEvent e) {
    if(activeScreen != null) activeScreen.onMouseWheelMoved(e);
  }

  /**
   * Called when the window's focus is changed.
   * @param newVal	a boolean representing the new focus state
   */
  @Override
  protected void onFocusChanged(boolean newVal) {
    if(activeScreen != null) activeScreen.onFocusChanged(newVal);
  }

  /**
   * Called when the window is resized.
   * @param newSize	the new size of the drawing area.
   */
  @Override
  protected void onResize(Vec2d newSize) {
    if(activeScreen != null) activeScreen.onResize(newSize);
  }

  /**
   * Called when the app is shutdown.
   */
  @Override
  protected void onShutdown() {

  }

  /**
   * Called when the app is starting up.
   */
  @Override
  protected void onStartup() {

  }

  // Add screen to the Application
  protected void addScreen(Screen screen) {
    screensName2Index.put(screen.name, screens.size());
    screens.add(screen);
  }

  // Activate the screen named name
  protected void activateScreen(String name) {
    Screen newScreen = screens.get(screensName2Index.get(name));
    if(newScreen.getSize() != currentStageSize)
      newScreen.onResize(currentStageSize);
    activeScreen = screens.get(screensName2Index.get(name));
  }

}
