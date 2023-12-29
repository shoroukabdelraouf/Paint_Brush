import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


public class FinalPaintBrush extends Applet 
{
    public static final int lin = 0;
    public static final int ova = 1;
    public static final int erase = 2;
	public static final int rect= 3;
	public static final int free= 4;
    int currentlyDrawing;
    Color currentColor;
    boolean solid;
    Checkbox fillCheck;
    Button linebut;
    Button ovalbut;
	Button rectbut;
    Button blue;
	Button green;
	Button free_hand;
    Button red;
	Button clear;
    Button eraser;
    private ArrayList<Shape> myShapes = new ArrayList<>();
    Shape newline;
	
 
    public void init() {
        currentColor = Color.black;
        solid = false;
		
		newline = new Line(0, 0, 0, 0, Color.black);

        clear = new Button("Clear");
        clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                myShapes.clear();
                repaint();
            }
        });
        add(clear);
		
		
		red = new Button("Red");
        red.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentColor = Color.red;
            }
        });
        add(red);
		
		
		blue= new Button("Blue");
        blue.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentColor = Color.blue;
            }
        });
        add(blue);
		
		 green = new Button("Green");
        green.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentColor = Color.green;
            }
        });
        add(green);

        fillCheck = new Checkbox("Fill Shape");
        fillCheck.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                solid = fillCheck.getState();
            }
        });
        add(fillCheck);

        linebut = new Button("Line");
        linebut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentlyDrawing = 0;
            }
        });
        add(linebut);
		
		rectbut = new Button("Rectangle");
        rectbut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentlyDrawing =3;
            }
        });
        add(rectbut);
		
		free_hand = new Button("Free Hand");
        free_hand.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentlyDrawing =4;
            }
        });
        add(free_hand);

        ovalbut = new Button("Oval");
        ovalbut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentlyDrawing = 1;
            }
        });
        add(ovalbut);

        eraser = new Button("Eraser");
        eraser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentColor = getBackground();
				currentlyDrawing =2;
            }
        });
        add(eraser);

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
				
				newline = new Line(e.getX(), e.getY(), e.getX(), e.getY(), currentColor);
				
               switch (currentlyDrawing) {
                    case lin:
                        newline = new Line(e.getX(), e.getY(), e.getX(), e.getY(), currentColor);
                        break;
                    case ova:
                        newline = new Oval(e.getX(), e.getY(), e.getX(), e.getY(), currentColor, solid);
                        break;
					case rect:
					     newline = new Rectangle(e.getX(), e.getY(), e.getX(), e.getY(), currentColor, solid);
						 break;
					case free:
					     newline = new FreeHand(e.getX(), e.getY(), e.getX(), e.getY(), currentColor);
						 break; 
					case erase:
                         newline = new Eraser(e.getX(), e.getY(),e.getX(), e.getY());
                        break;
					
                };

                repaint();
            }

            public void mouseReleased(MouseEvent e) {
                if (newline != null) {
                    newline.x2 = e.getX();
                    newline.y2 = e.getY();
                    myShapes.add(newline);
                    repaint();
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
           public void mouseDragged(MouseEvent e) {
        if (currentlyDrawing == free) {
            newline.x2 = e.getX();
            newline.y2 = e.getY();
            myShapes.add(new FreeHand(newline.x1, newline.y1, newline.x2, newline.y2, currentColor));
            repaint();
            newline.x1 = newline.x2;
            newline.y1 = newline.y2;
        } else   
			if (newline != null) 
			{
                newline.x2 = e.getX();
                newline.y2 = e.getY();
                repaint();
            }
        }
    });
	}

    public void paint(Graphics g) 
	{
        for (Shape shape : myShapes) 
		{
            shape.draw(g);
        }
       if (newline !=null)
	   {switch (currentlyDrawing){
            case lin:
                Line line = new Line(newline.x1, newline.y1, newline.x2, newline.y2, currentColor);
                line.draw(g);
                break;
            case ova:
                Oval oval = new Oval(newline.x1, newline.y1, newline.x2, newline.y2, currentColor, solid);
                oval.draw(g);
                break;
			case rect:
			    Rectangle recta=new Rectangle(newline.x1, newline.y1, newline.x2, newline.y2, currentColor, solid); 
			    recta.draw(g);
				break;
			case free:
			    FreeHand freehand =new FreeHand(newline.x1, newline.y1, newline.x2, newline.y2, currentColor); 
			    freehand.draw(g);
				break;
        }
	   }
    }
	
}
abstract class Shape {
    int x1, y1, x2, y2;
    Color color;

    public Shape(int x1, int y1, int x2, int y2, Color color) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
    }

    public abstract void draw(Graphics g);
}

class Line extends Shape {
    Line(int x1, int y1, int x2, int y2, Color color) {
        super(x1, y1, x2, y2, color);
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.drawLine(x1, y1, x2, y2);
    }
}

class Oval extends Shape 
{
    boolean fillShape;

    Oval(int x1, int y1, int x2, int y2, Color color, boolean fillShape) {
        super(x1, y1, x2, y2, color);
        this.fillShape = fillShape;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        if (fillShape) {
            g.fillOval(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));
        } else {
            g.drawOval(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));
        }
    }
}
class Rectangle extends Shape 
{
    boolean fillShape;

    Rectangle(int x1, int y1, int x2, int y2, Color color, boolean fillShape) {
        super(x1, y1, x2, y2, color);
        this.fillShape = fillShape;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        if (fillShape) {
            g.fillRect(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));
        } else {
            g.drawRect(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));
        }
    }
}
class Eraser extends Shape {
    Eraser(int x1, int y1, int x2, int y2) {
        super(x1, y1, x2, y2, Color.white);
    }

    public void draw(Graphics g) {
       
        g.setColor(Color.white);
        g.fillRect(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1)); 
    }
}
class FreeHand extends Shape 
{
    boolean fillShape=true;

    FreeHand(int x1, int y1, int x2, int y2, Color color)
	{
        super(x1, y1, x2, y2, color);
       
    }

    public void draw(Graphics g) 
	{
        g.setColor(color);
        g.fillRect(Math.min(x1, x2), Math.min(y1, y2),5,5 );
       
    }
}


