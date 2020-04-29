import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.*;
import javax.swing.*;

public class DrawGraph extends JPanel {
    public ArrayList<Double> xs;
    public ArrayList<Double> ys;
    private static final Stroke GRAPH_STROKE = new BasicStroke(2f);
    /**
     * This is a Class constructor setup the lists xs and ys. 
     * @param xs	a list of x of points in the graph
     * @param ys	a list of y of points in the graph
     */
    public DrawGraph(ArrayList<Double> xs,ArrayList<Double> ys) {
        this.xs = xs;
        this.ys = ys;
    }
    /**
     * This is an override function when extends JPanel. It draw the
     * whole graph using xs and ys for all points. 
     * @param g		this is a graphic .
     */
    @Override
    protected void paintComponent(Graphics g) {
    	
        int padding = 25;
        Color lineColor = new Color(244, 143, 177);//new Color(44, 102, 230, 180);
        Color pointColor = new Color(236, 64, 122);//new Color(100, 100, 100, 180);
        int pointWidth = 2;
        Color gridColor = new Color(230,230,230);
        super.paintComponent(g);
        int a;
        int xsteps = 1;
        double max_x;
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.WHITE);
        g2.fillRect(0,0, getWidth(),getHeight());
        g2.setColor(Color.BLACK);
        if (ys.size()!= 0) {
        	double max_y=Collections.max(ys);
        	max_x = Collections.max(xs);
        	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        	double xScale = ((double) getWidth() - (3 * padding)) / (max_x);
        	double yScale = ((double) getHeight() - padding * 3) / (max_y);

        	ArrayList<Point> graphPoints = new ArrayList<Point>();
        	for (int i = 0; i<ys.size();i++) {
        		double x1 = (xs.get(i) * xScale + padding * 2);
        		double y1 = ((max_y - ys.get(i)) * yScale + padding);
        		graphPoints.add(new Point(x1, y1));
        		
        	}
        	int ysteps = 1;
        	a = 0;
        	if (max_y > 4) {
            	while (max_y > 10 * ysteps) {
            		if (a == 1) {
            			ysteps = (int)(ysteps * 2.5);
            			a++;
            		}
            		else {
            			if (a == 3) {a = 0;}
            			ysteps *= 2;
            			a++;
            		}
            	}
        	}
            // create ticks and grid lines for y-axis.
            for (int i = 0; i <= max_y;) {
            	
                int x0 = padding*2;
                int x1 = pointWidth + padding*2;
                int y0 = (int) (getHeight() - ((i * (getHeight() - padding*3)) / max_y + padding*2));
                int y1 = y0;
                g2.drawLine(x0, y0, x1, y1);
                g2.setColor(gridColor);
                g2.drawLine(padding * 2 + 1 + pointWidth, y0, getWidth() - padding, y1);
                g2.setColor(Color.BLACK);
                String yLabel = "";
                if (i >=1000000) {
                	if (i % 1000000 == 0)
                		yLabel = i/1000000+"m";
                	else
                		yLabel = String.format("%.1fm",(double)i/1000000.0);
                }
                else if (i >= 1000) {
                	if (i % 1000 == 0)
                		yLabel = i/1000+"k";
                	else
                		yLabel = String.format("%.1fm",(double)i/1000.0);
                }
                else {yLabel = i + "";}
                if (yLabel.equals("0") == false){
                	FontMetrics metrics = g2.getFontMetrics();
                	int labelWidth = metrics.stringWidth(yLabel);
                	g2.drawString(yLabel, x0 - labelWidth - 5 , y0 + (metrics.getHeight()/2)-3);
                }
                i += ysteps;
            }
            
            // draw lines and points
            Stroke oldStroke = g2.getStroke();
            g2.setColor(lineColor);
            g2.setStroke(GRAPH_STROKE);
            for (int i = 0; i < graphPoints.size() - 1; i++ ) {
                double x1 = graphPoints.get(i).x;
                double y1 = graphPoints.get(i).y;
                double x2 = graphPoints.get(i + 1).x;
                double y2 = graphPoints.get(i + 1).y;
                Shape line = new Line2D.Double(x1, y1, x2, y2);
                g2.draw(line);
            }

            g2.setStroke(oldStroke);
            g2.setColor(pointColor);
            for (int i = 0; i < graphPoints.size(); i++) {
                double x = graphPoints.get(i).x - pointWidth / 2;
                double y = graphPoints.get(i).y - pointWidth / 2;
                int w = pointWidth;
                int h = pointWidth;
                Ellipse2D.Double shape = new Ellipse2D.Double(x, y, w, h);
                g2.draw(shape);
            }
 
        }
        else if (xs.size()!= 0) {
        	max_x = xs.get(0);
        }
        else{
        	xsteps = 50;
        	max_x = 600;
        	}
        g2.setColor(Color.BLACK);
        // x labels
     	a = 0;
     	while (max_x >= 24 * xsteps) {
     		if (a == 1) {
     			xsteps = (int)(xsteps * 2.5);
     			a++;
     			
     		}
     		else {
     			if (a == 3) {a = 0;}
     			xsteps *= 2;
     			a++;
     		}
     		
     	}
        for (int i = 0; i <= max_x;) {
			int x0 = (int) (i * (getWidth() - padding * 3) / max_x + padding * 2);
			int x1 = x0;
			int y0 = getHeight() - padding*2;
			int y1 = y0 - pointWidth*2;
            g2.drawLine(x0, y0, x1, y1);
            String xLabel = "";
            if (i >=1000000) {
            	if (i % 1000000 == 0)
            		xLabel = i/1000000+"m";
            	else
            		xLabel = String.format("%.1fm",(double)i/1000000.0);
            }
            else if (i >= 1000) {
            	if (i % 1000 == 0)
            		xLabel = i/1000+"k";
            	else
            		xLabel = String.format("%.1fm",(double)i/1000.0);
            }
            else {xLabel = i + "";}
            FontMetrics metrics = g2.getFontMetrics();
            int labelWidth = metrics.stringWidth(xLabel);
            g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
            i += xsteps;
        }
        // create x and y axes
        g2.drawString("0", 30, getHeight() - 45);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Font font = new Font("SansSerif", Font.PLAIN, 15);
        String string = "Volume [Byte]";
        g2.setFont(font);
    	g2.drawString(string, 10, 15);
    	string = "Time[s]";
    	g2.drawString(string, getWidth() / 2, getHeight()-12);
        g2.drawLine(padding * 2, getHeight() - padding*2, padding * 2, padding);
        g2.drawLine(padding * 2, getHeight() - padding*2, getWidth() - padding,
                getHeight()- padding*2);
    }
}
