package gui.root

import java.awt.*
import java.awt.geom.*
import java.awt.event.*
import javax.swing.*
import org.viewaframework.view.*


class TranslucentGlassPane extends JComponent {

     private static final long serialVersionUID = 1L;
     private AbstractViewContainer view;
     /**
      *
      */
     TranslucentGlassPane(AbstractViewContainer view){
         this.view = view;
         addMouseListener(new MouseAdapter() {});
         addMouseMotionListener(new MouseMotionAdapter() {});
         addKeyListener(new KeyAdapter() {});
         addComponentListener(new ComponentAdapter() {
             public void componentShown(ComponentEvent evt){
                 requestFocusInWindow();
             }
         });
         setFocusTraversalKeysEnabled(false);
         setOpaque(false);
     }

     public boolean contains(int x,int y){
         return !view.getJToolBar().getBounds().contains(x,y);
     }

     /* (non-Javadoc)
      * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
      */
     protected void paintComponent(Graphics g){
         Rectangle clip = g.getClipBounds();
         Area rect = new Area(clip);
         rect.subtract(new Area(view.getJToolBar().getBounds()));
         g.setClip(rect);
         Color alphaWhite = new Color(1.0f,1.0f,1.0f,0.65f);
         g.setColor(alphaWhite);
         g.fillRect(clip.x.intValue(), clip.y.intValue(), clip.width.intValue(), clip.height.intValue());
     }
}

