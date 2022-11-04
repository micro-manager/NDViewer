package org.micromanager.ndviewer.api;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import javax.swing.JPanel;
import mmcorej.org.json.JSONObject;
import org.micromanager.ndviewer.main.NDViewer;
import org.micromanager.ndviewer.overlay.Overlay;

/**
 * Interface for external methods of an {@link NDViewer}. The only requirement
 * is to call {@link #newImageArrived(java.util.HashMap, java.lang.String) 
 * newImageArrived} method each time a new image is available. It may also
 * be helpful to call {@link #setChannelDisplaySettings(java.lang.String, java.awt.Color, int) 
 * bitDepth} before each image arrives to initialize preferred
 * contrast controls
 * 
 */
public interface ViewerInterface {
   
   /**
    * Call this whenever a new image arrives to optionally show it, and also so
    * that the size of the scrollbars expands.
    *
    * @param axes map of axis labels to integer positions. channel does not need
    *     to be included in the axes because it will be automatically inferred
    * @param channelName Name of the channel of the image
    */
   void newImageArrived(HashMap<String, Integer> axes, String channelName);

   /**
    * Set display settings for channel with the given name.
    *
    * @param chName
    * @param c
    * @param bitDepth
    */
   void setChannelDisplaySettings(String chName, Color c, int bitDepth);

   /**
    * Set the scrollbar with a given axis label to a position.
    *
    * @param axis
    * @param pos
    */
   void setAxisPosition(String axis, int pos);

   /**
    * Get the position that the scrollbar with the given label is currently at.
    *
    * @param axis
    * @return
    */
   int getAxisPosition(String axis);

   /**
    * Initialize all controls needed for a dataset loaded from disk
    * where you're not calling newImageArrived each time.
    * 
    * @param channelNames names of all channels
    * @param axisMins map of axis names to miniumum extents (can be negative)
    * @param axisMaxs map of axis names to maximum extents
    */
    void initializeViewerToLoaded(List<String> channelNames,
              JSONObject displaySettings,
              HashMap<String, Integer> axisMins, 
              HashMap<String, Integer> axisMaxs);

   
   /**
    * Set the text in the windows frame.
    *
    * @param string
    */
   void setWindowTitle(String string);

   /**
    * Optional: Pass in a function that tells how to extract elapsed ms from
    * image metadata, for display purposes.
    *
    * @param fn
    */
   void setReadTimeMetadataFunction(Function<JSONObject, Long> fn);

   /**
    * Optional: Pass in a function that tells how to extract z position in µm
    * from image tags, for display purposes.
    *
    * @param fn
    */
   void setReadZMetadataFunction(Function<JSONObject, Double> fn);

   /**
    * Get the JSON represenation of the internal data structure use for saving
    * display and contrast settings. This can be used to save them and then re
    * load them later on.
    *
    * @return
    */
   JSONObject getDisplaySettingsJSON();

   /**
    * Forces the viewer to close, automatically aborting any acquisition in the
    * process.
    */
   void close();

   /**
    * Redraw current image and any overlay
    */
   void update();

   /**
    * get the offset of the top left displayed pixel to relative to top left of
    * the full image (in full resolution coordinates).
    *
    * @return
    */
   Point2D.Double getViewOffset();

   /**
    * Pixel size of the region currently being displayed in full resolution
    * pixels.
    *
    * @return
    */
   Point2D.Double getFullResSourceDataSize();

   /**
    * Return ratio between size of pixels on screen and size of full resolution
    * pixels in the imamge.
    *
    * @return
    */
   double getMagnification();

   /**
    * Change the view offset by the specified amount.
    *
    * @param dx
    * @param dy
    */
   void pan(int dx, int dy);

   /**
    * multiply zoom by given factor, centered at location.
    *
    * @param factor
    * @param location location in display pixel coordinates, or null to zoom in
    * on center
    */
   void zoom(double factor, Point location);

   /**
    * set the offset of the top left displayed pixel to relative to top left of
    * the full image (in full resolution coordinates).
    *
    */
   void setViewOffset(double newX, double newY);

   /**
    * Get reference to the JPanel on which image drawing takes place.
    *
    * @return
    */
   JPanel getCanvasJPanel();

   /**
    * Add a custom object to respond to different types of mouse events on the
    * canvas.
    *
    * @param m
    */
   void setCustomCanvasMouseListener(CanvasMouseListenerInterface m);

   /**
    * Get the size of the ge displayed on screen.
    *
    * @return
    */
   Point2D.Double getDisplayImageSize();

   /**
    * Set a custom overlay object to be displayed on top of the image. This
    * method can be called an arbitrary number of times by a custom.
    * {@link OverlayerPlugin}
    *
    * @param overlay
    */
   void setOverlay(Overlay overlay);

   /**
    * Set a custom object to provide overlays.
    *
    * @param overlayer
    */
   void setOverlayerPlugin(OverlayerPlugin overlayer);

   /**
    * trigger redraw of the image overlay.
    */
   void redrawOverlay();

   /**
    * Add a custom JPanel containing controls to the JTabbedPane on the right
    * side of the image.
    *
    * @param panel
    */
   void addControlPanel(ControlsPanelInterface panel);

}
